/**
 * CSCI 204 - Brian King
 * 
 * Assignment: FTPSearcher - Final Project
 * Team Members: Charles Cole, Christopher Rung, Alex Meijer
 * Created: Nov 16, 2011, 10:46:37 PM
 */

import java.util.ArrayList;

/**
 * The Tree class - implements a tree
 * 
 * 
 * @author clc031, clr023, atm011
 * @version 1.0
 */
public class Tree {

	// Instance variables
	private Node root;
	private int totalCount;
	private int fileCount;
	private int dirCount;

	/**
	 * Default constructor
	 * 
	 */
	public Tree() {
		this.root = null;
		this.totalCount = 0;
		this.fileCount = 0;
		this.dirCount = 0;
	}

	/**
	 * Private constructor that creates a new <code>Tree</code> with a specified
	 * root <code>Node</code>
	 * 
	 * 
	 * @param root
	 *            The root of the <code>Tree</code>
	 */
	@SuppressWarnings("unused")
	private Tree(Node root) {
		this.root = root;
		this.totalCount = root.getTotalCount();
		this.fileCount = root.getFileCount();
		this.dirCount = root.getDirCount();
	}

	/**
	 * Constructor that creates a new <code>Tree</code> with a specified root
	 * <code>FileObject</code>
	 * 
	 * 
	 * @param root
	 *            The root of the <code>Tree</code>
	 */
	public Tree(FileObject root) {
		this.root = new Node(root);
		this.totalCount = this.root.getTotalCount();
		this.fileCount = this.root.getFileCount();
		this.dirCount = this.root.getDirCount();
	}

	/**
	 * Get the root <code>FileObject</code>
	 * 
	 * 
	 * @return The root <code>FileObject</code>
	 */
	public FileObject getRoot() {
		return (this.root != null ? this.root.getFileObject() : null);
	}

	/**
	 * Set the root <code>FiileObject</code> of the <code>Tree</code>
	 * 
	 * 
	 * @param root
	 *            <code>FileObject</code> to set as the root
	 */
	public void setRoot(FileObject root) {
		// Create a new Node that will be the root Node
		this.root = new Node(root);
		// Set the parameters of the Node to be that of the root
		this.root.setRootParam();

		// Update the directory and file count
		if (root.isDirectory()) {
			this.totalCount++;
			this.dirCount++;
		} else {
			this.totalCount++;
			this.fileCount++;
		}
	}

	/**
	 * Get the total count of files and directories in the <code>Tree</code>
	 * 
	 * 
	 * @return The total count of files and directories in the <code>Tree</code>
	 */
	public int getTotalCount() {
		this.totalCount = this.root.getTotalCount();

		return this.totalCount;
	}

	/**
	 * Get the total count of files in the <code>Tree</code>
	 * 
	 * 
	 * @return The total count of files in the <code>Tree</code>
	 */
	public int getFileCount() {
		this.fileCount = this.root.getFileCount();

		return this.fileCount;
	}

	/**
	 * Get the total count of directories in the <code>Tree</code>
	 * 
	 * 
	 * @return The total count of directories in the <code>Tree</code>
	 */
	public int getDirCount() {
		this.dirCount = this.root.getDirCount();

		return this.dirCount;
	}

	/**
	 * Clear the entire tree
	 * 
	 */
	public void clear() {
		this.root = null;
		this.totalCount = 0;
		this.fileCount = 0;
		this.dirCount = 0;
	}

	/**
	 * Adds a <code>FileObject</code> to the <code>Tree</code>
	 * 
	 * 
	 * @param fileObj
	 *            The <code>FileObject</code> to add to the <code>Tree</code>
	 * 
	 * @throws InvalidPathException
	 *             If the root folder of the file is not the same as the root
	 *             folder of the <code>Tree</code>
	 */
	public void add(FileObject fileObj) throws InvalidPathException {
		// If there is no root, make the fileObj the root Node
		if (this.root == null) {
			this.root = new Node(new FileObject("_ROOT_", "/", true));
			this.root.setChild(fileObj);
		} else {
			// Add the Node at its proper location
			if (fileObj.getAbsPath().equals("/")
					|| (fileObj.getAbsPath().indexOf('/') == fileObj.getAbsPath().lastIndexOf('/') && fileObj.isDirectory())) {
				this.root.addChild(fileObj);
			} else {
				// If the fileObj doesn't belong in the root folder, you must
				// recurse through all of the subfolders to find its location
				add_HELP(this.root, fileObj);
			}
		}
	}

