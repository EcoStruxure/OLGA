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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.IRI;
import semanticstore.ontology.library.generator.global.UTILS;

public class ZInstance implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private IRI iri;
  private String namespace;
  private String zInstanceName;
  private IRI zClassInstanciated;
  private List<ZObjectProperty> listZObjectPropertyList;
  private List<ZDataProperty> listZDataPropertyList;
  private String packageName;
  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  private String label;
  private String comments;

  public ZInstance(IRI iri) {
    listZObjectPropertyList = new ArrayList<>();
    listZDataPropertyList = new ArrayList<>();
    this.iri = iri;
    this.namespace = iri.getNamespace();
    this.zInstanceName = iri.getShortForm();
    this.packageName = UTILS.cleanPackageName(iri);

    this.path = UTILS.cleanPath(iri);
  }

  public IRI getIri() {
    return iri;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getzInstanceName() {
    return zInstanceName;
  }

  public String getLabel() {
    return label;
  }

  public IRI getParentClassIRI() {
    return getzClassInstanciated();
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

  public List<ZObjectProperty> getListZObjectPropertyList() {
    return listZObjectPropertyList;
  }

  private void refineZObjectPropertyList() {
    Map<ZPair<IRI, IRI>, ZObjectProperty> refiner = new HashMap<>();

    for (ZObjectProperty zObjectProperty : listZObjectPropertyList) {
      Set<ZInstance> instances = zObjectProperty.getRangeListZInstances();
      if (instances != null && instances.size() > 0) {
        ZPair<IRI, IRI> key = new ZPair<>(zObjectProperty.getObjectProperty(),
            instances.iterator().next().getzClassInstanciated());
        ZObjectProperty property = refiner.get(key);

        if (property != null) {
          property.addAll_RangeZInstance(instances);
        } else {
          refiner.put(key, zObjectProperty);
        }
      }
    }
    listZObjectPropertyList = new ArrayList<ZObjectProperty>(refiner.values());
  }

  public List<ZDataProperty> getListZDataPropertyList() {
    return listZDataPropertyList;
  }

  public void setListZDataPropertyList(List<ZDataProperty> listZDataPropertyList) {
    this.listZDataPropertyList = listZDataPropertyList;
  }

  public void add_ObjectProperty(ZObjectProperty zObjectProperty) {
    listZObjectPropertyList.add(zObjectProperty);
    refineZObjectPropertyList();
  }

  public void add_DataProperty(ZDataProperty zDataProperty) {
    listZDataPropertyList.add(zDataProperty);
  }


  // public void add_ParentClass(ZClass zClass)
  // {
  // ZClassList.add(zClass);
  // }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comments == null) ? 0 : comments.hashCode());
    result = prime * result + ((iri == null) ? 0 : iri.hashCode());
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result =
        prime * result + ((listZDataPropertyList == null) ? 0 : listZDataPropertyList.hashCode());
    result = prime * result
        + ((listZObjectPropertyList == null) ? 0 : listZObjectPropertyList.hashCode());
    result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
    result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    result = prime * result + ((zClassInstanciated == null) ? 0 : zClassInstanciated.hashCode());
    result = prime * result + ((zInstanceName == null) ? 0 : zInstanceName.hashCode());
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
    ZInstance other = (ZInstance) obj;
    if (comments == null) {
      if (other.comments != null)
        return false;
    } else if (!comments.equals(other.comments))
      return false;
    if (iri == null) {
      if (other.iri != null)
        return false;
    } else if (!iri.equals(other.iri))
      return false;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (listZDataPropertyList == null) {
      if (other.listZDataPropertyList != null)
        return false;
    } else if (!listZDataPropertyList.equals(other.listZDataPropertyList))
      return false;
    if (listZObjectPropertyList == null) {
      if (other.listZObjectPropertyList != null)
        return false;
    } else if (!listZObjectPropertyList.equals(other.listZObjectPropertyList))
      return false;
    if (namespace == null) {
      if (other.namespace != null)
        return false;
    } else if (!namespace.equals(other.namespace))
      return false;
    if (packageName == null) {
      if (other.packageName != null)
        return false;
    } else if (!packageName.equals(other.packageName))
      return false;
    if (path == null) {
      if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
      return false;
    if (zClassInstanciated == null) {
      if (other.zClassInstanciated != null)
        return false;
    } else if (!zClassInstanciated.equals(other.zClassInstanciated))
      return false;
    if (zInstanceName == null) {
      if (other.zInstanceName != null)
        return false;
    } else if (!zInstanceName.equals(other.zInstanceName))
      return false;
    return true;
  }

  public IRI getzClassInstanciated() {
    return zClassInstanciated;
  }

  public void setzClassInstanciated(IRI zClassInstanciated) {
    this.zClassInstanciated = zClassInstanciated;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    // TODO Auto-generated method stub
    return super.clone();
  }
  // public void print()
  // {
  // System.out.println(" Classiri " + iri);
  // for (ZClass parentClass : ZClassList)
  // {
  // System.out.println("parent " + parentClass.iri);
  // }
  // for (ZObjectProperty objectProperty : listZObjectPropertyList)
  // {
  // objectProperty.print();
  // }
  // for (ZDataProperty dataProperty : listZDataPropertyList)
  // {
  // dataProperty.print();
  // }
  // }

}
