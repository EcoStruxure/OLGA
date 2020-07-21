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
package semanticstore.ontology.library.generator.generators.csharp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.model.IRI;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.generators.AbstractGenerator;
import semanticstore.ontology.library.generator.global.UTILS;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.resources.ResourceManager;

/**
 * TrinityGenerator is a class implements Generator interface, and we use TrinityGenerator to
 * produce C# ".Net Standard 2.0" projects.
 */

public class TrinityGenerator extends AbstractGenerator {

  private boolean generatePartial = false;
  private final static Logger log = Logger.getLogger(TrinityGenerator.class);

  public TrinityGenerator() {
    dataToBeInject = new HashMap<>();
    listOfParentZClass = new ArrayList<>();
  }

  /**
   * Initialize all the local variables in this function
   * 
   * @param Hashmap of all parameters that passed by the user.
   */

  public void setParameters(Map<String, Object> inputCmdParameters)
      throws IOException, XmlPullParserException, InvalidUriException {
    super.setParameters(inputCmdParameters);

    if (inputCmdParameters.containsKey("partial")) {
      this.generatePartial = true;
    } else {
      this.generatePartial = false;
    }

    pomFilePath = this.outDirectoryPath + ontologyName + "-dotnetTrinity/";
  }

  /**
   * @throws CloneNotSupportedException Used to invoke the classes and interfaces files generation
   *         process.
   */

  @SuppressWarnings("unchecked")
  @Override
  public void generateClass() throws IOException, TemplateException {
    String classTemplateName = "trinity_template_class.ftl";
    Template classTemplate = null;

    String interfaceTemplateName = "trinity_template_interface.ftl";
    Template interfaceTemplate = null;

    // destination target
    generatedLibraryConfig = pomFilePath;
    // templates location
    templateSelectionConfig = ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES");


    try {
      // preparing Freemarker configuration
      setFreeMarkerConfiguration(templateSelectionConfig);
    } catch (IOException e) {
      log.error(e);
      throw e;
    }

    for (Entry<IRI, ZClass> entry : mapIRI_to_Zclass.entrySet()) {    	
      ZClass zclass = entry.getValue(); // Get ZClass
      if(zclass.getGenerate()==true) {
      ZClass bestMotherToExtend = zclass.getParentToExtend();

      // user request to preserve the path or not
      if (preserveNameSpaceOfOntology) {
        path = zclass.getPath();
        packageName = UTILS.cleanPackageName(path);
      }

      UTILS.isPathValid(UTILS.rebuildPath(generatedLibraryConfig) + path);

      if (log.isDebugEnabled()) {
        log.debug(" -Generate Classes: " + zclass.getzClassName());
      }

      dataToBeInject.clear();
      List<ZObjectProperty> objectProperties = zclass.getListOfObjectPropertiesForClass();
      dataToBeInject.put("generatePartial", generatePartial);
      dataToBeInject.put("motherToExtend", bestMotherToExtend);
      dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));
      dataToBeInject.put("packageName", packageName);
      dataToBeInject.put("Zclass", zclass);
      dataToBeInject.put("listDataPropertiesToImplement", zclass.getListOfDataPropertiesForClass());
      dataToBeInject.put("listObjectPropertiesToImplement", objectProperties);

      // create File Directory for the code if does not exists already
      dataToBeInject.put("motherClassList", zclass.getListOfParentsToImplement());
      dataToBeInject.put("OLGAVersion", olgaVersion);
      dataToBeInject.put("preserveNameSpaceOfOntology", preserveNameSpaceOfOntology);

      boolean containsCollections = zclass.getZObjectPropertyList().parallelStream().anyMatch(f -> {
        return f.getObjectPropertyType().contains("Some")
            || f.getObjectPropertyType().contains("Only");
      });

      dataToBeInject.put("hasCollections", containsCollections);

      if (preserveNameSpaceOfOntology) {
        objectProperties.forEach(property -> {
          property.getRangeListZClasses().forEach(pair -> {
            zclass.add_listOfImports(pair.getKey().getIri());
          });
        });
      }

      try (StringWriter out = new StringWriter()) {
        // prepare to generate the Class
        classTemplate = cfg.getTemplate(classTemplateName); // Load Interface template file
        File classFile = new File(
            generatedLibraryConfig + path + File.separator + zclass.getzClassName() + ".cs");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(classFile), "UTF-8");) {
          classTemplate.process(dataToBeInject, writer); // Create classes\
        }

        if (bestMotherToExtend != null) {
          ((List<ZClass>) dataToBeInject.get("motherClassList")).add(bestMotherToExtend);
        }

