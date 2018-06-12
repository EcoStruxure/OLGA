/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.management.RuntimeErrorException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.model.IRI;
import org.apache.log4j.Logger;
import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;
import semanticstore.ontology.library.generator.compiler.OLGACompiler;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;
import semanticstore.ontology.library.generator.global.CONFIG;
import semanticstore.ontology.library.generator.global.UTILS;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZInstance;
import semanticstore.ontology.library.generator.model.ZObjectProperty;

/**
 * TrinityGenerator is a class implements Generator interface, and we use TrinityGenerator to
 * produce C# ".Net Standard 2.0" projects.
 */

public class TrinityGenerator implements Generator {

  private String pomFilePath;
  private String templateSelectionConfig;
  private String generatedLibraryConfig;
  private Configuration cfg;
  private String ontologyName;
  private String outDirectoryPath;
  private Map<String, Object> dataToBeInject;
  private Map<IRI, ZClass> mapIRI_to_Zclass;
  private List<String> listOfGeneratedClasses;
  private List<ZClass> listOfParentZClass;
  private List<String> listOfConsolidatedImports;
  private String path;
  private String packageName;
  private String ontologyVersion;
  private String olgaVersion;
  private String buildRepo;
  private String keyRepo;
  private boolean preserveNameSpaceOfOntology;
  private boolean generatePartial;
  private String cliDependency;
  private boolean skipCleaning;
  private final static Logger log = Logger.getLogger(TrinityGenerator.class);

  public TrinityGenerator() {
    dataToBeInject = new HashMap<>();
    listOfGeneratedClasses = new ArrayList<String>();
    listOfParentZClass = new ArrayList<>();
    listOfConsolidatedImports = new ArrayList<>();
    generatePartial = false;
  }

  /**
   * Initialize all the local variables in this function
   * 
   * @param Hashmap of all parameters that passed by the user.
   */

  public void setParameters(Map<String, Object> inputCmdParameters)
      throws IOException, XmlPullParserException, InvalidUriException {
    this.cfg = new Configuration(new Version("2.3.23")); // Set FreeMarker paramaters
    this.dataToBeInject.clear();
    this.ontologyName = (String) inputCmdParameters.get("name");
    this.olgaVersion = UTILS.getOlgaVersion();
    this.ontologyVersion = (String) inputCmdParameters.get("ontVersion");
    this.buildRepo = (String) inputCmdParameters.get("buildRepo");
    this.keyRepo = (String) inputCmdParameters.get("keyRepo");
    this.preserveNameSpaceOfOntology = false;
    if (inputCmdParameters.containsKey("preserve")) {
      preserveNameSpaceOfOntology = (boolean) inputCmdParameters.get("preserve");
    }

    this.skipCleaning = false;
    if (inputCmdParameters.containsKey("skipCleaning")) {
      this.skipCleaning = (boolean) inputCmdParameters.get("skipCleaning");
    }

    this.generatePartial = false;
    if (inputCmdParameters.containsKey("partial")) {
      this.generatePartial = (boolean) inputCmdParameters.get("partial");
    }

    this.path = UTILS.firstLetterCapital(ontologyName) + "/Rdf/Model/";
    this.packageName = UTILS.cleanPackageName(path);
    this.dataToBeInject = new HashMap<>();
    this.cliDependency = (String) inputCmdParameters.get("dependency");
    this.outDirectoryPath = CONFIG.PROJECT_DIRECTORY + CONFIG.GENERATED_LIBRARY;
    if (inputCmdParameters.get("out") != null
        && UTILS.isPathValid((String) inputCmdParameters.get("out"))) {
      this.outDirectoryPath = UTILS.rebuildPath((String) inputCmdParameters.get("out"));
    }

  }

  /**
   * Used to invoke the code generation process.
   * 
   * @param Hashmap of all ZClasses extracted from the ontology file.
   * @return A string <Code>fail</Code> if the code generation failed <Code>Success</Code>
   *         otherwise.
   */

