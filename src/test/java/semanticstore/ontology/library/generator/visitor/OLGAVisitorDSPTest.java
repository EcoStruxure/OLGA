/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.visitor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

/*
 * Testing with DSP ontology
 */
public class OLGAVisitorDSPTest implements OWLObjectVisitor {

	private static IRI elementsIRI = IRI.create("http://www.schneiderelectric.com/dsp#Elements");
	private static IRI measurementIRI = IRI.create("http://www.schneiderelectric.com/dsp#Measurement");
	private static IRI currentIRI = IRI.create("http://www.schneiderelectric.com/dsp#Current");
	private static IRI energyIRI = IRI.create("http://www.schneiderelectric.com/dsp#Energy");
	private static IRI humidityIRI = IRI.create("http://www.schneiderelectric.com/dsp#Humidity");
	private static IRI luminosityIRI = IRI.create("http://www.schneiderelectric.com/dsp#Luminosity");
	private static IRI powerIRI = IRI.create("http://www.schneiderelectric.com/dsp#Power");
	private static IRI temperatureIRI = IRI.create("http://www.schneiderelectric.com/dsp#Temperature");
	private static IRI voltageIRI = IRI.create("http://www.schneiderelectric.com/dsp#Voltage");
	private static IRI activeEnergyIRI = IRI.create("http://www.schneiderelectric.com/dsp#ActiveEnergy");
	private static IRI apparentEnergyIRI = IRI.create("http://www.schneiderelectric.com/dsp#ApparentEnergy");
	private static IRI activePowerIRI = IRI.create("http://www.schneiderelectric.com/dsp#ActivePower");
	private static IRI apparentPowerIRI = IRI.create("http://www.schneiderelectric.com/dsp#ApparentPower");
	private static IRI reactiveEnergyIRI = IRI.create("http://www.schneiderelectric.com/dsp#ReactiveEnergy");
	private static IRI reactivePowerIRI = IRI.create("http://www.schneiderelectric.com/dsp#ReactivePower");
	private static IRI actuatorIRI = IRI.create("http://www.schneiderelectric.com/dsp#Actuator");
	private static IRI applianceIRI = IRI.create("http://www.schneiderelectric.com/dsp#Appliance");
	private static IRI edgeIRI = IRI.create("http://www.schneiderelectric.com/dsp#Edge");
	private static IRI sensorIRI = IRI.create("http://www.schneiderelectric.com/dsp#Sensor");
	private static IRI systemIRI = IRI.create("http://www.schneiderelectric.com/dsp#System");
	private static IRI hvacIRI = IRI.create("http://www.schneiderelectric.com/dsp#HVAC");
	private static IRI lightBulbIRI = IRI.create("http://www.schneiderelectric.com/dsp#LightBulb");
	private static IRI motorIRI = IRI.create("http://www.schneiderelectric.com/dsp#Motor");
	private static IRI panelIRI = IRI.create("http://www.schneiderelectric.com/dsp#Panel");
	private static IRI solarPanelIRI = IRI.create("http://www.schneiderelectric.com/dsp#SolarPanel");
	private static IRI pumpIRI = IRI.create("http://www.schneiderelectric.com/dsp#Pump");
	private static IRI malFunctionSensorIRI = IRI.create("http://www.schneiderelectric.com/dsp#MalfunctionSensor");
	private static IRI physialLocationIRI = IRI.create("http://www.schneiderelectric.com/dsp#PhysicalLocation");
	private static IRI areaIRI = IRI.create("http://www.schneiderelectric.com/dsp#Area");
	private static IRI buildingIRI = IRI.create("http://www.schneiderelectric.com/dsp#Building");
	private static IRI floorIRI = IRI.create("http://www.schneiderelectric.com/dsp#Floor");
	private static IRI roomIRI = IRI.create("http://www.schneiderelectric.com/dsp#Room");
	private static IRI sectorIRI = IRI.create("http://www.schneiderelectric.com/dsp#Sector");
	private static IRI siteIRI = IRI.create("http://www.schneiderelectric.com/dsp#Site");
	private static IRI troubleSHoutingIRI = IRI.create("http://www.schneiderelectric.com/dsp#TroubleShouting");
	private static IRI unitofmeasureIRI = IRI.create("http://www.schneiderelectric.com/dsp#UnitofMeasure");
	private static IRI currentunitIRI = IRI.create("http://www.schneiderelectric.com/dsp#CurrentUnit");
	private static IRI energyUnitIRI = IRI.create("http://www.schneiderelectric.com/dsp#EnergyUnit");
	private static IRI humidityUnitIRI = IRI.create("http://www.schneiderelectric.com/dsp#HumidityUnit");
	private static IRI luminosityUnitIRI = IRI.create("http://www.schneiderelectric.com/dsp#LuminosityUnit");
	private static IRI powerunitIRI = IRI.create("http://www.schneiderelectric.com/dsp#PowerUnit");
	private static IRI temperatureUnitIRI = IRI.create("http://www.schneiderelectric.com/dsp#TemperatureUnit");
	private static IRI voltUnitIRI = IRI.create("http://www.schneiderelectric.com/dsp#VoltUnit");
	private static IRI nodeIRI = IRI.create("http://www.schneiderelectric.com/dsp#Node");
	private static IRI gasIRI = IRI.create("http://www.schneiderelectric.com/dsp#Gas");
	private static IRI liquidIRI = IRI.create("http://www.schneiderelectric.com/dsp#Liquid");
	private static IRI solidIRI = IRI.create("http://www.schneiderelectric.com/dsp#Solid");

