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
package semanticstore.ontology.library.generator.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.model.IRI;
import antlr.Utils;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.model.ZPair;

/**
 * UTILS is a class contains all of the static shared functions any class in OLGA will use.
 */

public class UTILS {

  final static Logger log = Logger.getLogger(Utils.class);

  /**
   * Used to refine the IRI and convert it to valid string could be used as class package name or
   * path.
   * 
   * @param IRI of any entity.
   */

  public static String cleanPath(IRI iri) {
    // Separate the domain from the path
    String path = iri.toURI().getPath();

    if (path == null) {
      throw new InvalidUriException(iri.toQuotedString());
    }

    // Replace all digits folder into string with _
    // example : /2006/bar -> /_2006/bar
    path = path.replaceAll("\\/(\\d+)", "/_$1");

    // Replace all . and - to _
    // example : /mylib.1.2.3/bar -> /mylib_1_2_3/bar
    path = path.replaceAll("(\\.|\\-)", "_");

    String domain = iri.toURI().getHost();
    // Remove https:// and http:// from domain
    domain = domain.replaceAll("^(https?):\\/\\/", "");

    // Special case if the URI dont end with a "#"
    int indexOfSharp = iri.getNamespace().indexOf("#");
    if (indexOfSharp < 0 && path.length() > 1) {
      int indexOfLastSeperator = path.lastIndexOf("/");
      path = path.substring(0, indexOfLastSeperator);
    }


    domain = domain + path;

    // Change . into / to generate folder structure
    domain = domain.replaceAll("\\.", "/");

    return domain;
  }

  public static boolean createNew(File file) {
    delete(file);
    return file.mkdirs();
  }

  /**
   * Used delete any file or directory recursively.
   * 
   * @param file wanted to be deleted.
   * @return Boolean <Code>true</Code> if the file is deleted <Code>false</Code> otherwise.
   */

  public static boolean delete(File file) {

    File[] flist = null;

    if (file == null) {
      return false;
    }

    if (file.isFile()) {
      return file.delete();
    }

    if (!file.isDirectory()) {
      return false;
    }

    flist = file.listFiles();
    if (flist != null && flist.length > 0) {
      for (File f : flist) {
        if (!delete(f)) {
          return false;
        }
      }
    }

    return file.delete();
  }

  /**
   * Used to get the OLGE version from POM file.
   * 
   * @return the version string.
   */

  // FIXME: this should not be read every time, refactor to read it once
  @SuppressWarnings("resource")
  public static String getOlgaVersion()
      throws FileNotFoundException, IOException, XmlPullParserException {
    String version = null;
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model;
    File pomFile = new File("pom.xml");
    InputStream pomStream = new FileInputStream(pomFile);
    try {      
      if (!pomFile.exists()) {
        pomStream = Utils.class.getClassLoader()
            .getResourceAsStream("META-INF/maven/semanticstore/ontology-library-generator/pom.xml");
      } 
      try (BufferedReader pomReader =
          new BufferedReader(new InputStreamReader(pomStream, StandardCharsets.UTF_8))) {
        model = reader.read(pomReader);
        version = model.getVersion();
      }
    } catch (IOException e) {
      log.error(e.getMessage());
      throw e;
    } catch (NullPointerException e) {
      log.error(e);
      throw e;
    } catch (XmlPullParserException e) {
      log.error(e);
      throw e;
    } finally {
      pomStream.close();
    }
    return version;
  }

  public static List<ZDataProperty> pickDataPropertiesForInterface(ZClass zclass) {
    // copy ZDataProperty of the zclass
    List<ZDataProperty> listDataPropertyFiltered =
        new ArrayList<ZDataProperty>(zclass.getZDataPropertyList());

    // Mapping of Objects based on their same IRI
    Map<String, List<ZDataProperty>> mappedProperties = listDataPropertyFiltered.stream().collect(
        Collectors.groupingBy(zDataProperty -> zDataProperty.getDataProperty().toString()));

    Iterator<String> iterator_MappedProperties = mappedProperties.keySet().iterator();
    while (iterator_MappedProperties.hasNext()) {
      List<ZDataProperty> dataPropertyList = mappedProperties.get(iterator_MappedProperties.next());
      List<ZDataProperty> preferredDataPropertyList =
          UTILS.pickBestDataPropertiesList(dataPropertyList);
      if (preferredDataPropertyList.size() > 0) {
        listDataPropertyFiltered.removeAll(dataPropertyList);
        listDataPropertyFiltered.addAll(preferredDataPropertyList);
      }
    }

    return listDataPropertyFiltered;
  }

