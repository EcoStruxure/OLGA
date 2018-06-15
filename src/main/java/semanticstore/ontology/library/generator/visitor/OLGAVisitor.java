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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnnotationValueVisitor;
import org.semanticweb.owlapi.model.OWLAnnotationValueVisitorEx;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualVisitor;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.search.EntitySearcher;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.CONFIG;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.global.UTILS;
import semanticstore.ontology.library.generator.model.ZClass;
import semanticstore.ontology.library.generator.model.ZDataProperty;
import semanticstore.ontology.library.generator.model.ZInstance;
import semanticstore.ontology.library.generator.model.ZObjectProperty;
import semanticstore.ontology.library.generator.model.ZPair;
import semanticstore.ontology.library.generator.model.xsd.XsdTypeMapper;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectUnionOfImpl;

public class OLGAVisitor implements OWLObjectVisitor, OWLIndividualVisitor, OWLAnnotationValue {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  final static Logger log = Logger.getLogger(OLGAVisitor.class);
  private OWLOntology ontology;
  private Map<IRI, ZClass> mapIRI_to_Zclass;
  private Map<IRI, ZInstance> mapIRI_to_Zinstance;
  private Map<ZPair<IRI, IRI>, List<ZObjectProperty>> mapPairOfZClassestoZobjects;
  private ZClass zclass;
  private ZInstance zInstance;
  private ZObjectProperty zObjectProperty;
  private ZDataProperty zDataProperty;
  private Stack<IRI> iriList;
  private CODE selectedCode;
  private LIBRARY selectedLibrary;
  private boolean skipInverseRelations;
  private String comment;
  private String label;

  public OLGAVisitor(OWLOntology merged, CODE code, LIBRARY library) {
    this.ontology = merged;
    this.selectedCode = code;
    this.selectedLibrary = library;
    this.iriList = new Stack<IRI>();
    this.skipInverseRelations = false;
  }

  public Map<IRI, ZClass> getMapIRI_to_Zclass() {
    return mapIRI_to_Zclass;
  }

  public Map<IRI, ZInstance> getMapIRI_to_ZIndidivual() {
    return mapIRI_to_Zinstance;
  }

