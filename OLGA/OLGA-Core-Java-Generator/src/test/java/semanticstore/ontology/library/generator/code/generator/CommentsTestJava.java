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
import static org.junit.Assert.fail;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import semanticstore.ontology.library.generator.test.utils.GeneratedOntologies;

@RunWith(Parameterized.class)
public class CommentsTestJava {

  @BeforeClass
  public static void generateOntology() throws Exception {
    // check if the needed ontologies have been generated
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("java", "rdf4j"));
  }

  // building set of parameter to test : (code , path to tested class, expected comment, test name )
  @Parameters(name = "{index}: {0}-Comment {3}")
  public static Collection<Object[]> data() {
    Path java =
        Paths.get("OLGA/generated/testsimple-RDF4J-java/src/main/java/testsimple/rdf/model/");

    List<Object[]> list = new ArrayList<>();

    list.add(new Object[] {"JAVA", java.resolve("Building.java"), "Class mabna", "Class"});
    list.add(new Object[] {"JAVA", java.resolve("Building.java"), "shows building property.",
        "Data property"});
    list.add(new Object[] {"JAVA", java.resolve("Building.java"), "Depends on Building class.",
        "Object Property"});

    return list;
  }

  @Parameter
  public String language;
  @Parameter(1)
  public Path classPath;
  @Parameter(2)
  public String expectedComment;
  @Parameter(3)
  public String class4TestName;

  @Test
  public void test() throws IOException {
    try (Stream<String> stream = Files.lines(classPath)) {
      assertTrue(stream.anyMatch(line -> line.contains(expectedComment)));
    } catch (IOException e) {
      fail();
    }
  }

}