	/*
	 * Property IRI
	 */
	private static IRI connectsToIRI = IRI.create("http://www.schneiderelectric.com/dsp#connectsTo");
	private static IRI controlsIRI = IRI.create("http://www.schneiderelectric.com/dsp#controls");
	private static IRI monitorsIRI = IRI.create("http://www.schneiderelectric.com/dsp#monitors");
	private static IRI hasbuildingIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasBuilding");
	private static IRI hasFloorIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasFloor");
	private static IRI hasRoomIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasRoom");
	private static IRI hasSectorIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasSector");
	private static IRI hasPhysicallocationIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasPhysicalLocation");
	private static IRI hasUnitOfMeasureIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasUnitOfMeasure");
	private static IRI isConnectedToIRI = IRI.create("http://www.schneiderelectric.com/dsp#isConnectedTo");
	private static IRI isControlledByIRI = IRI.create("http://www.schneiderelectric.com/dsp#isControlledBy");
	private static IRI isMonitoredByIRI = IRI.create("http://www.schneiderelectric.com/dsp#isMonitoredBy");
	private static IRI measuresIRI = IRI.create("http://www.schneiderelectric.com/dsp#measures");
	private static IRI ampereIRI = IRI.create("http://www.schneiderelectric.com/dsp#ampere");

	/*
	 * ZINstance
	 */
	private static IRI voltAmperHourIRI = IRI.create("http://www.schneiderelectric.com/dsp#voltAmper_Hour");
	private static IRI celciusIRI = IRI.create("http://www.schneiderelectric.com/dsp#celsius");
	private static IRI fahrenheitIRI = IRI.create("http://www.schneiderelectric.com/dsp#fahrenheit");
	private static IRI kelvinIRI = IRI.create("http://www.schneiderelectric.com/dsp#kelvin");
	private static IRI luxIRI = IRI.create("http://www.schneiderelectric.com/dsp#lux");
	private static IRI relativeHumidityIRI = IRI.create("http://www.schneiderelectric.com/dsp#relativeHumidity");
	private static IRI voltIRI = IRI.create("http://www.schneiderelectric.com/dsp#volt");
	private static IRI voltAmpereIRI = IRI.create("http://www.schneiderelectric.com/dsp#voltAmpere");
	private static IRI voltAmpereReactiveIRI = IRI.create("http://www.schneiderelectric.com/dsp#voltAmpereReactive");
	private static IRI voltAmpereReactive_hourIRI = IRI
			.create("http://www.schneiderelectric.com/dsp#voltAmpereReactive_hour");
	private static IRI wattIRI = IRI.create("http://www.schneiderelectric.com/dsp#watt");
	private static IRI watt_hourIRI = IRI.create("http://www.schneiderelectric.com/dsp#watt_hour");

