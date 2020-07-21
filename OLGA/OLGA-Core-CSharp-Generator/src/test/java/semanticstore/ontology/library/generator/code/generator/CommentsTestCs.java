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
public class CommentsTestCs {

  @BeforeClass
  public static void generateOntology() throws Exception {
    // check if the needed ontologies have been generated
    assertTrue(GeneratedOntologies.isSimpleBasicGenerated("cs", "trinity"));
    assertTrue(GeneratedOntologies.isSimpleLonelyBasicGenerated("cs", "trinity"));
    assertTrue(GeneratedOntologies.isSarefBasicGenerated("cs", "trinity"));
  }

  // building set of parameter to test : (code , path to tested class, expected comment, test name )
  @Parameters(name = "{index}: {0}-Comment {3}")
  public static Collection<Object[]> data() {
    Path csharp = Paths.get("OLGA/generated/testSimple-dotnetTrinity/TestSimple/Rdf/Model/");
    Path csharpLonely =
        Paths.get("OLGA/generated/testSimpleLonely-dotnetTrinity/TestSimpleLonely/Rdf/Model/");
    Path cSharpSaref = Paths.get(
        "OLGA/generated/Saref4CodeGenerationDataProperty-dotnetTrinity/Saref4CodeGenerationDataProperty/Rdf/Model/");


    List<Object[]> list = new ArrayList<>();

    list.add(new Object[] {"CSHARP", csharp.resolve("IBuilding.cs"),
        "/// testing comments in different classes.", "IBuilding.cs"});
    list.add(new Object[] {"CSHARP", csharp.resolve("IPhysicalLocation.cs"),
        "/// Shows actual location xxxx.", "IPhysicalLocation.cs"});
    list.add(new Object[] {"CSHARP", csharp.resolve("ILocationItem.cs"),
        "    /// Parent Class of the classes.", "ILocationItem.cs"});

    list.add(new Object[] {"CSHARP LONELY", csharpLonely.resolve("IBuilding.cs"),
        "/// shows building property.", "Data property"});
    list.add(new Object[] {"CSHARP LONELY", csharpLonely.resolve("IBuilding.cs"),
        "/// Depends on Building class.", "Object property 1"});
    list.add(new Object[] {"CSHARP LONELY", csharpLonely.resolve("IBuilding.cs"),
        "/// Object Property comment@!$%^*", "Object property 2"});
    list.add(new Object[] {"CSHARP LONELY", csharpLonely.resolve("ILocationItem.cs"),
        "/// Coordinator $", "Data property"});
    list.add(new Object[] {"CSHARP LONELY", csharpLonely.resolve("IFloor.cs"),
        "/// provides information about floor accronym~\"#%&*", "Data property"});

    list.add(new Object[] {"CSHARP", cSharpSaref.resolve("IWashingMachine.cs"),
        "/// <inheritdoc cref=\"IAppliance\"/>", "Comment Inheritance Saref WashMach"});
    list.add(new Object[] {"CSHARP", cSharpSaref.resolve("ITemporalUnit.cs"),
        "/// <inheritdoc cref=\"IUnitOfMeasure\"/>", "Comment Inheritance SarefTempUnit"});

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
