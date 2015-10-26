# FTP Searcher
This was our culminating project for an Introduction to Computer Science class at [Bucknell University](http://www.bucknell.edu).  It uses Java to launch a GUI that connects to an FTP server and allows a user to search for and download files on the server.
## Usage
1. Run GUI.java in eclipse or compile and run on the command line.  A window will appear with fields to input login credentials.

<p align="center">
<img src="https://raw.github.com/clrung/FTPSearcher/master/images/Login_Screen.png" alt="Login Screen"/>
</p>

2. Click the 'Connect' button, and a dialog box will indicate that a connection has been established.
3. Search for a filename in the search box and click the Search button.

 * If multiple files are found with the same filename, a numbered list showing the filename and absolute path will appear.  Choose which file number to download in the dropdown and click the 'Download' button.

 <p align="center">
 <img src="https://raw.github.com/clrung/FTPSearcher/master/images/Search_Results.png" alt="Search Results"/>
 </p>

4. If the filename and path are known, it may be input in the bottom text box and downloaded via the 'Download' button, forgoing the search process.
5. After the 'Download' button is clicked, a chooser will appear to designate the save location and name of the file to be downloaded.
6. To terminate the connection with the server, click the 'Logoff' button.  A dialog box will indicate that the logoff was successful.

## Design
There are three main components of this program:

1. `FTPConn`
 * Handles all integration with the server.  This class obtains information about the server's contents, file information, and is used to download files.
2. `Parser`
 * Retrieves an `ArrayList` of `String`s from `FTPConn`, containing every file and directory path, and creates an `ArrayList` of `FileObject`s to be sent to the `Tree`.
3. `Tree`
 * Reads the `Parser`s output and generates a tree of `FileObject`s.  This is used to search the server's contents efficiently.  When a search is performed, an `ArrayList` of `FileObject`s are sent to the `GUI`, and the chosen `FileObject` is sent to the download method of the `FTPConn`.

## Testing
Each class is tested with the [JUnit framework](http://junit.org).

After a connection to a server has been established, the program recursively iterates through all of the directories on the server and generates a Tree, containing `FileObject`s.  A `FileObject` contains the file name, path, and a boolean flag that indicates if it is a directory.

## Class Relationships
![Class Relationships](https://raw.github.com/clrung/FTPSearcher/master/images/Class_Relationships.png)

## UML Diagram
![UML Diagram](https://raw.github.com/clrung/FTPSearcher/master/images/UML_Diagram.png)