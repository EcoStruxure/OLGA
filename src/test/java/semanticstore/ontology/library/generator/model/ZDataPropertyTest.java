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

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

public class ZDataPropertyTest {

	public String rangeXSDType = "";

	@Test
	public void testCommentSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasMicelloID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		// when
		zdataproperty.setComments("this is a comment");
		// then
		assertEquals("Fields didn't match", zdataproperty.getComments(), "this is a comment");
	}

	@Test
	public void testLabelSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasMicelloID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		// when
		zdataproperty.setLabel("this is a comment");

		// then
		assertEquals("Fields didn't match", zdataproperty.getLabel(), "this is a comment");
		assertEquals(null, zdataproperty.getLabel(), "this is a comment");
	}

	@Test
	public void testRestrictionTypeSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasMicelloID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		// when
		zdataproperty.setRestrictionType("String");

		// then
		assertEquals("Fields didn't match", zdataproperty.getRestrictionType(), "String");
		assertTrue(zdataproperty.getRestrictionType().equals("String"));
	}

	@Test
	public void testzClassName() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasMicelloID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		// then
		assertEquals("Fields didn't match", zdataproperty.getDataPropertyShortForm(), "hasMicelloID");
	}

	@Test
	public void testZDataPropertynamespace() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasMicelloID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		// then
		assertEquals("Fields didn't match", zdataproperty.getDataPropertyNamespace(), "http://www.micello.com#");
	}

	@Test
	public void testEquals() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.boc.com#hasbocID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		String iriStr2 = "http://www.boc.com#hasbocID";
		IRI iri2 = IRI.create(iriStr2);
		ZDataProperty zdataproperty2 = new ZDataProperty(iri2);

		// then

		assertTrue(zdataproperty.equals(zdataproperty2));
	}

	@Test
	public void testNotEquals() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.boc.com#hasbocID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);
		zdataproperty.setComments("this is comment");

		String iriStr2 = "http://www.boc.com#hasbocID";
		IRI iri2 = IRI.create(iriStr2);
		ZDataProperty zdataproperty2 = new ZDataProperty(iri2);
		zdataproperty2.setComments(null);
		// then

		assertFalse(zdataproperty.getComments().equals(zdataproperty2.getComments()));
	}

	@Test
	public void testThisEquals() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.boc.com#hasbocID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);

		// then

		assertFalse(zdataproperty.equals(null));
	}

	@Test
	public void testLabelEquals() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.boc.com#hasbocID";
		IRI iri = IRI.create(iriStr);
		ZDataProperty zdataproperty = new ZDataProperty(iri);
		zdataproperty.setLabel("label");
		String iriStr2 = "http://www.boc.com#hasbocID";
		IRI iri2 = IRI.create(iriStr2);
		ZDataProperty zdataproperty2 = new ZDataProperty(iri2);
		zdataproperty2.setLabel("label");
		// then
		assertTrue(zdataproperty.getLabel().equals(zdataproperty2.getLabel()));
	}

	@Test
	public void testlabelNotEquals() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.boc.com#hasbocID";
		IRI iri = IRI.create(iriStr);

		ZDataProperty zdataproperty = new ZDataProperty(iri);
		zdataproperty.setLabel("label");
		zdataproperty.setRangeXSDType(rangeXSDType);
		String iriStr2 = "http://www.boc.com#hasbocID";
		IRI iri2 = IRI.create(iriStr2);
		ZDataProperty zdataproperty2 = new ZDataProperty(iri2);
		zdataproperty2.setLabel("label2");
		zdataproperty2.setRangeXSDType("some");
		// then
		assertFalse(zdataproperty2.getRangeXSDType().equals(zdataproperty.getRangeXSDType()));
		assertFalse(zdataproperty.getLabel().equals("some"));
		assertNotEquals(zdataproperty2.getLabel().hashCode(), zdataproperty.getLabel().hashCode());
	}

}