  public static List<ZObjectProperty> pickObjectPropertiesForInterface(ZClass zclass) {
    // copy ZObjectProperties of the zclass
    List<ZObjectProperty> listObjectPropertyFiltered =
        new ArrayList<ZObjectProperty>(zclass.getZObjectPropertyList());

    // Mapping of Objects based on their same IRI
    Map<String, List<ZObjectProperty>> mappedProperties =
        listObjectPropertyFiltered.stream().collect(Collectors.groupingBy(
            zobjectProperty -> ((ZObjectProperty) zobjectProperty).getObjectProperty().toString()));

    Iterator<String> iterator_MappedProperties = mappedProperties.keySet().iterator();
    while (iterator_MappedProperties.hasNext()) {
      List<ZObjectProperty> objectPropertyList =
          mappedProperties.get(iterator_MappedProperties.next());
      List<ZObjectProperty> preferredObjectPropertyList =
          UTILS.pickBestObjectPropertiesList(objectPropertyList);
      if (preferredObjectPropertyList.size() > 0) {
        listObjectPropertyFiltered.removeAll(objectPropertyList);
        listObjectPropertyFiltered.addAll(preferredObjectPropertyList);
      }
    }
    return listObjectPropertyFiltered;
  }

  public static List<ZDataProperty> pickBestDataPropertiesList(
      List<ZDataProperty> dataPropertyList) {
    List<ZDataProperty> pickedList = new ArrayList<>();
    for (int i = 0; i < dataPropertyList.size(); i++) {
      ZDataProperty selectedPreferredDataProperty = dataPropertyList.get(i);
      for (int j = 0; j < dataPropertyList.size(); j++) {
        ZDataProperty currentZDataProperty = dataPropertyList.get(j);
        /*
         * Compare the DataProperty only if they have the same range class hour max 1 rdfs:Literal
         * hour some rdfs:Literal Should not be competing for preference
         */
        // Quick check on the size if same range continue the evaluation if not pass on
        if (currentZDataProperty.getRangeXSDType()
            .equalsIgnoreCase(selectedPreferredDataProperty.getRangeXSDType())) {
          selectedPreferredDataProperty =
              preferredDataProperty(currentZDataProperty, selectedPreferredDataProperty);
        }
      }
      if (!pickedList.contains(selectedPreferredDataProperty)) {
        pickedList.add(selectedPreferredDataProperty);
      }
    }
    return pickedList;
  }

  public static List<ZObjectProperty> pickBestObjectPropertiesList(
      List<ZObjectProperty> objectPropertyList) {

    Map<ZPair<IRI, Boolean>, ZObjectProperty> map =
        new HashMap<ZPair<IRI, Boolean>, ZObjectProperty>();
    for (ZObjectProperty objectProperty : objectPropertyList) {
      for (ZPair<ZClass, Boolean> currentClass : objectProperty.getRangeListZClasses()) {
        ZPair<IRI, Boolean> key =
            new ZPair<IRI, Boolean>(currentClass.getKey().getIri(), objectProperty.isOverridable());
        if (map.containsKey(key)) {
          ZObjectProperty previousProperty = map.get(key);
          if (UTILS.propertyWeight(previousProperty) >= UTILS.propertyWeight(objectProperty)) {
            objectProperty.getRangeListZClasses().remove(currentClass);
          } else {
            previousProperty.getRangeListZClasses().remove(currentClass);
            map.replace(key, objectProperty);
          }
        } else {
          map.put(key, objectProperty);
        }
      }
    }

    return objectPropertyList;
  }

  public static ZObjectProperty preferredObjectProperty(ZObjectProperty leftZObjectProperty,
      ZObjectProperty rightZObjectProperty) {
    /*
     * Order of preference: Exactly Max Min Only Some
     */
    int leftPropertyType = propertyWeight(leftZObjectProperty);
    int rightPropertyType = propertyWeight(rightZObjectProperty);

    if (leftPropertyType > rightPropertyType)
      return leftZObjectProperty;
    else
      return rightZObjectProperty;
  }

  public static ZDataProperty preferredDataProperty(ZDataProperty leftZDataProperty,
      ZDataProperty rightZDataProperty) {
    /*
     * Order of preference: Exactly Max Min Only Some
     */
    int leftPropertyType = propertyWeight(leftZDataProperty);
    int rightPropertyType = propertyWeight(rightZDataProperty);

    if (leftPropertyType > rightPropertyType)
      return leftZDataProperty;
    else
      return rightZDataProperty;
  }

