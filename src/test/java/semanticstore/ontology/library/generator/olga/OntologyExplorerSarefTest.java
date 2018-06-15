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

import static org.junit.Assert.assertEquals;
import java.io.File;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.visitor.OLGAVisitor;

public class OntologyExplorerSarefTest implements OWLObjectVisitor {

  static OLGAVisitor owlExplorer;
  final static Logger log = Logger.getLogger(OntologyExplorerSarefTest.class);

  @BeforeClass
  public static void setUpBeforeClass() {
    String test_ont =
        new File(".").getAbsolutePath() + "/src/test/resources/complexOntologies/sarefMerged.owl";
    OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
    File test_ontFile = new File(test_ont);
    try {
      OWLOntology test_ontology_to_visit =
          owlManager.loadOntologyFromOntologyDocument(test_ontFile);
      owlExplorer = new OLGAVisitor(test_ontology_to_visit, CODE.C_SHARP, LIBRARY.TRINITY);
      owlExplorer.visit();
    } catch (OWLOntologyCreationException e) {
      log.error(e);
    }
  }

  @Test
  public void testClassLabel() {
    String iriStr = "https://w3id.org/saref#Command";
    IRI iri = IRI.create(iriStr);
    assertEquals(owlExplorer.getMapIRI_to_Zclass().get(iri).getLabel(), "Command");

  }

  @Test
  public void testClassComment() {
    String iriStr = "https://w3id.org/saref#Measurement";
    IRI iri = IRI.create(iriStr);
    assertEquals(owlExplorer.getMapIRI_to_Zclass().get(iri).getComments(),
        "Represents the measured value made over a property. It is also linked to the unit of measure in which the value is expressed and the timestamp of the measurement.");
  }

  @Test
  public void testObjectPropComment() {
    String iriStr = "https://w3id.org/saref#Commodity";
    IRI iri = IRI.create(iriStr);
    for (ZObjectProperty objectProperty : owlExplorer.getMapIRI_to_Zclass().get(iri)
        .getZObjectPropertyList()) {
      assertEquals(objectProperty.getComments(),
          "A relationship identifying the unit of measure used for a certain entity.");
    }
  }

  @Test
  public void testObjectPropLabel() {
    String iriStr = "https://w3id.org/saref#Commodity";
    IRI iri = IRI.create(iriStr);
    for (ZObjectProperty objectProperty : owlExplorer.getMapIRI_to_Zclass().get(iri)
        .getZObjectPropertyList()) {
      assertEquals(objectProperty.getLabel(), "is measured in");
    }
  }

  @Test
  public void testDataPropLabel() {
    String iriStr = "https://w3id.org/saref#Device";
    IRI iri = IRI.create(iriStr);
    for (ZDataProperty dataProperty : owlExplorer.getMapIRI_to_Zclass().get(iri)
        .getZDataPropertyList()) {
      if (dataProperty.getDataPropertyShortForm() == "hasDescription") {
        assertEquals(dataProperty.getLabel(), "has description");
      }
    }
  }

  @Test
  public void testDataPropComment() {
    String iriStr = "https://w3id.org/saref#Device";
    IRI iri = IRI.create(iriStr);
    for (ZDataProperty dataProperty : owlExplorer.getMapIRI_to_Zclass().get(iri)
        .getZDataPropertyList()) {
      if (dataProperty.getDataPropertyShortForm() == "hasDescription") {
        assertEquals(dataProperty.getComments(),
            "A relationship providing a description of an entity (e.g., device)");
      }
    }
  }

}
