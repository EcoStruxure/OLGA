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
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividualVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.model.ZPair;

/*
 * OLGA tested at visitor level.
 */
public class OLGAVisitorTest implements OWLObjectVisitor, OWLIndividualVisitor {

  String range;
  String dataProp;
  String connectsTo;
  String connectsToType;
  String cardinalityType;
  static List<String> list = new ArrayList<>();
  static OLGAVisitor olgaVisitor;

  @BeforeClass
  public static void setUpBeforeClass() {
    String test_ont = new File(".").getAbsolutePath() + "/src/test/resources/boc/boc_plan.owl";
    OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
    File test_ontFile = new File(test_ont);
    try {
      OWLOntology test_ontology_to_visit =
          owlManager.loadOntologyFromOntologyDocument(test_ontFile);
      olgaVisitor = new OLGAVisitor(test_ontology_to_visit, CODE.C_SHARP, LIBRARY.TRINITY);
      olgaVisitor.visit();
    } catch (OWLOntologyCreationException e) {
      e.printStackTrace();
    }
  }

  public void instanciate() {
    list.add("Bathroom");
    list.add("Building");
    list.add("Chair");
    list.add("Chat_room");
    list.add("Collaborative_area");
    list.add("Conference_room");
    list.add("Copy_print");
    list.add("Copy_room");
    list.add("Countertop");
    list.add("Desk");
    list.add("Door");
    list.add("Elevator");
    list.add("Entrance");
    list.add("Floor");
    list.add("Floor_opening");
    list.add("Hall_area");
    list.add("Hallway");
    list.add("Inaccessible_space");
    list.add("Meeting_room");
    list.add("Office");
    list.add("Partition");
    list.add("Room");
    list.add("Seat");
    list.add("Shelf");
    list.add("Stairs");
    list.add("Support_room");
    list.add("Table");
    list.add("Tv");
    list.add("Video_conference");
    list.add("Wall");
    list.add("Workstation");
  }

  @Test
  public void testSize() {
    assertEquals(olgaVisitor.getMapIRI_to_Zclass().entrySet().size(), 31);
  }

  @Test
  public void testAllClass() {
    instanciate();
    for (String element : list) {
      String iriStr = "http://www.micello.com#" + element;
      IRI iri = IRI.create(iriStr);
      assertTrue(olgaVisitor.getMapIRI_to_Zclass().containsKey(iri));
    }

  }

  @Test
  public void testalldataProperties() {
    instanciate();
    for (String element : list) {
      String iriStr = "http://www.micello.com#" + element;
      IRI iri = IRI.create(iriStr);

      for (ZDataProperty dataProperty : olgaVisitor.getMapIRI_to_Zclass().get(iri)
          .getZDataPropertyList()) {
        dataProp = dataProperty.getDataProperty().getShortForm();

        if (element == "Floor") {
          assertTrue(dataProp.equals("hasName") || dataProp.equals("hasShortName")
              || dataProp.equals("hasTypeOfFloor") || dataProp.equals("hasMicelloID")
              || dataProp.equals("hasTypeOfFloor") || dataProp.equals("hasSquareFeet"));
        } else if (element == "Building") {
          assertTrue(dataProp.equals("hasName") || dataProp.equals("hasAddress")
              || dataProp.equals("hasCity") || dataProp.equals("hasCountry")
              || dataProp.equals("hasDescription") || dataProp.equals("hasFloor")
              || dataProp.equals("hasMicelloID") || dataProp.equals("hasPhoneNumber")
              || dataProp.equals("hasPostalCode") || dataProp.equals("hasSquareFeet")
              || dataProp.equals("hasState") || dataProp.equals("hasUrl"));
        } else {
          assertTrue(dataProp.equals("hasMicelloID") || dataProp.equals("hasName")
              || dataProp.equals("hasSquareFeet"));
        }
      }

    }
  }

