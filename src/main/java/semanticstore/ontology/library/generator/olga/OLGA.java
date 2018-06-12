/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.olga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import freemarker.template.TemplateException;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;
import semanticstore.ontology.library.generator.generators.Generator;
import semanticstore.ontology.library.generator.generators.SingletonGenerator;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.global.UTILS;
import semanticstore.ontology.library.generator.visitor.OLGAVisitor;

public class OLGA {

  static OWLOntologyManager owlManager;
  static File directory;
  static DefaultParser defaultParser;
  static String pathToOntologiesParam = null;
  static String result;
  static Map<String, Object> inputCmdParameters = new HashMap<>();
  static boolean atLeastOneValidOntologyFile;
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

    // create Options object
    Options options = new Options();
    Option outPath = Option.builder().argName("out").hasArg().longOpt("out")
        .desc("a path to output directory").build();
    Option code = Option.builder().argName("code").hasArg().required().desc("java, cs, or py")
        .longOpt("code").build();
    Option library = Option.builder().argName("library").hasArg().required().longOpt("library")
        .desc("[trinity]").build();
    Option name = Option.builder().argName("name").hasArg().required().longOpt("name")
        .desc("Generated Library Name").build();
    Option pathToOntologies = Option.builder().argName("path").hasArg().required().longOpt("path")
        .desc("a path to a repository of one or more ontologies").build();
    Option version = Option.builder().argName("version").hasArg().longOpt("version").desc(
        "a new version forced in entry, used when merging two or more ontologies having different versions")
        .build();

    // add code option
    options.addOption(code);
    options.addOption(outPath);
    options.addOption(library);
    options.addOption(name);
    options.addOption(pathToOntologies);
    options.addOption(version);
    options.addOption("preserve", false,
        "a parameter if set to true will preserve the namespace of the ontology during code generation");
    options.addOption("partial", false,
        "a parameter if set to true will generate C# code where all classes are partial");
    options.addOption("skipCleaning", false, "Skip Directory Cleaning Phase");

    defaultParser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    try {
      inputCmdParameters.clear();
      CommandLine line = defaultParser.parse(options, args);

      if (line.hasOption("code")) {
        inputCmdParameters.put("code", CODE.fromString(line.getOptionValue("code")));
      }
      if (line.hasOption("out")) {
        inputCmdParameters.put("out", line.getOptionValue("out"));
      }
      if (line.hasOption("library")) {
        inputCmdParameters.put("library", LIBRARY.fromString(line.getOptionValue("library")));
      }
      if (line.hasOption("path")) {
        pathToOntologiesParam = line.getOptionValue("path");
      }
      if (line.hasOption("name")) {
        inputCmdParameters.put("name", line.getOptionValue("name"));
      }
      if (line.hasOption("version")) {
        inputCmdParameters.put("ontVersion", Optional.of(line.getOptionValue("version")).get());
      }
      if (line.hasOption("preserve")) {
        inputCmdParameters.put("preserve", true);
      }
      if (line.hasOption("skipCleaning")) {
        inputCmdParameters.put("skipCleaning", true);
      }
      if (line.hasOption("partial")) {
        inputCmdParameters.put("partial", true);
      }
      if (line.hasOption("help")) {
        formatter.printHelp("help", options);
      }
    } catch (ParseException exp) {
      System.out.println("Missing argument:" + exp.getMessage());
      formatter.printHelp("help", options);
      inputCmdParameters.clear();
      return;
    }

    owlManager = OWLManager.createOWLOntologyManager();
    directory = new File(pathToOntologiesParam);
    AutoIRIMapper mapper = new AutoIRIMapper(directory, true);
    owlManager.getIRIMappers().add(mapper);

    try (Stream<Path> paths = Files.walk(Paths.get(pathToOntologiesParam))) {
      paths.forEach(filePath -> {
        if (Files.isRegularFile(filePath)) {
          File test_ontFile = new File(filePath.toString());
          if (UTILS.hasOntologyFormatExtension(test_ontFile.getName())) {
            try {
              OWLOntology currentOntology =
                  owlManager.loadOntologyFromOntologyDocument(test_ontFile);

              atLeastOneValidOntologyFile = true;
              // If the user didn't specify a new version for the merge, just use one of the
              // versions
              String ontologyVersion;
              if (!(inputCmdParameters.containsKey("ontVersion")
                  && UTILS.isVersionPatternValid((String) inputCmdParameters.get("ontVersion")))) {
                Optional<IRI> versionOpt = currentOntology.getOntologyID().getVersionIRI();                
                if (versionOpt.isPresent()
                    && UTILS.isVersionPatternValid(versionOpt.get().getShortForm())) {
                  ontologyVersion = versionOpt.get().getShortForm();
                } else {
                  ontologyVersion = UTILS.getOlgaVersion();
                }
                inputCmdParameters.put("ontVersion", ontologyVersion);
              }else {
                ontologyVersion = (String) inputCmdParameters.get("ontVersion");
              }
            } catch (OWLOntologyCreationException | FileNotFoundException
                | XmlPullParserException e) {
              log.error(e);
              log.error(e.getStackTrace().toString());

              System.out.println(e.getMessage());
              return;
            } catch (IOException e) {
              log.error(e);
              log.error(e.getStackTrace().toString());
              System.out.println(e.getMessage());
              return;
            } catch (Exception e) {
              log.error(e);
              log.error(e.getStackTrace().toString());
              System.out.println(e.getMessage());
              return;
            }
          }
        }
      });
    } catch (IOException e) {
      log.error(e);
      log.error(e.getStackTrace());
    }

    if (!atLeastOneValidOntologyFile) {
      System.err.println("No valid Ontology File found");
      return;
    }
    OWLOntologyMerger merger = new OWLOntologyMerger(owlManager);
    OWLOntologyManager owlManager2 = OWLManager.createOWLOntologyManager();
    IRI mergedOntologyIRI = IRI.create("mergedOntology");

    try {
      OWLOntology merged = merger.createMergedOntology(owlManager2, mergedOntologyIRI);
      long start_millis_visitor = System.currentTimeMillis();
      OLGAVisitor olgaVisitor = new OLGAVisitor(merged, (CODE) inputCmdParameters.get("code"),
          (LIBRARY) inputCmdParameters.get("library"));

      olgaVisitor.visit();

      long end_millis_visitor = System.currentTimeMillis();
      Date time_diff_visitor = new Date(end_millis_visitor - start_millis_visitor);
      String visitorTime = dateFormatter.format(time_diff_visitor);

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
    } catch (OWLOntologyCreationException | XmlPullParserException | MavenInvocationException
        | IOException | TemplateException e) {
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
