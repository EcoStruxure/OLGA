/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.global;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;

public class UTILSTest {

	@Test
	public void testcleanPath() {
		String iriStr = "http://www.semanticweb.org/sesa/ontologies/2017/3/untitled-ontology-10#Hello";
		IRI iri = IRI.create(iriStr);
		String result = UTILS.cleanPath(iri);
		assertEquals("www/semanticweb/org/sesa/ontologies/_2017/_3/untitled_ontology_10", result);
	}

	@Test
	public void testcleanPackageName() {
		String iriStr = "http://www.semanticweb.org/sesa/ontologies/2017/3/untitled-ontology-10#Hello";
		IRI iri = IRI.create(iriStr);
		String result = UTILS.cleanPackageName(iri);
		assertEquals("www.semanticweb.org.sesa.ontologies._2017._3.untitled_ontology_10", result);
	}

	@Test
	public void testcleanPackageNameWithoutHash() {
		String iriStr = "http://www.semanticweb.org/sesa/ontologies/2017/3/untitled-ontology-10/Hello";
		IRI iri = IRI.create(iriStr);
		String result = UTILS.cleanPackageName(iri);
		assertEquals("www.semanticweb.org.sesa.ontologies._2017._3.untitled_ontology_10", result);
	}

	@Test
	public void testhasOntologyFormatExtension() {
		assertTrue(UTILS.hasOntologyFormatExtension("fileName.rdf"));

	}
	
	@Test
	public void testValidateClassName()
	{
		try {
			UTILS.validateClassName("class");
		} catch (InvalidClassNameException e) {
			assertEquals(e.getMessage(), "class Invalid class name, it doesn't follow the convention.");
		}
	}

	@Test
	public void testisVersionPatternValid() {
		assertFalse(UTILS.isVersionPatternValid("0.0.0.0.0"));

	}
	

}