  // Start to visit the ontology, all the classes
  public void visit() throws UnsupportedOperationException, ClassCastException,
      NullPointerException, IllegalArgumentException, InvalidClassNameException {
    try {
      mapIRI_to_Zclass = new LinkedHashMap<>();
      mapIRI_to_Zinstance = new LinkedHashMap<>();
      ontology.classesInSignature().forEach(action -> action.accept(this));
      visitLonleyObjectProperties();
      if (skipInverseRelations) {
        disableInverseProperties();
      }
      // visitLonleyDataProperties();
    } catch (UnsupportedOperationException | ClassCastException | NullPointerException
        | IllegalArgumentException e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  @Override
  public void visit(OWLClass owlClass) throws InvalidClassNameException {
    if (!owlClass.getIRI().getShortForm().equals("Thing")
        && !mapIRI_to_Zclass.containsKey(owlClass.getIRI())) {
      try {
        UTILS.validateClassName(owlClass.getIRI().getShortForm());
      } catch (InvalidClassNameException e) {
        log.error(e);
        throw e;
      }
      zclass = new ZClass(owlClass.getIRI());
      // owlClass.annotations().forEach(action -> System.out.println(action));
      if (log.isDebugEnabled()) {
        log.debug("iN Class " + owlClass);
      }

      if (zclass.getIri().toString().startsWith(CONFIG.RDFS)) {
        zclass.setGenerate(false);
      }
      mapIRI_to_Zclass.put(zclass.getIri(), zclass); // Add the class in the map

      // Retrieve comments and labels
      zclass.setLabel(getLabel(owlClass));
      zclass.setComments(getComment(owlClass));
      ontology.subClassAxiomsForSubClass(owlClass).forEach(action -> action.accept(this));
      EntitySearcher.getIndividuals(owlClass, ontology).forEach(action -> action.accept(this)); // Generate
                                                                                                // list
                                                                                                // of
                                                                                                // instances

      // mapIRI_to_Zclass.replace(zclass.getIri(), zclass); //Update the class in the
      // map
    }
  }

  @Override
  public void visit(OWLSubClassOfAxiom axiom) throws InvalidClassNameException {
    OWLObjectVisitor.super.visit(axiom);
    OWLClassExpression superClass = axiom.getSuperClass();
    // Check if the axiom is a class or a data/object property
    if (superClass.isOWLClass()
        && !(((OWLClass) superClass).getIRI().getShortForm().equals("Thing"))) {
      try {
        UTILS.validateClassName(superClass.asOWLClass().getIRI().getShortForm());
      } catch (InvalidClassNameException e) {
        log.error(e);
        throw e;
      }

      OWLClass currentOWLSuperClass = superClass.asOWLClass();
      ZClass currentZClass = mapIRI_to_Zclass.get(currentOWLSuperClass.getIRI());
      zclass.add_listOfImports(currentOWLSuperClass.getIRI());
      if (currentZClass != null) {
        zclass.add_ParentClass(currentZClass);
      } else {

        // Save the child class

        // mapIRI_to_Zclass.replace(zclass.getIri(), zclass);
        iriList.push(zclass.getIri());

        ZClass zclass_Parent = new ZClass(currentOWLSuperClass.getIRI());
        zclass = zclass_Parent;
        if (zclass.getIri().toString().startsWith(CONFIG.RDFS)) {
          zclass.setGenerate(false);
        }

        mapIRI_to_Zclass.put(zclass.getIri(), zclass);

        zclass.setLabel(getLabel(currentOWLSuperClass));
        zclass.setComments(getComment(currentOWLSuperClass));
        ontology.subClassAxiomsForSubClass(currentOWLSuperClass)
            .forEach(action -> action.accept(this));
        EntitySearcher.getIndividuals(currentOWLSuperClass, ontology)
            .forEach(action -> action.accept(this)); // Generate
                                                     // list
                                                     // of
                                                     // instances

        mapIRI_to_Zclass.put(zclass.getIri(), zclass);
        zclass_Parent = zclass;
        // Reload child class
        if (!iriList.isEmpty()) {
          zclass = mapIRI_to_Zclass.get(iriList.pop());
          zclass.add_ParentClass(zclass_Parent);
        }
      }
    } else {
      superClass.accept(this);
    }
  }

  @Override
  public void visit(OWLObjectPropertyAssertionAxiom axiom) {
    OWLObjectVisitor.super.visit(axiom);
  }

  @Override
  public void visit(OWLDataAllValuesFrom ce) throws IllegalArgumentException {
    OWLObjectVisitor.super.visit(ce);

    if (log.isDebugEnabled()) {
      log.debug("DataAllValuesFrom: " + ce.toString());
    }

    ce.dataPropertiesInSignature().forEach(action -> action.accept(this));
    ce.datatypesInSignature().forEach(action -> {
      if (log.isDebugEnabled()) {
        log.debug(" -Range " + action.toString());
      }

      if (action.toString() != null && !action.toString().isEmpty()) {
        ZPair<String, String> dataProperty =
            XsdTypeMapper.getClassName(action.toString(), selectedCode, selectedLibrary);
        zDataProperty.setRangeXSDType(dataProperty.getKey());
        zDataProperty.setPackageName(dataProperty.getValue());
      }
      // if the range is not typed put it as a string by default
      else {
        ZPair<String, String> dataProperty =
            XsdTypeMapper.getClassName("xsd:string", selectedCode, selectedLibrary);
        zDataProperty.setRangeXSDType(dataProperty.getKey());
        zDataProperty.setPackageName(dataProperty.getValue());
      }
    });
    zDataProperty.setRestrictionType("Only");
    zclass.add_DataProperty(zDataProperty);
  }

  @Override
  public void visit(OWLDataSomeValuesFrom ce) {
    OWLObjectVisitor.super.visit(ce);
    if (log.isDebugEnabled()) {
      log.debug("DataSomeValuesFrom: " + ce.toString());
    }
    ce.dataPropertiesInSignature().forEach(action -> action.accept(this));
    ce.datatypesInSignature().forEach(action -> {
      if (log.isDebugEnabled()) {
        log.debug(" -Range " + action.toString());
      }

      if (action.toString() != null && !action.toString().isEmpty()) {
        ZPair<String, String> dataProperty =
            XsdTypeMapper.getClassName(action.toString(), selectedCode, selectedLibrary);
        zDataProperty.setRangeXSDType(dataProperty.getKey());
        zDataProperty.setPackageName(dataProperty.getValue());
      }
      // if the range is not typed put it as a string by default
      else {
        ZPair<String, String> dataProperty =
            XsdTypeMapper.getClassName("xsd:string", selectedCode, selectedLibrary);
        zDataProperty.setRangeXSDType(dataProperty.getKey());
        zDataProperty.setPackageName(dataProperty.getValue());
      }
    });
    zDataProperty.setRestrictionType("Some");
    zclass.add_DataProperty(zDataProperty);
  }

  @Override
  public void visit(OWLDataExactCardinality ce) {
    OWLObjectVisitor.super.visit(ce);
    if (log.isDebugEnabled()) {
      log.debug("DataExactCardinality: " + ce.toString());
    }
    if (ce.getCardinality() != 0) {
      ce.dataPropertiesInSignature().forEach(action -> action.accept(this));
      ce.datatypesInSignature().forEach(action -> {
        if (log.isDebugEnabled()) {
          log.debug(" -Range " + action.toString());
        }
        if (action.toString() != null && !action.toString().isEmpty()) {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName(action.toString(), selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        }
        // if the range is not typed put it as a string by default
        else {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName("xsd:string", selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        }
      });
      zDataProperty.setRestrictionType("Exactly");
      zclass.add_DataProperty(zDataProperty);
    }
  }

  @Override
  public void visit(OWLDataMaxCardinality ce) {
    OWLObjectVisitor.super.visit(ce);
    if (ce.getCardinality() != 0) {
      if (log.isDebugEnabled()) {
        log.debug("DataMaxValuesFrom: " + ce.toString());
      }
      ce.dataPropertiesInSignature().forEach(action -> action.accept(this));
      ce.datatypesInSignature().forEach(action -> {
        if (log.isDebugEnabled()) {
          log.debug(" -Range " + action.toString());
        }
        if (action.toString() != null && !action.toString().isEmpty()) {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName(action.toString(), selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        }
        // if the range is not typed put it as a string by default
        else {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName("xsd:string", selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        }
      });
      zDataProperty.setRestrictionType("Max");
      zclass.add_DataProperty(zDataProperty);
    }
  }

  @Override
  public void visit(OWLDataMinCardinality ce) {
    OWLObjectVisitor.super.visit(ce);
    if (ce.getCardinality() != 0) {
      if (log.isDebugEnabled()) {
        log.debug("DataMinValuesFrom: " + ce.toString());
      }
      ce.dataPropertiesInSignature().forEach(action -> action.accept(this));
      ce.datatypesInSignature().forEach(action -> {
        if (log.isDebugEnabled()) {
          log.debug(" -Range " + action.toString());
        }
        if (action.toString() != null && !action.toString().isEmpty()) {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName(action.toString(), selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        }
        // if the range is not typed put it as a string by default
        else {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName("xsd:string", selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        }
      });
      zDataProperty.setRestrictionType("Min");
      zclass.add_DataProperty(zDataProperty);
    }
  }

  @Override
  public void visit(OWLDataProperty property) throws InvalidClassNameException {

    try {
      UTILS.validateClassName(property.getIRI().getShortForm());
    } catch (InvalidClassNameException e) {
      log.error(e);
      throw e;
    }

    OWLObjectVisitor.super.visit(property);
    zDataProperty = new ZDataProperty(property.getIRI());
    zDataProperty.setLabel(getLabel(property));
    zDataProperty.setComments(getComment(property));
    if (log.isDebugEnabled()) {
      log.debug(" -Data " + property.toStringID());
    }
  }

  @Override
  public void visit(OWLObjectHasValue individual) throws InvalidClassNameException {
    OWLObjectVisitor.super.visit(individual);
    individual.objectPropertiesInSignature().forEach(action -> action.accept(this));
    individual.individualsInSignature().forEach(currentClass -> {

      try {
        UTILS.validateClassName(currentClass.getIRI().getShortForm());
      } catch (InvalidClassNameException e) {
        log.error(e);
        throw e;
      }

      ZInstance zInstance = new ZInstance(currentClass.getIRI());
      zInstance.setzClassInstanciated(zclass.getIri());
      zclass
          .add_importsDeclarationsZInstance(importDeclarationFromIriZInstance(zInstance.getIri()));
      zObjectProperty.add_RangeZInstance(zInstance);
    });
  }

  @Override
  public void visit(OWLNamedIndividual individual)
      throws NullPointerException, ClassCastException, InvalidClassNameException {

    try {
      UTILS.validateClassName(individual.getIRI().getShortForm());
    } catch (InvalidClassNameException e) {
      log.error(e);
      throw e;
    }

    OWLObjectVisitor.super.visit(individual);
    zInstance = mapIRI_to_Zinstance.get(individual.getIRI());
    if (zInstance == null) {
      zInstance = new ZInstance(individual.getIRI());
    }
    zInstance.setzClassInstanciated(zclass.getIri());
    zInstance.setLabel(getLabel(individual));
    zInstance.setComments(getComment(individual));

    ontology.dataPropertyAssertionAxioms(individual).forEach(action -> {
      action.dataPropertiesInSignature()
          .forEach(dataprop -> zDataProperty = new ZDataProperty(dataprop.getIRI()));
      if (action.toString() != null && !action.toString().isEmpty()) {
        action.datatypesInSignature().forEach(datatype -> {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName(datatype.toString(), selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        });
      }
      // if the range is not typed put it as a string by default
      else
        action.datatypesInSignature().forEach(datatype -> {
          ZPair<String, String> dataProperty =
              XsdTypeMapper.getClassName("xsd:string", selectedCode, selectedLibrary);
          zDataProperty.setRangeXSDType(dataProperty.getKey());
          zDataProperty.setPackageName(dataProperty.getValue());
        });
      zDataProperty.setValue(action.getObject().getLiteral());
      zInstance.add_DataProperty(zDataProperty);
    });

    // handle the object properties
    ontology.objectPropertyAssertionAxioms(individual).forEach(objectProperty -> {
      OWLObjectPropertyExpression property = objectProperty.getProperty();
      OWLIndividual individualOfCurrentObjectProp = objectProperty.getObject();
      if (property != null && individualOfCurrentObjectProp != null) {
        zObjectProperty =
            new ZObjectProperty(objectProperty.getProperty().signature().findAny().get().getIRI());
        IRI iRI_IndividualCurrentOfObject =
            individualOfCurrentObjectProp.signature().findAny().get().getIRI();
        ZInstance zInstanceOfCurrentObjectProp =
            mapIRI_to_Zinstance.get(iRI_IndividualCurrentOfObject);
        if (zInstanceOfCurrentObjectProp == null) {
          zInstanceOfCurrentObjectProp = new ZInstance(iRI_IndividualCurrentOfObject);
          mapIRI_to_Zinstance.put(iRI_IndividualCurrentOfObject, zInstanceOfCurrentObjectProp);
        }



        zObjectProperty.add_RangeZInstance(zInstanceOfCurrentObjectProp);
      }
      // Check if the object property legally fit the instance or not.
      if (zclass.getZObjectPropertyList().stream().filter(p -> {
        return p.getObjectProperty().getFragment()
            .equals(zObjectProperty.getObjectProperty().getFragment());
      }).count() > 0)
        zInstance.add_ObjectProperty(zObjectProperty);
    });

    zclass.add_ZInstanceClass(zInstance);
    mapIRI_to_Zinstance.put(zInstance.getIri(), zInstance);
  }

  /*
   * @Override public void visit(OWLNamedIndividual individual) {
   * OWLObjectVisitor.super.visit(individual); ZInstance zInstance =
   * mapIRI_to_Zinstance.get(individual.getIRI()); if(zInstance == null) { zInstance = new
   * ZInstance(individual.getIRI()); } zInstance.zClassInstanciated = zclass.iri; zInstance.label =
   * getLabel(individual); zInstance.comments = getComment(individual);
   * 
   * //handle data properties ontology.dataPropertyAssertionAxioms(individual).forEach(action -> {
   * action.dataPropertiesInSignature().forEach(dataprop -> zDataProperty = new
   * ZDataProperty(dataprop.getIRI())); if(action.toString() != null &&
   * !action.toString().isEmpty()) { action.datatypesInSignature().forEach(datatype ->
   * zDataProperty.setRangeXSDType(XsdTypeMapper.getClassName(datatype.toString(), selectedCode,
   * selectedLibrary))); } // if the range is not typed put it as a string by default else
   * action.datatypesInSignature().forEach(datatype ->
   * zDataProperty.setRangeXSDType(XsdTypeMapper.getClassName("xsd:string", selectedCode,
   * selectedLibrary))); }); zInstance.add_DataProperty(zDataProperty);
   * 
   * //handle the object properties
   * ontology.objectPropertyAssertionAxioms(individual).forEach(objectProperty -> {
   * OWLObjectPropertyExpression property = objectProperty.getProperty(); OWLIndividual
   * individualOfCurrentObjectProp = objectProperty.getObject(); if(property != null &&
   * individualOfCurrentObjectProp != null) { zObjectProperty = new
   * ZObjectProperty(objectProperty.getProperty().signature().findAny().get(). getIRI()); IRI
   * iRI_IndividualCurrentOfObject =
   * individualOfCurrentObjectProp.signature().findAny().get().getIRI(); ZInstance
   * zInstanceOfCurrentObjectProp = mapIRI_to_Zinstance.get(iRI_IndividualCurrentOfObject);
   * if(zInstanceOfCurrentObjectProp == null) { zInstanceOfCurrentObjectProp = new
   * ZInstance(iRI_IndividualCurrentOfObject);
   * mapIRI_to_Zinstance.put(iRI_IndividualCurrentOfObject, zInstanceOfCurrentObjectProp); }
   * zObjectProperty.add_RangeZInstance(zInstanceOfCurrentObjectProp); } });
   * zInstance.add_ObjectProperty(zObjectProperty); zclass.add_ZInstanceClass(zInstance);
   * mapIRI_to_Zinstance.put(zInstance.getIri(), zInstance); }
   */

  @Override
  public void visit(OWLObjectProperty property) throws InvalidClassNameException {

    try {
      UTILS.validateClassName(property.getIRI().getShortForm());
    } catch (InvalidClassNameException e) {
      log.error(e);
      throw e;
    }

    zObjectProperty = new ZObjectProperty(property.getIRI());
    zObjectProperty.setLabel(getLabel(property));
    zObjectProperty.setComments(getComment(property));
    zObjectProperty
        .addAllInverseZObjectProperties(ontology.inverseObjectPropertyAxioms(property).map(m -> {
          IRI iri;
          if (m.getFirstProperty().asOWLObjectProperty().getIRI() == property.getIRI()) {
            iri = m.getSecondProperty().asOWLObjectProperty().getIRI();

          } else {
            iri = m.getFirstProperty().asOWLObjectProperty().getIRI();
          }
          return iri;
        }).collect(Collectors.toSet()));
    zclass.add_ObjectProperty(zObjectProperty);
  }

  @Override
  public void visit(OWLObjectInverseOf ce) {
    // System.out.println("OWLObjectOneOf: " + ce.toString());
  }

  @Override
  public void visit(OWLObjectSomeValuesFrom ce) throws NullPointerException, ClassCastException {
    OWLObjectVisitor.super.visit(ce);
    ce.objectPropertiesInSignature().forEach(action -> action.accept(this));
    ce.classesInSignature().forEach(currentClass -> handleObjectProp(currentClass));
    zObjectProperty.setObjectPropertyType("Some");
  }

  @Override
  public void visit(OWLObjectAllValuesFrom ce) {
    OWLObjectVisitor.super.visit(ce);
    ce.nestedClassExpressions().forEach(currentClass -> {
      if (currentClass.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
        ce.objectPropertiesInSignature().forEach(action -> action.accept(this));
        currentClass.accept(this);
      } else if (currentClass
          .getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
        ce.objectPropertiesInSignature().forEach(action -> action.accept(this));
        currentClass.accept(this);
      } else if (currentClass.getClassExpressionType() == ClassExpressionType.OBJECT_ALL_VALUES_FROM
          && !currentClass.toString().contains("ObjectUnionOf")) {
        ce.objectPropertiesInSignature().forEach(action -> action.accept(this));
        currentClass.classesInSignature().forEach(action -> handleObjectProp(action));
      }
    });
    zObjectProperty.setObjectPropertyType("Only");
  }

  @Override
  public void visit(OWLObjectUnionOf ce) throws NullPointerException, ClassCastException {
    OWLObjectVisitor.super.visit(ce);

    ce.classesInSignature().forEach(currentClass -> {
      ZClass currentZClass = mapIRI_to_Zclass.get(currentClass.getIRI());
      zclass.add_importsDeclarations(importDeclarationFromIri(currentClass.getIRI()),
          currentClass.getIRI());
      if (currentZClass != null) {
        zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
        validateInverseRelation(zclass, zObjectProperty, currentZClass);
        if (log.isDebugEnabled()) {
          log.debug(" -Range " + currentClass.toString());
        }
      } else {
        mapIRI_to_Zclass.replace(zclass.getIri(), zclass);
        iriList.push(zclass.getIri());

        currentClass.accept(this);
        currentZClass = mapIRI_to_Zclass.get(zclass.getIri());

        // Reload the former class
        if (!iriList.isEmpty()) {
          zclass = mapIRI_to_Zclass.get(iriList.pop());
          if (zclass.getZObjectPropertyList().size() > 0) {
            zObjectProperty =
                zclass.getZObjectPropertyList().get(zclass.getZObjectPropertyList().size() - 1);
            zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
            validateInverseRelation(zclass, zObjectProperty, currentZClass);
          }
        }
      }
    });
  }

  @Override
  public void visit(OWLObjectIntersectionOf ce) throws NullPointerException, ClassCastException {
    OWLObjectVisitor.super.visit(ce);

    ce.classesInSignature().forEach(currentClass -> {

      ZClass currentZClass = mapIRI_to_Zclass.get(currentClass.getIRI());
      zclass.add_importsDeclarations(importDeclarationFromIri(currentClass.getIRI()),
          currentClass.getIRI());

      if (currentZClass != null) {
        zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
        validateInverseRelation(zclass, zObjectProperty, currentZClass);
        if (log.isDebugEnabled()) {
          log.debug(" -Range " + currentClass.toString());
        }
      } else {
        mapIRI_to_Zclass.replace(zclass.getIri(), zclass);
        // IRI iriSave = zclass.iri;
        iriList.push(zclass.getIri());

        currentClass.accept(this);

        currentZClass = mapIRI_to_Zclass.get(zclass.getIri());
        // Reload the former class
        if (!iriList.isEmpty()) {
          zclass = mapIRI_to_Zclass.get(iriList.pop());
          zObjectProperty =
              zclass.getZObjectPropertyList().get(zclass.getZObjectPropertyList().size() - 1);
          zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
          validateInverseRelation(zclass, zObjectProperty, currentZClass);
        }
      }

    });
  }

  @Override
  public void visit(OWLObjectMinCardinality ce) throws NullPointerException, ClassCastException {
    OWLObjectVisitor.super.visit(ce);
    if (ce.getCardinality() != 0) {
      ce.objectPropertiesInSignature().forEach(action -> {
        action.accept(this);
      });
      ce.classesInSignature().forEach(currentClass -> {

        ZClass currentZClass = mapIRI_to_Zclass.get(currentClass.getIRI());
        zclass.add_importsDeclarations(importDeclarationFromIri(currentClass.getIRI()),
            currentClass.getIRI());
        if (currentZClass != null) {
          zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
          validateInverseRelation(zclass, zObjectProperty, currentZClass);
          if (log.isDebugEnabled()) {
            log.debug(" -Range " + currentClass.toString());
          }
        } else {
          mapIRI_to_Zclass.replace(zclass.getIri(), zclass);
          iriList.push(zclass.getIri());

          currentClass.accept(this);
          currentZClass = mapIRI_to_Zclass.get(zclass.getIri());
          // Reload the former class
          if (!iriList.isEmpty()) {
            zclass = mapIRI_to_Zclass.get(iriList.pop());
            zObjectProperty =
                zclass.getZObjectPropertyList().get(zclass.getZObjectPropertyList().size() - 1);
            zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
            validateInverseRelation(zclass, zObjectProperty, currentZClass);
          }
        }
      });
      zObjectProperty.setObjectPropertyCardinality(Integer.toString(ce.getCardinality()));
      zObjectProperty.setObjectPropertyType("Min");

    }
  }

  @Override
  public void visit(OWLObjectExactCardinality ce) throws NullPointerException, ClassCastException {
    OWLObjectVisitor.super.visit(ce);
    if (ce.getCardinality() != 0) {
      ce.objectPropertiesInSignature().forEach(action -> {
        action.accept(this);

      });
      ce.classesInSignature().forEach(currentClass -> {
        ZClass currentZClass = mapIRI_to_Zclass.get(currentClass.getIRI());
        zclass.add_importsDeclarations(importDeclarationFromIri(currentClass.getIRI()),
            currentClass.getIRI());
        if (currentZClass != null) {
          zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
          validateInverseRelation(zclass, zObjectProperty, currentZClass);
          if (log.isDebugEnabled()) {
            log.debug(" -Range " + currentClass.toString());
          }
        } else {
          mapIRI_to_Zclass.replace(zclass.getIri(), zclass);
          iriList.push(zclass.getIri());

          currentClass.accept(this);
          currentZClass = mapIRI_to_Zclass.get(zclass.getIri());
          // Reload the former class
          if (!iriList.isEmpty()) {
            zclass = mapIRI_to_Zclass.get(iriList.pop());
            zObjectProperty =
                zclass.getZObjectPropertyList().get(zclass.getZObjectPropertyList().size() - 1);
            zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
            validateInverseRelation(zclass, zObjectProperty, currentZClass);
          }
        }
      });
      zObjectProperty.setObjectPropertyCardinality(Integer.toString(ce.getCardinality()));
      zObjectProperty.setObjectPropertyType("Exactly");

    }
  }

  @Override
  public void visit(OWLObjectMaxCardinality ce) throws NullPointerException, ClassCastException {
    OWLObjectVisitor.super.visit(ce);
    if (ce.getCardinality() != 0) {
      ce.objectPropertiesInSignature().forEach(action -> {
        action.accept(this);

      });
      ce.classesInSignature().forEach(currentClass -> {
        ZClass currentZClass = mapIRI_to_Zclass.get(currentClass.getIRI());
        zclass.add_importsDeclarations(importDeclarationFromIri(currentClass.getIRI()),
            currentClass.getIRI());
        if (currentZClass != null) {
          zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
          validateInverseRelation(zclass, zObjectProperty, currentZClass);
          if (log.isDebugEnabled()) {
            log.debug(" -Range " + currentClass.toString());
          }
        } else {
          mapIRI_to_Zclass.replace(zclass.getIri(), zclass);
          iriList.push(zclass.getIri());

          currentClass.accept(this);
          currentZClass = mapIRI_to_Zclass.get(zclass.getIri());
          // Reload the former class
          if (!iriList.isEmpty()) {
            zclass = mapIRI_to_Zclass.get(iriList.pop());
            zObjectProperty =
                zclass.getZObjectPropertyList().get(zclass.getZObjectPropertyList().size() - 1);
            zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
            validateInverseRelation(zclass, zObjectProperty, currentZClass);
          }
        }
      });
      zObjectProperty.setObjectPropertyCardinality(Integer.toString(ce.getCardinality()));
      zObjectProperty.setObjectPropertyType("Max");
    }
  }

  @Override
  public void visit(OWLClassAssertionAxiom axiom) {
    OWLObjectVisitor.super.visit(axiom);
  }

  @Override
  public void visit(OWLObjectOneOf ce) {
    log.debug("OWLObjectOneOf: " + ce.toString());
    ce.objectPropertiesInSignature().forEach(action -> action.accept(this));
    ce.classesInSignature().forEach(action -> {
      if (log.isDebugEnabled()) {
        log.debug(" -Range " + action.toString());
      }
    });

  }

  @Override
  public void accept(OWLObjectVisitor arg0) {

  }

  @Override
  public <O> O accept(OWLObjectVisitorEx<O> arg0) {
    return null;
  }

  @Override
  public int typeIndex() {
    return 0;
  }

  @Override
  public int hashIndex() {
    return 0;
  }

  @Override
  public Stream<?> components() {
    return null;
  }

  @Override
  public void accept(OWLAnnotationValueVisitor arg0) {

  }

  @Override
  public <O> O accept(OWLAnnotationValueVisitorEx<O> arg0) {
    return null;
  }

  @Override
  public int initHashCode() {
    return 0;
  }

  @Override
  public int compareTo(OWLObject o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comment == null) ? 0 : comment.hashCode());
    result = prime * result + ((iriList == null) ? 0 : iriList.hashCode());
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + ((mapIRI_to_Zclass == null) ? 0 : mapIRI_to_Zclass.hashCode());
    result = prime * result + ((mapIRI_to_Zinstance == null) ? 0 : mapIRI_to_Zinstance.hashCode());
    result = prime * result
        + ((mapPairOfZClassestoZobjects == null) ? 0 : mapPairOfZClassestoZobjects.hashCode());
    result = prime * result + ((selectedCode == null) ? 0 : selectedCode.hashCode());
    result = prime * result + ((selectedLibrary == null) ? 0 : selectedLibrary.hashCode());
    result = prime * result + (skipInverseRelations ? 1231 : 1237);
    result = prime * result + ((zDataProperty == null) ? 0 : zDataProperty.hashCode());
    result = prime * result + ((zInstance == null) ? 0 : zInstance.hashCode());
    result = prime * result + ((zObjectProperty == null) ? 0 : zObjectProperty.hashCode());
    result = prime * result + ((zclass == null) ? 0 : zclass.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OLGAVisitor other = (OLGAVisitor) obj;
    if (comment == null) {
      if (other.comment != null)
        return false;
    } else if (!comment.equals(other.comment))
      return false;
    if (iriList == null) {
      if (other.iriList != null)
        return false;
    } else if (!iriList.equals(other.iriList))
      return false;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (mapIRI_to_Zclass == null) {
      if (other.mapIRI_to_Zclass != null)
        return false;
    } else if (!mapIRI_to_Zclass.equals(other.mapIRI_to_Zclass))
      return false;
    if (mapIRI_to_Zinstance == null) {
      if (other.mapIRI_to_Zinstance != null)
        return false;
    } else if (!mapIRI_to_Zinstance.equals(other.mapIRI_to_Zinstance))
      return false;
    if (mapPairOfZClassestoZobjects == null) {
      if (other.mapPairOfZClassestoZobjects != null)
        return false;
    } else if (!mapPairOfZClassestoZobjects.equals(other.mapPairOfZClassestoZobjects))
      return false;
    if (selectedCode != other.selectedCode)
      return false;
    if (selectedLibrary != other.selectedLibrary)
      return false;
    if (skipInverseRelations != other.skipInverseRelations)
      return false;
    if (zDataProperty == null) {
      if (other.zDataProperty != null)
        return false;
    } else if (!zDataProperty.equals(other.zDataProperty))
      return false;
    if (zInstance == null) {
      if (other.zInstance != null)
        return false;
    } else if (!zInstance.equals(other.zInstance))
      return false;
    if (zObjectProperty == null) {
      if (other.zObjectProperty != null)
        return false;
    } else if (!zObjectProperty.equals(other.zObjectProperty))
      return false;
    if (zclass == null) {
      if (other.zclass != null)
        return false;
    } else if (!zclass.equals(other.zclass))
      return false;
    return true;
  }

  /**
   * Visiting ObjectProperties defined apart and not as subclasses
   */
  private void visitLonleyObjectProperties() throws UnsupportedOperationException,
      ClassCastException, NullPointerException, IllegalArgumentException {
    ontology.objectPropertiesInSignature().forEach(objectProperty -> {
      if (log.isDebugEnabled()) {
        log.debug(" -Lonley Object Properties " + objectProperty.toString());
      }
      scanObjectPropertyDomainAndRange(objectProperty);
    });
  }

  /*
   * private List<String> getDataPropertyRange(OWLDataPropertyExpression dataPropertyExpression) {
   * List<ZClass> listOfRangeClasses = new ArrayList<ZClass>(); Stream<OWLDataPropertyRangeAxiom>
   * rangeStream = ontology.dataPropertyRangeAxioms(dataPropertyExpression.asOWLDataProperty());
   * Iterator<OWLDataPropertyRangeAxiom> itStream = rangeStream.iterator();
   * while(itStream.hasNext()) { OWLDataPropertyRangeAxiom owlRange = itStream.next(); if(owlRange
   * != null) { IRI classIriDomain = owlRange.getRange().asOWLDatatype(); ZClass zclass =
   * mapIRI_to_Zclass.get(classIriDomain); //it was never created, it must be visited if(zclass ==
   * null) { this.visit(owlRange.getRange().asOWLClass()); } zclass =
   * mapIRI_to_Zclass.get(classIriDomain); //A ZClass exist already or was just created, populate it
   * if(zclass != null) { listOfRangeClasses.add(zclass); } } } return listOfRangeClasses; }
   */

  private void validateInverseRelation(ZClass domainZClass, ZObjectProperty objectProperty,
      ZClass rangeZClass) throws NullPointerException, ClassCastException {
    if (!this.skipInverseRelations) {
      return;
    }

    ZPair<IRI, IRI> pair = new ZPair<>(domainZClass.getIri(), rangeZClass.getIri());

    try {
      if (mapPairOfZClassestoZobjects.containsKey(pair)
          || mapPairOfZClassestoZobjects.containsKey(pair.getReverse())) {
        List<ZObjectProperty> objectProperties = mapPairOfZClassestoZobjects.get(pair);
        if (objectProperties == null) {
          objectProperties = mapPairOfZClassestoZobjects.get(pair.getReverse());
        }

        if (objectProperties.parallelStream().filter(f -> {
          return f.getInverseZObjectProperties().contains(objectProperty.getObjectProperty());
        }).count() > 0) {
          objectProperties.add(objectProperty);
        }
      } else if (objectProperty.getInverseZObjectProperties().size() > 0) {
        List<ZObjectProperty> properties = new ArrayList<>();
        properties.add(objectProperty);
        mapPairOfZClassestoZobjects.put(pair, properties);
      }
    } catch (NullPointerException | ClassCastException e) {
      log.error(e);
      throw e;
    }
  }

  private void scanObjectPropertyDomainAndRange(OWLObjectProperty objectProperty)
      throws UnsupportedOperationException, ClassCastException, NullPointerException,
      IllegalArgumentException {
    OWLObjectPropertyExpression objectPropertyExpression =
        objectProperty.asObjectPropertyExpression();
    List<ZClass> domainList;
    List<ZClass> rangeList;
    try {
      domainList = getObjectPropertyDomainZClass(objectPropertyExpression);
      rangeList = getObjectPropertyRangeZClass(objectPropertyExpression);
    } catch (UnsupportedOperationException | ClassCastException | NullPointerException
        | IllegalArgumentException e) {
      log.error(e.getMessage());
      throw e;
    }


    IRI iri = objectProperty.asOWLObjectProperty().getIRI();

    for (ZClass zclassDomain : domainList) {
      ZObjectProperty zobjectProperty = new ZObjectProperty(iri);

      for (ZClass zclassRange : rangeList) {
        if (zclassDomain
            .getZObjectPropertyItem(iri).parallelStream().filter(o -> o.getRangeListZClasses()
                .parallelStream().allMatch(m -> m.getKey().getIri().equals(zclassRange.getIri())))
            .count() == 0) {
          zobjectProperty.add_RangeZClass(new ZPair<>(zclassRange, true));
          validateInverseRelation(zclassDomain, zobjectProperty, zclassRange);
        }
      }
      zclassDomain.add_ObjectProperty(zobjectProperty);
    }
  }

  /**
   * Visiting DataProperties defined apart and not as subclasses
   */
  private List<ZClass> getObjectPropertyRangeZClass(
      OWLObjectPropertyExpression objectPropertyExpression) throws UnsupportedOperationException,
      ClassCastException, NullPointerException, IllegalArgumentException {
    List<ZClass> listOfRangeClasses = new ArrayList<ZClass>();
    Iterator<OWLObjectPropertyRangeAxiom> itStream =
        ontology.objectPropertyRangeAxioms(objectPropertyExpression).iterator();
    while (itStream.hasNext()) {
      OWLObjectPropertyRangeAxiom owlRange = itStream.next();
      if (owlRange != null) {
        try {
          List<OWLClass> rangClassIris = new ArrayList<>();
          if ((owlRange.getRange() instanceof OWLClass))
            rangClassIris.add(owlRange.getRange().asOWLClass());
          else if (owlRange.getRange()
              .getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            ((OWLObjectUnionOfImpl) owlRange.getRange()).getOperandsAsList().forEach(action -> {
              rangClassIris.add(action.asOWLClass());
            });
          }

          rangClassIris.forEach((OWLClass owlClass) -> {
            if (owlClass != null && !mapIRI_to_Zclass.containsKey(owlClass.getIRI())) {
              this.visit(owlClass);
            }
            zclass = mapIRI_to_Zclass.get(owlClass.getIRI());
            // A ZClass exist already or was just created, populate it
            if (zclass != null) {
              listOfRangeClasses.add(zclass);
            }
          });

        } catch (UnsupportedOperationException | ClassCastException | NullPointerException
            | IllegalArgumentException e) {
          log.error("getObjectPropertyRangeZClass: " + e.getMessage());
          throw e;
        }
      }
    }
    return listOfRangeClasses;
  }

  private List<ZClass> getObjectPropertyDomainZClass(
      OWLObjectPropertyExpression objectPropertyExpression) throws UnsupportedOperationException,
      ClassCastException, NullPointerException, IllegalArgumentException {
    List<ZClass> listOfDomainClasses = new ArrayList<ZClass>();
    Iterator<OWLObjectPropertyDomainAxiom> itStream =
        ontology.objectPropertyDomainAxioms(objectPropertyExpression).iterator();
    while (itStream.hasNext()) {
      OWLObjectPropertyDomainAxiom owlDomain = itStream.next();
      if (owlDomain != null) {
        try {
          List<OWLClass> domainClassIris = new ArrayList<>();

          if ((owlDomain.getDomain() instanceof OWLClass))
            domainClassIris.add(owlDomain.getDomain().asOWLClass());
          else if (owlDomain.getDomain()
              .getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            ((OWLObjectUnionOfImpl) owlDomain.getDomain()).getOperandsAsList().forEach(action -> {
              domainClassIris.add(action.asOWLClass());
            });
          }

          domainClassIris.forEach((OWLClass owlClass) -> {
            if (owlClass != null && !mapIRI_to_Zclass.containsKey(owlClass.getIRI())) {
              this.visit(owlClass);
            }
            zclass = mapIRI_to_Zclass.get(owlClass.getIRI());
            // A ZClass exist already or was just created, populate it
            if (zclass != null) {
              listOfDomainClasses.add(zclass);
            }
          });

        } catch (UnsupportedOperationException | ClassCastException | NullPointerException
            | IllegalArgumentException e) {
          log.error("getObjectPropertyDomainZClass: " + e.getMessage());
          throw e;
        }
      }
    }

    return listOfDomainClasses;
  }

  /*
   * private List<String> getDataPropertyRange(OWLDataPropertyExpression dataPropertyExpression) {
   * List<ZClass> listOfRangeClasses = new ArrayList<ZClass>(); Stream<OWLDataPropertyRangeAxiom>
   * rangeStream = ontology.dataPropertyRangeAxioms(dataPropertyExpression.asOWLDataProperty());
   * Iterator<OWLDataPropertyRangeAxiom> itStream = rangeStream.iterator();
   * while(itStream.hasNext()) { OWLDataPropertyRangeAxiom owlRange = itStream.next(); if(owlRange
   * != null) { IRI classIriDomain = owlRange.getRange().asOWLDatatype(); ZClass zclass =
   * mapIRI_to_Zclass.get(classIriDomain); //it was never created, it must be visited if(zclass ==
   * null) { this.visit(owlRange.getRange().asOWLClass()); } zclass =
   * mapIRI_to_Zclass.get(classIriDomain); //A ZClass exist already or was just created, populate it
   * if(zclass != null) { listOfRangeClasses.add(zclass); } } } return listOfRangeClasses; }
   */

  private String importDeclarationFromIri(IRI iri) throws UnsupportedOperationException {
    String intermediate = UTILS.cleanPackageName(iri);

    if (selectedCode == CODE.C_SHARP) {
      return intermediate;
    } else if (selectedCode == CODE.JAVA) {
      return intermediate + ".I" + iri.getShortForm();
    } else if (selectedCode == CODE.PYTHON) {
      return intermediate + "." + iri.getShortForm();
    } else {
      throw new UnsupportedOperationException("Invalid Code " + selectedCode);
    }
  }

  private String importDeclarationFromIriZInstance(IRI iri) {
    String intermediate = UTILS.cleanPackageName(iri);
    return intermediate + "." + iri.getShortForm();
  }

  private void handleObjectProp(OWLClass owlclass) throws NullPointerException, ClassCastException {
    ZClass currentZClass = mapIRI_to_Zclass.get(owlclass.getIRI());
    zclass.add_importsDeclarations(importDeclarationFromIri(owlclass.getIRI()), owlclass.getIRI());
    if (currentZClass != null) {
      zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
      validateInverseRelation(zclass, zObjectProperty, currentZClass);
      // zclass.add_ObjectProperty(zObjectProperty);
      log.debug(" -Range " + owlclass.toString());
    } else {
      // Save the current class
      mapIRI_to_Zclass.replace(zclass.getIri(), zclass);

      iriList.push(zclass.getIri());

      owlclass.accept(this);

      currentZClass = mapIRI_to_Zclass.get(zclass.getIri());

      // Reload the former class
      if (!iriList.isEmpty()) {
        zclass = mapIRI_to_Zclass.get(iriList.pop());
        zObjectProperty =
            zclass.getZObjectPropertyList().get(zclass.getZObjectPropertyList().size() - 1);
        zObjectProperty.add_RangeZClass(new ZPair<>(currentZClass, true));
        validateInverseRelation(zclass, zObjectProperty, currentZClass);
      }
    }
  }

  private String getLabel(OWLEntity entity) {
    label = null;
    EntitySearcher.getAnnotationAssertionAxioms(entity, ontology).forEach(action -> {
      if (action.getProperty().isLabel()) {
        if (action.getValue() instanceof OWLLiteral) {
          OWLLiteral val = (OWLLiteral) action.getValue();
          label = val.getLiteral().toString();
        }
      }
    });
    return label;
  }

  private String getComment(OWLEntity entity) {
    comment = null;
    EntitySearcher.getAnnotationAssertionAxioms(entity, ontology).forEach(action -> {
      if (action.getProperty().isComment()) {
        if (action.getValue() instanceof OWLLiteral) {
          OWLLiteral val = (OWLLiteral) action.getValue();
          comment = val.getLiteral().toString();
        }
      }
    });
    return comment;
  }

  private void disableInverseProperties() {
    for (Entry<ZPair<IRI, IRI>, List<ZObjectProperty>> entry : mapPairOfZClassestoZobjects
        .entrySet()) {
      List<ZObjectProperty> bindedObjectProperties = entry.getValue();
      ZPair<IRI, IRI> key = entry.getKey();
      if (bindedObjectProperties.size() > 1) {
        for (int i = 0; i < bindedObjectProperties.size() - 1; i++) {
          ZObjectProperty previousProperty = bindedObjectProperties.get(i);
          for (int j = i + 1; j < bindedObjectProperties.size(); j++) {
            ZObjectProperty currentProperty = bindedObjectProperties.get(j);
            if (previousProperty.getInverseZObjectProperties()
                .contains(currentProperty.getObjectProperty()) && previousProperty.isGeneratable()
                && currentProperty.isGeneratable()) {
              if ((currentProperty.getObjectPropertyType().equalsIgnoreCase("Max")
                  && currentProperty.getObjectPropertyCardinality().equals("1"))
                  || (previousProperty.getObjectPropertyType().equalsIgnoreCase("Some"))) {
                previousProperty.getRangeListZClasses().parallelStream().filter(f -> {
                  return (f.getKey().getIri().equals(key.getKey())
                      || f.getKey().getIri().equals(key.getValue()));
                }).collect(Collectors.toList()).forEach(action -> action.setValue(false));
                break;
              } else {
                currentProperty.getRangeListZClasses().parallelStream().filter(f -> {
                  return (f.getKey().getIri().equals(key.getKey())
                      || f.getKey().getIri().equals(key.getValue()));
                }).collect(Collectors.toList()).forEach(action -> action.setValue(false));
              }
            }
          }
        }
      }
    }
  }

}
