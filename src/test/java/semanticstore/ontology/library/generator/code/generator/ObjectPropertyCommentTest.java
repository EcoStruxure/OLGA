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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import freemarker.template.TemplateException;
import semanticstore.ontology.library.generator.olga.OLGA;

public class ObjectPropertyCommentTest {

  @BeforeClass
  public static void testSimpleDataProperty() throws OWLOntologyStorageException, IOException,
      TemplateException, XmlPullParserException, MavenInvocationException {
    File resourcesDirectory = new File("src/test/resources/simple/simple-test-lonely.owl");
    OLGA.main(new String[] {"--out", "./Output/", "--code", "cs", "--library", "trinity", "--name",
        "SimpleForCommentDP", "--path", resourcesDirectory.getAbsolutePath()});
    assertTrue(!OLGA.getResult().contains("Fail"));
  }

  @Test
  public void testIBuilding() {
    Path fileName = Paths
        .get("Output/SimpleForCommentDP-dotnetTrinity/SimpleForCommentDP/Rdf/Model/IBuilding.cs")
        .toAbsolutePath();
    assertEquals(fileName.getFileName().toString(), "IBuilding.cs");
    try (Stream<String> stream = Files.lines(fileName)) {
      assertTrue(stream.anyMatch(line -> line.equals("        /// Depends on Building class.")));
    } catch (IOException e) {
      fail();
    }

  }

  @Test
  public void testILocationItem() {
    Path fileName = Paths
        .get(
            "Output/SimpleForCommentDP-dotnetTrinity/SimpleForCommentDP/Rdf/Model/ILocationItem.cs")
        .toAbsolutePath();
    assertEquals(fileName.getFileName().toString(), "ILocationItem.cs");
    try (Stream<String> stream = Files.lines(fileName)) {
      assertTrue(stream.anyMatch(line -> line.equals("        /// Coordinator $")));
    } catch (IOException e) {
      fail();
    }

  }

  @Test
  public void testIBuilding2() {
    Path fileName = Paths
        .get("Output/SimpleForCommentDP-dotnetTrinity/SimpleForCommentDP/Rdf/Model/IBuilding.cs")
        .toAbsolutePath();
    assertEquals(fileName.getFileName().toString(), "IBuilding.cs");
    try (Stream<String> stream = Files.lines(fileName)) {
      assertTrue(stream.anyMatch(line -> line.equals("        /// Object Property comment@!$%^*")));
    } catch (IOException e) {
      fail();
    }

  }
}
