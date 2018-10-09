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
package semanticstore.ontology.library.generator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;
import org.semanticweb.owlapi.model.IRI;
import semanticstore.ontology.library.generator.global.UTILS;

public class ZClass implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private IRI iri;
  private String namespace;
  private String zClassName;
  private String label;
  private String comments;
  private Map<String, Set<String>> listOfImportsFromParents;
  private String packageName;
  private String path;
  private boolean generate = true;
  private boolean hasSubClass = false;
  private ZClass parentToExtend;
  private List<ZClass> superZClassList;
  private List<ZObjectProperty> zObjectPropertyList;
  private List<ZDataProperty> zDataPropertyList;
  private List<ZObjectProperty> listOfObjectPropertiesForClass;
  private List<ZObjectProperty> listOfObjectPropertiesForInterface;
  List<ZDataProperty> listOfDataPropertiesForInterface;
  private List<ZDataProperty> listOfDataPropertiesForClass;
  private List<ZInstance> listZInstanceIRI;
  private List<String> importsDeclarationsFromObjectProperties;
  private List<String> importsDeclarationsFromObjectPropertiesInstances;
  // private List<ZClass> listOfRecuirciveParent;

  public ZClass(IRI iri) {
    superZClassList = new ArrayList<>();
    zObjectPropertyList = new ArrayList<>();
    zDataPropertyList = new ArrayList<>();
    listZInstanceIRI = new ArrayList<>();
    importsDeclarationsFromObjectPropertiesInstances = new ArrayList<>();
    importsDeclarationsFromObjectProperties = new ArrayList<>();
    listOfImportsFromParents = new HashMap<>();
    this.iri = iri;
    this.namespace = iri.getNamespace();
    this.packageName =
        UTILS.cleanPackageName(iri).replaceAll("[.]\\d+[.]", "").replaceAll("[.]\\d+[.]", "");
    this.path = UTILS.cleanPath(iri);
    this.zClassName = iri.getShortForm();
    // this.listOfRecuirciveParent = new ArrayList<>();
  }

  /**
   * get List of imports from Parents dependencies
   */
  public Map<String, Set<String>> getListOfImports() {
    return listOfImportsFromParents;
  }

  public Set<String> getSetOfImportPackages() {
    return listOfImportsFromParents.keySet();
  }

  /**
   * get List of imports from ObjectProperties dependencies
   */
  public List<String> getImportsDeclarations() {
    return importsDeclarationsFromObjectProperties;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public boolean containsIRI(final List<ZObjectProperty> list, final IRI name) {
    return list.stream().filter(o -> o.getObjectProperty().equals(name)).findFirst().isPresent();
  }

  public boolean getGenerate() {
    return generate;
  }

  public void setGenerate(boolean generate) {
    this.generate = generate;
  }

  public String getPackageName() {
    return UTILS.removeLastPoint(packageName);
  }

  public IRI getIri() {
    return iri;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getzClassName() {
    return zClassName;
  }

  public void setListZInstanceIRI(List<ZInstance> listZInstanceIRI) {
    this.listZInstanceIRI = listZInstanceIRI;
  }

  public List<String> getImportsDeclarationsFromObjectPropertiesInstances() {
    return importsDeclarationsFromObjectPropertiesInstances;
  }

  public void setImportsDeclarationsFromObjectPropertiesInstances(
      List<String> importsDeclarationsFromObjectPropertiesInstances) {
    this.importsDeclarationsFromObjectPropertiesInstances =
        importsDeclarationsFromObjectPropertiesInstances;
  }

  public void add_importsDeclarationsZInstance(String imports) {
    importsDeclarationsFromObjectPropertiesInstances.add(imports);
  }

  public void setSuperZClassList(List<ZClass> superZClassList) {
    this.superZClassList = superZClassList;
    superZClassList.forEach(parentClass -> {
      parentClass.setHasSubClass();
    });
  }

  public List<ZClass> getSuperZClassList() {
    return superZClassList;
  }

  public Set<ZClass> getAggregateSuperZClasses() {
    Set<ZClass> superZClassesIRIs = new HashSet<>();

    superZClassesIRIs.addAll(superZClassList);
    for (ZClass parent : this.superZClassList)
      superZClassesIRIs.addAll(parent.getAggregateSuperZClasses());

    return superZClassesIRIs;
  }

  public List<ZObjectProperty> getZObjectPropertyList() {
    return zObjectPropertyList;
  }

  public List<ZObjectProperty> getZObjectPropertyItem(IRI zObjectPropertyIRI) {
    return zObjectPropertyList.parallelStream()
        .filter(o -> o.getObjectProperty().equals(zObjectPropertyIRI)).collect(Collectors.toList());
  }

  public List<ZDataProperty> getZDataPropertyItem(String zDataPropertyIRI) {
    List<ZDataProperty> listToReturn = new ArrayList<>();

    for (ZDataProperty currentDataProperty : zDataPropertyList) {
      if (currentDataProperty.getDataProperty().toString().equalsIgnoreCase(zDataPropertyIRI)) {
        listToReturn.add(currentDataProperty);
      }
    }
    return listToReturn;
  }

  public List<ZDataProperty> getZDataPropertyList() {
    return zDataPropertyList;
  }

  public List<ZInstance> getListZInstanceIRI() {
    return listZInstanceIRI;
  }

  public void add_listOfImports(IRI iriImports) {
    String importedPackage = UTILS.removeLastPoint(UTILS.cleanPackageName(iriImports)
        .replaceAll("[.]\\d+[.]", "").replaceAll("[.]\\d+[.]", ""));
    if (!importedPackage.equals(getPackageName())) {
      if (listOfImportsFromParents.containsKey(importedPackage)) {
        listOfImportsFromParents.get(importedPackage).add(iriImports.getShortForm());
      } else {
        Set<String> list = new HashSet<>();
        list.add(iriImports.getShortForm());
        listOfImportsFromParents.put(importedPackage, list);
      }
    }
  }

  public void add_listOfImports(String packageName, String className) {
    if (!packageName.equals(getPackageName())) {
      if (listOfImportsFromParents.containsKey(packageName)) {
        listOfImportsFromParents.get(packageName).add(className);
      } else {
        Set<String> list = new HashSet<>();
        list.add(className);
        listOfImportsFromParents.put(packageName, list);
      }
    }
  }

  public void add_importsDeclarations(String imports, IRI iri) {
    if (!importsDeclarationsFromObjectProperties.contains(imports)
        && !listOfImportsFromParents.containsKey(iri.toString())) {
      importsDeclarationsFromObjectProperties.add(imports);
    }
  }

  public void add_ObjectProperty(ZObjectProperty zObjectProperty) {
    zObjectPropertyList.add(zObjectProperty);
  }

  public void add_DataProperty(ZDataProperty zDataProperty) {
    zDataPropertyList.add(zDataProperty);
  }

  public void add_ParentClass(ZClass zClass) {
    add_listOfImports(zClass.iri);
    superZClassList.add(zClass);
    zClass.setHasSubClass();
  }

  public void add_ZInstanceClass(ZInstance zInstance) {
    if (!listZInstanceIRI.contains(zInstance)) {
      listZInstanceIRI.add(zInstance);
    }
  }

  public void print() {
    System.out.println("====> Classiri " + iri);
    for (ZClass parentClass : superZClassList) {
      System.out.println(" -parent: " + parentClass.iri.getShortForm());
    }
    for (ZObjectProperty objectProperty : zObjectPropertyList) {
      objectProperty.print();
    }
    for (ZDataProperty dataProperty : zDataPropertyList) {
      dataProperty.print();
    }
    System.out.println("====");
  }

  public ZClass getParentToExtend() {
    return getBestParentToExtend();
  }

  private ZClass getBestParentToExtend() {
    if (parentToExtend == null) {
      int count = Integer.MIN_VALUE;
      ZClass parentHasMaxParents = null;

      for (ZClass parentClass : superZClassList) {
        int classDegree = getMaxCountOfParentLevels(parentClass);
        if (classDegree > count) {
          parentHasMaxParents = parentClass;
          count = classDegree;
        }
      }

      parentToExtend = parentHasMaxParents;
    }

    return parentToExtend;
  }

  int levelCount;

  private int getMaxCountOfParentLevels(ZClass parentClass) {
    levelCount = 0;
    if (parentClass.getSuperZClassList().size() == 0) {
      return 0;
    }

    parentClass.getSuperZClassList().forEach(action -> {
      levelCount = Math.max(levelCount, getMaxCountOfParentLevels(action));
    });
    return levelCount + 1;
  }

  public List<ZClass> getListOfParentsToImplement() {
    List<ZClass> parentsList = new ArrayList<>(this.superZClassList);
    parentsList.remove(getBestParentToExtend());
    return parentsList;
  }

  public boolean hasSubClass() {
    return hasSubClass;
  }

  public void setHasSubClass() {
    this.hasSubClass = true;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((iri == null) ? 0 : iri.hashCode());
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
    ZClass other = (ZClass) obj;
    if (iri == null) {
      if (other.iri != null)
        return false;
    } else if (!iri.equals(other.iri))
      return false;
    return true;
  }

  public void setzObjectPropertyList(List<ZObjectProperty> zObjectPropertyList) {
    this.zObjectPropertyList = zObjectPropertyList;
  }

  public void setzDataPropertyList(List<ZDataProperty> zDataPropertyList) {
    this.zDataPropertyList = zDataPropertyList;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////
  private void reconstructObjectPropertyList() {
    List<ZObjectProperty> tempList = new ArrayList<ZObjectProperty>();

    for (ZObjectProperty property : zObjectPropertyList) {
      mergeObjectPropertyToList(property, tempList);
    }

    zObjectPropertyList = tempList;
  }

  public void triageObjectProperties(List<ZObjectProperty> properties,
      Set<IRI> excludedClassesIris) {

    if (properties.size() > 0) {
      if (!excludedClassesIris.contains(this.getIri())) {
        excludedClassesIris.add(this.getIri());

        reconstructObjectPropertyList();
        setOverrideOptionForAllObjectPropertiesThatHasMoreThanOneRange();
        for (ZObjectProperty objectProperty : zObjectPropertyList) {
          searchForIdenticalObjectProperty(objectProperty, properties);
        }

        for (ZClass parent : getSuperZClassList()) {
          parent.triageObjectProperties(properties, excludedClassesIris);
        }
      }
    }
  }

  public List<ZObjectProperty> getListOfObjectPropertiesForInterface() {
    if (listOfObjectPropertiesForInterface == null) {
      Set<IRI> excludedClasses = new HashSet<>();
      excludedClasses.add(iri);

      reconstructObjectPropertyList();
      setOverrideOptionForAllObjectPropertiesThatHasMoreThanOneRange();

      listOfObjectPropertiesForInterface = new ArrayList<>(zObjectPropertyList);

      for (ZClass parent : getSuperZClassList()) {
        parent.triageObjectProperties(listOfObjectPropertiesForInterface, excludedClasses);
      }
    }

    return listOfObjectPropertiesForInterface;
  }

  public List<ZObjectProperty> getListOfObjectPropertiesForClass() {
    if (listOfObjectPropertiesForClass == null) {
      listOfObjectPropertiesForClass = new ArrayList<>(getListOfObjectPropertiesForInterface());
      if (getSuperZClassList().size() > 1) {
        for (ZClass parent : getSuperZClassList()) {
          if (parent != getBestParentToExtend()) {
            parent.getListOfObjectPropertiesForClass().forEach(objectProperty -> {
              mergeObjectPropertyToList(objectProperty, listOfObjectPropertiesForClass);
            });
          }
        }
      }
    }
    return listOfObjectPropertiesForClass;
  }

  private void searchForIdenticalObjectProperty(ZObjectProperty objectProperty,
      List<ZObjectProperty> properties) {
    List<ZObjectProperty> listOfPropertiesWhichHaveSameIris = properties.stream().filter(p -> {
      return (p.getObjectProperty() == objectProperty.getObjectProperty());
    }).collect(Collectors.toList());

    if (listOfPropertiesWhichHaveSameIris.size() > 0) {

      for (ZPair<ZClass, boolean[]> range : objectProperty.getRangeListZClasses()) {
        if (range.getValue()[0] == true && range.getValue()[1] == false) {
          List<ZObjectProperty> propertiesHasTheSameIriAndRange =
              listOfPropertiesWhichHaveSameIris.parallelStream().filter(predicate -> {
                return predicate.getRangeListZClasses().parallelStream().anyMatch(p -> {
                  return (p.getKey().equals(range.getKey())
                      || p.getKey().getAggregateSuperZClasses().parallelStream().anyMatch(pr -> {
                        return pr.getIri().equals(range.getKey().getIri());
                      }));
                });
              }).collect(Collectors.toList());

          for (ZObjectProperty propertyHasSameRangeAndIri : propertiesHasTheSameIriAndRange) {

            boolean isRangeEnabled =
                propertyHasSameRangeAndIri.getRangeListZClasses().parallelStream().anyMatch(f -> {
                  return f.getValue()[0] == true;
                });

            if (isRangeEnabled && range.getValue()[0]) {
              Optional<ZPair<ZClass, boolean[]>> sameRange =
                  propertyHasSameRangeAndIri.getRangeListZClasses().parallelStream().filter(p -> {
                    return p.getKey().equals(range.getKey());
                  }).findFirst();

              if (sameRange.isPresent()) {
                if (propertyHasSameRangeAndIri.getObjectPropertyShortFormCS()
                    .equals(objectProperty.getObjectPropertyShortFormCS())) {
                  sameRange.get().getValue()[0] = false;
                } else {
                  sameRange.get().getValue()[1] = true;
                }
              } else if (propertyHasSameRangeAndIri.isOverridable() == false
                  && objectProperty.isOverridable() == false
                  && propertyHasSameRangeAndIri.getObjectPropertyShortFormCS()
                      .equals(objectProperty.getObjectPropertyShortFormCS())) {
                Optional<ZPair<ZClass, boolean[]>> parentRanges =
                    propertyHasSameRangeAndIri.getRangeListZClasses().parallelStream().filter(p -> {
                      return p.getKey().getAggregateSuperZClasses().parallelStream()
                          .anyMatch(pr -> {
                            return pr.getIri().equals(range.getKey().getIri());
                          });
                    }).findFirst();
                parentRanges.get().getValue()[1] = true;
              }
            }
          }
        }
      }
    }
  }


  private void mergeObjectPropertyToList(ZObjectProperty objectProperty,
      List<ZObjectProperty> properties) {

    List<ZObjectProperty> listOfPropertiesWhichHaveSameIris = properties.stream().filter(p -> {
      return (p.getObjectProperty() == objectProperty.getObjectProperty());
    }).collect(Collectors.toList());

    if (listOfPropertiesWhichHaveSameIris.size() == 0) {
      properties.add(objectProperty);
    } else {
      ZObjectProperty newObjectProperty = new ZObjectProperty(objectProperty.getObjectProperty());
      newObjectProperty.setComments(objectProperty.getComments());
      newObjectProperty.setGenerate(objectProperty.isGeneratable());
      newObjectProperty.setInverseZObjectProperties(
          new HashSet<IRI>(objectProperty.getInverseZObjectProperties()));
      newObjectProperty.setLabel(objectProperty.getLabel());
      newObjectProperty.setObjectPropertyCardinality(objectProperty.getObjectPropertyCardinality());
      newObjectProperty.setObjectPropertyType(objectProperty.getObjectPropertyType());
      if (objectProperty.isOverridable())
        newObjectProperty.setOverridable();

      for (ZPair<ZClass, boolean[]> range : objectProperty.getRangeListZClasses()) {
        List<ZObjectProperty> propertiesHaveTheSameRange =
            listOfPropertiesWhichHaveSameIris.parallelStream().filter(predicate -> {
              return predicate.getRangeListZClasses().parallelStream().anyMatch(p -> {
                return p.getKey().equals(range.getKey());
              });
            }).collect(Collectors.toList());

        if (propertiesHaveTheSameRange.size() == 1) {
          // Compare between the weight of this property and and add this range to the
          // higher
          ZObjectProperty objectPropertyHasTheSamerange = propertiesHaveTheSameRange.get(0);
          boolean isRangeEnabled =
              objectPropertyHasTheSamerange.getRangeListZClasses().parallelStream().anyMatch(f -> {
                return f.getValue()[0] == true;
              });

          if (UTILS.propertyWeight(objectProperty) > UTILS
              .propertyWeight(objectPropertyHasTheSamerange) && isRangeEnabled) {
            Set<ZPair<ZClass, boolean[]>> pairs =
                objectPropertyHasTheSamerange.getRangeListZClasses().parallelStream().filter(f -> {
                  return f.getKey() == range.getKey();
                }).collect(Collectors.toSet());

            objectPropertyHasTheSamerange.getRangeListZClasses().removeAll(pairs);

            Optional<ZObjectProperty> identicalObjectProperty =
                properties.parallelStream().filter(p -> {
                  return p.equals(objectProperty)
                      && p.isOverridable() == objectProperty.isOverridable();
                }).findFirst();
            if (identicalObjectProperty.isPresent()) {
              identicalObjectProperty.get().getRangeListZClasses().add(range);
            } else {
              newObjectProperty.add_RangeZClass(range);
              properties.add(newObjectProperty);
              listOfPropertiesWhichHaveSameIris = properties.stream().filter(p -> {
                return (p.getObjectProperty() == objectProperty.getObjectProperty());
              }).collect(Collectors.toList());
            }
          }
        } else if (propertiesHaveTheSameRange.size() > 1) {
          for (int i = 0; i < propertiesHaveTheSameRange.size(); i++) {
            ZObjectProperty propertyWhichHasSameRange = propertiesHaveTheSameRange.get(i);
            if (propertyWhichHasSameRange.equals(objectProperty)
                && propertyWhichHasSameRange.isOverridable() == objectProperty.isOverridable()) {
              propertyWhichHasSameRange.add_RangeZClass(range);
            } else {
              Set<ZPair<ZClass, boolean[]>> pairs =
                  propertyWhichHasSameRange.getRangeListZClasses().parallelStream().filter(f -> {
                    return f.getKey() == range.getKey();
                  }).collect(Collectors.toSet());

              propertyWhichHasSameRange.getRangeListZClasses().removeAll(pairs);
            }
          }
        } else {
          // search for identical object property and add this range to it
          Optional<ZObjectProperty> identicalObjectProperty =
              listOfPropertiesWhichHaveSameIris.parallelStream().filter(p -> {
                return p.equals(objectProperty)
                    && p.isOverridable() == objectProperty.isOverridable();
              }).findFirst();

          if (identicalObjectProperty.isPresent()) {
            identicalObjectProperty.get().add_RangeZClass(range);
          } else {
            properties.add(objectProperty);
            listOfPropertiesWhichHaveSameIris = properties.stream().filter(p -> {
              return (p.getObjectProperty() == objectProperty.getObjectProperty());
            }).collect(Collectors.toList());
          }
        }
      }
    }
  }

  private void setOverrideOptionForAllObjectPropertiesThatHasMoreThanOneRange() {

    // override same ObjectProperties if they have the same IRI
    for (ZObjectProperty objectProperty : zObjectPropertyList) {
      if (objectProperty.getRangeListZClasses().size() > 1) {
        objectProperty.setOverridable();
      }
    }

    // Mapping of Objects based on their same IRI
    Map<IRI, List<ZObjectProperty>> mappedProperties = zObjectPropertyList.parallelStream()
        .collect(Collectors.groupingBy(zobjectProperty -> zobjectProperty.getObjectProperty()));

    // override same ObjectProperties if they have the same IRI
    for (List<ZObjectProperty> objectPropertyList : mappedProperties.values()) {
      if (objectPropertyList.size() > 1) {
        for (ZObjectProperty currentZObjectProperty : objectPropertyList) {
          currentZObjectProperty.setOverridable();
        }
      }
    }
  }

  private void reconstructDataPropertyList() {
    List<ZDataProperty> tempList = new ArrayList<ZDataProperty>();

    for (ZDataProperty property : zDataPropertyList) {
      mergeDataPropertyToList(property, tempList);
    }

    zDataPropertyList = tempList;
  }

  private void mergeDataPropertyToList(ZDataProperty dataProperty, List<ZDataProperty> properties) {

    List<ZDataProperty> listOfPropertiesWhichHaveSameIris = properties.stream().filter(p -> {
      return (p.getDataProperty().equals(dataProperty.getDataProperty()));
    }).collect(Collectors.toList());

    if (listOfPropertiesWhichHaveSameIris.size() == 0) {
      properties.add(dataProperty);
    } else if (listOfPropertiesWhichHaveSameIris.size() == 1) {
      if (UTILS.propertyWeight(listOfPropertiesWhichHaveSameIris.get(0)) < UTILS
          .propertyWeight(dataProperty)) {
        properties.remove(listOfPropertiesWhichHaveSameIris.get(0));
        properties.add(dataProperty);
      }
    } else {
      boolean addProperty = true;
      for (int i = 0; i < listOfPropertiesWhichHaveSameIris.size() - 1; i++) {
        if (UTILS.propertyWeight(listOfPropertiesWhichHaveSameIris.get(i)) < UTILS
            .propertyWeight(dataProperty)) {
          properties.remove(listOfPropertiesWhichHaveSameIris.get(i));
        } else {
          addProperty = false;
        }

        if (addProperty) {
          properties.add(dataProperty);
        }
      }
    }
  }

  public List<ZDataProperty> getListOfDataPropertiesForInterface() {

    if (listOfDataPropertiesForInterface == null) {
      Set<IRI> excludedClasses = new HashSet<>();
      excludedClasses.add(iri);

      reconstructDataPropertyList();
      refineInstancesDataProperties(zDataPropertyList);

      listOfDataPropertiesForInterface = new ArrayList<>(zDataPropertyList);
      for (ZClass parent : getSuperZClassList()) {
        parent.triageDataProperties(listOfDataPropertiesForInterface, excludedClasses);
      }
    }

    return listOfDataPropertiesForInterface;
  }

  public List<ZDataProperty> getListOfDataPropertiesForClass() {
    if (listOfDataPropertiesForClass == null) {
      listOfDataPropertiesForClass = new ArrayList<>(getListOfDataPropertiesForInterface());
      if (getSuperZClassList().size() > 1) {

        for (ZClass parent : getSuperZClassList()) {
          if (parent != getBestParentToExtend()) {
            parent.getListOfDataPropertiesForClass().forEach(dataProperty -> {
              mergeDataPropertyToList(dataProperty, listOfDataPropertiesForClass);
            });
          }
        }
      }
    }
    return listOfDataPropertiesForClass;
  }

  private void triageDataProperties(List<ZDataProperty> properties, Set<IRI> excludedClassesIris) {

    if (!excludedClassesIris.contains(this.getIri())) {
      excludedClassesIris.add(this.getIri());

      reconstructDataPropertyList();
      refineInstancesDataProperties(zDataPropertyList);

      for (ZDataProperty dataProperty : zDataPropertyList) {
        searchForIdenticalDataProperty(dataProperty, properties);
      }

      for (ZClass parent : getSuperZClassList()) {
        parent.triageDataProperties(properties, excludedClassesIris);
      }
    }
  }

  private void searchForIdenticalDataProperty(ZDataProperty dataProperty,
      List<ZDataProperty> properties) {

    List<ZDataProperty> listOfPropertiesWhichHaveSameIris = properties.stream().filter(p -> {
      return (p.getDataProperty().equals(dataProperty.getDataProperty()));
    }).collect(Collectors.toList());

    if (listOfPropertiesWhichHaveSameIris.size() > 0) {
      listOfPropertiesWhichHaveSameIris.forEach(action -> {
        action.setGenerate(false);
      });
    }
  }

  /**
   * This function used to remove any data property added to an instance in the ontolgy, and this
   * data property wasn't added to the class which this instance instantiates.
   * 
   * @param "zclass" is the class we want to filter all of its instances. "bestMotherToExtend" is
   *        the class to be extended as class. "listZDataPropertyDuplicationFiltered" list of all
   *        data properties inherited from parents.
   */
  private void refineInstancesDataProperties(
      List<ZDataProperty> listZDataPropertyDuplicationFiltered) {
    List<ZInstance> instances = new ArrayList<ZInstance>(getListZInstanceIRI());
    List<ZDataProperty> parentsDataProperties =
        new ArrayList<>(listZDataPropertyDuplicationFiltered);

    if (getParentToExtend() != null && getParentToExtend().getZDataPropertyList() != null) {
      parentsDataProperties.addAll(getParentToExtend().getZDataPropertyList());
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
    setListZInstanceIRI(instances);
  }

}
