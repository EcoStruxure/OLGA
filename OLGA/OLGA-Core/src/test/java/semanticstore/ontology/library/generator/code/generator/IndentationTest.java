package semanticstore.ontology.library.generator.code.generator;

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
public class IndentationTest {

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
