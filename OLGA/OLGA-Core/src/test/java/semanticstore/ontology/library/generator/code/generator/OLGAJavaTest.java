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
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.service.OlgaService;

public class OLGAJavaTest {
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private OlgaService service = new OlgaService();

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
  public void testSimple() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("simple/simple.owl").toURI())
            .toString();
    inputCmdParameters.put("name", "Simple");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Test
  public void testDsp() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("remi").toURI()).toString();
    inputCmdParameters.put("name", "DSP");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Test
  public void testoneM2M_Base_OntologyX() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(OLGAJavaTest.class.getClassLoader()
        .getResource("PublicOntologies/oneM2M_Base_Ontology").toURI()).toString();
    inputCmdParameters.put("name", "oneM2M");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Test
  public void testPizza() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("PublicOntologies/pizza").toURI())
            .toFile().toString();
    inputCmdParameters.put("name", "Pizza");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Test
  public void testProvo() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory = Paths
        .get(OLGAJavaTest.class.getClassLoader().getResource("PublicOntologies/prov-o").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "Prov");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Test
  public void testSsn() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("PublicOntologies/ssn").toURI())
            .toFile().toString();
    inputCmdParameters.put("name", "SSN");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Ignore
  @Test
  public void testBrick() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);

    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("brick").toURI())
            .toFile().toString();
    inputCmdParameters.put("name", "Brick");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    assertTrue(service.getResult().equals("Success"));
  }

  @Test(expected = InvalidClassNameException.class)
  public void testKnx() throws Exception {
    
    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);
    
    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("PublicOntologies/knx").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "Knx");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    
    assertTrue(service.getResult().equals("Success"));
  }
  
  @Test
  public void testSarefMerged() throws Exception {
    
    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);
    
    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("saref").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "SarefMerged");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    
    assertTrue(service.getResult().equals("Success"));
  }
  
  @Test
  public void testSarefImports() throws Exception {
    
    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);
    inputCmdParameters.put("preserve", true);
    
    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("sarefImports").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "SarefImports");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    
    assertTrue(service.getResult().equals("Success"));
  }
  
  @Test
  public void testFiesta() throws Exception {
    
    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);
    inputCmdParameters.put("preserve", true);
    
    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("fiesta").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "Fiesta");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    
    assertTrue(service.getResult().equals("Success"));
  }
  
  @Test
  public void testM3() throws Exception {
    
    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.JAVA);
    inputCmdParameters.put("library", LIBRARY.RDF4J);
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", false);
    
    String resourcesDirectory = null;
    resourcesDirectory =
        Paths.get(OLGAJavaTest.class.getClassLoader().getResource("m3").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "M3");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);
    
    assertTrue(service.getResult().equals("Success"));
  }
}
