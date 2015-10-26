/**
 * CSCI 204 - Brian King & Xiannong Meng
 * 
 * Assignment: FTPSearcher - Final Project
 * Team Members: Charles Cole, Christopher Rung, Alex Meijer
 * Created: Nov 17, 2011, 4:56:37 PM
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the <code>FileObject</code> class
 * 
 * 
 * @author atm011, clr023, clc031
 * @version 1.0
 */
public class FileObjectTest {

	FileObject testFile;
	FileObject testDir;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.testFile = new FileObject("testDoc", "/test/Documents", false);
		this.testDir = new FileObject("testDir", "/testdir", true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.testFile = null;
		this.testDir = null;
	}

	/**
	 * Test method for {@link FileObject#getFileName()}.
	 */
	@Test
	public void testGetFileName() {
		assertTrue(this.testFile.getFileName().equalsIgnoreCase("testDoc"));
		assertTrue(this.testDir.getFileName().equalsIgnoreCase("testDir"));
	}

	/**
	 * Test method for {@link FileObject#setFileName(java.lang.String)}.
	 */
	@Test
	public void testSetFileName() {
		this.testFile.setFileName("ChangedName");
		this.testDir.setFileName("Changedname 2");

		assertTrue(this.testFile.getFileName().equalsIgnoreCase("ChangedName"));
		assertTrue(this.testDir.getFileName().equalsIgnoreCase("Changedname 2"));
	}

	/**
	 * Test method for {@link FileObject#hasFileName()}.
	 */
	@Test
	public void testHasFileName() {
		assertTrue(testFile.hasFileName());
		this.testFile.setFileName(null);
		assertFalse(testFile.hasFileName());
	}

	/**
	 * Test method for {@link FileObject#getAbsPath()}.
	 */
	@Test
	public void testGetAbsPath() {
		assertTrue(testFile.getAbsPath().equalsIgnoreCase("/test/Documents"));
		assertTrue(testDir.getAbsPath().equalsIgnoreCase("/testdir"));

	}

	/**
	 * Test method for {@link FileObject#setAbsPath(java.lang.String)}.
	 */
	@Test
	public void testSetAbsPath() {
		testFile.setAbsPath("/new/path");
		assertTrue(testFile.getAbsPath().equals("/new/path"));
	}

	/**
	 * Test method for {@link FileObject#hasAbsPath()}.
	 */
	@Test
	public void testHasAbsPath() {
		assertTrue(this.testFile.hasAbsPath());
		assertTrue(this.testDir.hasAbsPath());

		this.testFile.setAbsPath(null);
		this.testDir.setAbsPath(null);

		assertFalse(this.testFile.hasAbsPath());
		assertFalse(this.testDir.hasAbsPath());
	}

	/**
	 * Test method for {@link FileObject#isDirectory()}.
	 */
	@Test
	public void testIsDirectory() {
		assertTrue(this.testDir.isDirectory());
		assertFalse(this.testFile.isDirectory());
	}

	/**
	 * Test method for {@link FileObject#search(java.lang.String)}.
	 */
	@Test
	public void testSearch() {
		assertTrue(this.testFile.search("testDoc"));
		assertFalse(this.testFile.search("purple"));

		assertTrue(this.testDir.search("testDir"));
		assertFalse(this.testDir.search("notTestDir"));
	}
}
