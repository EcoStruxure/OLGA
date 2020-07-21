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
import java.util.stream.Stream;
import org.junit.BeforeClass;
import org.junit.Test;
import semanticstore.ontology.library.generator.test.utils.GeneratedOntologies;

public class QueryWithReasoningTest {

  @BeforeClass
  public static void testSimple() {
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("cs", "trinity"));
  }

  @Test
  public void testWhenHasSubClass() {
    Path fileName =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/Sensor.cs")
            .toAbsolutePath();
    try (Stream<String> stream = Files.lines(fileName)) {
      assertTrue(stream.anyMatch(line -> line.contains("[QueryWithReasoning]")));
    } catch (IOException e) {
      fail();
    }

  }

  @Test
  public void testWhenHasNotSubClass() {
    Path fileName =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/EnergySensor.cs")
            .toAbsolutePath();
    try (Stream<String> stream = Files.lines(fileName)) {
      assertTrue(!stream.anyMatch(line -> line.contains("[QueryWithReasoning]")));
    } catch (IOException e) {
      fail();
    }

  }
}
