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
import static org.junit.Assert.assertTrue;
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
 * OLGA tested with saref
 * tested areas:
 * Classes,Subclasses,Class Properties.
 */
public class OLGAVisitorSarefFullTest implements OWLObjectVisitor {
	/*
	 * creating the IRI's to the classes
	 */
	private static IRI commandIRI = IRI.create("https://w3id.org/saref#Command");
	private static IRI closeCommandIRI = IRI.create("https://w3id.org/saref#CloseCommand");
	private static IRI getCommandIRI = IRI.create("https://w3id.org/saref#GetCommand");
	private static IRI notifyCommandIRI = IRI.create("https://w3id.org/saref#NotifyCommand");
	private static IRI offCommandIRI = IRI.create("https://w3id.org/saref#OffCommand");
	private static IRI onCommandIRI = IRI.create("https://w3id.org/saref#OnCommand");
	private static IRI openCommandIRI = IRI.create("https://w3id.org/saref#OpenCommand");
	private static IRI pauseCommandIRI = IRI.create("https://w3id.org/saref#PauseCommand");
	private static IRI setLevelCommandIRI = IRI.create("https://w3id.org/saref#SetLevelCommand");
	private static IRI startCommandIRI = IRI.create("https://w3id.org/saref#StartCommand");
	private static IRI stepDownCommandIRI = IRI.create("https://w3id.org/saref#StepDownCommand");
	private static IRI stepUpCommandIRI = IRI.create("https://w3id.org/saref#StepUpCommand");
	private static IRI stopCommandIRI = IRI.create("https://w3id.org/saref#StopCommand");
	private static IRI toggleCommandIRI = IRI.create("https://w3id.org/saref#ToggleCommand");
	private static IRI getCurrentMeterValueCommandIRI = IRI
			.create("https://w3id.org/saref#GetCurrentMeterValueCommand");
	private static IRI getMeterDataCommandIRI = IRI.create("https://w3id.org/saref#GetMeterDataCommand");
	private static IRI getMeterHistoryCommandIRI = IRI.create("https://w3id.org/saref#GetMeterHistoryCommand");
	private static IRI getSensingDataCommandIRI = IRI.create("https://w3id.org/saref#GetSensingDataCommand");
	private static IRI setAbsoluteLevelCommandIRI = IRI.create("https://w3id.org/saref#SetAbsoluteLevelCommand");
	private static IRI setRelativeLevelCommandIRI = IRI.create("https://w3id.org/saref#SetRelativeLevelCommand");
	private static IRI functionRelatedIRI = IRI.create("https://w3id.org/saref#FunctionRelated");
	private static IRI deviceIRI = IRI.create("https://w3id.org/saref#Device");
	private static IRI buildingRelatedIRI = IRI.create("https://w3id.org/saref#BuildingRelated");
	private static IRI energyRelatedIRI = IRI.create("https://w3id.org/saref#EnergyRelated");
	private static IRI generatorIRI = IRI.create("https://w3id.org/saref#Generator");
	private static IRI loadIRI = IRI.create("https://w3id.org/saref#Load");
	private static IRI storageIRI = IRI.create("https://w3id.org/saref#Storage");
	private static IRI washingMachineIRI = IRI.create("https://w3id.org/saref#WashingMachine");
	private static IRI applianceIRI = IRI.create("https://w3id.org/saref#Appliance");
	private static IRI actuatorIRI = IRI.create("https://w3id.org/saref#Actuator");
	private static IRI hvacIRI = IRI.create("https://w3id.org/saref#HVAC");
	private static IRI lightingDeviceIRI = IRI.create("https://w3id.org/saref#LightingDevice");
	private static IRI meterIRI = IRI.create("https://w3id.org/saref#Meter");
	private static IRI microRenewableIRI = IRI.create("https://w3id.org/saref#MicroRenewable");
	private static IRI multimediaIRI = IRI.create("https://w3id.org/saref#Multimedia");
	private static IRI networkIRI = IRI.create("https://w3id.org/saref#Network");
	private static IRI sensorkIRI = IRI.create("https://w3id.org/saref#Sensor");
	private static IRI switchIRI = IRI.create("https://w3id.org/saref#Switch");
	private static IRI doorSwitchIRI = IRI.create("https://w3id.org/saref#DoorSwitch");
	private static IRI lightSwitchIRI = IRI.create("https://w3id.org/saref#LightSwitch");
	private static IRI energyMeterIRI = IRI.create("https://w3id.org/saref#EnergyMeter");
	private static IRI smokeSensorIRI = IRI.create("https://w3id.org/saref#SmokeSensor");
	private static IRI temperatureSensorIRI = IRI.create("https://w3id.org/saref#TemperatureSensor");
	private static IRI functionIRI = IRI.create("https://w3id.org/saref#Function");
	private static IRI actuatingFunctionIRI = IRI.create("https://w3id.org/saref#ActuatingFunction");
	private static IRI eventFunctionIRI = IRI.create("https://w3id.org/saref#EventFunction");
	private static IRI meteringFunctionIRI = IRI.create("https://w3id.org/saref#MeteringFunction");
	private static IRI sensingFunctionIRI = IRI.create("https://w3id.org/saref#SensingFunction");
	private static IRI levelControlFunctionIRI = IRI.create("https://w3id.org/saref#LevelControlFunction");
	private static IRI onOffFunctionIRI = IRI.create("https://w3id.org/saref#OnOffFunction");
	private static IRI openCloseFunctionIRI = IRI.create("https://w3id.org/saref#OpenCloseFunction");
	private static IRI startStopFunctionIRI = IRI.create("https://w3id.org/saref#StartStopFunction");
	private static IRI propertyIRI = IRI.create("https://w3id.org/saref#Property");
	private static IRI energyIRI = IRI.create("https://w3id.org/saref#Energy");
	private static IRI lightIRI = IRI.create("https://w3id.org/saref#Light");
	private static IRI motionIRI = IRI.create("https://w3id.org/saref#Motion");
	private static IRI occupancyIRI = IRI.create("https://w3id.org/saref#Occupancy");
	private static IRI powerIRI = IRI.create("https://w3id.org/saref#Power");
	private static IRI pressureIRI = IRI.create("https://w3id.org/saref#Pressure");
	private static IRI priceIRI = IRI.create("https://w3id.org/saref#Price");
	private static IRI smokeIRI = IRI.create("https://w3id.org/saref#Smoke");
	private static IRI temperatureIRI = IRI.create("https://w3id.org/saref#Temperature");
	private static IRI timeIRI = IRI.create("https://w3id.org/saref#Time");
	private static IRI humidityIRI = IRI.create("https://w3id.org/saref#Humidity");
	private static IRI literalIRI = IRI.create("http://www.w3.org/2000/01/rdf-schema#Literal");
	private static IRI dateTimeStampIRI = IRI.create("http://www.w3.org/2001/XMLSchema#dateTimeStamp");
	private static IRI resourcesIRI = IRI.create("http://www.w3.org/2000/01/rdf-schema#Resource");
	private static IRI generalizedYearIRI = IRI.create("http://www.w3.org/2006/time#generalYear");
	private static IRI serviceIRI = IRI.create("https://w3id.org/saref#Service");
	private static IRI switchOnServiceIRI = IRI.create("https://w3id.org/saref#SwitchOnService");
	private static IRI stateIRI = IRI.create("https://w3id.org/saref#State");
	private static IRI multilevelStateIRI = IRI.create("https://w3id.org/saref#MultiLevelState");
	private static IRI onOffStateIRI = IRI.create("https://w3id.org/saref#OnOffState");
	private static IRI openCloseStateIRI = IRI.create("https://w3id.org/saref#OpenCloseState");
	private static IRI sssIRI = IRI.create("https://w3id.org/saref#StartStopState");
	private static IRI offStateIRI = IRI.create("https://w3id.org/saref#OffState");
	private static IRI onStateIRI = IRI.create("https://w3id.org/saref#OnState");
	private static IRI closeStateIRI = IRI.create("https://w3id.org/saref#CloseState");
	private static IRI openStateIRI = IRI.create("https://w3id.org/saref#OpenState");
	private static IRI startStateIRI = IRI.create("https://w3id.org/saref#StartState");
	private static IRI stopStateIRI = IRI.create("https://w3id.org/saref#StopState");
	private static IRI temporalDurationIRI = IRI.create("http://www.w3.org/2006/time#TemporalDuration");
	private static IRI generalizeddurationdescriptionIRI = IRI
			.create("http://www.w3.org/2006/time#GeneralDurationDescription");
	private static IRI temporalUnitIRI = IRI.create("http://www.w3.org/2006/time#TemporalUnit");
	private static IRI timeDurationIRI = IRI.create("http://www.w3.org/2006/time#Duration");
	private static IRI durationDescriptionIRI = IRI.create("http://www.w3.org/2006/time#DurationDescription");
	private static IRI yearIRI = IRI.create("http://www.w3.org/2006/time#Year");
	private static IRI unitOfmeasureIRI = IRI.create("https://w3id.org/saref#UnitOfMeasure");
	private static IRI temporalEntityIRI = IRI.create("http://www.w3.org/2006/time#TemporalEntity");
	private static IRI timeInstantIRI = IRI.create("http://www.w3.org/2006/time#Instant");
	private static IRI properIntervalIRI = IRI.create("http://www.w3.org/2006/time#ProperInterval");
	private static IRI dateTimeIntervalIRI = IRI.create("http://www.w3.org/2006/time#DateTimeInterval");
	private static IRI timeIntervalIRI = IRI.create("http://www.w3.org/2006/time#Interval");
	private static IRI temporalpositionIRI = IRI.create("http://www.w3.org/2006/time#TemporalPosition");
	private static IRI gDateTimeDescriptionIRI = IRI.create("http://www.w3.org/2006/time#GeneralDateTimeDescription");
	private static IRI timePositonIRI = IRI.create("http://www.w3.org/2006/time#TimePosition");
	private static IRI dateTimeDescriptionIRI = IRI.create("http://www.w3.org/2006/time#DateTimeDescription");
	private static IRI monthOfTheYearIRI = IRI.create("http://www.w3.org/2006/time#MonthOfYear");
	private static IRI januaryIRI = IRI.create("http://www.w3.org/2006/time#January");
	private static IRI currencyIRI = IRI.create("https://w3id.org/saref#Currency");
	private static IRI energyUnitIRI = IRI.create("https://w3id.org/saref#EnergyUnit");
	private static IRI illuminanceUnitIRI = IRI.create("https://w3id.org/saref#IlluminanceUnit");
	private static IRI powerUnitIRI = IRI.create("https://w3id.org/saref#PowerUnit");
	private static IRI pressureUnitIRI = IRI.create("https://w3id.org/saref#PressureUnit");
	private static IRI actsUponIRI = IRI.create("https://w3id.org/saref#actsUpon");
	private static IRI accomplishesIRI = IRI.create("https://w3id.org/saref#accomplishes");
	private static IRI consistsOfIRI = IRI.create("https://w3id.org/saref#consistsOf");
	private static IRI isCommandOfIRI = IRI.create("https://w3id.org/saref#isCommandOf");
	private static IRI hasDescriptionIRI = IRI.create("https://w3id.org/saref#hasDescription");
	private static IRI openIRI = IRI.create("https://w3id.org/saref#Open");
	private static IRI pauseIRI = IRI.create("https://w3id.org/saref#Pause");
	private static IRI profileIRI = IRI.create("https://w3id.org/saref#Profile");
	private static IRI measuresPropertyIRI = IRI.create("https://w3id.org/saref#measuresProperty");
	private static IRI closeIRI = IRI.create("https://w3id.org/saref#Close");
	private static IRI getCurrentMeterValueIRI = IRI.create("https://w3id.org/saref#GetCurrentMeterValue");
	private static IRI getMeterDataIRI = IRI.create("https://w3id.org/saref#GetMeterData");
	private static IRI getMeterHistoryIRI = IRI.create("https://w3id.org/saref#GetMeterHistory");
	private static IRI getSensingDataIRI = IRI.create("https://w3id.org/saref#GetSensingData");
	private static IRI notifyIRI = IRI.create("https://w3id.org/saref#Notify");
	private static IRI offIRI = IRI.create("https://w3id.org/saref#Off");
	private static IRI onIRI = IRI.create("https://w3id.org/saref#On");
	private static IRI setAbsoluteLevelIRI = IRI.create("https://w3id.org/saref#SetAbsoluteLevel");
	private static IRI setRelativeLevelIRI = IRI.create("https://w3id.org/saref#SetRelativeLevel");
	private static IRI startIRI = IRI.create("https://w3id.org/saref#Start");
	private static IRI stepDownIRI = IRI.create("https://w3id.org/saref#StepDown");
	private static IRI stepUpIRI = IRI.create("https://w3id.org/saref#StepUp");
	private static IRI stopIRI = IRI.create("https://w3id.org/saref#Stop");
	private static IRI toggleIRI = IRI.create("https://w3id.org/saref#Toggle");
	private static IRI controlsPropertyIRI = IRI.create("https://w3id.org/saref#controlsProperty");
	private static IRI hasFunctionIRI = IRI.create("https://w3id.org/saref#hasFunction");
	private static IRI hasProfileIRI = IRI.create("https://w3id.org/saref#hasProfile");
	private static IRI hasStateIRI = IRI.create("https://w3id.org/saref#hasState");
	private static IRI hasTypicalConsumptionIRI = IRI.create("https://w3id.org/saref#hasTypicalConsumption");
	private static IRI isusedForIRI = IRI.create("https://w3id.org/saref#isUsedFor");
	private static IRI commodityIRI = IRI.create("https://w3id.org/saref#Commodity");
	private static IRI makesMeasurementIRI = IRI.create("https://w3id.org/saref#makesMeasurement");
	private static IRI measurementIRI = IRI.create("https://w3id.org/saref#Measurement");
	private static IRI offersIRI = IRI.create("https://w3id.org/saref#offers");
	private static IRI taskIRI = IRI.create("https://w3id.org/saref#Task");
	private static IRI hasManufacturerIRI = IRI.create("https://w3id.org/saref#hasManufacturer");
	private static IRI hasModelIRI = IRI.create("https://w3id.org/saref#hasModel");
	private static IRI washingIRI = IRI.create("https://w3id.org/saref#Washing");
	private static IRI safetyIRI = IRI.create("https://w3id.org/saref#Safety");
	private static IRI lightingIRI = IRI.create("https://w3id.org/saref#Lighting");
	private static IRI comfortIRI = IRI.create("https://w3id.org/saref#Comfort");
	private static IRI meterReadingIRI = IRI.create("https://w3id.org/saref#MeterReading");
	private static IRI energyEfficiencyIRI = IRI.create("https://w3id.org/saref#EnergyEfficiency");
	private static IRI entertainmentIRI = IRI.create("https://w3id.org/saref#Entertainment");
	private static IRI hasCommandIRI = IRI.create("https://w3id.org/saref#hasCommand");
	private static IRI hasThresholdMeasurementIRI = IRI.create("https://w3id.org/saref#hasThresholdMeasurement");

