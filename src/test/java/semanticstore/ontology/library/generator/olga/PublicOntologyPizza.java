package semanticstore.ontology.library.generator.olga;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PublicOntologyPizza {
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

	/*
	 * this generates the code with given version and output folder.
	 */
	@Test
	public void testPizza() {
		File resourcesDirectory = new File("src/test/resources/PublicOntologies/pizza");

		OLGA.main(new String[] { "--version", "01.99.45", "--out", "./Output/", "--code", "cs", "--library", "trinity",
				"--name", "Pizza", "--path", resourcesDirectory.getAbsolutePath() });

	}
}
