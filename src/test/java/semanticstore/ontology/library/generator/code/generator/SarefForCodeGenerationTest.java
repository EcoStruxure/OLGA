/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.code.generator;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import semanticstore.ontology.library.generator.olga.OLGA;

public class SarefForCodeGenerationTest {

	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() throws IOException {
		PrintStream printStream = new PrintStream(errContent, false, "UTF-8");
		System.setErr(printStream);
		errContent.reset();
	}

	@After
	public void cleanUpStreams() {
		System.setErr(null);
	}

	@Test
	public void testSaref4CodeGeneration() {
		File resourcesDirectory = new File("src/test/resources/saref/sarefMerged.owl");
		OLGA.main(new String[] { "--out", "./Output/", "--code", "cs", "--library", "trinity", "--name",
				"SarefForGeneration", "--path", resourcesDirectory.getAbsolutePath() });
		assertTrue(!OLGA.getResult().contains("Fail"));

	}

}
