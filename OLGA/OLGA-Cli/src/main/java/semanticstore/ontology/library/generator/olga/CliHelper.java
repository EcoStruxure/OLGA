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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.resources.ResourceManager;

public class CliHelper {

  static Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
  static DefaultParser defaultParser;
  final static Logger log = Logger.getLogger(CliHelper.class);
  static boolean atLeastOneValidOntologyFile = false;

  public static Map<String, Object> parseCLI(String[] args) {
    // create Options object
    Options options = new Options();
    Option outPath = Option.builder().argName("out").hasArg().longOpt("out")
        .desc(ResourceManager.getResource("outPathDesc")).build();
    Option code = Option.builder().argName("code").hasArg().required()
        .desc(ResourceManager.getResource("codeDesc")).longOpt("code").build();
    Option library = Option.builder().argName("library").hasArg().required().longOpt("library")
        .desc(ResourceManager.getResource("libraryDesc")).build();
    Option name = Option.builder().argName("name").hasArg().required().longOpt("name")
        .desc(ResourceManager.getResource("nameDesc")).build();
    Option pathToOntologies = Option.builder().argName("path").hasArg().required().longOpt("path")
        .desc(ResourceManager.getResource("pathToOntologiesDesc")).build();
    Option version = Option.builder().argName("version").hasArg().longOpt("version")
        .desc(ResourceManager.getResource("versionDesc")).build();
    Option skipInverseRelations = Option.builder().argName("skipInverseRelations").hasArg(false)
        .longOpt("skipInverseRelations")
        .desc(ResourceManager.getResource("skipInverseRelationsDesc")).build();
    Option skipCompilation = Option.builder().argName("skipCompile").hasArg(false)
        .longOpt("skipCompile").desc(ResourceManager.getResource("skipCompileDesc")).build();

    // add code option
    options.addOption(code);
    options.addOption(skipInverseRelations);
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
    options.addOption(skipCompilation);

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
        inputCmdParameters.put("pathToOntologiesParam", line.getOptionValue("path"));
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
      if (line.hasOption("skipInverseRelations")) {
        inputCmdParameters.put("skipInverseRelations", true);
      } else {
        inputCmdParameters.put("skipInverseRelations", false);
      }
      inputCmdParameters.put("skipCompile", line.hasOption("skipCompile"));
    } catch (ParseException exp) {
      System.out.println("Missing argument:" + exp.getMessage());
      formatter.printHelp("help", options);
      inputCmdParameters.clear();
      return null;
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("help", options);
      inputCmdParameters.clear();
      return null;
    }
    return inputCmdParameters;
  }
}
