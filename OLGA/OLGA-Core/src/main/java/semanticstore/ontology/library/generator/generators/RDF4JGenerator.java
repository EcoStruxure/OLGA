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
import semanticstore.ontology.library.generator.global.UTILS;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.resources.ResourceManager;

public class RDF4JGenerator extends AbstractGenerator {

  private String globalFile;
  private final static Logger log = Logger.getLogger(RDF4JGenerator.class);

  public RDF4JGenerator() {
    dataToBeInject = new HashMap<>();
    listOfParentZClass = new ArrayList<>();
  }

  public void setParameters(Map<String, Object> inputCmdParameters)
      throws IOException, XmlPullParserException, InvalidUriException {
    super.setParameters(inputCmdParameters);

    pomFilePath = this.outDirectoryPath + ontologyName.toLowerCase(Locale.ENGLISH) + "-RDF4J-java/";
  }

  @SuppressWarnings("unchecked")
  @Override
  public void generateClass() throws IOException, TemplateException {
    String interfaceTemplateName = "template_java_rdf4j_interfaces.ftl";
    Template interfaceTemplate = null;

    String classTemplateName = "template_java_rdf4j_classes.ftl";
    Template classTemplate = null;
    // destination target

    generatedLibraryConfig = pomFilePath + "src/main/java/";
    globalFile =
        generatedLibraryConfig + ontologyName.toLowerCase(Locale.ENGLISH) + "/global/util/";
    // templates location
    templateSelectionConfig = ResourceManager.getResource("JAVA_RDF4J_TEMPLATES");

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

      UTILS.isPathValid(
          UTILS.rebuildPath(generatedLibraryConfig) + path.toLowerCase(Locale.ENGLISH));

      if (log.isDebugEnabled()) {
        log.debug(" -Generate Classes: " + zclass.getzClassName());
      }

      dataToBeInject.clear();

      List<ZObjectProperty> objectProperties = zclass.getListOfObjectPropertiesForClass();
      dataToBeInject.put("motherToExtend", bestMotherToExtend);
      dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));
      dataToBeInject.put("packageName", packageName.toLowerCase(Locale.ENGLISH));
      dataToBeInject.put("Zclass", zclass);
      dataToBeInject.put("listDataPropertiesToImplement", zclass.getListOfDataPropertiesForClass());
      dataToBeInject.put("listObjectPropertiesToImplement", objectProperties);

      // create File Directory for the code if does not exists already
      dataToBeInject.put("motherClassList", zclass.getListOfParentsToImplement());
      dataToBeInject.put("objectPropertyList", zclass.getListOfObjectPropertiesForInterface());
      dataToBeInject.put("dataPropertyList", zclass.getListOfDataPropertiesForInterface());
      dataToBeInject.put("OLGAVersion", olgaVersion);
      dataToBeInject.put("preserveNameSpaceOfOntology", preserveNameSpaceOfOntology);

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
        File classFile = new File(generatedLibraryConfig + path.toLowerCase(Locale.ENGLISH)
            + File.separator + zclass.getzClassName() + ".java");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(classFile), "UTF-8");) {
          classTemplate.process(dataToBeInject, writer); // Create classes\
        }

        if (bestMotherToExtend != null) {
          ((List<ZClass>) dataToBeInject.get("motherClassList")).add(bestMotherToExtend);
        }

        File interfaceFile = new File(generatedLibraryConfig + path.toLowerCase(Locale.ENGLISH)
            + File.separator + "I" + zclass.getzClassName() + ".java");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(interfaceFile), "UTF-8");) {
          // prepare to generate the Interface
          interfaceTemplate = cfg.getTemplate(interfaceTemplateName); // Load Interface template file
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

  @Override
  public void generatePom() throws TemplateNotFoundException, MalformedTemplateNameException,
      ParseException, IOException, TemplateException {
    if (log.isDebugEnabled()) {
      log.debug(" -Generate POM ");
    }

    Template template_pom;
    dataToBeInject.clear();
    dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));
    dataToBeInject.put("OLGAVersion", olgaVersion);
    dataToBeInject.put("OntologyVersion", ontologyVersion);

    try (BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pomFilePath + "pom.xml"),
            StandardCharsets.UTF_8));) {
      template_pom = cfg.getTemplate("template_pom.ftl");
      template_pom.process(dataToBeInject, writer);
    } catch (IOException e) {
      log.error(e);
      throw e;
    }
  }

  @Override
  public void generateProjectFiles() throws IOException, FileNotFoundException, TemplateException {

    if (log.isDebugEnabled()) {
      log.debug(" -Generate Java Global File");
    }

    Template template_refID;

    try {
      template_refID = cfg.getTemplate("template_java_rdf4j_global.ftl");
    } catch (IOException e1) {
      log.error(e1);
      throw e1;
    }

    dataToBeInject.clear();
    dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));

    UTILS.isPathValid(globalFile);

    try (BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(globalFile + "GLOBAL.java"),
            StandardCharsets.UTF_8));) {
      template_refID.process(dataToBeInject, writer);
    } catch (TemplateException e) {
      log.error(e);
      throw e;
    }
  }
}
