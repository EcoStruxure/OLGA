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
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.atteo.evo.inflector.English;
import org.semanticweb.owlapi.model.IRI;

public class ZObjectProperty implements Cloneable, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private IRI objectProperty;
  private Set<ZPair<ZClass, Boolean>> rangeListZClasses;
  private Set<ZInstance> rangeListZInstances;
  private String objectPropertyType = "";
  private String objectPropertyCardinality = "";
  private String label;
  private String comments;
  private boolean overridable = false;
  private boolean generate = true;
  private Set<IRI> inverseZObjectPropertyIris;
  private boolean hidingParentProperty = false;


  public ZObjectProperty(IRI objectProperty) {
    this.objectProperty = objectProperty;
    rangeListZClasses = new HashSet<>();
    rangeListZInstances = new HashSet<>();
    inverseZObjectPropertyIris = new HashSet<>();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    // TODO Auto-generated method stub
    return super.clone();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comments == null) ? 0 : comments.hashCode());
    result = prime * result + (generate ? 1231 : 1237);
    result = prime * result
        + ((inverseZObjectPropertyIris == null) ? 0 : inverseZObjectPropertyIris.hashCode());
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + ((objectProperty == null) ? 0 : objectProperty.hashCode());
    result = prime * result
        + ((objectPropertyCardinality == null) ? 0 : objectPropertyCardinality.hashCode());
    result = prime * result + ((objectPropertyType == null) ? 0 : objectPropertyType.hashCode());
    result = prime * result + (overridable ? 1231 : 1237);
    result = prime * result + ((rangeListZClasses == null) ? 0 : rangeListZClasses.hashCode());
    result = prime * result + ((rangeListZInstances == null) ? 0 : rangeListZInstances.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ZObjectProperty)) {
      return false;
    }

    ZObjectProperty that = (ZObjectProperty) other;

    // Custom equality check here.
    /*
     * this.objectProperty.equals(that.objectProperty) &&
     * this.objectPropertyType.equals(that.objectPropertyType) && this.objectPropertyCardinality ==
     * (that.objectPropertyCardinality) && this.rangeListZClasses.equals(that.rangeListZClasses);
     */

    boolean equal = this.objectProperty.equals(that.objectProperty);
    if (this.objectPropertyType != null && that.objectPropertyType != null) {
      equal = equal && this.objectPropertyType.equals(that.objectPropertyType);
    }
    if (this.objectPropertyCardinality != null && that.objectPropertyCardinality != null) {
      equal = equal && this.objectPropertyCardinality.equals(that.objectPropertyCardinality);
    }
    
    equal = equal && (this.isOverridable()==that.isOverridable());

    return equal;
  }

  public IRI getObjectProperty() {
    return objectProperty;
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

  public String getObjectPropertyShortFormCS() {
    String shortForm = objectProperty.getShortForm();
    if (shortForm.toLowerCase(Locale.ENGLISH).startsWith("is")) {
      shortForm = shortForm.replaceFirst("(?i)is", "");
    } else if (shortForm.toLowerCase(Locale.ENGLISH).startsWith("has")) {
      shortForm = shortForm.replaceFirst("(?i)has", "");
    }

    if (((!objectPropertyType.equalsIgnoreCase("max")
        || !objectPropertyType.equalsIgnoreCase("exactly"))
        && !objectPropertyCardinality.equalsIgnoreCase("1"))
        && !(shortForm.toLowerCase(Locale.ENGLISH).endsWith("in")
            || shortForm.toLowerCase(Locale.ENGLISH).endsWith("by")
            || shortForm.toLowerCase(Locale.ENGLISH).endsWith("s")
            || shortForm.toLowerCase(Locale.ENGLISH).endsWith("of")
            || shortForm.toLowerCase(Locale.ENGLISH).endsWith("for")
            || shortForm.toLowerCase(Locale.ENGLISH).endsWith("to"))

    ) {
      shortForm = English.plural(shortForm);
    }

    return shortForm;
  }

  public String getObjectPropertyShortForm() {
    return objectProperty.getShortForm();
  }

  public String getObjectPropertyNamespace() {
    return objectProperty.getNamespace();
  }

  public void setObjectProperty(IRI objectProperty) {
    this.objectProperty = objectProperty;
  }

  public String getObjectPropertyType() {
    return objectPropertyType;
  }

  public String getInstancesClass() {
    if (this.rangeListZInstances.size() == 0)
      return null;
    else
      return this.rangeListZInstances.iterator().next().getzClassInstanciated().getFragment();
  }

  public void setObjectPropertyType(String objectPropertyType) {
    this.objectPropertyType = objectPropertyType;
  }

  public String getObjectPropertyCardinality() {
    return objectPropertyCardinality;
  }

  public Set<ZInstance> getRangeListZInstances() {
    return rangeListZInstances;
  }

  public void setRangeListZInstances(Set<ZInstance> rangeListZInstances) {
    this.rangeListZInstances = rangeListZInstances;
  }

  public void setObjectPropertyCardinality(String objectPropertyCardinality) {
    this.objectPropertyCardinality = objectPropertyCardinality;
  }

  public void add_RangeZClass(ZPair<ZClass, Boolean> zClass) {
    rangeListZClasses.add(zClass);
  }

  public Set<ZPair<ZClass, Boolean>> getRangeListZClasses() {
    return rangeListZClasses;
  }

  public void setRangeListZClasses(Set<ZPair<ZClass, Boolean>> rangeListZClasses) {
    this.rangeListZClasses = rangeListZClasses;
  }

  public void add_RangeZInstance(ZInstance zInstance) {
    rangeListZInstances.add(zInstance);
  }

  public void addAll_RangeZInstance(Set<ZInstance> zInstance) {
    rangeListZInstances.addAll(zInstance);
  }

  public void addAll_RangeZClass(Collection<? extends ZPair<ZClass, Boolean>> zclasses) {
    rangeListZClasses.addAll(zclasses);
  }

  public void print() {
    for (ZPair<ZClass, Boolean> rangeClass : rangeListZClasses) {
      System.out.println(" -Object: " + objectProperty.getShortForm() + " , range:"
          + rangeClass.getKey().getIri().getShortForm() + " , type: " + objectPropertyType);
    }
  }

  public void setOverridable() {
    this.overridable = true;
  }

  public boolean isOverridable() {
    return this.overridable;
  }

  public boolean isGeneratable() {
    return generate;
  }

  public void setGenerate(boolean generate) {
    this.generate = generate;
  }

  public Set<IRI> getInverseZObjectProperties() {
    return inverseZObjectPropertyIris;
  }

  public void setInverseZObjectProperties(Set<IRI> inverseZObjectProperties) {
    this.inverseZObjectPropertyIris = inverseZObjectProperties;
  }

  public boolean addInverseZObjectProperty(IRI inverseZObjectProperty) {
    return this.inverseZObjectPropertyIris.add(inverseZObjectProperty);
  }

  public void addAllInverseZObjectProperties(Set<IRI> inverseZObjectProperties) {
    this.inverseZObjectPropertyIris.addAll(inverseZObjectProperties);
  }
  
  public boolean isHidingParentProperty() {
    return hidingParentProperty ;
  }

  public void setHidingParentProperty() {
    this.hidingParentProperty = true;
  }
}
