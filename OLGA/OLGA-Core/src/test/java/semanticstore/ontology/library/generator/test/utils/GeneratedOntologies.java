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
package semanticstore.ontology.library.generator.test.utils;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.service.OlgaService;

public class GeneratedOntologies {

  private static OlgaService service = new OlgaService();

  private static Map<String, Boolean> simpleBasicPreserveSuccess = new HashMap<>();
  private static Boolean simpleBasicPartialSuccess = null;
  private static Map<String, Boolean> simpleBasicSuccess = new HashMap<>();
  private static Map<String, Boolean> wpaInverseRelationsSkippedBasicSuccess = new HashMap<>();
  private static Map<String, Boolean> wpaInverseRelationsNotSkippedBasicSuccess = new HashMap<>();
  private static Map<String, Boolean> sarefBasicSuccess = new HashMap<>();
  private static Map<String, Boolean> simpleBasicLonelySuccess = new HashMap<>();

  public static synchronized boolean isSimpleBasicGenerated(String code, String library) {

    if (!simpleBasicSuccess.containsKey(code + library)) {
      try {
        generateSimpleBasic(code, library);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return simpleBasicSuccess.get(code + library);

  }

  private static void generateSimpleBasic(String code, String library) throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.fromString(code));
    inputCmdParameters.put("library", LIBRARY.fromString(library));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths
        .get(GeneratedOntologies.class.getClassLoader().getResource("simple/simple.owl").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "testSimple");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    simpleBasicSuccess.put(code + library, service.getResult().equals("Success"));
  }

  public static synchronized boolean isWpaInverseRelationsSkippedBasicGenerated(String code,
      String library) {

    if (!wpaInverseRelationsSkippedBasicSuccess.containsKey(code + library)) {
      try {
        generateWpaInverseRelationsSkippedBasic(code, library);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return wpaInverseRelationsSkippedBasicSuccess.get(code + library);

  }

  private static void generateWpaInverseRelationsSkippedBasic(String code, String library)
      throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.fromString(code));
    inputCmdParameters.put("library", LIBRARY.fromString(library));
    inputCmdParameters.put("skipInverseRelations", true);
    inputCmdParameters.put("skipCompile", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(GeneratedOntologies.class.getClassLoader()
        .getResource("wpa/BuildingsGraph Ontology.owl").toURI()).toFile().toString();
    inputCmdParameters.put("name", "testWpaInverseRelationsSkipped");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    wpaInverseRelationsSkippedBasicSuccess.put(code + library,
        service.getResult().equals("Success"));
  }

  public static synchronized boolean isWpaInverseRelationsNotSkippedBasicGenerated(String code,
      String library) {

    if (!wpaInverseRelationsNotSkippedBasicSuccess.containsKey(code + library)) {
      try {
        generateWpaInverseRelationsNotSkippedBasic(code, library);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return wpaInverseRelationsNotSkippedBasicSuccess.get(code + library);

  }

  private static void generateWpaInverseRelationsNotSkippedBasic(String code, String library)
      throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.fromString(code));
    inputCmdParameters.put("library", LIBRARY.fromString(library));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(GeneratedOntologies.class.getClassLoader()
        .getResource("wpa/BuildingsGraph Ontology.owl").toURI()).toFile().toString();
    inputCmdParameters.put("name", "testWpaInverseRelationsNotSkipped");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    wpaInverseRelationsNotSkippedBasicSuccess.put(code + library,
        service.getResult().equals("Success"));
  }

  public static synchronized boolean isSarefBasicGenerated(String code, String library) {

    if (!sarefBasicSuccess.containsKey(code + library)) {
      try {
        generateSarefBasic(code, library);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sarefBasicSuccess.get(code + library);

  }

  private static void generateSarefBasic(String code, String library) throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.fromString(code));
    inputCmdParameters.put("library", LIBRARY.fromString(library));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths
        .get(
            GeneratedOntologies.class.getClassLoader().getResource("saref/sarefMerged.owl").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "Saref4CodeGenerationDataProperty");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    sarefBasicSuccess.put(code + library, service.getResult().equals("Success"));
  }

  public static synchronized boolean isSimpleLonelyBasicGenerated(String code, String library) {

    if (!simpleBasicLonelySuccess.containsKey(code + library)) {
      try {
        generateSimpleLonelyBasic(code, library);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return simpleBasicLonelySuccess.get(code + library);

  }

  private static void generateSimpleLonelyBasic(String code, String library) throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.fromString(code));
    inputCmdParameters.put("library", LIBRARY.fromString(library));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths.get(GeneratedOntologies.class.getClassLoader()
        .getResource("simple/simple-test-lonely.owl").toURI()).toFile().toString();
    inputCmdParameters.put("name", "testSimpleLonely");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    simpleBasicLonelySuccess.put(code + library, service.getResult().equals("Success"));
  }

  public static synchronized boolean isSimpleBasicPreserveGenerated(String code, String library) {

    if (!simpleBasicPreserveSuccess.containsKey(code + library)) {
      try {
        generateSimpleBasicPreserve(code, library);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return simpleBasicPreserveSuccess.get(code + library);

  }

  private static void generateSimpleBasicPreserve(String code, String library) throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.fromString(code));
    inputCmdParameters.put("library", LIBRARY.fromString(library));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("preserve", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths
        .get(GeneratedOntologies.class.getClassLoader().getResource("simple/simple.owl").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "SimplePreserve");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    simpleBasicPreserveSuccess.put(code + library, service.getResult().equals("Success"));;
  }

  public static synchronized boolean isSimpleBasicPartialGenerated() {

    if (simpleBasicPartialSuccess == null) {
      try {
        generateSimpleBasicPartial();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return simpleBasicPartialSuccess;

  }

  private static void generateSimpleBasicPartial() throws Exception {

    Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
    inputCmdParameters.put("code", CODE.C_SHARP);
    inputCmdParameters.put("library", LIBRARY.fromString("trinity"));
    inputCmdParameters.put("skipInverseRelations", false);
    inputCmdParameters.put("skipCompile", true);
    inputCmdParameters.put("partial", true);

    String resourcesDirectory = null;
    resourcesDirectory = Paths
        .get(GeneratedOntologies.class.getClassLoader().getResource("simple/simple.owl").toURI())
        .toFile().toString();
    inputCmdParameters.put("name", "testSimplePartial");
    inputCmdParameters.put("pathToOntologiesParam", resourcesDirectory);
    service.invokeOlga(inputCmdParameters);

    simpleBasicPartialSuccess = service.getResult().equals("Success");
  }
}
