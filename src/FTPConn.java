/**
 * CSCI 204 - Brian King & Xiannong Meng
 * 
 * Assignment: FTPSearcher - Final Project
 * Team Members: Charles Cole, Christopher Rung, Alex Meijer
 * Created: Nov 17, 2011, 4:56:37 PM
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * The FTPConn class represents a connection to an FTP server and is responsible
 * for all interaction with the FTP server itself. This class can either
 * recursively list the files and directories on the server using an active data
 * connection, or can download a file that is present on the server.
 * 
 * NOTE: THIS CLASS USES UNSECURE FTP PROTOCOLS
 * 
 * 
 * @author atm011, clr023, clc031
 * @version 1.1
 */
public class FTPConn {

	// Instance variables
	private String serverName;
	private String userName;
	private String password;
	private FTPClient thisFTPConn;
	private ArrayList<FTPFile> serverFiles;
	private ArrayList<String> contents;
	private InputStream downloadStream;
	private boolean inRoot;
	private boolean inRootList;

	/**
	 * The constructor of the FTPConn class. This creates a connection with the
	 * target FTP server by creating an FTPConn object
	 * 
	 * 
	 * @param serverName
	 *            The name or IP address of the target FTP server as a string
	 * @param userName
	 *            The username required for access to the server as a string
	 * @param password
	 *            The password corresponding to the username entered as a string
	 */
	public FTPConn(String serverName, String userName, String password) {
		this.serverName = serverName;
		this.userName = userName;
		this.password = password;

		// create FTP client from apache commons
		this.thisFTPConn = new FTPClient();
	}

	/**
	 * This method actually opens the connection to the FTPserver.
	 * Postcondition: There will be a valid connection to the server in
	 * existence
	 * 
	 * 
	 * @throws IOException
	 * @throws LoginFailedException
	 *             Thrown when the login to the server failed, usually due to
	 *             bad login information
	 * @throws SocketException
	 */
	public void connect() throws IOException, LoginFailedException, SocketException {

		// connecting to server
		this.thisFTPConn.connect(serverName);

		// login
		this.thisFTPConn.login(userName, password);

		String loginCode = this.thisFTPConn.getReplyStrings()[0].substring(0, 3);

		// check to make sure that login is successful
		if (!loginCode.equalsIgnoreCase("230") && !loginCode.equalsIgnoreCase("250")) {
			throw new LoginFailedException("Not Logged in");
		}

		// At this point, the connection is made.
		// Instantiate the arrayLists to be filled
		this.serverFiles = new ArrayList<FTPFile>();
		this.contents = new ArrayList<String>();
	}

	/**
	 * LoginFailedException
	 * 
	 * This is an exception created to be thrown when the login request is
	 * rejected by the target server
	 * 
	 * 
	 * @author atm011, clc031, clr023
	 */
	public class LoginFailedException extends Exception {

		private static final long serialVersionUID = -1001567820267944175L;

		/**
		 * Default Exception
		 * 
		 */
		public LoginFailedException() {
			super();
		}

		/**
		 * Exception with a custom message
		 * 
		 * 
		 * @param message
		 *            The message of the Exception
		 */
		public LoginFailedException(String message) {
			super(message);
		}
	}

	/**
	 * Precondition: assumes a valid connection to the server exists
	 * 
	 * This method gets the last reply sequence from the server and displays
	 * that as a string.
	 * 
	 * 
	 * @return A string containing the server's last reply(s)
	 */
	public String getReply() {
		// Just use the pre-existing method from apache commons
		return this.thisFTPConn.getReplyString();
	}

	/**
	 * Precondition: assumes a valid connection to the server exists
	 * 
	 * This method was designed to show the recursive nature of the serverside
	 * filename gathering. This method was also used to develop the searching
	 * process used in the other methods
	 * 
	 * 
	 * @throws IOException
	 *             Thrown when the files/directories
	 */
	public void showProcess() throws IOException {

		// Prepare an array to store the
		// file/directory objects found by the connection method
		//
		// This array was required by the method
		// used from the Apache commons
		FTPFile[] files = this.thisFTPConn.listFiles();

		// for every file/directory in the array (which are all the
		// files/directories in the current directory)
		for (FTPFile file : files) {

			// if the file is a directory
			if (file.isDirectory()) {
				String name = file.getName();
				System.out.println("");
				System.out.println("Entering Directory: " + name);

				// change current directory to that directory
				this.thisFTPConn.changeWorkingDirectory(name);
				System.out.println("In Directory: " + this.thisFTPConn.printWorkingDirectory());

				// recursive call on that subdirectory
				this.showProcess();

				// after that recursive call returns, all directories/files in
				// the current directory have been listed.
				// move to the parent directory
				this.thisFTPConn.changeToParentDirectory();
				name = file.getName();
				System.out.println("Moving out of directory: " + name);
			}

			// if the file is a file, print its name
			if (file.isFile()) {
				System.out.println("File: " + file.getName() + " found");
			}
		}
	}

