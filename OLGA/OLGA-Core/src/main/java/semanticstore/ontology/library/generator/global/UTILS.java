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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;
import antlr.Utils;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.resources.ResourceManager;

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
   * Used to delete any file or directory recursively.
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
   * Used to get the OLGE version from resources file.
   * 
   * @return the version string.
   */

  public static String getOlgaVersion() {
    String version = ResourceManager.getResource("OLGAVersion");
    return version;
  }

  public static String cleanPackageName(String path) throws InvalidUriException {

    String[] tokens = path.split("\\/");

    for (String token : tokens) {
      if (!isTokenValid(token)) {
        throw new InvalidUriException(token);
      }
    }

    return UTILS.removeLastPoint(path.replaceAll("\\/", "."));
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

  public static boolean hasOntologyFormatExtension(String fileName) {
    if (fileName == null || fileName.isEmpty())
      return false;

    String fileExtension;
    if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
      fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

      switch (fileExtension.toLowerCase(Locale.ENGLISH)) {
        case "rdf":
        case "owl":
        case "ttl":
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

  public static String cleanPackageName(IRI iri) {
    return removeLastPoint(cleanPath(iri).replaceAll("\\/", "."));
  }

  public static void copyFiles(String fileName, String pathSource, String pathDestination)
      throws IOException, NullPointerException {
     
    try ( InputStream inputStream = UTILS.class.getResourceAsStream(pathSource + "/" + fileName); OutputStream outputStream = new FileOutputStream(new File(pathDestination + "/" + fileName));){
      
      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }

    } catch (IOException | NullPointerException e) {
      log.error(e);
      throw e;
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

  /**
   * If path is correct, and directory does not exist, creates a directory
   * 
   * @param path
   * @return boolean
   */
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

  public static int propertyWeight(ZDataProperty zDataProperty) {
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
