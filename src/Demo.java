import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * CSCI 204, Meijer Alexander, Cole Charles, Rung Christopher
 * Assignment FTPSearcher
 * Created: 1 Dec 2011, 13:25:01
 */

/**
 * This is a demo class that displays all functionality of the project thus far
 * 
 * @author clr023, atm011, clc031
 * @version 1.0
 */
public class Demo {

	// Instance variables
	private static Scanner input = new Scanner(System.in);

	/**
	 * @param args
	 * @throws IOException
	 * @throws LoginFailedException
	 * @throws NoFileException
	 */
	public static void main(String[] args) throws IOException, FTPConn.LoginFailedException, FTPConn.NoFileException {

		System.out.println("Welcome to FBGM's FTP server searching demo.");
		System.out.println("");

		// prompt for server name
		System.out.print("Please enter the name/IP of the FTP server you want to search: ");
		String serverName = input.nextLine();
		System.out.println("");

		// prompt for username
		System.out.print("Please enter your username: ");
		String userName = input.nextLine();
		System.out.println("");

		// prompt for password
		System.out.print("Please enter your password: ");
		String password = input.nextLine();
		System.out.println("");

		// create ftp connection, connect
		System.out.println("Creating ftp connection using following credentials:");
		System.out.println("Server name: " + serverName);
		System.out.println("Username:    " + userName);
		System.out.println("Password:    " + password);
		System.out.println("");
		FTPConn demoConn = new FTPConn(serverName, userName, password);
		System.out.print("FTP connection created. Connect to server? (y/n) ");
		String connectionChoice = input.nextLine();

		if (connectionChoice.equalsIgnoreCase("n")) {
			System.exit(0);
		}

		// connect to FTP
		System.out.println("");
		System.out.println("Establishing connection...");
		demoConn.connect();
		System.out.println("Reply from " + serverName + ":");
		System.out.println("");
		System.out.println(demoConn.getReply());
		System.out.println("Connection Established!");

		// show iteration if needed
		System.out.println("");
		System.out.print("Display recursive file/directory retrieval? (y/n)");
		String recurChoice = input.nextLine();

		if (recurChoice.equalsIgnoreCase("y")) {
			System.out.println("Showing recursion through server directories.");
			demoConn.showProcess();
		} else {
			System.out.println("Not Showing recursion through server directories.");
		}

		ArrayList<String> contents = demoConn.getAllContents(0);
		System.out.println("");
		System.out.println("");
		System.out.print("Print output to parser? (y/n) ");
		if (input.nextLine().equals("y")) {
			System.out.println("The FTPConn class will send the parser an array list containing the following:");
			System.out.println("");
			for (int i = 0; i < contents.size(); i++) {
				System.out.println(contents.get(i));
			}
		}

		System.out.println("At this point, the FTPConn's job is finished.\n");
		System.out.println("---------------Stage II: Parsing----------------------");

		// Parser's section
		System.out.println("Creating new parser object...");
		Parser p = new Parser(contents);
		ArrayList<FileObject> files = p.parse();
		System.out.println("Created successfully!");
		System.out.print("Print list of files on server? (y/n) ");
		if (input.nextLine().equals("y")) {
			p.printFiles(files);
		}
		System.out.println("\nAt this point, the parser's job is finished.\n");
		System.out.println("---------------Stage III: Storage in tree----------------------");

		// tree section
		Tree tree = new Tree(new FileObject("root", "/", true));

		for (int i = 0; i < files.size(); i++) {
			try {
				tree.add(files.get(i));
			} catch (Tree.InvalidPathException e1) {
				e1.printStackTrace();
			}
		}

		System.out.println("\nTree successfully built.\n");

		// TO-DO
		System.out.println("---------------Stage IV: Searching----------------------");

		// searcher section
		System.out.print("Search for a file: ");

		FileObject[] results = tree.find(input.nextLine());

		if (results == null) {
			System.out.println("No files were found.");
		} else {
			System.out.println(results.length + " file(s) were found:\n");

			for (int i = 0; i < results.length; i++) {
				System.out.println("File " + i + 1 + ": " + results[i].getFileName());
			}
		}

		System.out.println("\nAt this point, the tree & searcher's jobs are finished.");
		System.out.println("---------------Stage V: Downloading----------------------");
		// download section
		System.out.print("Download file(s) from server? (y/n) ");
		if (input.nextLine().equals("y")) {
			System.out.print("File number: ");
			if (input.nextLine().equals("1")) {
				System.out.println("Downloading " + results[0].getAbsPath().trim() + "/" + results[0].getFileName().trim());

				demoConn.ftpDownload(results[0].getAbsPath().trim() + "/" + results[0].getFileName().trim(), results[0].getFileName().trim());

				System.out.println("Downloading complete");
			}
		}

		System.out.println("\nlogging out...");
		demoConn.logout();
		System.out.println("Goodbye!");
	}
}