	/*
	 * creating ZClasses for IRI's.
	 */
	ZClass zPressureUnit = olgaVisitor.getMapIRI_to_Zclass().get(pressureUnitIRI);
	ZClass zPowerUnit = olgaVisitor.getMapIRI_to_Zclass().get(powerUnitIRI);
	ZClass zIlluminanceUnit = olgaVisitor.getMapIRI_to_Zclass().get(illuminanceUnitIRI);
	ZClass zEnergyUnit = olgaVisitor.getMapIRI_to_Zclass().get(energyUnitIRI);
	ZClass zCurrency = olgaVisitor.getMapIRI_to_Zclass().get(currencyIRI);
	ZClass zJanuary = olgaVisitor.getMapIRI_to_Zclass().get(januaryIRI);
	ZClass zmonthOfTheYear = olgaVisitor.getMapIRI_to_Zclass().get(monthOfTheYearIRI);
	ZClass zDateTimeDescription = olgaVisitor.getMapIRI_to_Zclass().get(dateTimeDescriptionIRI);
	ZClass zTimePosition = olgaVisitor.getMapIRI_to_Zclass().get(timePositonIRI);
	ZClass zGDTDescription = olgaVisitor.getMapIRI_to_Zclass().get(gDateTimeDescriptionIRI);
	ZClass zTimeIntevral = olgaVisitor.getMapIRI_to_Zclass().get(timeIntervalIRI);
	ZClass zDateTimeIntevral = olgaVisitor.getMapIRI_to_Zclass().get(dateTimeIntervalIRI);
	ZClass zProperIntevral = olgaVisitor.getMapIRI_to_Zclass().get(properIntervalIRI);
	ZClass ztimeInstant = olgaVisitor.getMapIRI_to_Zclass().get(timeInstantIRI);
	ZClass zYear = olgaVisitor.getMapIRI_to_Zclass().get(yearIRI);
	ZClass zDurationDescription = olgaVisitor.getMapIRI_to_Zclass().get(durationDescriptionIRI);
	ZClass ztimeDuration = olgaVisitor.getMapIRI_to_Zclass().get(timeDurationIRI);
	ZClass ztemporalUnit = olgaVisitor.getMapIRI_to_Zclass().get(temporalUnitIRI);
	ZClass zgeneralizeddurationdescription = olgaVisitor.getMapIRI_to_Zclass().get(generalizeddurationdescriptionIRI);
	ZClass zstartState = olgaVisitor.getMapIRI_to_Zclass().get(startStateIRI);
	ZClass zstopState = olgaVisitor.getMapIRI_to_Zclass().get(stopStateIRI);
	ZClass zOpenState = olgaVisitor.getMapIRI_to_Zclass().get(openStateIRI);
	ZClass zCloseState = olgaVisitor.getMapIRI_to_Zclass().get(closeStateIRI);
	ZClass zOnState = olgaVisitor.getMapIRI_to_Zclass().get(onStateIRI);
	ZClass zOffState = olgaVisitor.getMapIRI_to_Zclass().get(offStateIRI);
	ZClass zSSS = olgaVisitor.getMapIRI_to_Zclass().get(sssIRI);
	ZClass zOpenCloseState = olgaVisitor.getMapIRI_to_Zclass().get(openCloseStateIRI);
	ZClass zOnOffState = olgaVisitor.getMapIRI_to_Zclass().get(onOffStateIRI);
	ZClass zmultiLevelState = olgaVisitor.getMapIRI_to_Zclass().get(multilevelStateIRI);
	ZClass zswitchOnService = olgaVisitor.getMapIRI_to_Zclass().get(switchOnServiceIRI);
	ZClass zgeneralizedYear = olgaVisitor.getMapIRI_to_Zclass().get(generalizedYearIRI);
	ZClass zdateTimeStamp = olgaVisitor.getMapIRI_to_Zclass().get(dateTimeStampIRI);
	ZClass zhumidity = olgaVisitor.getMapIRI_to_Zclass().get(humidityIRI);
	ZClass ztime = olgaVisitor.getMapIRI_to_Zclass().get(timeIRI);
	ZClass ztemperature = olgaVisitor.getMapIRI_to_Zclass().get(temperatureIRI);
	ZClass zsmoke = olgaVisitor.getMapIRI_to_Zclass().get(smokeIRI);
	ZClass zprice = olgaVisitor.getMapIRI_to_Zclass().get(priceIRI);
	ZClass zpressure = olgaVisitor.getMapIRI_to_Zclass().get(pressureIRI);
	ZClass zpower = olgaVisitor.getMapIRI_to_Zclass().get(powerIRI);
	ZClass zoccupancy = olgaVisitor.getMapIRI_to_Zclass().get(occupancyIRI);
	ZClass zmotion = olgaVisitor.getMapIRI_to_Zclass().get(motionIRI);
	ZClass zlight = olgaVisitor.getMapIRI_to_Zclass().get(lightIRI);
	ZClass zenergy = olgaVisitor.getMapIRI_to_Zclass().get(energyIRI);
	ZClass zstartStopFunction = olgaVisitor.getMapIRI_to_Zclass().get(startStopFunctionIRI);
	ZClass zopenCloseFunction = olgaVisitor.getMapIRI_to_Zclass().get(openCloseFunctionIRI);
	ZClass zonOffFunction = olgaVisitor.getMapIRI_to_Zclass().get(onOffFunctionIRI);
	ZClass zlevelcontrolfunction = olgaVisitor.getMapIRI_to_Zclass().get(levelControlFunctionIRI);
	ZClass zsensingfunction = olgaVisitor.getMapIRI_to_Zclass().get(sensingFunctionIRI);
	ZClass zmeteringfunction = olgaVisitor.getMapIRI_to_Zclass().get(meteringFunctionIRI);
	ZClass zeventfunction = olgaVisitor.getMapIRI_to_Zclass().get(eventFunctionIRI);
	ZClass zactuatingfunction = olgaVisitor.getMapIRI_to_Zclass().get(actuatingFunctionIRI);
	ZClass zfunction = olgaVisitor.getMapIRI_to_Zclass().get(functionIRI);
	ZClass ztemperaturesensor = olgaVisitor.getMapIRI_to_Zclass().get(temperatureSensorIRI);
	ZClass zsmokesensor = olgaVisitor.getMapIRI_to_Zclass().get(smokeSensorIRI);
	ZClass zenergyMeter = olgaVisitor.getMapIRI_to_Zclass().get(energyMeterIRI);
	ZClass zligthswitch = olgaVisitor.getMapIRI_to_Zclass().get(lightSwitchIRI);
	ZClass zdoorswitch = olgaVisitor.getMapIRI_to_Zclass().get(doorSwitchIRI);
	ZClass zswitch = olgaVisitor.getMapIRI_to_Zclass().get(switchIRI);
	ZClass zsensor = olgaVisitor.getMapIRI_to_Zclass().get(sensorkIRI);
	ZClass znetwork = olgaVisitor.getMapIRI_to_Zclass().get(networkIRI);
	ZClass zmultimedia = olgaVisitor.getMapIRI_to_Zclass().get(multimediaIRI);
	ZClass zmicroRenewable = olgaVisitor.getMapIRI_to_Zclass().get(microRenewableIRI);
	ZClass zmeter = olgaVisitor.getMapIRI_to_Zclass().get(meterIRI);
	ZClass zlightingDevice = olgaVisitor.getMapIRI_to_Zclass().get(lightingDeviceIRI);
	ZClass zhvac = olgaVisitor.getMapIRI_to_Zclass().get(hvacIRI);
	ZClass zActuator = olgaVisitor.getMapIRI_to_Zclass().get(actuatorIRI);
	ZClass zAppliance = olgaVisitor.getMapIRI_to_Zclass().get(applianceIRI);
	ZClass zwashingMachine = olgaVisitor.getMapIRI_to_Zclass().get(washingMachineIRI);
	ZClass zstorage = olgaVisitor.getMapIRI_to_Zclass().get(storageIRI);
	ZClass zload = olgaVisitor.getMapIRI_to_Zclass().get(loadIRI);
	ZClass zGenerator = olgaVisitor.getMapIRI_to_Zclass().get(generatorIRI);
	ZClass zFunctionRelated = olgaVisitor.getMapIRI_to_Zclass().get(functionRelatedIRI);
	ZClass zEnergyRelated = olgaVisitor.getMapIRI_to_Zclass().get(energyRelatedIRI);
	ZClass zBuildingRelated = olgaVisitor.getMapIRI_to_Zclass().get(buildingRelatedIRI);
	ZClass zDevice = olgaVisitor.getMapIRI_to_Zclass().get(deviceIRI);
	ZClass zSetAbsoluteLevelCommand = olgaVisitor.getMapIRI_to_Zclass().get(setAbsoluteLevelCommandIRI);
	ZClass zSetRelativeLevelCommand = olgaVisitor.getMapIRI_to_Zclass().get(setRelativeLevelCommandIRI);
	ZClass zGetCurrentMeterValueCommand = olgaVisitor.getMapIRI_to_Zclass().get(getCurrentMeterValueCommandIRI);
	ZClass zGetMeterDataCommand = olgaVisitor.getMapIRI_to_Zclass().get(getMeterDataCommandIRI);
	ZClass zGetMeterHistoryCommand = olgaVisitor.getMapIRI_to_Zclass().get(getMeterHistoryCommandIRI);
	ZClass zGGetSensingDataCommand = olgaVisitor.getMapIRI_to_Zclass().get(getSensingDataCommandIRI);
	ZClass zcommand = olgaVisitor.getMapIRI_to_Zclass().get(commandIRI);
	ZClass zCloseCommand = olgaVisitor.getMapIRI_to_Zclass().get(closeCommandIRI);
	ZClass zgetCommand = olgaVisitor.getMapIRI_to_Zclass().get(getCommandIRI);
	ZClass znotifyCommand = olgaVisitor.getMapIRI_to_Zclass().get(notifyCommandIRI);
	ZClass zOffCommand = olgaVisitor.getMapIRI_to_Zclass().get(offCommandIRI);
	ZClass zonCommand = olgaVisitor.getMapIRI_to_Zclass().get(onCommandIRI);
	ZClass zopenCommand = olgaVisitor.getMapIRI_to_Zclass().get(openCommandIRI);
	ZClass zpauseCommand = olgaVisitor.getMapIRI_to_Zclass().get(pauseCommandIRI);
	ZClass zSetLevelCommand = olgaVisitor.getMapIRI_to_Zclass().get(setLevelCommandIRI);
	ZClass zstartCommand = olgaVisitor.getMapIRI_to_Zclass().get(startCommandIRI);
	ZClass zstepDownCommand = olgaVisitor.getMapIRI_to_Zclass().get(stepDownCommandIRI);
	ZClass zstepUpCommand = olgaVisitor.getMapIRI_to_Zclass().get(stepUpCommandIRI);
	ZClass zstopCommand = olgaVisitor.getMapIRI_to_Zclass().get(stopCommandIRI);
	ZClass ztoggleCommand = olgaVisitor.getMapIRI_to_Zclass().get(toggleCommandIRI);

