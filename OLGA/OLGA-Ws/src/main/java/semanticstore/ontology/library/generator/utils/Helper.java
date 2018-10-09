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
package semanticstore.ontology.library.generator.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.resources.ResourceManager;

public class Helper {

  static Map<String, Object> inputCmdParameters = new HashMap<String, Object>();
  static DefaultParser defaultParser;
  final static Logger log = Logger.getLogger(Helper.class);


  public static Map<String, Object> parseInputs(@NotNull @Valid String code,
      @NotNull @Valid String name, @NotNull @Valid String library, @Valid String preserve,
      @Valid String version, @Valid String partial, @Valid boolean skipCompile) {
    if (log.isDebugEnabled()) {
      log.debug(" -Helper ");
    }

    // create Options object
    Options options = new Options();
    Option outPathOption = Option.builder().argName("out").hasArg().longOpt("out")
        .desc(ResourceManager.getResource("outPathDesc")).build();
    Option codeOption = Option.builder().argName("code").hasArg().required()
        .desc(ResourceManager.getResource("codeDesc")).longOpt("code").build();
    Option libraryOption = Option.builder().argName("library").hasArg().required()
        .longOpt("library").desc(ResourceManager.getResource("libraryDesc")).build();
    Option nameOption = Option.builder().argName("name").hasArg().required().longOpt("name")
        .desc(ResourceManager.getResource("nameDesc")).build();
    Option pathToOntologiesOption = Option.builder().argName("path").hasArg().required()
        .longOpt("path").desc(ResourceManager.getResource("pathToOntologiesDesc")).build();
    Option versionOption = Option.builder().argName("version").hasArg().longOpt("version")
        .desc(ResourceManager.getResource("versionDesc")).build();
    Option skipInverseRelationsOption = Option.builder().argName("skipInverseRelations")
        .hasArg(false).longOpt("skipInverseRelations")
        .desc(ResourceManager.getResource("skipInverseRelationsDesc")).build();
    Option skipCompilation = Option.builder().argName("skipCompile").hasArg(false)
        .longOpt("skipCompile").desc(ResourceManager.getResource("skipCompileDesc")).build();

    // add code option
    options.addOption(codeOption);
    options.addOption(skipInverseRelationsOption);
    options.addOption(outPathOption);
    options.addOption(libraryOption);
    options.addOption(nameOption);
    options.addOption(pathToOntologiesOption);
    options.addOption(versionOption);
    options.addOption("preserve", false,
        "a parameter if set to true will preserve the namespace of the ontology during code generation");
    options.addOption("partial", false,
        "a parameter if set to true will generate C# code where all classes are partial");
    options.addOption("skipCleaning", false, "Skip Directory Cleaning Phase");
    options.addOption(skipCompilation);

    defaultParser = new DefaultParser();
    try {
      inputCmdParameters.clear();
      if (code != null) {
        inputCmdParameters.put("code", CODE.fromString(code));
      }
      if (library != null) {
        inputCmdParameters.put("library", LIBRARY.fromString(library));
      }
      if (name != null) {
        inputCmdParameters.put("name", name);
      }
      if (version != null) {
        inputCmdParameters.put("ontVersion", version);
      }
      if (preserve.equals("true")) {
        inputCmdParameters.put("preserve", true);
      } else {
        inputCmdParameters.put("preserve", false);
      }
      if (partial.equals("true")) {
        inputCmdParameters.put("partial", true);
      } else {
        inputCmdParameters.put("partial", false);
      }
      inputCmdParameters.put("skipCompile", skipCompile);
      UUID guid = java.util.UUID.randomUUID();
      inputCmdParameters.put("out", "./temp/" + guid.toString() + "/out");
      inputCmdParameters.put("skipInverseRelations", false);
    } catch (UnsupportedOperationException | ClassCastException | NullPointerException
        | IllegalArgumentException e) {
      log.error(e);
      throw e;
    }
    return inputCmdParameters;
  }
}
