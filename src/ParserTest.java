/**
 * CSCI 204 - Brian King & Xiannong Meng
 * 
 * Assignment: FTPSearcher - Final Project
 * Team Members: Charles Cole, Christopher Rung, Alex Meijer
 * Created: Nov 17, 2011, 4:56:37 PM
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the <code>Parser</code> class
 *
 * 
 * @author atm011, clr023, clc031
 * @version 1.0
 */
public class ParserTest {

	// Instance variables
	private ArrayList<String> testInput;
	private Parser testParser;
	private ArrayList<FileObject> output;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.testInput = new ArrayList<String>();

		// add output of FTPConn class
		this.testInput.add("Path: /Documents"); // 0
		this.testInput.add("File: Doc1.txt"); // 1
		this.testInput.add("File: Doc2.txt"); // 2
		this.testInput.add("Path: /Documents/Doc3"); // 3
		this.testInput.add("File: Doc3.txt"); // 4
		this.testInput.add("Path: /Movies"); // 5
		this.testInput.add("File: movie1.mkv"); // 6
		this.testInput.add("File: movie2.avi"); // 7
		this.testInput.add("File: movie3.mkv"); // 8
		this.testInput.add("Path: /Music"); // 9
		this.testInput.add("File: song1.mp3"); // 10
		this.testInput.add("File: song34.mp3"); // 11
		this.testInput.add("File: song3.m4p"); // 12
		this.testInput.add("Path: /"); // 13
		this.testInput.add("File: rootDoc1.txt"); // 14
		this.testInput.add("File: rootDoc2.txt"); // 15
		this.testInput.add("Path: /Software"); // 16
		this.testInput.add("Path: /Software/mac software"); // 17
		this.testInput.add("File: macprogram.dmg"); // 18
		this.testInput.add("Path: /Software/mac software/photo editing"); // 19
		this.testInput.add("File: macphotosoftware.dmg"); // 20
		this.testInput.add("Path: /Software/windows software"); // 21
		this.testInput.add("File: windowsprogram2.msi"); // 22
		this.testInput.add("File: windowstestprogram1.exe"); // 23

		this.testParser = new Parser(this.testInput);
		this.output = this.testParser.parse();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.testInput = null;
		this.testParser = null;
		this.output = null;
	}

	/**
	 * Test method for {@link Parser#parse()}.
	 */
	@Test
	public void testParse() {
		assertTrue(this.output.get(19).getAbsPath().equalsIgnoreCase("/Software/mac software/photo editing"));
		assertTrue(this.output.get(19).isDirectory());
		assertTrue(this.output.get(19).getFileName().equals("photo editing"));
	}

	/**
	 * Test method for {@link Parser#parse()}.
	 */
	@Test
	public void testParse2() {
		assertTrue(this.output.get(0).getAbsPath().equalsIgnoreCase("/Documents"));
		assertTrue(this.output.get(0).isDirectory());
		assertTrue(this.output.get(0).getFileName().equals("Documents"));
	}

	/**
	 * Test method for {@link Parser#parse()}.
	 */
	@Test
	public void testParse3() {
		assertTrue(this.output.get(10).getAbsPath().equalsIgnoreCase("/Music"));
		assertFalse(this.output.get(10).isDirectory());
		assertTrue(this.output.get(10).getFileName().equals("song1.mp3"));
	}

	/**
	 * Test method for {@link Parser#parse()}.
	 */
	@Test
	public void testParse4() {
		assertTrue(this.output.get(13).getAbsPath().equalsIgnoreCase("/"));
		assertTrue(this.output.get(13).isDirectory());
		assertTrue(this.output.get(13).getFileName().equals(""));
	}

	/**
	 * Test method for {@link Parser#parse()}.
	 */
	@Test
	public void testParse5() {
		assertTrue(this.output.get(14).getAbsPath().equalsIgnoreCase("/"));
		assertFalse(this.output.get(14).isDirectory());
		assertTrue(this.output.get(14).getFileName().equals("rootDoc1.txt"));
	}

	/**
	 * Test method for {@link Parser#parse()}.
	 */
	@Test
	public void testParse6() {
		assertTrue(this.output.get(23).getAbsPath().equalsIgnoreCase("/Software/windows software"));
		assertFalse(this.output.get(23).isDirectory());
		assertTrue(this.output.get(23).getFileName().equals("windowstestprogram1.exe"));
	}
}
