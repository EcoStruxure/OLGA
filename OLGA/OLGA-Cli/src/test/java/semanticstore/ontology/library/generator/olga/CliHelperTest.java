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
package semanticstore.ontology.library.generator.olga;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Test;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;

public class CliHelperTest {

  @Test
  public void testParseCli() {
    String[] args = new String[] {};
    assertNull(CliHelper.parseCLI(args));
  }

  @Test
  public void testParseCli1() {
    String[] args =
        new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae", "--path", "."};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
  }

  @Test
  public void testParseCli2() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "--skipCompile"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue((boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
  }

  @Test
  public void testParseCli3() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "--skipInverseRelations"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue((boolean) input.get("skipInverseRelations"));
  }

  @Test
  public void testParseCli4() {
    String[] args =
        new String[] {"--code", "c", "--library", "trinity", "--name", "nmae", "--path", "."};
    assertNull(CliHelper.parseCLI(args));
  }

  @Test
  public void testParseCli5() {
    String[] args =
        new String[] {"--code", "cs", "--library", "trinit", "--name", "nmae", "--path", "."};
    assertNull(CliHelper.parseCLI(args));
  }

  @Test
  public void testParseCli6() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "--version", "1.2.3.4"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
  }

  @Test
  public void testParseCli7() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "-preserve"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
    assertTrue(input.containsKey("preserve") && (boolean) input.get("preserve"));
  }

  @Test
  public void testParseCli8() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "-partial"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
    assertTrue(input.containsKey("partial") && (boolean) input.get("partial"));
  }

  @Test
  public void testParseCli9() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "--out", "test"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
    assertTrue(input.containsKey("out") && input.get("out").equals("test"));
  }

  @Test
  public void testParseCli10() {
    String[] args = new String[] {"--code", "cs", "--library", "trinity", "--name", "nmae",
        "--path", ".", "-skipCleaning"};
    Map<String, Object> input = CliHelper.parseCLI(args);
    assertTrue(input.get("code").equals(CODE.C_SHARP));
    assertTrue(input.get("library").equals(LIBRARY.TRINITY));
    assertTrue(input.get("name").equals("nmae"));
    assertTrue(input.get("pathToOntologiesParam").equals("."));
    assertTrue(!(boolean) input.get("skipCompile"));
    assertTrue(!(boolean) input.get("skipInverseRelations"));
    assertTrue(input.containsKey("skipCleaning") && (boolean) input.get("skipCleaning"));
  }

}