  public String generateCode(Map<IRI, ZClass> mapIRI_to_Zclass) throws IOException,
      TemplateException, MavenInvocationException, UnableToCompileGeneratedCodeException,
      RuntimeErrorException, MojoFailureException, NullPointerException {
    try {
      this.mapIRI_to_Zclass = mapIRI_to_Zclass;
      listOfGeneratedClasses.clear();

      long start_millis_generation = System.currentTimeMillis();
      pomFilePath = this.outDirectoryPath + ontologyName + "-dotnetTrinity/";
      if (!skipCleaning) {
        UTILS.cleanDirectory(new File(pomFilePath));
      }
      generateClass();
      generatePom();

      DateFormat dateFormatter = new SimpleDateFormat("mm:ss:SSS");
      long end_millis_generation = System.currentTimeMillis();
      Date generationTime = new Date(end_millis_generation - start_millis_generation);
      String generationTimeString = dateFormatter.format(generationTime);
      System.out.println("Code Generation Time (mm:ss:SSS): " + generationTimeString);

      start_millis_generation = System.currentTimeMillis();

      generateDotNetProject();

      end_millis_generation = System.currentTimeMillis();

      generationTime = new Date(end_millis_generation - start_millis_generation);
      generationTimeString = dateFormatter.format(generationTime);

      System.out.println("Copying dotNet nuget dependencies (mm:ss:SSS): " + generationTimeString);

      String result;
      if (buildRepo != null && keyRepo != null) {
        result = OLGACompiler.maven_exec(pomFilePath, Arrays.asList("clean", "deploy"));
      } else {
        result = OLGACompiler.maven_clean_install(pomFilePath);
      }

      return result;
    } catch (IOException | TemplateException | UnableToCompileGeneratedCodeException
        | NullPointerException e) {
      log.error(e);
      throw e;
    }
  }

  /**
   * Used to invoke the classes and interfaces files generation process.
   */

