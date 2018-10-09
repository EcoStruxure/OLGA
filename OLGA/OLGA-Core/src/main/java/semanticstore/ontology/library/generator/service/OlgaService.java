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
package semanticstore.ontology.library.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
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

public class OlgaService {

  private String result = "Fail";
  private boolean atLeastOneValidOntologyFile = false;
  private final static Logger log = Logger.getLogger(OlgaService.class);
  private Map<String, Object> inputParameters;

  public void invokeOlga(Map<String, Object> inputParameters, List<InputStream> ontologyFiles)
      throws Exception {
    String directoryName = (new File(inputParameters.get("out").toString())).getParent();
    saveOntologyFilesInTempDirectory(ontologyFiles, directoryName);
    inputParameters.put("pathToOntologiesParam", directoryName);
    try {
      invokeOlga(inputParameters);
    } catch (Exception e) {
      log.error(e);
      throw e;
    }

  }

  public void invokeOlga(Map<String, Object> inputParameters) throws Exception {
    if (System.getenv("M2_HOME") == null) {
      System.err
          .println("Please set M2_HOME in your environment to point to a valid maven binaries");
      return;
    }

    if (inputParameters == null) {
      return;
    }
    this.inputParameters = inputParameters;
    try {
      OWLOntology owlOntology =
          prepareOntology(inputParameters.get("pathToOntologiesParam").toString());
      if (owlOntology == null) {
        result = "Fail";
        return;
      }
      DateFormat dateFormatter = new SimpleDateFormat("mm:ss:SSS");
      long start_millis_visitor = System.currentTimeMillis();
      // Visitor Module
      OLGAVisitor olgaVisitor = new OLGAVisitor(owlOntology, (CODE) inputParameters.get("code"),
          (LIBRARY) inputParameters.get("library"),
          (boolean) inputParameters.get("skipInverseRelations"));

      olgaVisitor.visit();

      long end_millis_visitor = System.currentTimeMillis();
      Date time_diff_visitor = new Date(end_millis_visitor - start_millis_visitor);
      String visitorTime = dateFormatter.format(time_diff_visitor);
      System.out.println("Visitor Time (mm:ss:SSS): " + visitorTime);

      // Generator Module
      Generator generator = SingletonGenerator.getInstance(inputParameters);
      result = generator.generateCode(olgaVisitor.getMapIRI_to_Zclass());

    } catch (XmlPullParserException | MavenInvocationException | IOException
        | TemplateException e) {
      result = "Fail";
      log.error(e);
      throw e;
    } catch (InvalidClassNameException | UnableToCompileGeneratedCodeException
        | InvalidUriException e) {
      result = "Fail";
      log.error(e);
      throw e;
    } catch (Exception e) {
      result = "Fail";
      log.error(e);
      throw e;
    }
  }

  public String getResult() {
    return result;
  }

  private void saveOntologyFilesInTempDirectory(List<InputStream> ontologyFiles,
      String outDirectory) throws IOException {

    for (int i = 1; i <= ontologyFiles.size(); i++) {
      String fileName = outDirectory + File.separator + Integer.toString(i) + ".owl";
      File targetFile = new File(fileName);
      FileUtils.copyInputStreamToFile(ontologyFiles.get(i - 1), targetFile);
    }
  }

  private OWLOntology prepareOntology(String ontologyFilesDirectory)
      throws Exception, OWLOntologyCreationException {
    OWLOntologyManager owlManager;
    File directory;

    owlManager = OWLManager.createOWLOntologyManager();
    directory = new File(ontologyFilesDirectory);
    AutoIRIMapper mapper = new AutoIRIMapper(directory, true);
    owlManager.getIRIMappers().add(mapper);

    try (Stream<Path> paths = Files.walk(Paths.get(ontologyFilesDirectory))) {
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
              if (!(inputParameters.containsKey("ontVersion")
                  && UTILS.isVersionPatternValid((String) inputParameters.get("ontVersion")))) {
                Optional<IRI> versionOpt = currentOntology.getOntologyID().getVersionIRI();
                if (versionOpt.isPresent()
                    && UTILS.isVersionPatternValid(versionOpt.get().getShortForm())) {
                  ontologyVersion = versionOpt.get().getShortForm();
                } else {
                  ontologyVersion = UTILS.getOlgaVersion();
                }
                inputParameters.put("ontVersion", ontologyVersion);
              } else {
                ontologyVersion = (String) inputParameters.get("ontVersion");
              }
            } catch (Exception e) {
              log.error(e);
              System.out.println(e.getMessage());

            }
          }
        }
      });
    } catch (IOException e) {
      log.error(e);
      throw e;
    }

    if (!atLeastOneValidOntologyFile) {
      System.err.println("No valid Ontology File found");
      return null;
    }
    // Merging ontologies if any
    OWLOntologyMerger merger = new OWLOntologyMerger(owlManager);
    OWLOntologyManager owlManager2 = OWLManager.createOWLOntologyManager();
    IRI mergedOntologyIRI = IRI.create("mergedOntology");

    OWLOntology merged;
    try {
      merged = merger.createMergedOntology(owlManager2, mergedOntologyIRI);
    } catch (OWLOntologyCreationException e) {
      log.error(e);
      System.out.println(e.getMessage());
      return null;
    }
    return merged;
  }
}
