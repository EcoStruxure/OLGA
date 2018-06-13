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

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import freemarker.template.TemplateException;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;
import semanticstore.ontology.library.generator.generators.Generator;
import semanticstore.ontology.library.generator.generators.SingletonGenerator;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.visitor.OLGAVisitor;

public class OLGA {

  static OWLOntologyManager owlManager;
  static Map<String, Object> inputCmdParameters;
  static String pathToOntologiesParam = null;
  static String result = "Fail";

  final static Charset UTF_8 = Charset.forName("UTF-8");
  final static Logger log = Logger.getLogger(OLGA.class);

  public static void main(String[] args) {
    if (System.getenv("M2_HOME") == null) {
      System.err
          .println("Please set M2_HOME in your environment to point to a valid maven binaries");
      return;
    }

    DateFormat dateFormatter = new SimpleDateFormat("mm:ss:SSS");
    long start_millis_overall = System.currentTimeMillis();

    // Parsing the user arguments
    inputCmdParameters = CliHelper.parseCLI(args);
    if (inputCmdParameters == null) {
      return;
    }
    // Loading one or more ontologies
    pathToOntologiesParam = (String) inputCmdParameters.get("pathToOntologiesParam");
    OWLOntology owlOntology = CliHelper.prepareOntology(pathToOntologiesParam);
    if (owlOntology == null) {
      result = "Fail";
      return;
    }
    try {
      long start_millis_visitor = System.currentTimeMillis();

      // Visitor Module
      OLGAVisitor olgaVisitor = new OLGAVisitor(owlOntology, (CODE) inputCmdParameters.get("code"),
          (LIBRARY) inputCmdParameters.get("library"));

      olgaVisitor.visit();

      long end_millis_visitor = System.currentTimeMillis();
      Date time_diff_visitor = new Date(end_millis_visitor - start_millis_visitor);
      String visitorTime = dateFormatter.format(time_diff_visitor);

      // Generator Module
      Generator generator = SingletonGenerator.getInstance(inputCmdParameters);
      result = generator.generateCode(olgaVisitor.getMapIRI_to_Zclass());

      long end_millis_overall = System.currentTimeMillis();
      Date time_diff_overall = new Date(end_millis_overall - start_millis_overall);
      String overallTime = dateFormatter.format(time_diff_overall);


      System.out.println("Visitor Time (mm:ss:SSS): " + visitorTime);
      System.out.println(
          "========== " + inputCmdParameters.get("name") + "-" + inputCmdParameters.get("library")
              + " (" + ((CODE) inputCmdParameters.get("code")).getText() + ") by OLGA : Build "
              + result + " in (mm:ss:SSS) " + overallTime + " ===========\n");
      if (!result.contains("Success")) {
        System.err.println(result);
        System.exit(42);
      }
    } catch (XmlPullParserException | MavenInvocationException | IOException
        | TemplateException e) {
      result = "Fail";
      log.error(e);
      System.out.println(e.getMessage());
      return;
    } catch (InvalidClassNameException | UnableToCompileGeneratedCodeException
        | InvalidUriException e) {
      result = "Fail";
      log.error(e);
      System.out.println(e.getMessage());
      return;
    } catch (Exception e) {
      result = "Fail";
      log.error(e);
      System.out.println(e.getMessage());
      return;
    }
  }

  public static String getResult() {
    return result;
  }
}