	/*
	 * DataProperties
	 */
	private static IRI hasDescriptionIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasDescription");
	private static IRI hasIDIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasID");
	private static IRI hasNameIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasName");
	private static IRI hasTimeStampIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasTimeStamp");
	private static IRI hasUnitSymboleIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasUnitSymbole");
	private static IRI hasValueIRI = IRI.create("http://www.schneiderelectric.com/dsp#hasValue");

	/*
	 * creating ZClasses
	 */
	ZClass zcurrentunit = olgaVisitor.getMapIRI_to_Zclass().get(currentunitIRI);
	ZClass zenergyUnit = olgaVisitor.getMapIRI_to_Zclass().get(energyUnitIRI);
	ZClass zhumidityUnit = olgaVisitor.getMapIRI_to_Zclass().get(humidityUnitIRI);
	ZClass zluminosityUnit = olgaVisitor.getMapIRI_to_Zclass().get(luminosityUnitIRI);
	ZClass zpowerUnit = olgaVisitor.getMapIRI_to_Zclass().get(powerunitIRI);
	ZClass ztemperatureUnit = olgaVisitor.getMapIRI_to_Zclass().get(temperatureUnitIRI);
	ZClass zvoltUnit = olgaVisitor.getMapIRI_to_Zclass().get(voltUnitIRI);
	ZClass zarea = olgaVisitor.getMapIRI_to_Zclass().get(areaIRI);
	ZClass zbuilding = olgaVisitor.getMapIRI_to_Zclass().get(buildingIRI);
	ZClass zfloor = olgaVisitor.getMapIRI_to_Zclass().get(floorIRI);
	ZClass zroom = olgaVisitor.getMapIRI_to_Zclass().get(roomIRI);
	ZClass zsector = olgaVisitor.getMapIRI_to_Zclass().get(sectorIRI);
	ZClass zsite = olgaVisitor.getMapIRI_to_Zclass().get(siteIRI);
	ZClass zmalFunctionSensor = olgaVisitor.getMapIRI_to_Zclass().get(malFunctionSensorIRI);
	ZClass zsolarpanel = olgaVisitor.getMapIRI_to_Zclass().get(solarPanelIRI);
	ZClass zphysicalLocation = olgaVisitor.getMapIRI_to_Zclass().get(physialLocationIRI);
	ZClass zgas = olgaVisitor.getMapIRI_to_Zclass().get(gasIRI);
	ZClass zliquid = olgaVisitor.getMapIRI_to_Zclass().get(liquidIRI);
	ZClass zsolid = olgaVisitor.getMapIRI_to_Zclass().get(solidIRI);

	ZClass zcurrent = olgaVisitor.getMapIRI_to_Zclass().get(currentIRI);
	ZClass zenergy = olgaVisitor.getMapIRI_to_Zclass().get(energyIRI);
	ZClass zhumidity = olgaVisitor.getMapIRI_to_Zclass().get(humidityIRI);
	ZClass zluminosity = olgaVisitor.getMapIRI_to_Zclass().get(luminosityIRI);
	ZClass zpower = olgaVisitor.getMapIRI_to_Zclass().get(powerIRI);
	ZClass ztemperature = olgaVisitor.getMapIRI_to_Zclass().get(temperatureIRI);
	ZClass zvoltage = olgaVisitor.getMapIRI_to_Zclass().get(voltageIRI);

	ZClass zactiveEnergy = olgaVisitor.getMapIRI_to_Zclass().get(activeEnergyIRI);
	ZClass zapparentEnergy = olgaVisitor.getMapIRI_to_Zclass().get(apparentEnergyIRI);
	ZClass zreactiveEnergy = olgaVisitor.getMapIRI_to_Zclass().get(reactiveEnergyIRI);

	ZClass zactivePower = olgaVisitor.getMapIRI_to_Zclass().get(activePowerIRI);
	ZClass zapparentPower = olgaVisitor.getMapIRI_to_Zclass().get(apparentPowerIRI);
	ZClass zreactivePower = olgaVisitor.getMapIRI_to_Zclass().get(reactivePowerIRI);

	ZClass zactuator = olgaVisitor.getMapIRI_to_Zclass().get(actuatorIRI);
	ZClass zappliance = olgaVisitor.getMapIRI_to_Zclass().get(applianceIRI);
	ZClass zedge = olgaVisitor.getMapIRI_to_Zclass().get(edgeIRI);
	ZClass zsensor = olgaVisitor.getMapIRI_to_Zclass().get(sensorIRI);
	ZClass zsystem = olgaVisitor.getMapIRI_to_Zclass().get(systemIRI);

