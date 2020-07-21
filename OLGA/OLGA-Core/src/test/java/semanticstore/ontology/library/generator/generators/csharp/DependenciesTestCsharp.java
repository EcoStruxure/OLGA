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
import java.util.stream.Stream;
import org.junit.BeforeClass;
import org.junit.Test;
import semanticstore.ontology.library.generator.test.utils.GeneratedOntologies;

public class DependenciesTestCsharp {

  @BeforeClass
  public static void generateOntology() throws Exception {
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("cs", "trinity"));
  }

  @Test
  public void testBuildingDependencies() throws IOException {

    Path building =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/Building.cs")
            .toAbsolutePath();
    try (Stream<String> stream = Files.lines(building)) {
      assertTrue(stream
          .anyMatch(line -> line.contains("public class Building : PhysicalLocation, IBuilding")));
    }
  }


  // Building class has two parents. It should extend two parent interface

  @Test
  public void testIBuildingDependencies() throws IOException {

    Path iBuilding =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/IBuilding.cs")
            .toAbsolutePath();
    try (Stream<String> stream = Files.lines(iBuilding)) {
      assertTrue(stream.anyMatch(line -> line
          .contains("public interface IBuilding : IMonitorableItem, IPhysicalLocation")));
    }
  }

  @Test
  public void testLocationItemDependencies() throws IOException {

    Path locationItem =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/LocationItem.cs")
            .toAbsolutePath();
    try (Stream<String> stream = Files.lines(locationItem)) {
      assertTrue(stream.anyMatch(
          line -> line.contains("public class LocationItem : ContextResource, ILocationItem")));
    }
  }

  @Test
  public void testILocationItemDependencies() throws IOException {

    Path iLocationItem =
        Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/ILocationItem.cs")
            .toAbsolutePath();
    try (Stream<String> stream = Files.lines(iLocationItem)) {
      assertTrue(stream.anyMatch(
          line -> line.contains("public interface ILocationItem : Semiodesk.Trinity.IResource")));
    }
  }


}
