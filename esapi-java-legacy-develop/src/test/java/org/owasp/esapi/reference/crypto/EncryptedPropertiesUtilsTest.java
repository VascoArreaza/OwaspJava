/**
 * OWASP Enterprise Security API (ESAPI)
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */
package org.owasp.esapi.reference.crypto;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;


/**
 * The Class EncryptedPropertiesTest.
 * 
 * @author August Detlefsen (augustd at codemagi dot com)
 *         <a href="http://www.codemagi.com">CodeMagi, Inc.</a>
 * @since October 8, 2010
 */
public class EncryptedPropertiesUtilsTest {

	private static final String KEY1	= "quick";
	private static final String VALUE1	= "brown fox";
	private static final String KEY2	= "jumps";
	private static final String VALUE2	= "lazy dog";
	private static final String KEY3	= "joe bob";
	private static final String VALUE3	= "jim bob";
	private static final String KEY4	= "sally sue";
	private static final String VALUE4	= "betty mae";

	
	/** Rule to acquire the running test's information at runtime.*/
	@Rule
	public TestName testName = new TestName();

	/** File management component for IO targets during test executions.*/
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	/** Counter used to track the number of files created for a single test to assist with creating unique file references.*/
	private int fileIndex = 0;
	
	/**
	 * Creates a new properties file reference for the active test which will be cleaned up upon test completion.
	 * @return File New unique file reference for the active test.
	 * @throws IOException If a new file could not be generated.
	 */
	private final File getTempPropertiesFile() throws IOException {
	    return tempFolder.newFile(String.format("%s_%2d.properties", testName.getMethodName(), fileIndex++));
	}
	/**
	 * Test of creating and storing a new EncryptedProperties from scratch,
	 * as if calling:
	 * <code>
	 *     EncryptedPropertiesUtils --out encrypted.properties
	 * </code>
	 *
	 * @throws Exception Any exception that occurs
	 */
	@Test public void testCreateNew() throws Exception {
	    File encryptedFile = getTempPropertiesFile();
	    
		//create a new properties with no input
		Properties props = EncryptedPropertiesUtils.loadProperties(null, null);
		
		//add some properties
		Object prop1 = EncryptedPropertiesUtils.addProperty(props, KEY1, VALUE1);
		assertNull("Expected null but returned: " + prop1, prop1);

		Object prop2 = EncryptedPropertiesUtils.addProperty(props, KEY2, VALUE2);
		assertNull("Expected null but returned: " + prop2, prop2);

		//store the file 
		EncryptedPropertiesUtils.storeProperties(encryptedFile.getAbsolutePath(), props, "Encrypted Properties File generated by EncryptedPropertiesUtilsTest");

		//try reading in the resulting file
		ReferenceEncryptedProperties loadedProps = new ReferenceEncryptedProperties();
		loadedProps.load(new FileReader(encryptedFile.getAbsolutePath()));
		
		assertEquals(VALUE1, loadedProps.getProperty(KEY1));
		assertEquals(VALUE2, loadedProps.getProperty(KEY2));
	}

	/**
	 * Test of loading a plaintext file and storing it as an encrypted properties file,
	 * as if calling:
	 * <code>
	 *     EncryptedPropertiesUtils --in plaintext.properties --out encrypted.properties --in-encrypted false
	 * </code>
	 *
	 * @throws Exception Any exception that occurs
	 */
	@Test public void testLoadPlaintextAndEncrypt() throws Exception {
	    File encryptedFile = getTempPropertiesFile();
	    File plainTextFile = getTempPropertiesFile();
	    
	  //write an initial plaintext properties file
        Properties props = new Properties();
        props.setProperty(KEY3, VALUE3);
        props.setProperty(KEY4, VALUE4);

        props.store(new FileOutputStream(plainTextFile.getAbsolutePath()), "Plaintext test file created by EncryptedPropertiesUtilsTest");
	    
		//load the plaintext properties file
		props = EncryptedPropertiesUtils.loadProperties(plainTextFile.getAbsolutePath(), false);

		//test some properties using getProperty
		assertEquals(VALUE3, props.getProperty(KEY3));
		assertEquals(VALUE4, props.getProperty(KEY4));

		//store the file
		EncryptedPropertiesUtils.storeProperties(encryptedFile.getAbsolutePath(), props, "Encrypted Properties File generated by EncryptedPropertiesUtilsTest");

		//try reading in the resulting file
		ReferenceEncryptedProperties loadedProps = new ReferenceEncryptedProperties();
		loadedProps.load(new FileReader(encryptedFile.getAbsolutePath()));

		assertEquals(VALUE3, loadedProps.getProperty(KEY3));
		assertEquals(VALUE4, loadedProps.getProperty(KEY4));
	}

	/**
	 * Test of loading an encrypted file, adding new properties and storing it as an encrypted properties file,
	 * as if calling:
	 * <code>
	 *     EncryptedPropertiesUtils --in encrypted.properties --out encrypted.2.properties
	 * </code>
	 *
	 * @throws Exception Any exception that occurs
	 */
	@Test public void testLoadEncryptedAndAdd() throws Exception {
	    File encryptedFile = getTempPropertiesFile();
	    File encryptedFile2 = getTempPropertiesFile();
		//load the plaintext properties file
		Properties props = EncryptedPropertiesUtils.loadProperties(encryptedFile.getAbsolutePath(), true);

		//add some new properties
		EncryptedPropertiesUtils.addProperty(props, KEY1, VALUE1);
		EncryptedPropertiesUtils.addProperty(props, KEY2, VALUE2);

		//test the newly added properties
		assertEquals(VALUE1, props.getProperty(KEY1));
		assertEquals(VALUE2, props.getProperty(KEY2));

		//store the file
		EncryptedPropertiesUtils.storeProperties(encryptedFile2.getAbsolutePath(), props, "Encrypted Properties File generated by EncryptedPropertiesUtilsTest");

		//try reading in the resulting file
		ReferenceEncryptedProperties loadedProps = new ReferenceEncryptedProperties();
		loadedProps.load(new FileReader(encryptedFile2.getAbsolutePath()));

		//test the values read in
		assertEquals(VALUE1, loadedProps.getProperty(KEY1));
		assertEquals(VALUE2, loadedProps.getProperty(KEY2));
	}

}