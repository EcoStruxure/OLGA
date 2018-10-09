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
package semanticstore.ontology.library.generator.visitor;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.model.ZPair;
import semanticstore.ontology.library.generator.visitor.OLGAVisitor;

public class OLGAVisitorTestInverseRelationTest implements OWLObjectVisitor {

  private static IRI floorIRI = IRI.create("http://www.simple.com#Floor");
  private static IRI buildingIRI = IRI.create("http://www.simple.com#Building");
  private static IRI hasFloorIri = IRI.create("http://www.simple.com#hasFloor");
  ZClass zBuilding = olgaVisitor.getMapIRI_to_Zclass().get(buildingIRI);
  ZClass zFloor = olgaVisitor.getMapIRI_to_Zclass().get(floorIRI);

  private static OLGAVisitor olgaVisitor;

  @BeforeClass
  public static void setUpBeforeClass() {
    String test_ont = new File(".").getAbsolutePath() + "/src/test/resources/simple/simple.owl";
    OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
    File test_ontFile = new File(test_ont);
    try {
      OWLOntology test_ontology_to_visit =
          owlManager.loadOntologyFromOntologyDocument(test_ontFile);
      olgaVisitor = new OLGAVisitor(test_ontology_to_visit, CODE.C_SHARP, LIBRARY.TRINITY, true);
      olgaVisitor.visit();
    } catch (OWLOntologyCreationException e) {
      e.printStackTrace();
    }
  }

  private List<ZObjectProperty> findObjectPropertyByIRI(IRI iriTofind,
      List<ZObjectProperty> listToSearchIn) {
    List<ZObjectProperty> newList = new ArrayList<>();

    for (ZObjectProperty currentProperty : listToSearchIn) {
      if (currentProperty.getObjectProperty().equals(iriTofind)) {
        newList.add(currentProperty);
      }
    }

    return newList;
  }

  @Test
  public void testHasFloor() {
    List<ZObjectProperty> hasFloor =
        findObjectPropertyByIRI(hasFloorIri, zBuilding.getZObjectPropertyList());
    Optional<ZPair<ZClass, boolean[]>> pair =
        hasFloor.get(0).getRangeListZClasses().parallelStream().filter(f -> {
          return f.getKey().getIri().equals(zFloor.getIri());
        }).findFirst();

    assertEquals(pair.get().getValue()[0], true);
    assertEquals(hasFloor.size(), 1);
    assertEquals(hasFloor.get(0).getInverseZObjectProperties().iterator().hasNext(), false);
  }
}
