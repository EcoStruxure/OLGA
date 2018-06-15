/*
 * -------------------------
 * 
 * MIT License
 * 
 * Copyright (c) 2018, Schneider Electric USA, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * ---------------------
 */
package semanticstore.ontology.library.generator.olga;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * testing the trinity library with different ontologies.
 */
public class OLGATrinityTest {
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
  public void testSimple() {
    File resourcesDirectory = new File("src/test/resources/simple");

    OLGA.main(
        new String[] {"--version", "1.00.43", "--out", "./Output/", "--code", "cs", "--library",
            "trinity", "--name", "Simple", "--path", resourcesDirectory.getAbsolutePath()});

    assertTrue(!OLGA.getResult().contains("Fail"));
  }

  @Test
  public void testTrinityDSP() {
    File resourcesDirectory = new File("src/test/resources/remi");

    OLGA.main(new String[] {"--code", "cs", "--library", "trinity", "--name", "REMI", "--path",
        resourcesDirectory.getAbsolutePath()});
    assertTrue(!OLGA.getResult().contains("Fail"));
  }

  @Test
  public void testTrinityDSPPreservePath() {
    File resourcesDirectory = new File("src/test/resources/remi");

    OLGA.main(new String[] {"--code", "cs", "--library", "trinity", "--name", "REMI", "-preserve",
        "--path", resourcesDirectory.getAbsolutePath()});
    assertTrue(!OLGA.getResult().contains("Fail"));
  }

  @Test
  public void testTrinityDSPreservePathPartial() {
    File resourcesDirectory = new File("src/test/resources/remi");

    OLGA.main(new String[] {"--code", "cs", "--library", "trinity", "--name", "DSP", "-preserve",
        "-partial", "--path", resourcesDirectory.getAbsolutePath()});
    assertTrue(!OLGA.getResult().contains("Fail"));
  }

}
