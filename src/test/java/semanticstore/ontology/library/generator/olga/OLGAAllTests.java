/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.olga;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import freemarker.template.TemplateException;
import semanticstore.ontology.library.generator.global.CONFIG;
import semanticstore.ontology.library.generator.global.UTILS;

/*
 * OLGA tested with Saref ontology.
 * tested areas:
 * output,library,code.
 */
public class OLGAAllTests {

	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() throws UnsupportedEncodingException {
		PrintStream printStream = new PrintStream(errContent, false, "UTF-8");
		System.setErr(printStream);
		errContent.reset();
	}

	@After
	public void cleanUpStreams() {
		System.setErr(null);
		
	}

	@Test
	public void testObjectProperties() throws OWLOntologyStorageException, IOException, TemplateException,
			XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/testProperties");

		String output = CONFIG.PROJECT_DIRECTORY + CONFIG.GENERATED_LIBRARY + "SAREF" + "-dotnetRomanticWeb/";
		File outputFolder = new File(output);
		UTILS.createNew(outputFolder);

		OLGA.main(new String[] { "--code", "cs", "--library", "romanticweb", "--name", "TestProperties", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

	@Test
	public void testRomanticWebSaref() throws OWLOntologyStorageException, IOException, TemplateException,
			XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/saref");

		

		OLGA.main(new String[] { "--code", "cs", "--library", "romanticweb", "--name", "SAREF", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

	@Test
	public void testRomanticWebSarefOntologiesInTwoSeperateFiles() throws OWLOntologyStorageException, IOException,
			TemplateException, XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/saref");
		String output = CONFIG.PROJECT_DIRECTORY + CONFIG.GENERATED_LIBRARY + "SAREF" + "-dotnetRomanticWeb/";
		File outputFolder = new File(output);
		UTILS.createNew(outputFolder);
		OLGA.main(new String[] { "--code", "cs", "--library", "romanticweb", "--name", "SAREFMerged", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

	@Test
	public void testJacksonSaref() throws OWLOntologyStorageException, IOException, TemplateException,
			XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/saref");
		String output = CONFIG.PROJECT_DIRECTORY + CONFIG.GENERATED_LIBRARY + "SAREF" + "-java/";
		File outputFolder = new File(output);

		UTILS.createNew(outputFolder);
		OLGA.main(new String[] { "--code", "java", "--library", "jackson-jsonld", "--name", "SAREF", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

	@Test
	public void testRDF4JSaref() throws OWLOntologyStorageException, IOException, TemplateException,
			XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/saref");
		String output = CONFIG.PROJECT_DIRECTORY + CONFIG.GENERATED_LIBRARY + "SAREF" + "-RDF4J-java/";
		File outputFolder = new File(output);
		UTILS.createNew(outputFolder);

		OLGA.main(new String[] { "--code", "java", "--library", "rdf4j", "--name", "SAREF", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

	@Test
	public void testRDFAlchemySaref() throws OWLOntologyStorageException, IOException, TemplateException,
			XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/saref");
		String output = CONFIG.PROJECT_DIRECTORY + CONFIG.GENERATED_LIBRARY + "SAREF" + "-python/";
		File outputFolder = new File(output);
		UTILS.createNew(outputFolder);

		OLGA.main(new String[] { "--code", "python", "--library", "rdfalchemy", "--name", "SAREF", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

	@Test
	public void testDotNetRDFSaref() throws OWLOntologyStorageException, IOException, TemplateException,
			XmlPullParserException, MavenInvocationException {
		File resourcesDirectory = new File("src/test/resources/saref");
		OLGA.main(new String[] { "--code", "cs", "--library", "dotnetrdf", "--name", "SAREF", "--path",
				resourcesDirectory.getAbsolutePath() });
		assertTrue(OLGA.getResult().contains("Fail"));
	}

}