	ZClass zhvac = olgaVisitor.getMapIRI_to_Zclass().get(hvacIRI);
	ZClass zlightbulb = olgaVisitor.getMapIRI_to_Zclass().get(lightBulbIRI);
	ZClass zmotor = olgaVisitor.getMapIRI_to_Zclass().get(motorIRI);
	ZClass zpanel = olgaVisitor.getMapIRI_to_Zclass().get(panelIRI);
	ZClass zpump = olgaVisitor.getMapIRI_to_Zclass().get(pumpIRI);
	ZClass zmeasurement = olgaVisitor.getMapIRI_to_Zclass().get(measurementIRI);

	ZClass znode = olgaVisitor.getMapIRI_to_Zclass().get(nodeIRI);
	ZClass zappl = olgaVisitor.getMapIRI_to_Zclass().get(applianceIRI);
	ZClass zUnitOfMeasure = olgaVisitor.getMapIRI_to_Zclass().get(unitofmeasureIRI);

	static OLGAVisitor olgaVisitor;

	@BeforeClass
	public static void setUpBeforeClass() {
		String test_ont = new File(".").getAbsolutePath() + "/src/test/resources/remi/dsp-demo.owl";
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		File test_ontFile = new File(test_ont);
		try {
			OWLOntology test_ontology_to_visit = owlManager.loadOntologyFromOntologyDocument(test_ontFile);
			olgaVisitor = new OLGAVisitor(test_ontology_to_visit, CODE.C_SHARP, LIBRARY.TRINITY);
			olgaVisitor.visit();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSize() {
		assertEquals(olgaVisitor.getMapIRI_to_Zclass().entrySet().size(), 47);
	}

	@Test
	public void testElemntsSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zgas);
		subClassList.add(zliquid);
		subClassList.add(zsolid);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), elementsIRI);
		}
	}

	@Test
	public void testMeasurementSub() {
		List<ZClass> subClasslist = new ArrayList<ZClass>();
		subClasslist.add(zcurrent);
		subClasslist.add(zhumidity);
		subClasslist.add(zluminosity);
		subClasslist.add(zpower);
		subClasslist.add(ztemperature);
		subClasslist.add(zvoltage);
		subClasslist.add(zenergy);
		for (int i = 0; i < subClasslist.size(); i++) {
			assertEquals(subClasslist.get(i).getSuperZClassList().get(0).getIri(), measurementIRI);
		}
	}

	@Test
	public void testEnergySubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zactiveEnergy);
		subClassList.add(zapparentEnergy);
		subClassList.add(zreactiveEnergy);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), energyIRI);
		}
	}

	@Test
	public void testPowerSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zactivePower);
		subClassList.add(zapparentPower);
		subClassList.add(zreactivePower);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), powerIRI);
		}
	}

	@Test
	public void testNodeSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zactuator);
		subClassList.add(zappliance);
		subClassList.add(zedge);
		subClassList.add(zsensor);
		subClassList.add(zsystem);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), nodeIRI);
		}
	}

	@Test
	public void testApplianceSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zhvac);
		subClassList.add(zlightbulb);
		subClassList.add(zmotor);
		subClassList.add(zpanel);
		subClassList.add(zpump);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), applianceIRI);
		}
	}

	@Test
	public void testSolarPanelParents() {
		assertEquals(zsolarpanel.getSuperZClassList().size(), 1);
		assertEquals(zsolarpanel.getSuperZClassList().get(0).getIri(), panelIRI);
	}

	@Test
	public void testmalFunctionServerParents() {
		assertEquals(zmalFunctionSensor.getSuperZClassList().size(), 1);
		assertEquals(zmalFunctionSensor.getSuperZClassList().get(0).getIri(), troubleSHoutingIRI);
	}

	@Test
	public void testphysicalLocationSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zarea);
		subClassList.add(zbuilding);
		subClassList.add(zfloor);
		subClassList.add(zroom);
		subClassList.add(zsector);
		subClassList.add(zsite);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), physialLocationIRI);
		}
	}

	@Test
	public void testUnitOfMeasureSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zcurrentunit);
		subClassList.add(zenergyUnit);
		subClassList.add(zhumidityUnit);
		subClassList.add(zhumidityUnit);
		subClassList.add(zluminosityUnit);
		subClassList.add(zpowerUnit);
		subClassList.add(ztemperatureUnit);
		subClassList.add(zvoltUnit);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), unitofmeasureIRI);
		}
	}

	/*
	 * testing All Properties
	 */
	private List<ZObjectProperty> findObjectPropertyByIRI(IRI iriTofind, List<ZObjectProperty> listToSearchIn) {
		List<ZObjectProperty> newList = new ArrayList<>();

		for (ZObjectProperty currentProperty : listToSearchIn) {
			if (currentProperty.getObjectProperty().equals(iriTofind)) {
				newList.add(currentProperty);
			}
		}

		return newList;
	}

	private List<ZDataProperty> findZDataPropertyItemByIRI(IRI iriTofind, List<ZDataProperty> listToSearchIn) {
		List<ZDataProperty> newList = new ArrayList<>();

		for (ZDataProperty currentDataProperty : listToSearchIn) {
			if (currentDataProperty.getDataProperty().equals(iriTofind)) {
				newList.add(currentDataProperty);
			}
		}

		return newList;
	}

	@Test
	public void testMeasurementProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zmeasurement.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				unitofmeasureIRI);

		List<ZDataProperty> dataProperList = zmeasurement.getZDataPropertyList();
		assertEquals(dataProperList.size(), 4);

		List<ZDataProperty> listFilteredByIRI = findZDataPropertyItemByIRI(hasDescriptionIRI, dataProperList);
		assertEquals(listFilteredByIRI.size(), 1);
		assertEquals(((ZDataProperty) listFilteredByIRI.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> listFilterByIRI = findZDataPropertyItemByIRI(hasNameIRI, dataProperList);
		assertEquals(listFilterByIRI.size(), 1);
		assertEquals(((ZDataProperty) listFilterByIRI.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> FilterByIRI = findZDataPropertyItemByIRI(hasTimeStampIRI, dataProperList);
		assertEquals(FilterByIRI.size(), 1);
		assertEquals(((ZDataProperty) FilterByIRI.get(0)).getRangeXSDType(), "DateTime");

		List<ZDataProperty> FilteredByIRI = findZDataPropertyItemByIRI(hasValueIRI, dataProperList);
		assertEquals(FilteredByIRI.size(), 1);
		assertEquals(((ZDataProperty) FilteredByIRI.get(0)).getRangeXSDType(), "float");
	}

	@Test
	public void testCurrentProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zcurrent.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				currentunitIRI);
	}

	@Test
	public void testEnergyProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zenergy.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), energyUnitIRI);
	}

	@Test
	public void testHumidityProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zhumidity.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				humidityUnitIRI);
	}

	@Test
	public void testluminosityProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zluminosity.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				luminosityUnitIRI);
	}

	@Test
	public void testPowerProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zpower.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), powerunitIRI);
	}

	@Test
	public void testTemperatureProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				ztemperature.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				temperatureUnitIRI);
	}

	@Test
	public void testVoltageProperties() {
		List<ZObjectProperty> hasUnitOfMeasure = findObjectPropertyByIRI(hasUnitOfMeasureIRI,
				zvoltage.getZObjectPropertyList());
		assertEquals(hasUnitOfMeasure.size(), 1);
		assertEquals(hasUnitOfMeasure.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), voltUnitIRI);
	}

	@Test
	public void testNodeProperties() {
		List<ZDataProperty> dataProperList = znode.getZDataPropertyList();
		assertEquals(dataProperList.size(), 3);
		List<ZDataProperty> FilterByIRI = findZDataPropertyItemByIRI(hasDescriptionIRI, dataProperList);
		assertEquals(FilterByIRI.size(), 1);
		assertEquals(((ZDataProperty) FilterByIRI.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> FilterBy1IRI = findZDataPropertyItemByIRI(hasIDIRI, dataProperList);
		assertEquals(FilterBy1IRI.size(), 1);
		assertEquals(((ZDataProperty) FilterBy1IRI.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> FilterBy2IRI = findZDataPropertyItemByIRI(hasNameIRI, dataProperList);
		assertEquals(FilterBy2IRI.size(), 1);
		assertEquals(((ZDataProperty) FilterBy2IRI.get(0)).getRangeXSDType(), "string");

		List<ZObjectProperty> connectsTo = findObjectPropertyByIRI(connectsToIRI, znode.getZObjectPropertyList());
		assertEquals(connectsTo.size(), 1);
		assertEquals(connectsTo.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), nodeIRI);

		List<ZObjectProperty> controls = findObjectPropertyByIRI(controlsIRI, znode.getZObjectPropertyList());
		assertEquals(controls.size(), 1);
		assertEquals(controls.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), nodeIRI);

		List<ZObjectProperty> monitors = findObjectPropertyByIRI(monitorsIRI, znode.getZObjectPropertyList());
		assertEquals(monitors.size(), 1);
		assertEquals(monitors.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), nodeIRI);

		List<ZObjectProperty> measures = findObjectPropertyByIRI(measuresIRI, znode.getZObjectPropertyList());
		assertEquals(measures.size(), 1);
		assertEquals(measures.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), measurementIRI);

		List<ZObjectProperty> hasPhysicallocation = findObjectPropertyByIRI(hasPhysicallocationIRI,
				znode.getZObjectPropertyList());
		assertEquals(hasPhysicallocation.size(), 1);
		assertEquals(hasPhysicallocation.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				physialLocationIRI);
	}

	@Test
	public void testApplianceProperties() {
		List<ZObjectProperty> isConnectedTo = findObjectPropertyByIRI(isConnectedToIRI, zappl.getZObjectPropertyList());
		assertEquals(isConnectedTo.size(), 1);
		assertEquals(isConnectedTo.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), nodeIRI);

		List<ZObjectProperty> isControlledBy = findObjectPropertyByIRI(isControlledByIRI,
				zappl.getZObjectPropertyList());
		assertEquals(isControlledBy.size(), 1);
		assertEquals(isControlledBy.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), nodeIRI);

		List<ZObjectProperty> isMonitoredBy = findObjectPropertyByIRI(isMonitoredByIRI, zappl.getZObjectPropertyList());
		assertEquals(isMonitoredBy.size(), 1);
		assertEquals(isMonitoredBy.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), nodeIRI);

	}

	@Test
	public void testPhysicalLocationProperties() {
		List<ZDataProperty> dataProperList = zphysicalLocation.getZDataPropertyList();
		assertEquals(dataProperList.size(), 3);

		List<ZDataProperty> hasname = findZDataPropertyItemByIRI(hasNameIRI, dataProperList);
		assertEquals(hasname.size(), 1);
		assertEquals(((ZDataProperty) hasname.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> hasDescription = findZDataPropertyItemByIRI(hasDescriptionIRI, dataProperList);
		assertEquals(hasDescription.size(), 1);
		assertEquals(((ZDataProperty) hasDescription.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> hasID = findZDataPropertyItemByIRI(hasIDIRI, dataProperList);
		assertEquals(hasID.size(), 1);
		assertEquals(((ZDataProperty) hasID.get(0)).getRangeXSDType(), "string");

	}

	@Test
	public void testBuildingProperties() {
		List<ZObjectProperty> hasFloor = findObjectPropertyByIRI(hasFloorIRI, zbuilding.getZObjectPropertyList());
		assertEquals(hasFloor.size(), 1);
		assertEquals(hasFloor.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), floorIRI);
	}

	@Test
	public void testFloorProperties() {
		List<ZObjectProperty> hasRoom = findObjectPropertyByIRI(hasRoomIRI, zfloor.getZObjectPropertyList());
		assertEquals(hasRoom.size(), 1);
		assertEquals(hasRoom.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), roomIRI);

		List<ZObjectProperty> hasSector = findObjectPropertyByIRI(hasSectorIRI, zfloor.getZObjectPropertyList());
		assertEquals(hasSector.size(), 1);
		assertEquals(hasSector.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sectorIRI);
	}

	@Test
	public void testSectorProperties() {
		List<ZObjectProperty> hasRoom = findObjectPropertyByIRI(hasRoomIRI, zsector.getZObjectPropertyList());
		assertEquals(hasRoom.size(), 1);
		assertEquals(hasRoom.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), roomIRI);
	}

	@Test
	public void testSiteProperties() {
		List<ZObjectProperty> hasbuilding = findObjectPropertyByIRI(hasbuildingIRI, zsite.getZObjectPropertyList());
		assertEquals(hasbuilding.size(), 1);
		assertEquals(hasbuilding.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), buildingIRI);
	}

	@Test
	public void testuniteOfMeasureProperties() {
		List<ZDataProperty> dataProperList = zUnitOfMeasure.getZDataPropertyList();
		assertEquals(dataProperList.size(), 1);

		List<ZDataProperty> hasUnitSymbole = findZDataPropertyItemByIRI(hasUnitSymboleIRI, dataProperList);
		assertEquals(hasUnitSymbole.size(), 1);
		assertEquals(((ZDataProperty) hasUnitSymbole.get(0)).getRangeXSDType(), "string");
	}

	private List<ZInstance> findZinstanceItemByIRI(IRI iriTofind, List<ZInstance> listToSearchIn) {
		List<ZInstance> newList = new ArrayList<>();

		for (ZInstance currentInstance : listToSearchIn) {
			if (currentInstance.getIri().equals(iriTofind)) {
				newList.add(currentInstance);
			}
		}

		return newList;
	}

	@Test
	public void testCurrentunitProperties() {
		List<ZInstance> ZinstanceList = zcurrentunit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> ampere = findZinstanceItemByIRI(ampereIRI, ZinstanceList);
		assertEquals(ampere.size(), 1);
	}

	@Test
	public void testEnergyUnitProperties() {

		List<ZInstance> ZinstanceList = zenergyUnit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 3);
		List<ZInstance> wattHour = findZinstanceItemByIRI(watt_hourIRI, ZinstanceList);
		assertEquals(wattHour.size(), 1);
		List<ZInstance> voltAmperReactive = findZinstanceItemByIRI(voltAmpereReactive_hourIRI, ZinstanceList);
		assertEquals(voltAmperReactive.size(), 1);
		List<ZInstance> voltAmperHour = findZinstanceItemByIRI(voltAmperHourIRI, ZinstanceList);
		assertEquals(voltAmperHour.size(), 1);
	}

	@Test
	public void testHumidityUnitProperties() {
		List<ZInstance> ZinstanceList = zhumidityUnit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> relativehumidity = findZinstanceItemByIRI(relativeHumidityIRI, ZinstanceList);
		assertEquals(relativehumidity.size(), 1);
	}

	@Test
	public void testLuminosityUnitProperties() {
		List<ZInstance> ZinstanceList = zluminosityUnit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> lux = findZinstanceItemByIRI(luxIRI, ZinstanceList);
		assertEquals(lux.size(), 1);
	}

	@Test
	public void testPowerUnitProperties() {
		List<ZInstance> ZinstanceList = zpowerUnit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 3);
		List<ZInstance> voltAmpere = findZinstanceItemByIRI(voltAmpereIRI, ZinstanceList);
		assertEquals(voltAmpere.size(), 1);
		List<ZInstance> voltAmpereReactive = findZinstanceItemByIRI(voltAmpereReactiveIRI, ZinstanceList);
		assertEquals(voltAmpereReactive.size(), 1);

		List<ZInstance> watt = findZinstanceItemByIRI(wattIRI, ZinstanceList);
		assertEquals(watt.size(), 1);
	}

	@Test
	public void testTemperatureUnitProperties() {
		List<ZInstance> ZinstanceList = ztemperatureUnit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 3);
		List<ZInstance> celcius = findZinstanceItemByIRI(celciusIRI, ZinstanceList);
		assertEquals(celcius.size(), 1);
		List<ZInstance> fahrenheit = findZinstanceItemByIRI(fahrenheitIRI, ZinstanceList);
		assertEquals(fahrenheit.size(), 1);

		List<ZInstance> kelvin = findZinstanceItemByIRI(kelvinIRI, ZinstanceList);
		assertEquals(kelvin.size(), 1);
	}

	@Test
	public void testVoltUnitProperties() {
		List<ZInstance> ZinstanceList = zvoltUnit.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> volt = findZinstanceItemByIRI(voltIRI, ZinstanceList);
		assertEquals(volt.size(), 1);
	}
}
