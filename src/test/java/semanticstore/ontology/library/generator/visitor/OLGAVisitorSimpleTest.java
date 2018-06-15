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
import java.util.Locale;
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
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZInstance;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.visitor.OLGAVisitor;

public class OLGAVisitorSimpleTest implements OWLObjectVisitor {

  private static IRI copyRoomIRI = IRI.create("http://www.simple.com#CopyRoom");
  private static IRI floorIRI = IRI.create("http://www.simple.com#Floor");
  private static IRI buildingIRI = IRI.create("http://www.simple.com#Building");
  private static IRI physicalLocationIRI = IRI.create("http://www.simple.com#PhysicalLocation");
  private static IRI monitorableitemIRI = IRI.create("http://www.simple.com#MonitorableItem");
  private static IRI hasSensorIri = IRI.create("http://www.simple.com#hasSensor");
  private static IRI energySensorIri = IRI.create("http://www.simple.com#EnergySensor");
  private static IRI temperatureSensorIri = IRI.create("http://www.simple.com#TemperatureSensor");
  private static IRI sensorIri = IRI.create("http://www.simple.com#Sensor");
  private static IRI hasFloorIri = IRI.create("http://www.simple.com#hasFloor");
  private static IRI isLocatedInBuildingIri =
      IRI.create("http://www.simple.com#isLocatedInBuilding");
  private static IRI buildingIri = IRI.create("http://www.simple.com#Building");
  private static IRI hasRoomIRI = IRI.create("http://www.simple.com#hasRoom");
  private static IRI roomIRI = IRI.create("http://www.simple.com#Room");
  private static IRI islocatedInFloorIRI = IRI.create("http://www.simple.com#isLocatedInFloor");
  private static IRI hasItemNameIRI = IRI.create("http://www.simple.com#hasItemName");
  private static IRI monitorIRI = IRI.create("http://www.simple.com#monitors");
  private static IRI bostonOneCampusIRI = IRI.create("http://www.simple.com#BostonOneCampus");
  private static IRI room1IRI = IRI.create("http://www.simple.com#room1");
  ZInstance zRoom1 = olgaVisitor.getMapIRI_to_ZIndidivual().get(room1IRI);
  ZInstance zBostonOneCampus = olgaVisitor.getMapIRI_to_ZIndidivual().get(bostonOneCampusIRI);
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
      olgaVisitor = new OLGAVisitor(test_ontology_to_visit, CODE.C_SHARP, LIBRARY.TRINITY);
      olgaVisitor.visit();
    } catch (OWLOntologyCreationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSize() {
    assertEquals(olgaVisitor.getMapIRI_to_Zclass().entrySet().size(), 10);

  }

  @Test
  public void testFloorParents() {

    assertEquals(zFloor.getSuperZClassList().size(), 1);
    assertEquals(zFloor.getSuperZClassList().get(0).getIri(), physicalLocationIRI);
  }

  @Test
  public void testCopyRoomParent() {

    ZClass zCopyRoom = olgaVisitor.getMapIRI_to_Zclass().get(copyRoomIRI);
    assertEquals(zCopyRoom.getSuperZClassList().size(), 1);
    assertEquals(zCopyRoom.getSuperZClassList().get(0).getIri(), physicalLocationIRI);

  }

  /*
   * testing comments
   */
  @Test
  public void testBuildingParents() {

    String comment = "testing comments in different classes.";
    ZClass zBuilding = olgaVisitor.getMapIRI_to_Zclass().get(buildingIRI);

    IRI moonitorableitemIRI = IRI.create("http://www.simple.com#MonitorableItem");
    IRI physicalLocationIRI = IRI.create("http://www.simple.com#PhysicalLocation");

    List<ZClass> parents = zBuilding.getSuperZClassList();

    assertEquals(parents.size(), 2);
    assertTrue(parents.stream().anyMatch(p -> p.getIri().equals(moonitorableitemIRI)));
    assertTrue(parents.stream().anyMatch(p -> p.getIri().equals(physicalLocationIRI)));
    assertTrue(zBuilding.getComments().equals(comment));
  }

  @Test
  public void testRoomParents() {
    IRI roomIRI = IRI.create("http://www.simple.com#Room");
    ZClass zRoom = olgaVisitor.getMapIRI_to_Zclass().get(roomIRI);

    IRI physicalLocationIRI = IRI.create("http://www.simple.com#PhysicalLocation");

    assertEquals(zRoom.getSuperZClassList().size(), 1);
    assertEquals(zRoom.getSuperZClassList().get(0).getIri(), physicalLocationIRI);
  }

  @Test
  public void testphysicalLoctionParents() {
    IRI physicalLocationIRI = IRI.create("http://www.simple.com#PhysicalLocation");
    ZClass zPhysicalLocation = olgaVisitor.getMapIRI_to_Zclass().get(physicalLocationIRI);

    IRI LocationItemIRI = IRI.create("http://www.simple.com#LocationItem");

    assertEquals(zPhysicalLocation.getSuperZClassList().size(), 1);
    assertEquals(zPhysicalLocation.getSuperZClassList().iterator().next().getIri(),
        LocationItemIRI);

  }

  @Test
  public void testEnergySensorParents() {
    ZClass zEnergySensor = olgaVisitor.getMapIRI_to_Zclass().get(energySensorIri);

    IRI SensorIRI = IRI.create("http://www.simple.com#Sensor");

    assertEquals(zEnergySensor.getSuperZClassList().size(), 1);
    assertEquals(zEnergySensor.getSuperZClassList().iterator().next().getIri(), SensorIRI);

  }

  @Test
  public void testSensorParents() {

    ZClass zTSensor = olgaVisitor.getMapIRI_to_Zclass().get(temperatureSensorIri);
    assertEquals(zTSensor.getSuperZClassList().size(), 1);
    assertEquals(zTSensor.getSuperZClassList().get(0).getIri(), sensorIri);

  }
  /*
   * Starting to check all object properties.
   */

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

  private List<ZDataProperty> findZDataPropertyItemByIRI(IRI iriTofind,
      List<ZDataProperty> listToSearchIn) {
    List<ZDataProperty> newList = new ArrayList<>();

    for (ZDataProperty currentDataProperty : listToSearchIn) {
      if (currentDataProperty.getDataProperty().equals(iriTofind)) {
        newList.add(currentDataProperty);
      }
    }

    return newList;
  }

  /*
   * testallPropertiesBuilding() tests comments as well.
   */
  @Test
  public void testallPropertiesBuilding() {
    String comment = "shows building property.";
    List<ZObjectProperty> hasSensorList =
        findObjectPropertyByIRI(hasSensorIri, zBuilding.getZObjectPropertyList());
    assertEquals(hasSensorList.size(), 2);
    if (hasSensorList.get(0).getRangeListZClasses().iterator().next().getKey().getIri()
        .equals(energySensorIri)) {
      assertEquals(hasSensorList.get(1).getRangeListZClasses().iterator().next().getKey().getIri(),
          temperatureSensorIri);
    } else {
      if (hasSensorList.get(1).getRangeListZClasses().iterator().next().getKey().getIri()
          .equals(energySensorIri)) {
        assertEquals(
            hasSensorList.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
            temperatureSensorIri);
      }
    }

    List<ZObjectProperty> hasFloorList =
        findObjectPropertyByIRI(hasFloorIri, zBuilding.getZObjectPropertyList());
    assertEquals(hasFloorList.size(), 1);
    assertEquals(hasFloorList.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
        floorIRI);
    ZDataProperty hasBuildingAccronym = zBuilding.getZDataPropertyList().get(0);
    assertEquals(hasBuildingAccronym.getRestrictionType().toLowerCase(Locale.ROOT), "some");
    assertEquals(hasBuildingAccronym.getRangeXSDType(), "string");
    assertTrue(hasBuildingAccronym.getComments().equals(comment));

  }

  /*
   * comments tested.
   */
  @Test
  public void testFloorPropert() {
    String comment = "Yaxshilab tekshirish kerak. G'ayrat qil!";
    ZClass zFloor = olgaVisitor.getMapIRI_to_Zclass().get(floorIRI);
    ZDataProperty hasFloorAccronym = zFloor.getZDataPropertyList().get(0);
    assertEquals(hasFloorAccronym.getRestrictionType().toLowerCase(Locale.ROOT), "some");
    assertEquals(hasFloorAccronym.getRangeXSDType(), "string");
    assertTrue(hasFloorAccronym.getComments().equals(comment));
    List<ZObjectProperty> isLocatedInBuildingList =
        findObjectPropertyByIRI(isLocatedInBuildingIri, zFloor.getZObjectPropertyList());
    assertEquals(isLocatedInBuildingList.size(), 1);
    assertEquals(
        isLocatedInBuildingList.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
        buildingIri);

    List<ZObjectProperty> hasRoomList =
        findObjectPropertyByIRI(hasRoomIRI, zFloor.getZObjectPropertyList());
    assertEquals(hasRoomList.size(), 2);
    if (hasRoomList.get(0).getRangeListZClasses().iterator().next().getKey().getIri()
        .equals(copyRoomIRI)) {
      assertEquals(hasRoomList.get(1).getRangeListZClasses().iterator().next().getKey().getIri(),
          roomIRI);
    } else {
      if (hasRoomList.get(1).getRangeListZClasses().iterator().next().getKey().getIri()
          .equals(copyRoomIRI)) {
        assertEquals(hasRoomList.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
            roomIRI);
      }
    }
  }

  @Test
  public void testCopyRoom() {
    ZClass zCopyRoomIRI = olgaVisitor.getMapIRI_to_Zclass().get(copyRoomIRI);
    List<ZObjectProperty> hasSensorList =
        findObjectPropertyByIRI(hasSensorIri, zCopyRoomIRI.getZObjectPropertyList());
    assertEquals(hasSensorList.size(), 1);
    assertEquals(hasSensorList.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
        temperatureSensorIri);
  }

  @Test
  public void testRoom() {
    ZClass zRoomIRI = olgaVisitor.getMapIRI_to_Zclass().get(roomIRI);
    List<ZObjectProperty> hasSensorList =
        findObjectPropertyByIRI(hasSensorIri, zRoomIRI.getZObjectPropertyList());
    assertEquals(hasSensorList.size(), 2);
    if (hasSensorList.get(0).getRangeListZClasses().iterator().next().getKey().getIri()
        .equals(energySensorIri)) {
      assertEquals(hasSensorList.get(1).getRangeListZClasses().iterator().next().getKey().getIri(),
          temperatureSensorIri);
    } else {
      if (hasSensorList.get(1).getRangeListZClasses().iterator().next().getKey().getIri()
          .equals(energySensorIri)) {
        assertEquals(hasSensorList.iterator().next().getRangeListZClasses().iterator().next()
            .getKey().getIri(), temperatureSensorIri);
      }
    }
    ZDataProperty hasRoomAccronyme = zRoomIRI.getZDataPropertyList().get(0);
    assertEquals(hasRoomAccronyme.getRestrictionType().toLowerCase(Locale.ROOT), "some");
    assertEquals(hasRoomAccronyme.getRangeXSDType(), "string");

    List<ZObjectProperty> isLocatedInFloorList =
        findObjectPropertyByIRI(islocatedInFloorIRI, zRoomIRI.getZObjectPropertyList());
    assertEquals(isLocatedInFloorList.size(), 1);
    assertEquals(
        isLocatedInFloorList.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
        floorIRI);
  }

  @Test
  public void testMonitorableItem() {
    ZClass zMonitorableItem = olgaVisitor.getMapIRI_to_Zclass().get(monitorableitemIRI);
    List<ZDataProperty> dataProperList = zMonitorableItem.getZDataPropertyList();
    assertEquals(dataProperList.size(), 2);

    List<ZDataProperty> listFilteredByIRI =
        findZDataPropertyItemByIRI(hasItemNameIRI, dataProperList);
    assertEquals(listFilteredByIRI.size(), 1);
    assertEquals(((ZDataProperty) listFilteredByIRI.get(0)).getRangeXSDType(), "string");

    List<ZDataProperty> listFilterByIRI = findZDataPropertyItemByIRI(monitorIRI, dataProperList);
    assertEquals(listFilteredByIRI.size(), 1);
    assertEquals(((ZDataProperty) listFilterByIRI.get(0)).getRangeXSDType(), "string");
  }

  /*
   * Comments tested.
   */
  @Test
  public void testPhysicallocation() {
    String comment = "Shows actual location xxxx.";
    ZClass zphysicalLocation = olgaVisitor.getMapIRI_to_Zclass().get(physicalLocationIRI);
    IRI hasNameIRI = IRI.create("http://www.simple.com#hasName");
    IRI hasIDIRI = IRI.create("http://www.simple.com#hasID");
    List<ZDataProperty> dataProperList = zphysicalLocation.getZDataPropertyList();
    assertEquals(dataProperList.size(), 2);
    assertTrue(zphysicalLocation.getComments().equals(comment));// failing

    List<ZDataProperty> listFilteredByIRI = findZDataPropertyItemByIRI(hasNameIRI, dataProperList);
    assertEquals(listFilteredByIRI.size(), 1);
    assertEquals(((ZDataProperty) listFilteredByIRI.get(0)).getRangeXSDType(), "string");

    List<ZDataProperty> listFilterByIRI = findZDataPropertyItemByIRI(hasIDIRI, dataProperList);
    assertEquals(listFilteredByIRI.size(), 1);
    assertEquals(((ZDataProperty) listFilterByIRI.get(0)).getRangeXSDType(), "decimal");

  }

  /*
   * comments tested.
   */
  @Test
  public void testallPropertiesLocationItem() {
    String comment1 = "Coordinator $";
    String comment = "Parent Class of the classes.";
    IRI locationItemIRI = IRI.create("http://www.simple.com#LocationItem");
    ZClass zlocationItem = olgaVisitor.getMapIRI_to_Zclass().get(locationItemIRI);

    ZDataProperty hasCoordinates = zlocationItem.getZDataPropertyList().get(0);
    assertEquals(hasCoordinates.getRestrictionType().toLowerCase(Locale.ROOT), "some");
    assertEquals(hasCoordinates.getRangeXSDType(), "string");
    assertTrue(hasCoordinates.getComments().equals(comment1));
    assertTrue(zlocationItem.getComments().equals(comment));

  }

  @Test
  public void testHasFloorComments() {
    String comment1 = "Depends on Building class.";
    List<ZObjectProperty> hasFloor =
        findObjectPropertyByIRI(hasFloorIri, zBuilding.getZObjectPropertyList());
    assertEquals(hasFloor.size(), 1);
    assertTrue(hasFloor.get(0).getComments().equals(comment1));

  }

  @Test
  public void testHasRoomComments() {
    String comment2 = "shows room number or location.";
    List<ZObjectProperty> hasRoom =
        findObjectPropertyByIRI(hasRoomIRI, zFloor.getZObjectPropertyList());

    assertTrue(hasRoom.get(0).getComments().equals(comment2));
  }

  @Test
  public void testIndividualsComments() {
    String comment = "The Boston One Campus Building comment";
    String comment1 = "C'est une chambre bien éclairée";
    assertTrue(zBostonOneCampus.getComments().equals(comment));
    assertTrue(zRoom1.getComments().equals(comment1));
  }

}
