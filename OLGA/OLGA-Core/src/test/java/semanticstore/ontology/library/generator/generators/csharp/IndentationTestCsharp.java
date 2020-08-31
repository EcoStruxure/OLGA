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
package semanticstore.ontology.library.generator.code.generator.generators.csharp;

import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import semanticstore.ontology.library.generator.test.utils.GeneratedOntologies;

/**
 * Test indention for csharp projects ( CTRL + K, D in visual studio must be green )
 *
 */
@RunWith(Parameterized.class)
public class IndentationTestCsharp {

  @BeforeClass
  public static void generateOntology() throws Exception {
    // check if the needed ontologies have been generated
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("cs", "trinity"));
  }

  // building set of parameter to test : (path to tested class, expected comment )
  @Parameters(name = "{index}: Indentation {1}")
  public static Collection<Object[]> data() {
    Path path =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model").toAbsolutePath();
    return Arrays.asList(new Object[][] {{path.resolve("Building.cs"), "Building"},
        {path.resolve("IBuilding.cs"), "IBuilding"}});
  }

  @Parameter
  public Path classPath;
  @Parameter(1)
  public String class4TestName;

  @Test
  public void testIndentIsNotTab() throws IOException {

    try (Stream<String> stream = Files.lines(classPath)) {
      assertFalse(stream.anyMatch(line -> line.contains("/t")));
    }
  }
}