  @Test
  public void testalldataPropertieRange() {
    instanciate();
    for (String element : list) {
      String iriStr = "http://www.micello.com#" + element;
      IRI iri = IRI.create(iriStr);

      for (ZDataProperty dataProperty : olgaVisitor.getMapIRI_to_Zclass().get(iri)
          .getZDataPropertyList()) {
        range = dataProperty.getRangeXSDType();
        dataProp = dataProperty.getDataProperty().getShortForm();

        if (dataProp.equals("hasSquareFeet")) {
          assertTrue(range.equals("double"));
        } else if (dataProp.equals("hasMicelloID")) {
          assertTrue(range.equals("decimal"));
        } else {
          assertTrue(range.equals("string"));
        }
      }

    }
  }

  @Test
  public void testallObjectProperties() {
    instanciate();

    for (String element : list) {
      String iriStr = "http://www.micello.com#" + element;
      IRI iri = IRI.create(iriStr);

      for (ZObjectProperty objectProperty : olgaVisitor.getMapIRI_to_Zclass().get(iri)
          .getZObjectPropertyList()) {
        connectsToType = objectProperty.getObjectProperty().getShortForm();
        if (element == "Building") {
          assertEquals(connectsToType, "hasFloor");

        } else if (element == "Floor") {
          assertTrue(connectsToType.equals("hasBathroom") || connectsToType.equals("hasChair")
              || connectsToType.equals("hasChatRoom")
              || connectsToType.equals("hasCollaborativeArea")
              || connectsToType.equals("hasConferenceRoom") || connectsToType.equals("hasCopyPrint")
              || connectsToType.equals("hasCopyRoom") || connectsToType.equals("hasCountertop")
              || connectsToType.equals("hasDesk") || connectsToType.equals("hasDoor")
              || connectsToType.equals("hasElevator") || connectsToType.equals("hasEntrance")
              || connectsToType.equals("hasFloorOpening") || connectsToType.equals("hasHallArea")
              || connectsToType.equals("hasHallway")
              || connectsToType.equals("hasInaccessibleSpace")
              || connectsToType.equals("hasMeetingRoom") || connectsToType.equals("hasOffice")
              || connectsToType.equals("hasPartition") || connectsToType.equals("hasRoom")
              || connectsToType.equals("hasSeat") || connectsToType.equals("hasShelf")
              || connectsToType.equals("hasStairs") || connectsToType.equals("hasSupportRoom")
              || connectsToType.equals("hasTable") || connectsToType.equals("hasTV")
              || connectsToType.equals("hasVideoConference") || connectsToType.equals("hasWall")
              || connectsToType.equals("hasWorkstation"));

        }
        for (ZPair<ZClass, Boolean> ZClassRange : objectProperty.getRangeListZClasses()) {
          connectsTo = ZClassRange.getKey().getzClassName();
          if (element == "Building") {
            assertEquals(connectsTo, "Floor");

          } else if (element == "Floor") {
            assertTrue(connectsTo.equals("Bathroom") || connectsTo.equals("Chair")
                || connectsTo.equals("Chat_room") || connectsTo.equals("Collaborative_area")
                || connectsTo.equals("Conference_room") || connectsTo.equals("Copy_print")
                || connectsTo.equals("Copy_room") || connectsTo.equals("Countertop")
                || connectsTo.equals("Desk") || connectsTo.equals("Door")
                || connectsTo.equals("Elevator") || connectsTo.equals("Entrance")
                || connectsTo.equals("Floor_opening") || connectsTo.equals("Hall_area")
                || connectsTo.equals("Hallway") || connectsTo.equals("Inaccessible_space")
                || connectsTo.equals("Meeting_room") || connectsTo.equals("Office")
                || connectsTo.equals("Partition") || connectsTo.equals("Room")
                || connectsTo.equals("Seat") || connectsTo.equals("Shelf")
                || connectsTo.equals("Stairs") || connectsTo.equals("Support_room")
                || connectsTo.equals("Table") || connectsTo.equals("Tv")
                || connectsTo.equals("Video_conference") || connectsTo.equals("Wall")
                || connectsTo.equals("Workstation"));

          }
        }
        cardinalityType = objectProperty.getObjectPropertyType();
        assertEquals(cardinalityType, "Some");

      }
    }
  }
}
