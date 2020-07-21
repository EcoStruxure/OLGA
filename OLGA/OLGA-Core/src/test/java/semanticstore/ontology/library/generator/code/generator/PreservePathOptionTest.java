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
import java.io.File;
import org.junit.BeforeClass;
import org.junit.Test;
import semanticstore.ontology.library.generator.test.utils.GeneratedOntologies;

public class PreservePathOptionTest {

  @BeforeClass
  public static void generateOntology() {
    assertTrue(GeneratedOntologies.isSimpleBasicPreserveGenerated("cs", "trinity"));
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("cs", "trinity"));
    assertTrue(GeneratedOntologies.isSimpleBasicPreserveGenerated("java", "rdf4j"));
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("java", "rdf4j"));
    assertTrue(GeneratedOntologies.isSimpleBasicPreserveGenerated("python", "rdfalchemy"));
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("python", "rdfalchemy"));
  }

  @Test
  public void testPathWhenOptionSet_CSharp() {
    File file = new File("OLGA/generated/SimplePreserve-dotnetTrinity/www/simple/com/Building.cs");
    assertTrue(file.exists());
  }

  @Test
  public void testPathWhenOptionNotSet_CSharp() {
    File file =
        new File("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/Building.cs");
    assertTrue(file.exists());
  }

  @Test
  public void testPathWhenOptionSet_Java() {
    File file = new File(
        "OLGA/generated/simplepreserve-RDF4J-java/src/main/java/www/simple/com/Building.java");
    assertTrue(file.exists());
  }

  @Test
  public void testPathWhenOptionNotSet_Java() {
    File file = new File(
        "OLGA/generated/testsimple-RDF4J-java/src/main/java/testsimple/rdf/model/Building.java");
    assertTrue(file.exists());
  }

  @Test
  public void testPathWhenOptionSet_Python() {
    File file =
        new File("OLGA/generated/SimplePreserve-python/simplepreserve/www/simple/com/Building.py");
    assertTrue(file.exists());
  }

  @Test
  public void testPathWhenOptionNotSet_Python() {
    File file =
        new File("OLGA/generated/testSimple-python/testsimple/TestSimple/Rdf/Model/Building.py");
    assertTrue(file.exists());
  }
}
