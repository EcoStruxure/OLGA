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
import java.util.Locale;
import org.semanticweb.owlapi.model.IRI;

public class ZDataProperty implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private IRI dataProperty;
  private String restrictionType;
  private String rangeXSDType;
  private String packageName;
  private String templateForm;
  private String label;
  private String comments;
  private String value;
  private boolean overridable = false;
  private boolean hidingParentProperty = false;

  public ZDataProperty(IRI dataProperty) {
    rangeXSDType = null;
    this.dataProperty = dataProperty;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comments == null) ? 0 : comments.hashCode());
    result = prime * result + ((dataProperty == null) ? 0 : dataProperty.hashCode());
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + (overridable ? 1231 : 1237);
    result = prime * result + ((rangeXSDType == null) ? 0 : rangeXSDType.hashCode());
    result = prime * result + ((restrictionType == null) ? 0 : restrictionType.hashCode());
    result = prime * result + ((templateForm == null) ? 0 : templateForm.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    ZDataProperty other = (ZDataProperty) obj;
    if (comments == null) {
      if (other.comments != null)
        return false;
    } else if (!comments.equals(other.comments))
      return false;
    if (dataProperty == null) {
      if (other.dataProperty != null)
        return false;
    } else if (!dataProperty.equals(other.dataProperty))
      return false;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (overridable != other.overridable)
      return false;
    if (rangeXSDType == null) {
      if (other.rangeXSDType != null)
        return false;
    } else if (!rangeXSDType.equals(other.rangeXSDType))
      return false;
    if (restrictionType == null) {
      if (other.restrictionType != null)
        return false;
    } else if (!restrictionType.equals(other.restrictionType))
      return false;
    if (templateForm == null) {
      if (other.templateForm != null)
        return false;
    } else if (!templateForm.equals(other.templateForm))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getTemplateForm() {
    return rangeXSDType.substring(rangeXSDType.lastIndexOf(".") + 1);
  }

  public void setTemplateForm(String templateForm) {
    this.templateForm = templateForm;
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

  public IRI getDataProperty() {
    return dataProperty;
  }

  public String getDataPropertyShortFormCS() {
    String shortForm = dataProperty.getShortForm();
    if (shortForm.toLowerCase(Locale.ENGLISH).startsWith("is")) {
      shortForm = shortForm.replaceFirst("(?i)is", "");
    } else if (shortForm.toLowerCase(Locale.ENGLISH).startsWith("has")) {
      shortForm = shortForm.replaceFirst("(?i)has", "");
    }
    return shortForm;
  }

  public String getDataPropertyShortForm() {
    return dataProperty.getShortForm();
  }

  public String getDataPropertyNamespace() {
    return dataProperty.getNamespace();
  }

  public void setDataProperty(IRI dataProperty) {
    this.dataProperty = dataProperty;
  }

  public String getRestrictionType() {
    return restrictionType;
  }

  public void setRestrictionType(String restrictionType) {
    this.restrictionType = restrictionType;
  }

  public String getRangeXSDType() {
    return rangeXSDType;
  }

  public void setRangeXSDType(String rangeXSDType) {
    this.rangeXSDType = rangeXSDType;
  }

  public void print() {
    System.out.println(" -Data: " + dataProperty + " , range : " + rangeXSDType);
  }

  public void setOverridable() {
    this.overridable = true;
  }

  public boolean isOverridable() {
    return this.overridable;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public boolean isHidingParentProperty() {
    return hidingParentProperty;
  }

  public void setHidingParentProperty() {
    this.hidingParentProperty = true;
  }
}