	/**
	 * Private method for helping to add a file to the <code>Tree</code>
	 * 
	 * 
	 * @param node
	 *            The current <code>Node</code> (starts with the root)
	 * @return <code>true</code> if the file was added to the <code>Tree</code>,
	 *         <code>false</code> if it wasn't
	 */
	private boolean add_HELP(Node node, FileObject fileObj) {
		// An ArrayList to hold all of the children of the current Node
		ArrayList<Node> list = new ArrayList<Node>();

		// Iterate through all of the children and add them to the ArrayList
		Node currentNode = node.child;
		while (currentNode != null) {
			list.add(currentNode);
			currentNode = currentNode.next;
		}

		// Helper path of the file - removes the last folder in the path
		String helpPath = fileObj.getAbsPath().substring(0, fileObj.getAbsPath().lastIndexOf('/'));

		// Iterate through all of the children of the Node
		for (int i = 0; i < list.size(); i++) {
			// If the fileObj is a directory
			if (fileObj.isDirectory() && list.get(i).fileObj.getAbsPath().equals(helpPath)) {
				list.get(i).addChild(fileObj);
				return true;
				// If the fileObj is not a directory
			} else if (!fileObj.isDirectory() && list.get(i).fileObj.getAbsPath().equals(fileObj.getAbsPath())) {
				list.get(i).addChild(fileObj);
				return true;
			} else {
				// Recurse deeper to find where to add the fileObj
				add_HELP(list.get(i), fileObj);
			}
		}
		return false;
	}

	/**
	 * Adds a file to the <code>Tree</code> based on the arrays of information
	 * given
	 * 
	 * The <code>FileObject</code> will be created using the same index value
	 * for each <code>ArrayList</code>. This means that the first
	 * <code>FileObject</code> using an index of 0 for each array, 1, for the
	 * second in each array, ect.
	 * 
	 * If all three array lists that are passed are not the same length, an
	 * <code>Exception</code> is thrown
	 * 
	 * 
	 * @param fileName
	 *            array of the file names
	 * @param absPath
	 *            array of the file paths
	 * @param isDirectory
	 *            array of directory boolean expressions
	 * 
	 * @throws InvalidArrayListException
	 *             If all of the <code>ArrayList</code> objects are not the same
	 *             length
	 * @throws InvalidPathException
	 *             If the root folder of the file is not the same as the root
	 *             folder of the <code>Tree</code>
	 */
	public void add(ArrayList<String> fileName, ArrayList<String> absPath, ArrayList<Boolean> isDirectory) throws InvalidArrayListException,
			InvalidPathException {

		// Check to see if all of the arrays are the same length
		if (fileName.size() != absPath.size() || absPath.size() != isDirectory.size() || isDirectory.size() != fileName.size()) {
			throw new InvalidArrayListException();
		}

		// Iterate through the ArrayLists and add the files to the Tree
		for (int i = 0; i < fileName.size(); i++) {
			add(new FileObject(fileName.get(i), absPath.get(0), isDirectory.get(i)));
		}
	}

	/**
	 * Search for a <code>FileObject</code> in the <code>Tree</code>
	 * 
	 * 
	 * @param criteria
	 *            The search criteria
	 * @return an array of type <code>FileObject</code> containing all the
	 *         matching files, or <code>null</code> if none were found
	 */
	public FileObject[] find(String criteria) {
		ArrayList<FileObject> list = new ArrayList<FileObject>();
		list = find_HELP(this.root, criteria);

		if (list.size() == 0) {
			return null;
		} else {

			FileObject[] filesFound = new FileObject[list.size()];

			for (int i = 0; i < filesFound.length; i++) {
				filesFound[i] = list.get(i);
			}

			return filesFound;
		}
	}

