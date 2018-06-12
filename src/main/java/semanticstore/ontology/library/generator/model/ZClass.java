/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
  private Set<String> listOfImportsFromParents;
  private String packageName;
  private String path;
  private boolean generate = true;
  private boolean hasSubClass = false;
  private ZClass parentToExtend;
  private List<ZClass> superZClassList;
  private List<ZObjectProperty> zObjectPropertyList;
  private List<ZDataProperty> zDataPropertyList;
  private List<ZInstance> listZInstanceIRI;
  private List<String> importsDeclarationsFromObjectProperties;
  private List<String> importsDeclarationsFromObjectPropertiesInstances;

  public ZClass(IRI iri) {
    superZClassList = new ArrayList<>();
    zObjectPropertyList = new ArrayList<>();
    zDataPropertyList = new ArrayList<>();
    listZInstanceIRI = new ArrayList<>();
    importsDeclarationsFromObjectPropertiesInstances = new ArrayList<>();
    importsDeclarationsFromObjectProperties = new ArrayList<>();
    listOfImportsFromParents = new HashSet<>();
    this.iri = iri;
    this.namespace = iri.getNamespace();
    this.packageName =
        UTILS.cleanPackageName(iri).replaceAll("[.]\\d+[.]", "").replaceAll("[.]\\d+[.]", "");
    this.path = UTILS.cleanPath(iri);
    this.zClassName = iri.getShortForm();
  }

  /**
   * get List of imports from Parents dependencies
   */
  public Set<String> getListOfImports() {
    return listOfImportsFromParents;
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
      listOfImportsFromParents.add(importedPackage);
    }
  }

  public void add_listOfImports(String packageName) {
    if (!packageName.equals(getPackageName())) {
      listOfImportsFromParents.add(packageName);
    }
  }

  public void add_importsDeclarations(String imports, IRI iri) {
    if (!importsDeclarationsFromObjectProperties.contains(imports)
        && !listOfImportsFromParents.contains(iri.toString())) {
      importsDeclarationsFromObjectProperties.add(imports);
    }
  }

  public void add_ObjectProperty(ZObjectProperty zObjectProperty) {
    zObjectPropertyList.add(zObjectProperty);
  }

  public void updateObjectPropertyList() {
    Map<String, List<ZObjectProperty>> mappedProperties =
        zObjectPropertyList.parallelStream().collect(Collectors
            .groupingBy(zobjectProperty -> zobjectProperty.getObjectProperty().toString()));
    zObjectPropertyList.clear();

    for (List<ZObjectProperty> list : mappedProperties.values()) {
      ZObjectProperty property = list.get(0);
      for (int i = 1; i < list.size(); i++) {
        property.addAll_RangeZClass(list.get(i).getRangeListZClasses());
      }
      zObjectPropertyList.add(property);
    }
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

}
