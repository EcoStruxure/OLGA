
/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
/*
 * Testing the ZInstance.java with complex cases.
 * Tested areas:
 * Data Property
 * Object Property
 * Instance name,label,PackageName,path
 * equals method
 * hashCode
 */
public class ZInstanceTest {

	@Test
	public void testCommentSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		// when
		zinstance.setComments("this is a comment");

		// then
		assertEquals("Fields didn't match", zinstance.getComments(), "this is a comment");
	}

	@Test
	public void testLabelSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		// when
		zinstance.setLabel("my label");

		// then
		assertEquals("Fields didn't match", zinstance.getLabel(), "my label");
	}

	@Test
	public void testzClassName() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		// then
		assertEquals("Fields didn't match", zinstance.getzInstanceName(), "test");
	}

	@Test
	public void testzClassnamespace() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zclass = new ZInstance(iri);

		// then
		assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
	}

	@Test
	public void testLabelSetterAndHashcode() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		String iriStr1 = "http://www.test.com#test1";
		IRI iri1 = IRI.create(iriStr1);
		ZInstance zinstance1 = new ZInstance(iri1);
		// when
		zinstance.setLabel("my label");
		zinstance1.setLabel("not equal");

		// then
		assertFalse(zinstance.getLabel().equals(zinstance1.getLabel()));
		assertNotEquals(zinstance.hashCode(), zinstance1.hashCode());
		assertEquals("Fields didn't match", zinstance.getLabel(), "my label");
		assertFalse(zinstance.equals(zinstance1));
	}

	@Test
	public void testPathAndIRI() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		String iriStr1 = "http://www.test.com#test1";
		IRI iri1 = IRI.create(iriStr1);
		ZInstance zinstance1 = new ZInstance(iri1);
		// when
		zinstance.setPath("path");
		zinstance1.setPath("path");

		// then
		assertTrue(zinstance.getPath().equals(zinstance1.getPath()));
		assertFalse(zinstance.getIri().equals(zinstance1.getIri()));
		assertNotEquals(zinstance.hashCode(), zinstance1.hashCode());
		assertFalse(zinstance.equals(zinstance1));
	}

	@Test
	public void testgetPackageNameAndParentIRI() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		String iriStr1 = "http://www.test.com#test1";
		IRI iri1 = IRI.create(iriStr1);
		ZInstance zinstance1 = new ZInstance(iri1);

		String iriparent = "http://www.test.com#Parent";
		IRI iriParent = IRI.create(iriparent);

		String iriparent1 = "http://www.test.com#Parent1";
		IRI iriParent1 = IRI.create(iriparent1);
		// when
		zinstance.setPath("path");
		zinstance1.setPath("path");
		zinstance.setzClassInstanciated(iriParent1);
		zinstance1.setzClassInstanciated(iriParent);

		// then
		assertFalse(zinstance.getzClassInstanciated().equals(zinstance1.getzClassInstanciated()));
		assertTrue(zinstance.getPackageName().equals(zinstance1.getPackageName()));
		assertFalse(zinstance.getParentClassIRI().equals(zinstance1.getParentClassIRI()));
		assertNotEquals(zinstance.hashCode(), zinstance1.hashCode());
		assertTrue(zinstance.equals(zinstance));
		assertFalse(zinstance.equals(zinstance1));
	}

	@Test
	public void testInstanceProperties() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.test.com#test";
		IRI iri = IRI.create(iriStr);
		ZInstance zinstance = new ZInstance(iri);

		String iriStr1 = "http://www.test.com#test1";
		IRI iri1 = IRI.create(iriStr1);
		ZInstance zinstance1 = new ZInstance(iri1);

		String iriData = "http://www.test.com#Data";
		IRI iridata = IRI.create(iriData);
		ZDataProperty zData = new ZDataProperty(iridata);

		String iriData1 = "http://www.test.com#Data1";
		IRI iridata1 = IRI.create(iriData1);
		ZDataProperty zData1 = new ZDataProperty(iridata1);

		String iriObj = "http://www.test.com#Obj";
		IRI iriobj = IRI.create(iriObj);
		ZObjectProperty ZObj = new ZObjectProperty(iriobj);

		String iriObj1 = "http://www.test.com#Obj1";
		IRI iriobj1 = IRI.create(iriObj1);
		ZObjectProperty ZObj1 = new ZObjectProperty(iriobj1);
		// when

		ZObj1.setComments("comment");
		ZObj.setLabel("label");
		zinstance.add_DataProperty(zData);
		zinstance1.add_DataProperty(zData1);
		zinstance.add_ObjectProperty(ZObj);
		zinstance1.add_ObjectProperty(ZObj1);
		
		// then
		assertFalse(zinstance.getListZDataPropertyList().equals(zinstance1.getListZDataPropertyList()));
		assertTrue(zinstance.getListZDataPropertyList().equals(zinstance.getListZDataPropertyList()));
		assertTrue(zinstance.getListZObjectPropertyList().equals(zinstance1.getListZObjectPropertyList()));
		assertTrue(zinstance.getListZObjectPropertyList().equals(zinstance.getListZObjectPropertyList()));
		assertTrue(zinstance.getPackageName().equals(zinstance.getPackageName()));
		assertTrue(zinstance.equals(zinstance));
		assertTrue(zinstance.getPackageName().equals(zinstance1.getPackageName()));
		assertNotEquals(zinstance.hashCode(), zinstance1.hashCode());
		assertFalse(zinstance.equals(zinstance1));
		assertTrue(zinstance.getClass().equals(zinstance1.getClass()));
		assertTrue(zinstance.getClass().equals(zinstance.getClass()));
		assertEquals(zinstance.getListZObjectPropertyList().hashCode(),zinstance1.getListZObjectPropertyList().hashCode());

	}

}