	/**
	 * A private helper method for the <code>find(String)</code> method
	 * 
	 * 
	 * @param node
	 *            The current <code>Node</code> being searched
	 * @param criteria
	 *            The search criteria
	 * @return an <code>ArrayList<FileObject></code> object, or
	 *         <code>null</code> if nothing was found
	 */
	private ArrayList<FileObject> find_HELP(Node node, String criteria) {
		// ArrayList to hold the children
		ArrayList<Node> list = new ArrayList<Node>();
		// ArrayList of the results that were found
		ArrayList<FileObject> results = new ArrayList<FileObject>();

		// Add the children to the ArrayList
		Node currentNode = node.child;
		while (currentNode != null) {
			list.add(currentNode);
			currentNode = currentNode.next;
		}

		// Iterate through the children
		for (int i = 0; i < list.size(); i++) {
			// If we find a match, add it to the results list
			if (list.get(i).fileObj.getFileName().equalsIgnoreCase(criteria)) {
				results.add(list.get(i).fileObj);
			}

			// Recursively search the rest of the Tree
			ArrayList<FileObject> fo = find_HELP(list.get(i), criteria);
			if (fo != null) {
				for (int j = 0; j < fo.size(); j++) {
					results.add(fo.get(j));
				}
			}
		}
		return results;
	}

	/**
	 * toString
	 * 
	 * Returns a multi-line String representation of this tree. Subtrees are
	 * recursively indented, showing the tree hierarchically.
	 */
	public String toString() {
		return toString_HELP(this.root, 0);
	}

	/**
	 * Private helper method for toString()
	 * 
	 * Returns a multi-line string representation of the given subtree.
	 * 
	 * 
	 * @param node
	 *            The tree to print.
	 * @param indent
	 *            The number of spaces to indent (increases by 2 for each
	 *            level).
	 */
	private String toString_HELP(Node node, int indent) {
		// String to hold the output
		String result = "";

		// If we don't have any more Nodes
		if (node == null) {
			return "";
		}

		// The level of indentation we need
		String indentation = "";
		for (int i = 0; i < indent; i++) {
			indentation += " ";
		}

		// ArrayList to hold the children
		ArrayList<Node> list = new ArrayList<Node>();

		// Add the children to the ArrayList
		Node currentNode = node.child;
		while (currentNode != null) {
			list.add(currentNode);
			currentNode = currentNode.next;
		}

		// Iterate through the children
		for (int i = 0; i < list.size(); i++) {
			// Recurse through it further if the Node is a directory
			if (list.get(i).fileObj.isDirectory()) {
				result = result + (indentation + list.get(i).fileObj.getFileName() + "\n");
				result = result + toString_HELP(list.get(i), indent + 2);
			} else {
				result = result + (indentation + list.get(i).fileObj.getFileName() + "\n");
			}
		}
		return result;
	}

	/**
	 * A nested class used to manage each leaf, parent, child, ect., in the tree
	 * 
	 * 
	 * @author clc031 (Charles Cole)
	 * 
	 * @version 0.2
	 */
	public static class Node {

		// Instance variables
		private FileObject fileObj;
		private Node next;
		private Node prev;
		private Node parent;
		private Node child;

		/**
		 * Default Constructor
		 * 
		 */
		public Node() {
			this.fileObj = null;
			this.next = null;
			this.prev = null;
			this.parent = null;
			this.child = null;
		}

		/**
		 * Constructs a <code>Node</code> containing the given
		 * <code>FileObject</code>
		 * 
		 * 
		 * @param obj
		 *            The <code>FileObject</code> to add to the
		 *            <code>Node</code>
		 */
		public Node(FileObject obj) {
			this.fileObj = obj;
		}

		/**
		 * Constructor that sets the instance variables of the <code>Node</code>
		 * correctly if it is the root
		 * 
		 */
		public void setRootParam() {
			this.next = null;
			this.prev = null;
			this.parent = null;
		}

		/**
		 * This method checks to see whether the current <code>Node</code> is
		 * the root or not
		 * 
		 * 
		 * @return <code>true</code> if the current <code>Node</code> is the
		 *         root, <code>false</code> if it isn't
		 */
		public boolean isRoot() {
			return (this.next == null && this.prev == null && this.parent == null ? true : false);
		}

