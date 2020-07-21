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
package semanticstore.ontology.library.generator.generators.python;

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
import java.nio.file.Paths;
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
import semanticstore.ontology.library.generator.model.ZInstance;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.resources.ResourceManager;

public class RdfAlchemyGenerator extends AbstractGenerator {

  private final static Logger log = Logger.getLogger(RdfAlchemyGenerator.class);

  public RdfAlchemyGenerator() {
    dataToBeInject = new HashMap<>();
    listOfParentZClass = new ArrayList<>();
  }

  public void setParameters(Map<String, Object> inputCmdParameters)
      throws IOException, XmlPullParserException, InvalidUriException {
    super.setParameters(inputCmdParameters);

    pomFilePath = this.outDirectoryPath + ontologyName + "-python/";
  }

  @Override
  public void generateClass() throws IOException, TemplateException {
    String instanceTemplateName = "template_python_instances.ftl";
    Template instanceTemplate = null;

    String classTemplateName = "template_python_classes.ftl";
    Template classTemplate = null;
    // destination target

    generatedLibraryConfig = pomFilePath + ontologyName.toLowerCase(Locale.ENGLISH) + "/";
    // templates location
    templateSelectionConfig = ResourceManager.getResource("PYTHON_RDFALCHEMY_TEMPLATES");

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
      // user request to preserve the path or not
      if (preserveNameSpaceOfOntology) {
        path = zclass.getPath();
        packageName = UTILS.cleanPackageName(path);
      }

      if (log.isDebugEnabled()) {
        log.debug(" -Generate Classes: " + zclass.getzClassName());
      }

      dataToBeInject.clear();
      List<ZObjectProperty> objectProperties = zclass.getListOfObjectPropertiesForClass();
      dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));
      dataToBeInject.put("Zclass", zclass);
      dataToBeInject.put("listDataPropertiesToImplement", zclass.getListOfDataPropertiesForClass());
      dataToBeInject.put("listObjectPropertiesToImplement", objectProperties);
      dataToBeInject.put("preserveNameSpaceOfOntology", preserveNameSpaceOfOntology);


      if (preserveNameSpaceOfOntology) {
        objectProperties.forEach(property -> {
          property.getRangeListZClasses().forEach(pair -> {
            zclass.add_listOfImports(pair.getKey().getIri());
          });
        });
      }

      try (StringWriter out = new StringWriter()) {

        // create File Directory for the code if does not exists already
        if (UTILS.isPathValid(generatedLibraryConfig + path)) {
          addInitpy(generatedLibraryConfig + path, ontologyName.toLowerCase(Locale.ENGLISH));
        }


        // prepare to generate the Class according to its namespace
        classTemplate = cfg.getTemplate(classTemplateName); // Load Class template file
        File classFile = new File(
            generatedLibraryConfig + path + File.separator + zclass.getzClassName() + ".py");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(classFile), "UTF-8");) {
          classTemplate.process(dataToBeInject, writer); // Create classes\

        }

        if (!zclass.getListZInstanceIRI().isEmpty()) {
          for (ZInstance zInstance : zclass.getListZInstanceIRI()) {
            // user request to preserve the path or not
            if (preserveNameSpaceOfOntology) {
              path = zInstance.getPath();
              packageName = UTILS.cleanPackageName(path);
            }

            if (UTILS.isPathValid(generatedLibraryConfig + path)) {
              addInitpy(generatedLibraryConfig + path, ontologyName.toLowerCase(Locale.ENGLISH));
            }

            File instanceFile = new File(generatedLibraryConfig + path + File.separator
                + zInstance.getzInstanceName() + ".py");

            dataToBeInject.put("ZInstance", zInstance);

            instanceTemplate = cfg.getTemplate(instanceTemplateName); // Load Class template file
            try (Writer instanceWriter = new OutputStreamWriter(new FileOutputStream(instanceFile), "UTF-8");) {
              instanceTemplate.process(dataToBeInject, instanceWriter);
            }
          }
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

    UTILS.isPathValid(pomFilePath);
    // File pom = new File(pomFilePath);
    Template template_pom;
    dataToBeInject.clear();
    dataToBeInject.put("ontologyName", ontologyName);
    dataToBeInject.put("path", Paths.get(pomFilePath).toAbsolutePath().toString());
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

    // copy app.config
    UTILS.copyFiles("__init__.py", ResourceManager.getResource("PYTHON_RDFALCHEMY_TEMPLATES"),
        pomFilePath + ontologyName.toLowerCase(Locale.ENGLISH));
    UTILS.copyFiles("setup.cfg", ResourceManager.getResource("PYTHON_RDFALCHEMY_TEMPLATES"),
        pomFilePath);
    UTILS.copyFiles("README.rst", ResourceManager.getResource("PYTHON_RDFALCHEMY_TEMPLATES"),
        pomFilePath);

    Template template_setup;

    try {
      template_setup = cfg.getTemplate("setup.ftl");
    } catch (IOException e1) {
      log.error(e1);
      throw e1;
    }

    dataToBeInject.clear();
    dataToBeInject.put("ontologyName", ontologyName);

    try (BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pomFilePath + "setup.py"),
            StandardCharsets.UTF_8));) {
      template_setup.process(dataToBeInject, writer);
    } catch (TemplateException e) {
      log.error(e);
      throw e;
    }
  }

  public void addInitpy(String path, String ontologyName) throws IOException {
    boolean isOutputDirectory = false;
    String[] tokens = path.split("/");
    // copy __init__.py
    StringBuilder address = new StringBuilder();
    for (String token : tokens) {
      if (token.equalsIgnoreCase(ontologyName + "-python")) {
        isOutputDirectory = true;
      }
      address.append(token + "/");
      if (isOutputDirectory) {
        UTILS.copyFiles("__init__.py", ResourceManager.getResource("PYTHON_RDFALCHEMY_TEMPLATES"),
            address.toString());
      }
    }
  }
}
