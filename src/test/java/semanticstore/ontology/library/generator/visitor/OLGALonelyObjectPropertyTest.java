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

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZObjectProperty;

public class OLGALonelyObjectPropertyTest {
  static OLGAVisitor olgaVisitor;
  private static IRI lonelyIRI = IRI.create("http://www.simple.com#lonely");
  private static IRI lonely2IRI = IRI.create("http://www.simple.com#lonely2");
  private static IRI floorIRI = IRI.create("http://www.simple.com#Floor");
  private static IRI copyRoomIRI = IRI.create("http://www.simple.com#CopyRoom");
  private static IRI roomIRI = IRI.create("http://www.simple.com#Room");
  private static IRI buildingIRI = IRI.create("http://www.simple.com#Building");
  ZClass zCopyRoom = olgaVisitor.getMapIRI_to_Zclass().get(copyRoomIRI);
  ZClass zfloor = olgaVisitor.getMapIRI_to_Zclass().get(floorIRI);
  final static Logger log = Logger.getLogger(OLGALonelyObjectPropertyTest.class);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    String test_ont =
        new File(".").getAbsolutePath() + "/src/test/resources/simple/simple-test-lonely.owl";
    OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
    File test_ontFile = new File(test_ont);
    try {
      OWLOntology test_ontology_to_visit =
          owlManager.loadOntologyFromOntologyDocument(test_ontFile);
      olgaVisitor = new OLGAVisitor(test_ontology_to_visit, CODE.C_SHARP, LIBRARY.TRINITY);
      olgaVisitor.visit();
    } catch (OWLOntologyCreationException e) {
      log.error(e);
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
  public void testLonelyObj() {

    List<ZObjectProperty> lonely =
        findObjectPropertyByIRI(lonelyIRI, zfloor.getZObjectPropertyList());
    assertEquals(lonely.size(), 1);
    assertEquals(lonely.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
        buildingIRI);
  }

  @Test
  public void testLonel2yObj() {

    List<ZObjectProperty> lonely2 =
        findObjectPropertyByIRI(lonely2IRI, zCopyRoom.getZObjectPropertyList());
    assertEquals(lonely2.size(), 1);
    assertEquals(lonely2.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
        roomIRI);
  }

}