		/**
		 * Set the <code>FileObject</code> that the current <code>Node</code>
		 * should contain
		 * 
		 * 
		 * @param fileObj
		 *            The <code>FileObject</code> that the current
		 *            <code>Node</code> should contain
		 */
		public void setFileObject(FileObject fileObj) {
			this.fileObj = fileObj;
		}

		/**
		 * Get the <code>FileObject</code> of the current <code>Node</code>
		 * 
		 * 
		 * @return The <code>FileObject</code> of the current <code>Node</code>
		 */
		public FileObject getFileObject() {
			return this.fileObj;
		}

		/**
		 * Returns the total number of Nodes that the current Node contains,
		 * including the current Node itself
		 * 
		 * 
		 * @return The total number of Nodes that the current Node contains,
		 *         including the current Node itself
		 */
		public int getTotalCount() {
			return (getFileCount() + getDirCount());
		}

		/**
		 * Returns the total number of file Nodes that the current Node
		 * contains, including the current Node itself
		 * 
		 * 
		 * @return The total number of file Nodes that the current Node
		 *         contains, including the current Node itself
		 */
		public int getFileCount() {
			// Total file count
			int count = 0;
			// ArrayList to store the children of a Node
			ArrayList<Node> list = new ArrayList<Node>();

			// Retrieve all of the children
			Node currentNode = this.child;
			while (currentNode != null) {
				list.add(currentNode);
				currentNode = currentNode.next;
			}
			// Iterate through all of the children,
			// and repeat on their children, ect.
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).fileObj.isDirectory()) {
					count++;
				} else {
					count += list.get(i).getFileCount();
				}
			}
			return count;
		}

		/**
		 * Returns the total number of directory Nodes that the current Node
		 * contains, including the current Node itself
		 * 
		 * 
		 * @return The total number of directory Nodes that the current Node
		 *         contains, including the current Node itself
		 */
		public int getDirCount() {
			// Count the root file
			int count = 0;

			if (this.fileObj.getFileName().equals("_ROOT_")) {
				count--;
			}

			// ArrayList to store the children of a Node
			ArrayList<Node> list = new ArrayList<Node>();

			// Retrieve all of the children
			Node currentNode = this;
			while (currentNode != null) {
				list.add(currentNode);
				currentNode = currentNode.next;
			}

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).fileObj.isDirectory()) {
					count++;
					if (list.get(i).child != null) {
						count += list.get(i).child.getDirCount();
					}
				}
			}
			return count;
		}

		/**
		 * Set the parent of the current <code>Node</code>
		 * 
		 * 
		 * @param node
		 *            The parent <code>Node</code>
		 */
		public void setParent(Node node) {
			this.parent = node;
		}

		/**
		 * Get the parent of the current <code>Node</code>
		 * 
		 * 
		 * @return The parent <code>Node</code>
		 */
		public Node getParent() {
			return this.parent;
		}

		/**
		 * Set the next <code>Node</code> of the current <code>Node</code>
		 * 
		 * 
		 * @param node
		 *            The next <code>Node</code>
		 */
		public void setNext(Node node) {
			this.next = node;
		}

		/**
		 * Get the next <code>Node</code> of the current <code>Node</code>
		 * 
		 * 
		 * @return The next <code>Node</code>
		 */
		public Node getNext() {
			return this.next;
		}

		/**
		 * Check to see if there is another sister <code>Node</code>
		 * 
		 * 
		 * @return <code>true</code> if there is another <code>Node</code>,
		 *         <code>false</code> if there isn't
		 */
		public boolean hasNext() {
			return (this.next != null ? true : false);
		}

		/**
		 * Set the previous <code>Node</code> of the current <code>Node</code>
		 * 
		 * 
		 * @param node
		 *            The previous <code>Node</code>
		 */
		public void setPrev(Node node) {
			this.prev = node;
		}

		/**
		 * Get the previous <code>Node</code> of the current <code>Node</code>
		 * 
		 * 
		 * @return The previous <code>Node</code>
		 */
		public Node getPrev() {
			return this.prev;
		}

		/**
		 * Check to see if there is a previous sister <code>Node</code>
		 * 
		 * 
		 * @return <code>true</code> if there is a previous <code>Node</code>,
		 *         <code>false</code> if there isn't
		 */
		public boolean hasPrev() {
			return (this.prev != null ? true : false);
		}

		/**
		 * Set the child <code>Node</code> of the current <code>Node</code>
		 * 
		 * 
		 * @param node
		 *            The child <code>Node</code>
		 */
		public void setChild(Node node) {
			this.child = node;
		}

		/**
		 * Set the child <code>FileObject</code> of the current
		 * <code>Node</code>
		 * 
		 * 
		 * @param fileObj
		 *            The child <code>FileObject</code>
		 */
		public void setChild(FileObject fileObj) {
			this.child = new Node(fileObj);
		}

		/**
		 * Get the child <code>Node</code> of the current <code>Node</code>
		 * 
		 * 
		 * @return The child <code>Node</code>
		 */
		public Node getChild() {
			return this.child;
		}

		/**
		 * Check to see if there is a child <code>Node</code>
		 * 
		 * 
		 * @return <code>true</code> if there is a child <code>Node</code>,
		 *         <code>false</code> if there isn't
		 */
		public boolean hasChild() {
			return (this.child != null ? true : false);
		}

		/**
		 * Add a child to a <code>Node</code>
		 * 
		 * 
		 * @param fileObj
		 *            The <code>FileObject</code> to be a child
		 */
		public void addChild(FileObject fileObj) {
			// If the Node has no children, set its child to the given fileObj
			if (this.child == null) {
				this.child = new Node(fileObj);
			} else { // Go to the end of the child list and add it to the end
				Node currentNode = this.child;
				while (currentNode.hasNext()) {
					currentNode = currentNode.getNext();
				}
				currentNode.setNext(new Node(fileObj));
			}
		}

		/**
		 * Checks if a <code>Node</code> is equal to another <code>Node</code>
		 * 
		 * 
		 * @param node
		 *            The <code>Node</code> to compare it to
		 * @return <code>true</code if the two Nodes are the same,
		 *         <code>false</code> if they aren't
		 */
		public boolean isEqual(Node node) {
			// Check the fileName, path, and isDirectory variables
			return (this.fileObj.getFileName().equalsIgnoreCase(node.fileObj.getFileName())
					&& this.fileObj.getAbsPath().equalsIgnoreCase(node.fileObj.getAbsPath())
					&& this.fileObj.isDirectory() == node.fileObj.isDirectory() ? true : false);
		}

		/**
		 * toString method
		 * 
		 * Returns the name, path, and directory status of the FileObject that
		 * the Node contains
		 */
		public String toString() {
			return "\n" + this.fileObj.toString() + "\nNext: " + this.next + "\nPrev: " + this.prev + "\nParent: " + this.parent + "\nChild: "
					+ this.child;
		}
	}

	/**
	 * InvalidArrayListException
	 * 
	 * Thrown if all of the <code>ArrayList</code> objects containing
	 * information to be added to the tree are not the same length
	 * 
	 * 
	 * @author clc031 (Charles Cole)
	 * 
	 * @version 0.5
	 */
	protected static class InvalidArrayListException extends Exception {

		private static final long serialVersionUID = -6594962340528505337L;

		/**
		 * Default Exception
		 * 
		 */
		public InvalidArrayListException() {
			super("Arrays are not the same length!");
		}

		/**
		 * Exception with a custom message
		 * 
		 * 
		 * @param message
		 *            The message of the Exception
		 */
		public InvalidArrayListException(String message) {
			super(message);
		}
	}

	/**
	 * InvalidPathException
	 * 
	 * Thrown if the root folder of a file trying to be added is not the same as
	 * the root folder of the <code>Tree</code>
	 * 
	 * 
	 * @author clc031 (Charles Cole)
	 * 
	 * @version 0.5
	 */
	protected static class InvalidPathException extends Exception {

		private static final long serialVersionUID = -1123701404696645035L;

		/**
		 * Default Exception
		 * 
		 */
		public InvalidPathException() {
			super("This file does not point to the same location!");
		}

		/**
		 * Exception with a custom message
		 * 
		 * 
		 * @param message
		 *            The message of the Exception
		 */
		public InvalidPathException(String message) {
			super(message);
		}
	}

	/**
	 * Main method - used for testing
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// TESTING

	}
}