	/**
	 * This method is an adaptation of the showProcess method that prints the
	 * information that the parser receives. Used for demonstration/debugging
	 * 
	 * 
	 * @param depth
	 *            An integer that represents the staring level of the file path
	 *            naming. use 0 for root reference
	 * @throws IOException
	 *             Thrown when the files on the server cannot be accessed
	 */
	public void listContents(int depth) throws IOException {

		this.thisFTPConn.sendNoOp();

		// this stores the contents
		FTPFile[] files = this.thisFTPConn.listFiles();

		// for every file in the directory
		for (FTPFile file : files) {

			// if the file is a directory
			if (file.isDirectory()) {
				String name = file.getName();

				// change to that directory
				this.thisFTPConn.changeWorkingDirectory(name);

				// print that directory name
				System.out.println("Path: " + this.thisFTPConn.printWorkingDirectory());

				// recursive call
				this.listContents(depth + 1);
				this.thisFTPConn.changeToParentDirectory();
				name = file.getName();
			}

			// if it is a file, print the file name
			if (file.isFile()) {
				if (depth == 0 && !this.inRootList) {
					this.contents.add("Path: /");
					this.inRootList = true;
				}
				// print its name in the directory after a parser tag
				this.contents.add("File: " + file.getName());
			}
		}
	}

	/**
	 * This method uses the NOOP FTP connection to check if the connection
	 * exists between the server and the client
	 * 
	 * 
	 * @return True if the connection exists, and false otherwise
	 */
	public boolean checkConnection() {
		try {
			return this.thisFTPConn.sendNoOp();
		} catch (IOException e) {
			// if an exception is thrown, the connection is deemed not to exist
			return false;
		}
	}

	/**
	 * Precondition: assumes a valid connection to the server exists
	 * 
	 * This method gets both the file and the directories on the server, and
	 * stores them as strings in an arrayList. This method is designed to create
	 * the output used by the parser to create the file objects
	 * 
	 * Postcondition: An arraylist full of the names of all files and
	 * directories on the server is created
	 * 
	 * 
	 * @param depth
	 *            An integer that represents the staring level of the file path
	 *            naming. use 0 for root reference
	 * @return An ArrayList<String> that contains files and directories in the
	 *         server in raw text
	 * @throws IOException
	 *             Thrown when there is a problem acceing/getting the files from
	 *             the server
	 */
	public ArrayList<String> getAllContents(int depth) throws IOException {

		this.thisFTPConn.sendNoOp();

		// Create an array of files
		// This is needed by the apache commons method used
		FTPFile[] files = this.thisFTPConn.listFiles();

		// for every file in the array
		for (FTPFile file : files) {

			// if it is a directory
			if (file.isDirectory()) {
				String name = file.getName();

				// change to that directory
				this.thisFTPConn.changeWorkingDirectory(name);

				// add a parser tag followed by the name of the directory to the
				// output ArrayList
				this.contents.add("Path: " + this.thisFTPConn.printWorkingDirectory());

				// recursive call
				this.getAllContents(depth + 1);
				this.thisFTPConn.changeToParentDirectory();
				name = file.getName();
			}

			// if the FTP object is a file
			if (file.isFile()) {

				if (depth == 0 && !this.inRoot) {
					this.contents.add("Path: /");
					this.inRoot = true;
				}
				// print its name in the directory after a parser tag
				this.contents.add("File: " + file.getName());
			}
		}
		// return the full array
		return this.contents;
	}