	static OLGAVisitor olgaVisitor;

	/*
	 * launching the OLGA
	 */
	@BeforeClass
	public static void setUpBeforeClass() {

		String test_ont = new File(".").getAbsolutePath()
				+ "/src/test/resources/complexOntologiesModified/sarefMergedModifiedForTest.owl";
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

	/*
	 * Testing the total numbers of the classes in ontology.
	 */
	@Test
	public void testSize() {
		assertEquals(olgaVisitor.getMapIRI_to_Zclass().entrySet().size(), 116);

	}

	/*
	 * Testing subclasses of the command class
	 */
	@Test
	public void testCommandSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zCloseCommand);
		subClassList.add(zgetCommand);
		subClassList.add(zOffCommand);
		subClassList.add(zonCommand);
		subClassList.add(zopenCommand);
		subClassList.add(zpauseCommand);
		subClassList.add(zSetLevelCommand);
		subClassList.add(zstartCommand);
		subClassList.add(zstepUpCommand);
		subClassList.add(zstopCommand);
		subClassList.add(ztoggleCommand);
		subClassList.add(zstepDownCommand);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), commandIRI);

		}

	}

	@Test
	public void testGetCommandSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zGetCurrentMeterValueCommand);
		subClassList.add(zGetMeterDataCommand);
		subClassList.add(zGetMeterHistoryCommand);
		subClassList.add(zGGetSensingDataCommand);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), getCommandIRI);
		}
	}

	@Test
	public void testSetLevelCommandSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zSetAbsoluteLevelCommand);
		subClassList.add(zSetRelativeLevelCommand);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), setLevelCommandIRI);
		}
	}

	@Test
	public void testDeviceSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zBuildingRelated);
		subClassList.add(zEnergyRelated);
		subClassList.add(zFunctionRelated);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), deviceIRI);
		}
	}

	@Test
	public void testEnergyRelatedSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zGenerator);
		subClassList.add(zload);
		subClassList.add(zstorage);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), energyRelatedIRI);
		}
	}

	@Test
	public void testWashingMachineSubClasses() {
		List<ZClass> parents = zwashingMachine.getSuperZClassList();
		assertEquals(parents.size(), 2);
		assertTrue(parents.stream().anyMatch(p -> p.getIri().equals(loadIRI)));
		assertTrue(parents.stream().anyMatch(p -> p.getIri().equals(applianceIRI)));
	}

	@Test
	public void testFunctionRelatedSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zActuator);
		subClassList.add(zAppliance);
		subClassList.add(zhvac);
		subClassList.add(zlightingDevice);
		subClassList.add(zmeter);
		subClassList.add(zmicroRenewable);
		subClassList.add(zmultimedia);
		subClassList.add(znetwork);
		subClassList.add(zsensor);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), functionRelatedIRI);
		}
	}

	@Test
	public void testSwitchSubClasses() {
		assertEquals(zswitch.getSuperZClassList().size(), 1);
		assertEquals(zswitch.getSuperZClassList().get(0).getIri(), actuatorIRI);
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zdoorswitch);
		subClassList.add(zligthswitch);

		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), switchIRI);
		}
	}

	@Test
	public void testMeterSubClasses() {
		assertEquals(zenergyMeter.getSuperZClassList().size(), 1);
		assertEquals(zenergyMeter.getSuperZClassList().get(0).getIri(), meterIRI);
	}

	@Test
	public void testSensorSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zsmokesensor);
		subClassList.add(ztemperaturesensor);

		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), sensorkIRI);
		}
	}

	@Test
	public void testFunctionSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zactuatingfunction);
		subClassList.add(zeventfunction);
		subClassList.add(zmeteringfunction);
		subClassList.add(zsensingfunction);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), functionIRI);
		}
	}

	@Test
	public void testActuatingFunctionSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zlevelcontrolfunction);
		subClassList.add(zonOffFunction);
		subClassList.add(zopenCloseFunction);
		subClassList.add(zstartStopFunction);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), actuatingFunctionIRI);
		}
	}

	@Test
	public void testPropertySubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zenergy);
		subClassList.add(zhumidity);
		subClassList.add(zlight);
		subClassList.add(zmotion);
		subClassList.add(zoccupancy);
		subClassList.add(zpower);
		subClassList.add(zpressure);
		subClassList.add(zprice);
		subClassList.add(zsmoke);
		subClassList.add(ztemperature);
		subClassList.add(ztime);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), propertyIRI);
		}
	}

	@Test
	public void testLiteralSubClasses() {
		assertEquals(zdateTimeStamp.getSuperZClassList().size(), 1);
		assertEquals(zdateTimeStamp.getSuperZClassList().get(0).getIri(), literalIRI);
	}

	@Test
	public void testResourcesSubClasses() {
		assertEquals(zgeneralizedYear.getSuperZClassList().size(), 1);
		assertEquals(zgeneralizedYear.getSuperZClassList().get(0).getIri(), resourcesIRI);
	}

	@Test
	public void testServiceSubClasses() {
		assertEquals(zswitchOnService.getSuperZClassList().size(), 1);
		assertEquals(zswitchOnService.getSuperZClassList().get(0).getIri(), serviceIRI);
	}

	@Test
	public void testStateSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zmultiLevelState);
		subClassList.add(zOnOffState);
		subClassList.add(zOpenCloseState);
		subClassList.add(zSSS);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), stateIRI);
		}
	}

	@Test
	public void testOnOffStateSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zOnState);
		subClassList.add(zOffState);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), onOffStateIRI);
		}
	}

	@Test
	public void testOpenCloseStateSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zOpenState);
		subClassList.add(zCloseState);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), openCloseStateIRI);
		}
	}

	@Test
	public void testStartStopStateSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zstartState);
		subClassList.add(zstopState);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), sssIRI);
		}
	}

	@Test
	public void testTemporalDurationSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zgeneralizeddurationdescription);
		subClassList.add(ztimeDuration);

		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), temporalDurationIRI);
		}
	}

	@Test
	public void testGeneralizedDurationDescriptionSubClasses() {
		assertEquals(zDurationDescription.getSuperZClassList().size(), 1);
		assertEquals(zDurationDescription.getSuperZClassList().get(0).getIri(), generalizeddurationdescriptionIRI);
	}

	@Test
	public void testDurationDescriptionSubClasses() {
		assertEquals(zYear.getSuperZClassList().size(), 1);
		assertEquals(zYear.getSuperZClassList().get(0).getIri(), durationDescriptionIRI);
	}

	@Test
	public void testTemporalUnitParentClasses() {
		List<ZClass> parents = ztemporalUnit.getSuperZClassList();
		assertEquals(parents.size(), 2);
		assertTrue(parents.stream().anyMatch(p -> p.getIri().equals(temporalDurationIRI)));
		assertTrue(parents.stream().anyMatch(p -> p.getIri().equals(unitOfmeasureIRI)));
	}

	@Test
	public void testTemporalEntitySubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(ztimeInstant);
		subClassList.add(zTimeIntevral);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), temporalEntityIRI);
		}

	}

	@Test
	public void testTimeInstantSubClasses() {
		assertEquals(zProperIntevral.getSuperZClassList().size(), 1);
		assertEquals(zProperIntevral.getSuperZClassList().get(0).getIri(), timeIntervalIRI);
	}

	@Test
	public void testTimeIntervalSubClasses() {
		assertEquals(zDateTimeIntevral.getSuperZClassList().size(), 1);
		assertEquals(zDateTimeIntevral.getSuperZClassList().get(0).getIri(), properIntervalIRI);
	}

	@Test
	public void testTemporalPositonSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zGDTDescription);
		subClassList.add(zTimePosition);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), temporalpositionIRI);
		}
	}

	@Test
	public void testdateTimeDescriptionSubClasses() {
		assertEquals(zDateTimeDescription.getSuperZClassList().size(), 1);
		assertEquals(zDateTimeDescription.getSuperZClassList().get(0).getIri(), gDateTimeDescriptionIRI);
	}

	@Test
	public void testDateTimeDescriptionSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zmonthOfTheYear);
		subClassList.add(zJanuary);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), dateTimeDescriptionIRI);
		}

	}

	@Test
	public void testUnitOfMeasureSubClasses() {
		List<ZClass> subClassList = new ArrayList<ZClass>();
		subClassList.add(zCurrency);
		subClassList.add(zEnergyUnit);
		subClassList.add(zIlluminanceUnit);
		subClassList.add(zPowerUnit);
		subClassList.add(zPressureUnit);
		for (int i = 0; i < subClassList.size(); i++) {
			assertEquals(subClassList.get(i).getSuperZClassList().get(0).getIri(), unitOfmeasureIRI);
		}
	}

	/*
	 * testing all Properties of the classes
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
	public void testCommandProperties() {
		List<ZDataProperty> dataProperList = zcommand.getZDataPropertyList();
		assertEquals(dataProperList.size(), 1);
		List<ZDataProperty> FilterByIRI = findZDataPropertyItemByIRI(hasDescriptionIRI, dataProperList);
		assertEquals(FilterByIRI.size(), 1);
		assertEquals(((ZDataProperty) FilterByIRI.get(0)).getRangeXSDType(), "string");

		List<ZObjectProperty> isCommand = findObjectPropertyByIRI(isCommandOfIRI, zcommand.getZObjectPropertyList());
		assertEquals(isCommand.size(), 1);
		assertEquals(isCommand.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), functionIRI);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zcommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), stateIRI);

	}

	@Test
	public void testGettingDataPropertyByIRI() {
		List<ZDataProperty> dataProperList = zcommand.getZDataPropertyItem(hasDescriptionIRI.getIRIString());
		assertEquals(dataProperList.size(), 1);
		List<ZDataProperty> FilterByIRI = findZDataPropertyItemByIRI(hasDescriptionIRI, dataProperList);
		assertEquals(FilterByIRI.size(), 1);
		assertEquals(((ZDataProperty) FilterByIRI.get(0)).getRangeXSDType(), "string");

		List<ZObjectProperty> isCommand = findObjectPropertyByIRI(isCommandOfIRI, zcommand.getZObjectPropertyList());
		assertEquals(isCommand.size(), 1);
		assertEquals(isCommand.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), functionIRI);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zcommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), stateIRI);

	}

	@Test
	public void testCloseCommandProperties() {
		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zCloseCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), openCloseStateIRI);

		List<ZInstance> ZinstanceList = zCloseCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> Close = findZinstanceItemByIRI(closeIRI, ZinstanceList);
		assertEquals(Close.size(), 1);
	}

	@Test
	public void testGetCurrentMeterValueCommandProperties() {
		List<ZInstance> ZinstanceList = zGetCurrentMeterValueCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> GCMValue = findZinstanceItemByIRI(getCurrentMeterValueIRI, ZinstanceList);
		assertEquals(GCMValue.size(), 1);

	}

	@Test
	public void testGetMeterDataCommandProperties() {
		List<ZInstance> ZinstanceList = zGetMeterDataCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> getMeterData = findZinstanceItemByIRI(getMeterDataIRI, ZinstanceList);
		assertEquals(getMeterData.size(), 1);

	}

	@Test
	public void testGetMeterHistoryCommandProperties() {
		List<ZInstance> ZinstanceList = zGetMeterHistoryCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> getMeterHistory = findZinstanceItemByIRI(getMeterHistoryIRI, ZinstanceList);
		assertEquals(getMeterHistory.size(), 1);

	}

	@Test
	public void testGetSensingDataCommandProperties() {
		List<ZInstance> ZinstanceList = zGGetSensingDataCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> getSensingData = findZinstanceItemByIRI(getSensingDataIRI, ZinstanceList);
		assertEquals(getSensingData.size(), 1);

	}

	@Test
	public void testNotifyCommandProperties() {
		List<ZInstance> ZinstanceList = znotifyCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> notify = findZinstanceItemByIRI(notifyIRI, ZinstanceList);
		assertEquals(notify.size(), 1);

	}

	@Test
	public void testOffCommandProperties() {
		List<ZInstance> ZinstanceList = zOffCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> Off = findZinstanceItemByIRI(offIRI, ZinstanceList);
		assertEquals(Off.size(), 1);
		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zOffCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), onOffStateIRI);
	}

	@Test
	public void testOnCommandProperties() {
		List<ZInstance> ZinstanceList = zonCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> On = findZinstanceItemByIRI(onIRI, ZinstanceList);
		assertEquals(On.size(), 1);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zonCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), onOffStateIRI);
	}

	@Test
	public void testOpenCommandProperties() {
		List<ZInstance> ZinstanceList = zopenCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> Open = findZinstanceItemByIRI(openIRI, ZinstanceList);
		assertEquals(Open.size(), 1);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zopenCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), openCloseStateIRI);
	}

	@Test
	public void testPauseCommandProperties() {
		List<ZInstance> ZinstanceList = zpauseCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> setAbsoluteLevel = findZinstanceItemByIRI(pauseIRI, ZinstanceList);
		assertEquals(setAbsoluteLevel.size(), 1);
	}

	@Test
	public void testSetAbsoluteLevelCommandProperties() {
		List<ZInstance> ZinstanceList = zSetAbsoluteLevelCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> setAbsoluteLevel = findZinstanceItemByIRI(setAbsoluteLevelIRI, ZinstanceList);
		assertEquals(setAbsoluteLevel.size(), 1);
	}

	@Test
	public void testSetRelativeLevelCommandProperties() {
		List<ZInstance> ZinstanceList = zSetRelativeLevelCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> setRelativeLevel = findZinstanceItemByIRI(setRelativeLevelIRI, ZinstanceList);
		assertEquals(setRelativeLevel.size(), 1);
	}

	@Test
	public void testStartCommandProperties() {
		List<ZInstance> ZinstanceList = zstartCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> start = findZinstanceItemByIRI(startIRI, ZinstanceList);
		assertEquals(start.size(), 1);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zstartCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sssIRI);
	}

	@Test
	public void testStepDownCommandProperties() {
		List<ZInstance> ZinstanceList = zstepDownCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> stepDown = findZinstanceItemByIRI(stepDownIRI, ZinstanceList);
		assertEquals(stepDown.size(), 1);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI,
				zstepDownCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), multilevelStateIRI);
	}

	@Test
	public void testStepUpCommandProperties() {
		List<ZInstance> ZinstanceList = zstepUpCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> stepUp = findZinstanceItemByIRI(stepUpIRI, ZinstanceList);
		assertEquals(stepUp.size(), 1);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI,
				zstepDownCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), multilevelStateIRI);
	}

	@Test
	public void testStopCommandProperties() {
		List<ZInstance> ZinstanceList = zstopCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> stepUp = findZinstanceItemByIRI(stopIRI, ZinstanceList);
		assertEquals(stepUp.size(), 1);

		List<ZObjectProperty> actsUpon = findObjectPropertyByIRI(actsUponIRI, zstopCommand.getZObjectPropertyList());
		assertEquals(actsUpon.size(), 1);
		assertEquals(actsUpon.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sssIRI);
	}

	@Test
	public void testToggleCommandProperties() {
		List<ZInstance> ZinstanceList = ztoggleCommand.getListZInstanceIRI();
		assertEquals(ZinstanceList.size(), 1);
		List<ZInstance> toggle = findZinstanceItemByIRI(toggleIRI, ZinstanceList);
		assertEquals(toggle.size(), 1);
	}

	@Test
	public void testDeviceProperties() {
		List<ZObjectProperty> objectProperList = zDevice.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 11);

		List<ZObjectProperty> consistsOf = findObjectPropertyByIRI(consistsOfIRI, zDevice.getZObjectPropertyList());
		assertEquals(consistsOf.size(), 1);
		assertEquals(consistsOf.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), deviceIRI);

		List<ZObjectProperty> controlsProperty = findObjectPropertyByIRI(controlsPropertyIRI,
				zDevice.getZObjectPropertyList());
		assertEquals(controlsProperty.size(), 1);
		assertEquals(controlsProperty.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), propertyIRI);

		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI, zDevice.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 1);
		assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), functionIRI);

		List<ZObjectProperty> hasProfile = findObjectPropertyByIRI(hasProfileIRI, zDevice.getZObjectPropertyList());
		assertEquals(hasProfile.size(), 1);
		assertEquals(hasProfile.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), profileIRI);

		List<ZObjectProperty> hasState = findObjectPropertyByIRI(hasStateIRI, zDevice.getZObjectPropertyList());
		assertEquals(hasState.size(), 1);
		assertEquals(hasState.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), stateIRI);

		List<ZObjectProperty> isUsedFor = findObjectPropertyByIRI(isusedForIRI, zDevice.getZObjectPropertyList());
		assertEquals(isUsedFor.size(), 1);
		assertEquals(isUsedFor.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), commodityIRI);

		List<ZObjectProperty> makesMeasurement = findObjectPropertyByIRI(makesMeasurementIRI,
				zDevice.getZObjectPropertyList());
		assertEquals(makesMeasurement.size(), 1);
		assertEquals(makesMeasurement.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				measurementIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI, zDevice.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), taskIRI);

		List<ZObjectProperty> offers = findObjectPropertyByIRI(offersIRI, zDevice.getZObjectPropertyList());
		assertEquals(offers.size(), 1);
		assertEquals(offers.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), serviceIRI);

		List<ZDataProperty> dataProperList = zDevice.getZDataPropertyList();
		assertEquals(dataProperList.size(), 3);
		List<ZDataProperty> FilterByIRI = findZDataPropertyItemByIRI(hasDescriptionIRI, dataProperList);
		assertEquals(FilterByIRI.size(), 1);
		assertEquals(((ZDataProperty) FilterByIRI.get(0)).getRangeXSDType(), "string");
		List<ZDataProperty> hasManufacturer = findZDataPropertyItemByIRI(hasManufacturerIRI, dataProperList);
		assertEquals(hasManufacturer.size(), 1);
		assertEquals(((ZDataProperty) hasManufacturer.get(0)).getRangeXSDType(), "string");

		List<ZDataProperty> hasModel = findZDataPropertyItemByIRI(hasModelIRI, dataProperList);
		assertEquals(hasModel.size(), 1);
		assertEquals(((ZDataProperty) hasModel.get(0)).getRangeXSDType(), "string");

	}

	@Test
	public void testWashingMachineProperties() {
		List<ZObjectProperty> objectProperList = zwashingMachine.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 4);
		List<ZObjectProperty> hasfunction = findObjectPropertyByIRI(hasFunctionIRI,
				zwashingMachine.getZObjectPropertyList());
		assertEquals(hasfunction.size(), 1);
		assertEquals(hasfunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				startStopFunctionIRI);
		List<ZObjectProperty> hasprofile = findObjectPropertyByIRI(hasProfileIRI,
				zwashingMachine.getZObjectPropertyList());
		assertEquals(hasprofile.size(), 1);
		assertEquals(hasprofile.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), profileIRI);

		List<ZObjectProperty> hasState = findObjectPropertyByIRI(hasStateIRI, zwashingMachine.getZObjectPropertyList());
		assertEquals(hasState.size(), 1);
		assertEquals(hasState.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sssIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zwashingMachine.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), washingIRI);
	}

	@Test
	public void testActuatorProperties() {
		List<ZObjectProperty> objectProperList = zActuator.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 1);
		List<ZObjectProperty> hasfunction = findObjectPropertyByIRI(hasFunctionIRI, zActuator.getZObjectPropertyList());
		assertEquals(hasfunction.size(), 1);
		assertEquals(hasfunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				actuatingFunctionIRI);
	}

	@Test
	public void testDoorSwitchProperties() {
		List<ZObjectProperty> objectProperList = zdoorswitch.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 4);

		List<ZObjectProperty> consistsOf = findObjectPropertyByIRI(consistsOfIRI, zdoorswitch.getZObjectPropertyList());
		assertEquals(consistsOf.size(), 1);
		assertEquals(consistsOf.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), switchIRI);

		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI,
				zdoorswitch.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 1);
		assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				openCloseFunctionIRI);

		List<ZObjectProperty> hasState = findObjectPropertyByIRI(hasStateIRI, zdoorswitch.getZObjectPropertyList());
		assertEquals(hasState.size(), 1);
		assertEquals(hasState.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), openCloseStateIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zdoorswitch.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), safetyIRI);
	}

	@Test
	public void testlightSwitchProperties() {
		List<ZObjectProperty> objectProperList = zligthswitch.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 6);

		List<ZObjectProperty> consistsOf = findObjectPropertyByIRI(consistsOfIRI,
				zligthswitch.getZObjectPropertyList());
		assertEquals(consistsOf.size(), 1);
		assertEquals(consistsOf.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), switchIRI);

		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI,
				zligthswitch.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 1);
		assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), onOffFunctionIRI);

		List<ZObjectProperty> hasState = findObjectPropertyByIRI(hasStateIRI, zligthswitch.getZObjectPropertyList());
		assertEquals(hasState.size(), 1);
		assertEquals(hasState.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), onOffStateIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zligthswitch.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), lightingIRI);

		List<ZObjectProperty> measuresProperty = findObjectPropertyByIRI(measuresPropertyIRI,
				zligthswitch.getZObjectPropertyList());
		assertEquals(measuresProperty.size(), 1);
		assertEquals(measuresProperty.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), lightIRI);

		List<ZObjectProperty> offers = findObjectPropertyByIRI(offersIRI, zligthswitch.getZObjectPropertyList());
		assertEquals(offers.size(), 1);
		assertEquals(offers.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), switchOnServiceIRI);
	}

	@Test
	public void testHVACProperties() {
		List<ZObjectProperty> objectProperList = zhvac.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 1);
		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI, zhvac.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), comfortIRI);
	}

	@Test
	public void testLightingDeviceProperties() {
		List<ZObjectProperty> objectProperList = zlightingDevice.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 1);
		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zlightingDevice.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), comfortIRI);
	}

	@Test
	public void testMeterProperties() {
		List<ZObjectProperty> objectProperList = zmeter.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 1);
		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI, zmeter.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 1);
		assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				meteringFunctionIRI);
	}

	@Test
	public void testEnergyMeterProperties() {
		List<ZObjectProperty> objectProperList = zenergyMeter.getZObjectPropertyList();
		assertEquals(objectProperList.size(), 4);

		List<ZObjectProperty> consistsOf = findObjectPropertyByIRI(consistsOfIRI,
				zenergyMeter.getZObjectPropertyList());
		assertEquals(consistsOf.size(), 1);
		assertEquals(consistsOf.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), meterIRI);

		List<ZObjectProperty> measuresProperty = findObjectPropertyByIRI(measuresPropertyIRI,
				zenergyMeter.getZObjectPropertyList());
		assertEquals(measuresProperty.size(), 1);
		assertEquals(measuresProperty.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), energyIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zenergyMeter.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 2);

		if (accomplishes.get(1).getRangeListZInstances().iterator().next().getIri().equals(meterReadingIRI)) {
			assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), energyEfficiencyIRI);

		} else {
			if (accomplishes.get(0).getRangeListZInstances().iterator().next().getIri().equals(energyEfficiencyIRI)) {
				assertEquals(accomplishes.get(1).getRangeListZClasses().iterator().next().getKey().getIri(),
						meterReadingIRI);
			}
		}

	}

	@Test
	public void testMicroRenewableProperties() {
		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zmicroRenewable.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), energyEfficiencyIRI);
	}

	@Test
	public void testmultimediaProperties() {
		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zmultimedia.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), entertainmentIRI);
	}

	@Test
	public void testSensorProperties() {
		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI, zsensor.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 1);
		assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sensingFunctionIRI);
	}

	@Test
	public void testSmokeSensorProperties() {
		List<ZObjectProperty> consistsOf = findObjectPropertyByIRI(consistsOfIRI,
				zsmokesensor.getZObjectPropertyList());
		assertEquals(consistsOf.size(), 1);
		assertEquals(consistsOf.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sensorkIRI);

		List<ZObjectProperty> measuresproperty = findObjectPropertyByIRI(measuresPropertyIRI,
				zsmokesensor.getZObjectPropertyList());
		assertEquals(measuresproperty.size(), 1);
		assertEquals(measuresproperty.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), smokeIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				zsmokesensor.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), safetyIRI);

		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI,
				zsmokesensor.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 2);

		if (hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri().equals(eventFunctionIRI)) {
			assertEquals(hasFunction.get(1).getRangeListZClasses().iterator().next().getKey().getIri(), smokeSensorIRI);
		} else {
			if (hasFunction.get(1).getRangeListZClasses().iterator().next().getKey().getIri()
					.equals(sensingFunctionIRI)) {
				assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
						smokeSensorIRI);
			}
		}

	}

	@Test
	public void testTemperatureSensorProperties() {
		List<ZObjectProperty> consistsOf = findObjectPropertyByIRI(consistsOfIRI,
				ztemperaturesensor.getZObjectPropertyList());
		assertEquals(consistsOf.size(), 1);
		assertEquals(consistsOf.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sensorkIRI);

		List<ZObjectProperty> hasFunction = findObjectPropertyByIRI(hasFunctionIRI,
				ztemperaturesensor.getZObjectPropertyList());
		assertEquals(hasFunction.size(), 1);
		assertEquals(hasFunction.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), sensingFunctionIRI);

		List<ZObjectProperty> measuresproperty = findObjectPropertyByIRI(measuresPropertyIRI,
				ztemperaturesensor.getZObjectPropertyList());
		assertEquals(measuresproperty.size(), 1);
		assertEquals(measuresproperty.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				temperatureIRI);

		List<ZObjectProperty> accomplishes = findObjectPropertyByIRI(accomplishesIRI,
				ztemperaturesensor.getZObjectPropertyList());
		assertEquals(accomplishes.size(), 1);
		assertEquals(accomplishes.get(0).getRangeListZInstances().iterator().next().getIri(), comfortIRI);
	}

	@Test
	public void testFunctionProperties() {
		List<ZObjectProperty> hasCommand = findObjectPropertyByIRI(hasCommandIRI, zfunction.getZObjectPropertyList());
		assertEquals(hasCommand.size(), 1);
		assertEquals(hasCommand.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), commandIRI);
	}

	@Test
	public void testEventFunctionProperties() {
		String comment = "A function that allows to notify another device that a certain threshold value has been exceeded.";
		List<ZObjectProperty> hasCommand = findObjectPropertyByIRI(hasCommandIRI,
				zeventfunction.getZObjectPropertyList());
		assertEquals(hasCommand.size(), 1);
		assertEquals(hasCommand.get(0).getRangeListZClasses().iterator().next().getKey().getIri(), notifyCommandIRI);
		assertTrue(zeventfunction.getComments().equals(comment));

		List<ZObjectProperty> hasthresholdmeasurement = findObjectPropertyByIRI(hasThresholdMeasurementIRI,
				zeventfunction.getZObjectPropertyList());
		assertEquals(hasthresholdmeasurement.size(), 1);
		assertEquals(hasthresholdmeasurement.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
				measurementIRI);

	}

	@Test

	public void testallObjectProperties() {

		List<ZObjectProperty> hasTypicalConsumption = findObjectPropertyByIRI(hasTypicalConsumptionIRI,
				zDevice.getZObjectPropertyList());
		assertEquals(hasTypicalConsumption.size(), 1);
		if (hasTypicalConsumption.get(0).getRangeListZClasses().iterator().next().getKey().getIri().equals(energyIRI)) {
			assertEquals(hasTypicalConsumption.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
					energyIRI);
		} else {

			assertEquals(hasTypicalConsumption.get(0).getRangeListZClasses().iterator().next().getKey().getIri(),
					powerIRI);

		}

	}
}
