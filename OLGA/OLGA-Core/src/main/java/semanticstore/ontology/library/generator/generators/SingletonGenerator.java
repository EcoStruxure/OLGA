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
package semanticstore.ontology.library.generator.generators;

import java.io.IOException;
import java.util.Map;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.global.LIBRARY;

/**
 * SingletonGenerator is a class follow singleton design pattern to generate different generator
 * classes we will use to generate code in different programing languages.
 */

public class SingletonGenerator {

  private static AbstractGenerator trinitySingleton = null;
  private static AbstractGenerator rdf4jSingleton = null;
  private static AbstractGenerator rdfAlchemySingleton = null;

  protected SingletonGenerator() {
    // Exists only to defeat instantiation.
  }

  public static synchronized AbstractGenerator getInstance(Map<String, Object> inputCmdParameters)
      throws IOException, XmlPullParserException, InvalidUriException {
    LIBRARY library = (LIBRARY) inputCmdParameters.get("library");
    switch (library) {
      case TRINITY:
        if (trinitySingleton == null) {
          trinitySingleton = new TrinityGenerator();
        }
        trinitySingleton.setParameters(inputCmdParameters);
        return trinitySingleton;
      case RDF4J:
        if (rdf4jSingleton == null) {
          rdf4jSingleton = new RDF4JGenerator();
        }
        rdf4jSingleton.setParameters(inputCmdParameters);
        return rdf4jSingleton;
      case RDFALCHEMY:
        if (rdfAlchemySingleton == null) {
          rdfAlchemySingleton = new RdfAlchemyGenerator();
        }
        rdfAlchemySingleton.setParameters(inputCmdParameters);
        return rdfAlchemySingleton;
      default:
        break;
    }
    return null;
  }
}