  @SuppressWarnings("unchecked")
  public void generateClass() throws IOException, TemplateException, NullPointerException {
    String classTemplateName = "trinity_template_class.ftl";
    Template classTemplate = null;

    String interfaceTemplateName = "trinity_template_interface.ftl";
    Template interfaceTemplate = null;

    // destination target
    generatedLibraryConfig = pomFilePath;
    // templates location
    templateSelectionConfig = CONFIG.DOTNET_TRINITYRDF_TEMPLATES;

    try {
      // preparing Freemarker configuration
      setFreeMarkerConfiguration(templateSelectionConfig);
    } catch (IOException e) {
      log.error(e);
      throw e;
    }

    for (Entry<IRI, ZClass> entry : mapIRI_to_Zclass.entrySet()) {
      ZClass zclass = entry.getValue(); // Get ZClass
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

      listOfConsolidatedImports.clear();
      dataToBeInject.clear();
      List<ZObjectProperty> objectProperties =
          handleObjectPropertiesDuplication(zclass, bestMotherToExtend);
      dataToBeInject.put("generatePartial", generatePartial);
      dataToBeInject.put("motherToExtend", bestMotherToExtend);
      dataToBeInject.put("ontologyName", ontologyName.toLowerCase(Locale.ENGLISH));
      dataToBeInject.put("packageName", packageName);
      dataToBeInject.put("Zclass", zclass);
      dataToBeInject.put("listDataPropertiesToImplement",
          handleDataPropertyDuplication(zclass, bestMotherToExtend));
      dataToBeInject.put("listObjectPropertiesToImplement", objectProperties);
      dataToBeInject.put("ontologyVersion", ontologyVersion);

      // create File Directory for the code if does not exists already
      addToListOfGeneratedClasses(path + "/", "I" + zclass.getzClassName());
      dataToBeInject.put("motherClassList", zclass.getListOfParentsToImplement());
      dataToBeInject.put("objectPropertyList", UTILS.pickObjectPropertiesForInterface(zclass));
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
            zclass.add_listOfImports(pair.getKey().getPackageName());
          });
        });
      }

      try (StringWriter out = new StringWriter()) {
        // prepare to generate the Class
        addToListOfGeneratedClasses(path + File.separator, zclass.getzClassName());
        classTemplate = cfg.getTemplate(classTemplateName); // Load Interface template file
        File classFile = new File(
            generatedLibraryConfig + path + File.separator + zclass.getzClassName() + ".cs");
        try (FileWriter writer = new FileWriter(classFile);) {
          classTemplate.process(dataToBeInject, writer); // Create classes\
        }

        if (bestMotherToExtend != null) {
          ((List<ZClass>) dataToBeInject.get("motherClassList")).add(bestMotherToExtend);
        }

        File interfaceFile = new File(
            generatedLibraryConfig + path + File.separator + "I" + zclass.getzClassName() + ".cs");
        try (FileWriter writer = new FileWriter(interfaceFile);) {
          // prepare to generate the Interface
          interfaceTemplate = cfg.getTemplate(interfaceTemplateName); // Load Interface template
                                                                      // file
          interfaceTemplate.process(dataToBeInject, writer); // Create interfaces
        }

        /*
         * if(!zclass.getListZInstanceIRI().isEmpty()) { //FIXME:
         * ModelOptimizer.injectCardinalityAndRestrictionOfInstances(zclass,
         * listObjectPropertiesToImplement, listDataPropertiesToImplement);
         * 
         * FileWriter instancefileWriter = new FileWriter( generatedLibraryConfig +
         * path+"/InstancesOf"+zclass.getzClassName()+".cs"); addToListOfGeneratedClasses(path+"/",
         * "InstancesOf"+zclass.getzClassName()); dataToBeInject.put("ZinstanceList",
         * zclass.getListZInstanceIRI()); instanceTemplate = cfg.getTemplate(instanceTemplateName);
         * instanceTemplate.process(dataToBeInject, instancefileWriter); instancefileWriter.close();
         * }
         */
        listOfParentZClass.clear();
        out.flush();
      } catch (NullPointerException | IOException | TemplateException e) {
        log.error(e);
        throw e;
      }
    }
  }

  /**
   * Used to invoke the .Net Standard project and the static dependence files generation process.
   */

  public void generateDotNetProject() throws IOException, FileNotFoundException, TemplateException {
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
    dataToBeInject.put("listOfGeneratedClasses", listOfGeneratedClasses);

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(pomFilePath + ontologyName.toLowerCase(Locale.ENGLISH) + ".csproj"),
        StandardCharsets.UTF_8));) {
      template_proj.process(dataToBeInject, writer);
    } catch (TemplateException e) {
      log.error(e);
      throw e;
    }

    try {
      // directory = new File(pomFilePath+"OLGA/");
      String ontologiesOut = pomFilePath + "Ontologies/";
      if (UTILS.isPathValid(ontologiesOut)) {
        UTILS.copyDirectory("Ontologies", "src/main/resources", ontologiesOut);
        addToListOfGeneratedClasses("Ontologies/", "Ontologies.i");
      }

      String oLGAOut = pomFilePath + "OLGA/";
      if (UTILS.isPathValid(oLGAOut)) {
        UTILS.copyDirectory("OLGA", CONFIG.OLGA_Dependencies, oLGAOut);
      }

    } catch (FileNotFoundException | NullPointerException e1) {
      log.error(e1);
      throw e1;
    }
  }

  /**
   * Used to invoke the POM file generation process.
   */

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

  /**
   * Used to populate "listOfParentZClass" list which contains all the classes will be implemented
   * as interfaces and be sure to exclude the class which will be extended as a class.
   * 
   * @param "zclass" is the which this function will extract all of its parents, and
   *        "exceptBestMother" is the class which will be excluded to be add to
   *        "listOfParentZClass".
   */

  private void recursiveGrandMothers(ZClass zclass, ZClass exceptBestMother) {
    if (zclass.getSuperZClassList() != null) {
      for (ZClass parent : zclass.getSuperZClassList()) {
        if (exceptBestMother != null
            && !parent.getIri().toString().equalsIgnoreCase(exceptBestMother.getIri().toString())) {
          listOfParentZClass.add(parent);
          recursiveGrandMothers(parent, exceptBestMother);
        }
      }
    }
  }

  private List<ZClass> getAllParentsList(ZClass zclass) {
    if (zclass == null) {
      return new ArrayList<>();
    }

    ZClass parent = zclass.getParentToExtend();
    List<ZClass> parentsList = new ArrayList<ZClass>();
    if (parent != null) {
      parentsList.add(parent);
      parentsList.addAll(getAllParentsList(parent));
    }
    return parentsList;
  }

  /**
   * This function used to get the list of right object properties from the implemented interfaces
   * to be add to the generated class, and triage them from duplication and collision.
   * 
   * @param "zclass" is the class we want to get all its inherited properties from its parents.
   *        "bestMotherToExtend" is the class to be excluded from parents list which will be
   *        implemented.
   * @return list of refined object properties.
   */

  private List<ZObjectProperty> handleObjectPropertiesDuplication(ZClass zclass,
      ZClass bestMotherToExtend) {

    // Mapping of Objects based on their same IRI
    Map<String, List<ZObjectProperty>> mappedProperties =
        zclass.getZObjectPropertyList().stream().collect(Collectors
            .groupingBy(zobjectProperty -> zobjectProperty.getObjectProperty().toString()));

    // override same ObjectProperties if they have the same IRI
    for (Entry<String, List<ZObjectProperty>> objectPropertyList : mappedProperties.entrySet()) {
      if (objectPropertyList.getValue().size() > 1) {
        for (ZObjectProperty currentZObjectProperty : objectPropertyList.getValue()) {
          currentZObjectProperty.setOverridable();
        }
      }
    }

    listOfParentZClass.clear();
    recursiveGrandMothers(zclass, bestMotherToExtend);

    for (ZClass motherClass : listOfParentZClass) {
      for (ZObjectProperty motherObjectProperty : motherClass.getZObjectPropertyList()) {
        ZObjectProperty currentToOverride =
            UTILS.listContainsObjectProperty(zclass.getZObjectPropertyList(), motherObjectProperty);
        // it is contained then remove it
        if (currentToOverride != null) {
          // remove duplication from mother
          zclass.getZObjectPropertyList().remove(motherObjectProperty);
        }
        // it is not contained, the mother object properties need to be implemented
        // only if not at the mother list
        else if (UTILS.listContainsObjectProperty(bestMotherToExtend.getZObjectPropertyList(),
            motherObjectProperty) == null) {
          zclass.getZObjectPropertyList().add(motherObjectProperty);
        }
      }
    }

    // Mapping of Objects based on their same IRI
    mappedProperties = zclass.getZObjectPropertyList().stream().collect(
        Collectors.groupingBy(zobjectProperty -> zobjectProperty.getObjectProperty().toString()));

    for (Entry<String, List<ZObjectProperty>> objectPropertyList : mappedProperties.entrySet()) {
      UTILS.pickBestObjectPropertiesList(objectPropertyList.getValue());
    }

    return zclass.getZObjectPropertyList();
  }

  /**
   * This function used to get the list of right data properties from the implemented interfaces to
   * be add to the generated class, and triage them from duplication and collision.
   * 
   * @param "zclass" is the class we want to get all its inherited properties from its parents.
   *        "bestMotherToExtend" is the class to be excluded from parents list which will be
   *        implemented.
   * @return list of refined data properties.
   */

  private List<ZDataProperty> handleDataPropertyDuplication(ZClass zclass,
      ZClass bestMotherToExtend) {

    List<ZDataProperty> inheritedObjectProperties =
        getAllParentsList(bestMotherToExtend).parallelStream().map(m -> m.getZDataPropertyList())
            .flatMap(List::stream).collect(Collectors.toList());

    if (inheritedObjectProperties != null) {
      zclass.getZDataPropertyList().forEach((ZDataProperty property) -> {
        if (inheritedObjectProperties.parallelStream().anyMatch(m -> {
          return property.getDataProperty().equals(m.getDataProperty()) && !property.isOverridable()
              && !m.isOverridable();
        })) {
          property.setHidingParentProperty();
        }
      });
    }
    // copy ZDataProperty of the zclass
    List<ZDataProperty> listZDataPropertyDuplicationFiltered =
        new ArrayList<ZDataProperty>(zclass.getZDataPropertyList());

    listOfParentZClass.clear();
    recursiveGrandMothers(zclass, bestMotherToExtend);
    for (ZClass motherClass : listOfParentZClass) {
      if (bestMotherToExtend != null && !motherClass.getIri().toString()
          .equalsIgnoreCase(bestMotherToExtend.getIri().toString())) {
        for (ZDataProperty motherZDataProperty : motherClass.getZDataPropertyList()) {
          if (UTILS.listContainsDataProperty(listZDataPropertyDuplicationFiltered,
              motherZDataProperty)) {
            listZDataPropertyDuplicationFiltered.remove(motherZDataProperty);
          }
          // it is not contained, the mother data properties need to be implemented
          else {
            if (!UTILS.listContainsDataProperty(bestMotherToExtend.getZDataPropertyList(),
                motherZDataProperty)) {
              listZDataPropertyDuplicationFiltered.add(motherZDataProperty);
            }
          }
        }
      }
    }
    refineInstancesDataProperties(zclass, listZDataPropertyDuplicationFiltered, bestMotherToExtend);
    zclass.setzDataPropertyList(listZDataPropertyDuplicationFiltered);
    return listZDataPropertyDuplicationFiltered;
  }

  /**
   * This function used to remove any data property added to an instance in the ontolgy, and this
   * data property wasn't added to the class which this instance instantiates.
   * 
   * @param "zclass" is the class we want to filter all of its instances. "bestMotherToExtend" is
   *        the class to be extended as class. "listZDataPropertyDuplicationFiltered" list of all
   *        data properties inherited from parents.
   */

  private void refineInstancesDataProperties(ZClass zclass,
      List<ZDataProperty> listZDataPropertyDuplicationFiltered, ZClass bestMotherToExtend) {
    List<ZInstance> instances = new ArrayList<ZInstance>(zclass.getListZInstanceIRI());
    List<ZDataProperty> parentsDataProperties =
        new ArrayList<>(listZDataPropertyDuplicationFiltered);

    if (bestMotherToExtend != null && bestMotherToExtend.getZDataPropertyList() != null) {
      parentsDataProperties.addAll(bestMotherToExtend.getZDataPropertyList());
    }

    instances.forEach(zInstance -> {
      List<ZDataProperty> dataProperties =
          new ArrayList<ZDataProperty>(zInstance.getListZDataPropertyList());
      zInstance.getListZDataPropertyList().forEach(dataProperty -> {
        if (!parentsDataProperties.parallelStream().anyMatch(predicate -> {
          return predicate.getDataProperty().equals(dataProperty.getDataProperty());
        })) {
          dataProperties.remove(dataProperty);
        }
      });
      zInstance.setListZDataPropertyList(dataProperties);
    });
    zclass.setListZInstanceIRI(instances);
  }


  private void setFreeMarkerConfiguration(String templateSelectionConfig) throws IOException {
    cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), templateSelectionConfig));
    cfg.setDefaultEncoding("UTF-8");
  }

  private void addToListOfGeneratedClasses(String path, String fileName) {
    String classFileName = path.replace("/", "\\") + fileName + ".cs";
    if (!listOfGeneratedClasses.contains(classFileName)) {
      listOfGeneratedClasses.add(classFileName);
    }
  }
}
