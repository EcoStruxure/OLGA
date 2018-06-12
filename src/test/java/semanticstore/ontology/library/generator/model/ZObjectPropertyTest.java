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
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

/*
 * ZObjectProperty class is tested.
 */
public class ZObjectPropertyTest {

	@Test
	public void testEquals() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty1 = new ZObjectProperty(iri);
		ZObjectProperty zojectProperty2 = new ZObjectProperty(iri);

		zojectProperty1.setObjectPropertyCardinality("1");
		zojectProperty1.setObjectPropertyType("String");
		zojectProperty2.setObjectPropertyCardinality("2");
		zojectProperty2.setObjectPropertyType("String");

		boolean test = zojectProperty1.equals(zojectProperty2);

		// then

		assertFalse(test);
	}

	/*
	 * Setting a comment to object property.
	 */
	@Test
	public void testCommentSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty = new ZObjectProperty(iri);

		// when
		zojectProperty.setComments("this is a comment");

		// then
		assertEquals("Fields didn't match", zojectProperty.getComments(), "this is a comment");
	}

	/*
	 * Setting label.
	 */
	@Test
	public void testLabelSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty = new ZObjectProperty(iri);

		// when
		zojectProperty.setLabel("this is a comment");

		// then
		assertEquals("Fields didn't match", zojectProperty.getLabel(), "this is a comment");
	}

	@Test
	public void testObjectPropertyCardinalitySetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty = new ZObjectProperty(iri);

		// when
		zojectProperty.setObjectPropertyCardinality("1");

		// then
		assertEquals("Fields didn't match", zojectProperty.getObjectPropertyCardinality(), "1");
	}

	@Test
	public void testObjectPropertyTypeSetter() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty = new ZObjectProperty(iri);

		// when
		zojectProperty.setObjectPropertyType("Some");
		;

		// then
		assertEquals("Fields didn't match", zojectProperty.getObjectPropertyType(), "Some");
	}

	@Test
	public void testzClassName() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty = new ZObjectProperty(iri);

		// then
		assertEquals("Fields didn't match", zojectProperty.getObjectPropertyShortForm(), "hasFloor");
	}

	@Test
	public void testZDataPropertynamespace() throws NoSuchFieldException, IllegalAccessException {
		// given
		String iriStr = "http://www.micello.com#hasFloor";
		IRI iri = IRI.create(iriStr);
		ZObjectProperty zojectProperty = new ZObjectProperty(iri);

		// then
		assertEquals("Fields didn't match", zojectProperty.getObjectPropertyNamespace(), "http://www.micello.com#");
	}
}