        dataToBeInject.put("objectPropertyList", zclass.getListOfObjectPropertiesForInterface());
        dataToBeInject.put("dataPropertyList", zclass.getListOfDataPropertiesForInterface());
        File interfaceFile = new File(
            generatedLibraryConfig + path + File.separator + "I" + zclass.getzClassName() + ".cs");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(interfaceFile), "UTF-8");) {
          // prepare to generate the Interface
          interfaceTemplate = cfg.getTemplate(interfaceTemplateName); // Load Interface template
                                                                      // file
          interfaceTemplate.process(dataToBeInject, writer); // Create interfaces
        }

        listOfParentZClass.clear();
        out.flush();
      } catch (NullPointerException | IOException | TemplateException e) {
        log.error(e);
        throw e;
      }
    }
    }
  }

  /**
   * Used to invoke the .Net Standard project and the static dependence files generation process.
   */

  @Override
  public void generateProjectFiles() throws IOException, FileNotFoundException, TemplateException {

    if (log.isDebugEnabled()) {
      log.debug(" -Generate .Net Project ");
    }

    Template template_proj;

    try {
      template_proj = cfg.getTemplate("trinity_template_project_Standard.ftl");
    } catch (IOException e1) {
      log.error(e1);
      throw e1;
    }

    dataToBeInject.clear();
    dataToBeInject.put("ontologyName", ontologyName);
    dataToBeInject.put("version", ontologyVersion);
    dataToBeInject.put("dependency", cliDependency);
    dataToBeInject.put("ontologyVersion", ontologyVersion);

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(pomFilePath + ontologyName.toLowerCase(Locale.ENGLISH) + ".csproj"),
        StandardCharsets.UTF_8));) {
      template_proj.process(dataToBeInject, writer);
    } catch (TemplateException e) {
      log.error(e);
      throw e;
    }

    copyDependencies();
  }

  private void copyDependencies() throws IOException {
    try {
      String pathOut = pomFilePath + "Ontologies/";
      if (UTILS.isPathValid(pathOut)) {
        UTILS.copyFiles("Ontologies.i.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
      }
      pathOut = pomFilePath + "Dependencies/Olga/Trinity/Resources/";
      if (UTILS.isPathValid(pathOut)) {
        UTILS.copyFiles("Glossary.Designer.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
        UTILS.copyFiles("Glossary.resx", ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"),
            pathOut);
      }
      pathOut = pomFilePath + "Olga/Resources/";
      if (UTILS.isPathValid(pathOut)) {
        UTILS.copyFiles("OlgaResources.properties",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
      }
      pathOut = pomFilePath + "Dependencies/Olga/Trinity/Exceptions/";
      if (UTILS.isPathValid(pathOut)) {
        UTILS.copyFiles("InvalidContextException.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
      }
      pathOut = pomFilePath + "Dependencies/Olga/Trinity/Attriubtes/";
      if (UTILS.isPathValid(pathOut)) {
        UTILS.copyFiles("QueryWithReasoningAttribute.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
      }
      pathOut = pomFilePath + "Dependencies/Olga/Trinity/";
      if (UTILS.isPathValid(pathOut)) {
        UTILS.copyFiles("ContextResource.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
        UTILS.copyFiles("IRdfContext.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
        UTILS.copyFiles("ModelTypeReasonerInspector.cs",
            ResourceManager.getResource("DOTNET_TRINITYRDF_TEMPLATES"), pathOut);
      }
    } catch (FileNotFoundException | NullPointerException e1) {
      log.error(e1);
      throw e1;
    }
  }

  /**
   * Used to invoke the POM file generation process.
   */

  @Override
  public void generatePom() throws TemplateNotFoundException, MalformedTemplateNameException,
      ParseException, IOException, TemplateException, NullPointerException, FileNotFoundException {
    if (log.isDebugEnabled()) {
      log.debug(" -Generate POM ");
    }

    Template template_pom;
    dataToBeInject.clear();
    dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));
    dataToBeInject.put("OLGAVersion", olgaVersion);
    dataToBeInject.put("OntologyVersion", ontologyVersion);
    dataToBeInject.put("buildRepo", buildRepo);
    dataToBeInject.put("keyRepo", keyRepo);

    try (BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pomFilePath + "pom.xml"),
            StandardCharsets.UTF_8));) {
      template_pom = cfg.getTemplate("trinity_template_pom.ftl");
      template_pom.process(dataToBeInject, writer);
    } catch (IOException e) {
      log.error(e);
      throw e;
    }
  }

}