	/**
	 * Precondition: assumes a valid connection to the server exists
	 * 
	 * This method gets the file objects on the server and stores them in an
	 * arraylist
	 * 
	 * Postcondition: An arraylist full of FTPFile objects is created
	 * 
	 * 
	 * @return An ArrayList<FTPFile> that contains all files on server as
	 *         FTPFile objects
	 * @throws IOException
	 *             Thrown then there is a issue accessing/retrieving files on
	 *             the server
	 */
	public ArrayList<FTPFile> getFiles() throws IOException {

		// Create an array of files
		// This is needed by the apache commons method used
		FTPFile[] files = this.thisFTPConn.listFiles();

		// for every file in the array
		for (FTPFile file : files) {

			// if it is a directory
			if (file.isDirectory()) {

				// change to that directory
				this.thisFTPConn.changeWorkingDirectory(file.getName());

				// recursive call
				this.getFiles();
				this.thisFTPConn.changeToParentDirectory();
			}

			// if the FTP object is a file
			if (file.isFile()) {

				// add to output array
				this.serverFiles.add(file);
			}
		}
		// return the array of the file objects used in there server
		return this.serverFiles;
	}

	/**
	 * Precondition: assumes a valid connection to the server exists
	 * 
	 * Logout, closes connection with FTP server
	 * 
	 * Postcondition: the connection with the FTP server no longer exists
	 * 
	 * 
	 * @throws IOException
	 *             Thrown when there is a serverside issue logging out
	 */
	public void logout() throws IOException {

		// use pre-existing method provided by apache commons api
		this.thisFTPConn.logout();
	}

	/**
	 * This is a method that downloads a file from the server.
	 * 
	 * 
	 * @param filePath
	 *            The absolute path to the file on the server as a string
	 * @param outputName
	 *            The name to save the file as on the client computer as a
	 *            string. can also be used to specify the save location on the
	 *            client computer
	 * @throws IOException
	 *             Thrown when there is an issue downloading the file
	 */
	public void ftpDownload(String filePath, String outputName) throws IOException, NoFileException {

		// create in inputStream that contains the file data
		// using apache commons method
		this.downloadStream = this.thisFTPConn.retrieveFileStream(filePath);

		// wrap a reader around that InputStream
		InputStreamReader in;
		try {
			in = new InputStreamReader(this.downloadStream);
		} catch (NullPointerException e) {
			throw new NoFileException("File not found on server!");
		}

		// prepare output file
		File outFile = new File(outputName);

		// wrap printwriter around output file
		PrintWriter writer = new PrintWriter(outFile);
		int readPart;

		// while there is still data in the input stream
		while ((readPart = in.read()) > -1) {
			// write the next part of the input stream to the target file
			writer.write(readPart);
		}

		// close resources
		writer.close();
		in.close();
		this.downloadStream.close();
		this.thisFTPConn.abort();
		this.thisFTPConn.sendNoOp();
	}

	/**
	 * This is an exception created to be thrown if the file that is trying to
	 * be downloaded does not exist at the specified location
	 * 
	 * 
	 * @author atm011, clc031, clr023
	 */
	public class NoFileException extends Exception {

		private static final long serialVersionUID = -10015678267944175L;

		/**
		 * Default Exception
		 * 
		 */
		public NoFileException() {
			super();
		}

		/**
		 * Exception with a custom message
		 * 
		 * 
		 * @param message
		 *            The message of the Exception
		 */
		public NoFileException(String message) {
			super(message);
		}
	}

	/**
	 * Main method of the FTPConn class, used here for testing
	 * 
	 * 
	 * @param args
	 *            Unused
	 * @throws IOException
	 *             Thrown when there is a problem with the server
	 * @throws LoginFailedException
	 *             Thrown if bad login creds are used
	 */
	public static void main(String[] args) throws IOException, LoginFailedException {

		// step 1: instantiate FTPConn object
		FTPConn testConn = new FTPConn("134.82.168.211", "demo_user", "demo_pw");

		// step 2: connect the ftpconn
		testConn.connect();

		System.out.println("Connected");
		// testConn2.ftpDownload("/Movies/movie2.avi", "testdownload6.avi");

		// step 3: call getAllContents method. this gets you an arraylist
		// of strings with file names and their paths

		ArrayList<String> thingsToParse = testConn.getAllContents(0);

		// I will print the array to show you its contents
		for (int i = 0; i < thingsToParse.size(); i++) {
			System.out.println("index " + i + " of the arraylist contains the following string:   " + thingsToParse.get(i));
		}
	}
}