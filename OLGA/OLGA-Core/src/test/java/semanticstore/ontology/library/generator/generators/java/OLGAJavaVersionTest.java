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
package semanticstore.ontology.library.generator.code.generator.generators.java;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.resources.ResourceManager;
import semanticstore.ontology.library.generator.service.OlgaService;

public class OLGAJavaVersionTest {

  private OlgaService service = new OlgaService();

  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  private final static Path folder4Versions = Paths.get("./OLGA/generated/4Versions/java");

  @BeforeClass
  public static void beforeClass() throws IOException {
    //create a folder to put all the generated sources
    if (Files.exists(folder4Versions))
      FileUtils.deleteDirectory(folder4Versions.toFile());
    Files.createDirectories(folder4Versions);
  }
  
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
  public void testSimple4CodeGenerationMyValidVersion() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.fromString("rdf4j"));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("ontVersion", "4.3.2.1");
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("out", folder4Versions.toFile().toString());

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(OLGAJavaVersionTest.class.getClassLoader()
        .getResource("simple/simple.owl").toURI()).toFile().toString();
    inputCmdParameters.put("name", "SimpleValidVersion");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    assertTrue(service.getResult().equals("Success"));

    Path pom = folder4Versions.resolve("simplevalidversion-RDF4J-java/pom.xml");
    try (Stream<String> stream = Files.lines(pom)) {
      assertTrue("Version in pom",
          stream.anyMatch(line -> line.contains("<version>4.3.2.1</version>")));
    }
  }

  /**
   * If no version is specified in the ontology and from command line use OLGA version
   * 
   * @throws Exception
   */
  @Test
  public void testSimpleWithoutMyVersion() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.fromString("rdf4j"));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("out", folder4Versions.toFile().toString());

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(OLGAJavaVersionTest.class.getClassLoader()
        .getResource("simple/simple.owl").toURI()).toFile().toString();
    inputCmdParameters.put("name", "SimpleWithoutMyVersion");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    assertTrue(service.getResult().equals("Success"));

    Path pom = folder4Versions.resolve("simplewithoutmyversion-RDF4J-java/pom.xml");
    try (Stream<String> stream = Files.lines(pom)) {
      assertTrue("Version in pom", stream.anyMatch(line -> line
          .contains("<version>" + ResourceManager.getResource("OLGAVersion") + "</version>")));
    }
  }

  /**
   * version is specified in the ontology
   * 
   * @throws Exception
   */
  @Test
  public void testSimpleVersionInOntology() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.fromString("rdf4j"));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("out", folder4Versions.toFile().toString());

    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaVersionTest.class.getClassLoader().getResource("versionedsimple").toURI())
            .toFile().toString();
    inputCmdParameters.put("name", "versionedsimple");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    assertTrue(service.getResult().equals("Success"));

    Path pom = folder4Versions.resolve("versionedsimple-RDF4J-java/pom.xml");
    try (Stream<String> stream = Files.lines(pom)) {
      assertTrue("Version in pom",
          stream.anyMatch(line -> line.contains("<version>1.0.0</version>")));
    }
  }

  /**
   * Wrong version leads to olga version
   * 
   * @throws Exception
   */
  @Test
  public void testSimpleMyWrongVersion() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.fromString("rdf4j"));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("ontVersion", "4.3.2.one");
    inputCmdParameters.put("out", folder4Versions.toFile().toString());

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(OLGAJavaVersionTest.class.getClassLoader()
        .getResource("simple/simple.owl").toURI()).toFile().toString();
    inputCmdParameters.put("name", "WrongVersionName");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    assertTrue(service.getResult().equals("Success"));

    Path pom = folder4Versions.resolve("wrongversionname-RDF4J-java/pom.xml");
    try (Stream<String> stream = Files.lines(pom)) {
      assertTrue("Version in pom", stream.anyMatch(line -> line
          .contains("<version>" + ResourceManager.getResource("OLGAVersion") + "</version>")));
    }
  }
}
