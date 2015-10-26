/**
 * CSCI 204 - Brian King & Xiannong Meng
 * 
 * Assignment: FTPSearcher - Final Project
 * Team Members: Charles Cole, Christopher Rung, Alex Meijer
 * Created: Nov 17, 2011, 4:56:37 PM
 */

import java.util.ArrayList;

/**
 * This class is a parser class which takes raw directory and file data from the
 * FTPConn class and parses that data into file objects. This is an important
 * class because as the files names and directory names are returned from the
 * FTP class, they are not associated with each other. This class associates the
 * file name with the directory that it in in in the FileObjects that it outputs
 * 
 * 
 * @author atm011, clr023, clc031
 * @version 1.0
 */
public class Parser {

	// Instance variable
	private ArrayList<String> contents;

	/**
	 * Default constructor
	 * 
	 */
	public Parser() {
		this.contents = null;
	}

	/**
	 * The constructor for this class. Creates a new parser object wrapped
	 * around the output from a FTPConn object
	 * 
	 * 
	 * @param contents
	 *            An ArrayList<String> that contains the output of the FTPConn
	 *            class, both file and directory names
	 */
	public Parser(ArrayList<String> contents) {
		// store internally
		this.contents = contents;
	}

	/**
	 * The main method of this class. This method parses the data from the
	 * FTPCOnn class by associating the data (file and directory names) in a
	 * fileObject object
	 * 
	 * this.
	 * 
	 * @return An ArrayList of File objects that contain the file/directory
	 *         names and their associated paths
	 */
	public ArrayList<FileObject> parse() {
		ArrayList<FileObject> files = new ArrayList<FileObject>();
		String fileName = null;
		String absPath = null;
		boolean isDirectory = false;

		// for every line of input from the FTPConn object
		for (int i = 0; i < this.contents.size(); i++) {

			// if the line has the tag Path
			if (this.contents.get(i).substring(0, 4).equals("Path")) {
				absPath = this.contents.get(i).substring(6);
				fileName = this.contents.get(i).substring(this.contents.get(i).lastIndexOf("/") + 1);

				// it is a directory
				isDirectory = true;

				// if it has the file tag
			} else if (this.contents.get(i).substring(0, 4).equals("File")) {
				fileName = this.contents.get(i).substring(6);

				// files are not directories
				isDirectory = false;
			}
			// add new file object to output ArrayList
			files.add(new FileObject(fileName, absPath, isDirectory));
		}
		// return completed list
		return files;
	}

	/**
	 * This method prints all the files in the passed ArrayList. Is formatted to
	 * show the association of file names and paths in each fileObject in the
	 * array. Used for testing purposes.
	 * 
	 * 
	 * @param files
	 *            An array list of file objects to be printed
	 */
	public void printFiles(ArrayList<FileObject> files) {
		// for every file object in the array list
		for (int i = 0; i < files.size(); i++) {
			// print all data as a contiguous block
			System.out.println("File " + (i + 1) + ": ");
			System.out.println("     Name: " + files.get(i).getFileName());
			System.out.println("     Path: " + files.get(i).getAbsPath());
			System.out.println("     isDirectory?: " + files.get(i).isDirectory());
			System.out.println("");
		}
	}
}