  public static boolean hasOntologyFormatExtension(String fileName) {
    if (fileName == null || fileName.isEmpty())
      return false;

    String fileExtension;
    if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
      fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

      switch (fileExtension.toLowerCase(Locale.ENGLISH)) {
        case "rdf":
          return true;
        case "owl":
          return true;
        case "ttl":
          return true;
        case "nq":
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  public static int weightOfProperties(ZClass zclass) {
    // Multiplying x2 the ObjectProperties to give more weight
    int weight = zclass.getZObjectPropertyList().size() * 2 + zclass.getZDataPropertyList().size();
    for (ZClass zclassMother : zclass.getSuperZClassList()) {
      weight = weight + weightOfProperties(zclassMother);
    }
    return weight;
  }

  /**
   * Picking the best mother class to extend in the multi-composition The selection is based on: 1-
   * number of ObjectProperties 2- number of DataProperties The objectProperties requires more
   * initialization such as List therefore the less the better in case of equality The best mother
   * to extend is the class having a lot of properties. The idea is to avoid its implementation
   * 
   * @param zclass
   * @return
   */
  public static ZClass pickBestMotherToExtend(ZClass zclass) {

    ZClass selectedBestMotherToExtend = null;

    int weightMother = 0;

    if (zclass.getSuperZClassList().size() > 0) {
      selectedBestMotherToExtend = zclass.getSuperZClassList().get(0);
      weightMother = weightOfProperties(selectedBestMotherToExtend);

      for (int i = 1; i < zclass.getSuperZClassList().size(); i++) {
        ZClass zcurrent = zclass.getSuperZClassList().get(i);
        int weightCurrent = weightOfProperties(zcurrent);

        if (weightCurrent > weightMother) {
          selectedBestMotherToExtend = zcurrent;
          weightMother = weightCurrent;
        }
      }
    }
    return selectedBestMotherToExtend;
  }

  private static ZObjectProperty identicalItem = null;

  public static ZObjectProperty listContainsObjectProperty(
      List<ZObjectProperty> listZObjectProperty, ZObjectProperty zObjectProperty) {
    identicalItem = null;
    List<ZObjectProperty> filteredList = listZObjectProperty.parallelStream().filter(f -> {
      return f.getObjectProperty().equals(zObjectProperty.getObjectProperty());
    }).collect(Collectors.toList());

    for (ZObjectProperty objectProperty : filteredList) {
      objectProperty.setOverridable();
      objectProperty.getRangeListZClasses().forEach(action -> {
        if (zObjectProperty.getRangeListZClasses().contains(action)) {
          identicalItem = objectProperty;
        }
      });
    }
    return identicalItem;
  }

  public static boolean listContainsDataProperty(List<ZDataProperty> listZDataProperty,
      ZDataProperty zDataProperty) {
    for (ZDataProperty zcurrent : listZDataProperty) {
      if (zcurrent.getDataProperty().toString()
          .contentEquals(zDataProperty.getDataProperty().toString())) {
        return true;
      }
    }
    return false;
  }

  public static String cleanPackageName(String path) throws InvalidUriException {

    String[] tokens = path.split("\\/");

    for (String token : tokens) {
      if (!isTokenValid(token)) {
        throw new InvalidUriException(token);
      }
    }

    return removeLastPoint(path.replaceAll("\\/", "."));
  }

  public static String cleanPackageName(IRI iri) {
    return removeLastPoint(cleanPath(iri).replaceAll("\\/", "."));
  }

  public static String firstLetterCapital(String inputString) {
    String cap = inputString.substring(0, 1).toUpperCase(Locale.ENGLISH) + inputString.substring(1);
    return cap;
  }

  public static void copyFiles(String fileName, String pathSource, String pathDestination)
      throws IOException, NullPointerException {
    InputStream inputStream = null;
    OutputStream outputStream = null;

    try {
      inputStream = UTILS.class.getResourceAsStream(pathSource + "/" + fileName);
      outputStream = new FileOutputStream(new File(pathDestination + "/" + fileName));

      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }

    } catch (IOException | NullPointerException e) {
      log.error(e);
      throw e;
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          log.error(e);
        }
      }
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          log.error(e);
        }
      }
    }

  }

  public static void copyDirectory(String directoryName, String pathSource, String pathDestination)
      throws IOException {
    File srcDir = new File(pathSource + "/" + directoryName);
    File destDir = new File(pathDestination);

    try {
      FileUtils.copyDirectory(srcDir, destDir);
    } catch (IOException e) {
      log.error(e);
      throw e;
    }
  }

  public static String removeLastPoint(String packageName) {
    if (packageName.lastIndexOf(".") == packageName.length() - 1) {
      packageName = packageName.substring(0, packageName.length() - 1);
    }
    return packageName;
  }

  public static boolean isPathValid(String path) {
    File file = new File(path);
    if ((file.exists() || file.mkdirs()) && file.canWrite()) {
      return true;
    } else {
      return false;
    }
  }

  public static String rebuildPath(String path) {
    Character lastCharacter = path.trim().charAt(path.length() - 1);
    if (lastCharacter.equals('/') || path.endsWith(File.separator)) {
      return path;
    }
    return path + File.separator;
  }


  public static void cleanDirectory(File directory) throws SecurityException, IOException {
    if (directory.exists()) {
      deleteDirectory(directory);
    }
    try {
      Files.createDirectory(directory.toPath());
    } catch (SecurityException e) {
      log.error(e);
      throw e;
    }
  }

  public static boolean deleteDirectory(File dir) {
    if (dir.isDirectory()) {
      File[] children = dir.listFiles();
      if (children == null)
        return dir.delete();

      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDirectory(children[i]);
        if (!success) {
          return false;
        }
      }
    }
    // either file or an empty directory
    return dir.delete();
  }

  /**
   * Used to validate any version.
   * 
   * @param "version" version string to be validated.
   * @return Boolean <Code>true</Code> if version is valid <Code>false</Code> otherwise.
   */

  public static boolean isVersionPatternValid(String version) {
    if (version == null || version.isEmpty()) {
      return false;
    }

    String[] tokens = version.split("\\.");

    if (tokens.length > 4) {
      return false;
    }

    for (String token : tokens) {
      try {
        Integer.parseInt(token);
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  /**
   * Used to validate class name.
   * 
   * @param "className" class name to be validated.
   */

  public static void validateClassName(String className) throws InvalidClassNameException {
    if (className == null || className.isEmpty() || !isTokenValid(className)) {
      throw new InvalidClassNameException(className);
    }
  }

  /**
   * Used to validate any token which might be class name, instance name, property or part of
   * package name.
   * 
   * @param "token" token to be validated.
   * @return Boolean <Code>true</Code> if token is valid <Code>false</Code> otherwise.
   */

  private static boolean isTokenValid(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }

    if (!keywordsSet.contains(token)
        && Pattern.compile("[A-Za-z_]+[a-zA-Z0-9_]*").matcher(token).matches()) {
      return true;
    }

    return false;
  }

  /**
   * Used to weight object property.
   * 
   * @param "zObjectProperty" object property to be weighted.
   * @return An Integer represents the weight.
   */

  public static int propertyWeight(ZObjectProperty zObjectProperty) {
    switch (zObjectProperty.getObjectPropertyType().toLowerCase(Locale.ENGLISH)) {
      case "some":
        return 1;
      case "only":
        return 2;
      case "min":
        return 3;
      case "max":
        return 4;
      case "exactly":
        return 5;
      default:
        return -1;
    }
  }

  /**
   * Used to weight data property.
   * 
   * @param "zDataProperty" data property to be weighted.
   * @return An Integer represents the weight.
   */

  private static int propertyWeight(ZDataProperty zDataProperty) {
    switch (zDataProperty.getRestrictionType().toLowerCase(Locale.ENGLISH)) {
      case "some":
        return 1;
      case "only":
        return 2;
      case "min":
        return 3;
      case "max":
        return 4;
      case "exactly":
        return 5;
      default:
        return -1;
    }
  }

  // These are the keywords of java and C# combined.
  private static final Set<String> keywordsSet = new HashSet<>(Arrays.asList("abstract", "as",
      "assert", "base", "bool", "boolean", "break", "byte", "case", "catch", "char", "checked",
      "class", "const", "continue", "decimal", "default", "delegate", "do", "double", "else",
      "enum", "extends", "event", "explicit", "extern", "false", "final", "finally", "fixed",
      "float", "for", "foreach", "goto", "if", "implicit", "implements", "import", "in", "int",
      "interface", "internal", "instanceof", "is", "lock", "long", "native", "namespace", "new",
      "null", "object", "operator", "out", "override", "params", "package", "private", "protected",
      "public", "readonly", "ref", "return", "sbyte", "sealed", "short", "sizeof", "stackalloc",
      "strictfp", "super", "static", "string", "struct", "switch", "this", "transient", "throw",
      "throws", "true", "try", "typeof", "uint", "ulong", "unchecked", "unsafe", "ushort", "using",
      "using static", "virtual", "void", "volatile", "while"));

}
