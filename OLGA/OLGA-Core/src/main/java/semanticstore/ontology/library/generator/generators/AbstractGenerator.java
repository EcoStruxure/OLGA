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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.management.RuntimeErrorException;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.model.IRI;
import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;
import semanticstore.ontology.library.generator.compiler.OLGACompiler;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;
import semanticstore.ontology.library.generator.global.UTILS;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.resources.ResourceManager;

/**
 * AbstractGenerator is an abstract layer implementing template design pattern to add all common
 * functionality between different generators in it.
 */

public abstract class AbstractGenerator implements Generator {

  protected boolean preserveNameSpaceOfOntology = false;
  protected boolean skipCleaning = false;
  protected boolean skipCompilation;
  protected Configuration cfg;
  protected List<ZClass> listOfParentZClass;
  protected Map<IRI, ZClass> mapIRI_to_Zclass;
  protected Map<String, Object> dataToBeInject;
  protected String pomFilePath;
  protected String generatedLibraryConfig;
  protected String ontologyName;
  protected String outDirectoryPath;
  protected String path;
  protected String packageName;
  protected String ontologyVersion;
  protected String olgaVersion;
  protected String buildRepo;
  protected String keyRepo;
  protected String templateSelectionConfig;
  protected String cliDependency;
  private final static Logger log = Logger.getLogger(AbstractGenerator.class);

  @Override
  public void setParameters(Map<String, Object> inputCmdParameters)
      throws IOException, XmlPullParserException, InvalidUriException {
    this.cfg = new Configuration(new Version("2.3.23")); // Set FreeMarker paramaters
    this.ontologyName = (String) inputCmdParameters.get("name");
    this.olgaVersion = UTILS.getOlgaVersion();
    this.ontologyVersion = (String) inputCmdParameters.get("ontVersion");
    this.buildRepo = (String) inputCmdParameters.get("buildRepo");
    this.keyRepo = (String) inputCmdParameters.get("keyRepo");

    if (inputCmdParameters.containsKey("preserve")) {
      preserveNameSpaceOfOntology = true;
    } else {
      preserveNameSpaceOfOntology = false;
    }

    if (inputCmdParameters.containsKey("skipCleaning")) {
      this.skipCleaning = true;
    } else {
      this.skipCleaning = false;
    }

    this.skipCompilation = (boolean) inputCmdParameters.get("skipCompile");

    this.path = firstLetterCapital(ontologyName) + "/Rdf/Model/";
    this.packageName = UTILS.cleanPackageName(path);
    this.cliDependency = (String) inputCmdParameters.get("dependency");
    this.outDirectoryPath = ResourceManager.getResource("PROJECT_DIRECTORY")
        + ResourceManager.getResource("GENERATED_LIBRARY");

    if (inputCmdParameters.get("out") != null
        && UTILS.isPathValid((String) inputCmdParameters.get("out"))) {
      this.outDirectoryPath = UTILS.rebuildPath((String) inputCmdParameters.get("out"));
    } else {
      Path currentRelativePath = Paths.get("");
      String path = currentRelativePath.toAbsolutePath().toString()
          + ResourceManager.getResource("GENERATED_LIBRARY");
      if (UTILS.isPathValid(path)) {
        this.outDirectoryPath = path;
      }
    }
  }

  /**
   * Used to invoke the code generation process.
   * 
   * @param Hashmap of all ZClasses extracted from the ontology file.
   * @return A string <Code>fail</Code> if the code generation failed <Code>Success</Code>
   *         otherwise.
   */
  @Override
  public String generateCode(Map<IRI, ZClass> mapIRI_to_Zclass)
      throws IOException, TemplateException, MavenInvocationException,
      UnableToCompileGeneratedCodeException, RuntimeErrorException, MojoFailureException {
    try {
      this.mapIRI_to_Zclass = mapIRI_to_Zclass;

      long start_millis_generation = System.currentTimeMillis();
      if (!skipCleaning) {
        UTILS.cleanDirectory(new File(pomFilePath));
      }
      generateClass();
      generatePom();
      generateProjectFiles();

      DateFormat dateFormatter = new SimpleDateFormat("mm:ss:SSS");

      String result;
      if (!skipCompilation) {
        if (buildRepo != null && keyRepo != null) {
          result = OLGACompiler.maven_exec(pomFilePath, Arrays.asList("clean", "deploy"));
        } else {
          result = OLGACompiler.maven_clean_install(pomFilePath);
        }
      } else {
        result = "Success";
      }

      long end_millis_generation = System.currentTimeMillis();
      Date generationTime = new Date(end_millis_generation - start_millis_generation);
      String generationTimeString = dateFormatter.format(generationTime);
      System.out.println("Code Generation Time (mm:ss:SSS): " + generationTimeString);

      return result;
    } catch (IOException | TemplateException | UnableToCompileGeneratedCodeException
        | NullPointerException e) {
      log.error(e);
      throw e;
    }
  }

  @Override
  public abstract void generateClass() throws IOException, TemplateException;

  @Override
  public abstract void generatePom() throws TemplateNotFoundException,
      MalformedTemplateNameException, ParseException, IOException, TemplateException;

  @Override
  public abstract void generateProjectFiles()
      throws IOException, FileNotFoundException, TemplateException;

  protected String firstLetterCapital(String inputString) {
    String cap = inputString.substring(0, 1).toUpperCase(Locale.ENGLISH) + inputString.substring(1);
    return cap;
  }

  protected void setFreeMarkerConfiguration(String templateSelectionConfig) throws IOException {
    cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), templateSelectionConfig));
    cfg.setDefaultEncoding("UTF-8");
  }
}
