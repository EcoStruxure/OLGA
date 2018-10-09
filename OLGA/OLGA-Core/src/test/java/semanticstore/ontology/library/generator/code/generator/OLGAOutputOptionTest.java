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
package semanticstore.ontology.library.generator.code.generator;

import static org.junit.Assert.assertTrue;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.service.OlgaService;
import semanticstore.ontology.library.generator.test.utils.GeneratedOntologies;

public class OLGAOutputOptionTest {


  static Path outputPath;

  @BeforeClass
  public static void setUpStreams() throws Exception {

    outputPath = Files.createTempDirectory("testOutput");
    outputPath.toFile().deleteOnExit();

    OlgaService service = new OlgaService();

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.C_SHARP);
    inputCmdParameters.put("library", LIBRARY.TRINITY);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("out", outputPath.toFile().toString());

    String resourcesDirectory = null;
    resourcesDirectory = Paths
        .get(GeneratedOntologies.class.getClassLoader().getResource("simple/simple.owl").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "testSimpleWithOut");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
  }

  @After
  public void cleanUpStreams() {
    System.setErr(null);
  }

  @Test
  public void testFolderExists() {
    assertTrue(Files.exists(outputPath.resolve("testSimpleWithOut-dotnetTrinity")));
    assertTrue(Files.exists(
        outputPath.resolve("testSimpleWithOut-dotnetTrinity").resolve("testsimplewithout.csproj")));
  }
}
