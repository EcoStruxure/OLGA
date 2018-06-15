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

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import static org.junit.Assert.*;

public class ZClassTest {

  @Test
  public void testCommentSetter() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    // when
    zclass.setComments("this is a comment");

    // then
    assertEquals("Fields didn't match", zclass.getComments(), "this is a comment");
  }

  @Test
  public void testLabelSetter() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    // when
    zclass.setLabel("this is a comment");

    // then
    assertEquals("Fields didn't match", zclass.getLabel(), "this is a comment");
  }

  @Test
  public void testGenerateSetter() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    // when
    zclass.setGenerate(true);

    // then
    assertTrue(zclass.getGenerate());
  }

  @Test
  public void testzClassName() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    // then
    assertEquals("Fields didn't match", zclass.getzClassName(), "test");
  }

  @Test
  public void testzClassnamespace() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    // then
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
  }

  @Test
  public void testzClassequals() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);

    assertTrue(zclass.equals(zclass2));
    // then
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertEquals(zclass.hashCode(), zclass2.hashCode());
  }

  /*
   * creating different properties and adding to zclass and testing with 'equals' method of
   * ZCass.java.
   */
  @Test
  public void testzClassequalsMethod() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    String data = "http://www.test.com#DataProperty";
    String instance = "http://www.test.com#Instance";
    IRI iriInstance = IRI.create(instance);
    IRI iridata = IRI.create(data);
    ZDataProperty zdata = new ZDataProperty(iridata);
    ZInstance ZInstance = new ZInstance(iriInstance);
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);
    zclass2.setComments("this is same");
    zclass.setComments("this is same");
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    zclass.setLabel("same");
    zclass2.setLabel("same");
    zclass.add_DataProperty(zdata);
    zclass2.add_DataProperty(zdata);
    zclass.add_ZInstanceClass(ZInstance);
    // then
    assertTrue(zclass.equals(zclass2));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertEquals(zclass.hashCode(), zclass2.hashCode());
  }

  @Test
  public void testzClassWhenNotequalsMethod() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    String data = "http://www.test.com#DataProperty";
    String data1 = "http://www.test.com#DataProperty1";
    IRI iridata = IRI.create(data);
    IRI iridata1 = IRI.create(data1);
    ZDataProperty zdata = new ZDataProperty(iridata);
    ZDataProperty zdata1 = new ZDataProperty(iridata1);
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);
    zclass2.setComments("this is same");
    zclass.setComments("this is not the same");
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    zclass.setLabel("same");
    zclass2.setLabel("not the same");
    zclass.add_DataProperty(zdata);
    zclass2.add_DataProperty(zdata1);
    // then
    assertFalse(!zclass.equals(zclass2));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertEquals(zclass.hashCode(), zclass2.hashCode());
  }

  @Test
  public void testzClassWhenNotequals() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    String data = "http://www.test.com#DataProperty";
    String data1 = "http://www.test.com#DataProperty1";
    IRI iridata = IRI.create(data);
    IRI iridata1 = IRI.create(data1);
    ZDataProperty zdata = new ZDataProperty(iridata);
    ZDataProperty zdata1 = new ZDataProperty(iridata1);
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);
    zclass2.setComments("this is same");
    zclass.setComments("this is same");
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    zclass.setLabel("same");
    zclass2.setLabel("same");
    zclass.add_DataProperty(zdata);
    zclass2.add_DataProperty(zdata1);
    // then
    assertTrue(zclass.equals(zclass2));
    assertFalse(zclass.getZDataPropertyItem(data).equals(zclass2.getZDataPropertyItem(data1)));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertEquals(zclass.hashCode(), zclass2.hashCode());
  }

  @Test
  public void testzClassWhenequalsObjectProperty()
      throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    String object = "http://www.test.com#ObjectProperty";
    String data1 = "http://www.test.com#DataProperty1";
    IRI iriObject = IRI.create(object);
    IRI iridata1 = IRI.create(data1);
    ZObjectProperty Zobj = new ZObjectProperty(iriObject);
    ZDataProperty zdata1 = new ZDataProperty(iridata1);
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);
    zclass2.setComments("this is same");
    zclass.setComments("this is same");
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    zclass.setLabel("same");
    zclass2.setLabel("same");
    zclass.add_ObjectProperty(Zobj);
    zclass2.add_DataProperty(zdata1);
    // then
    assertTrue(zclass.equals(zclass2));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertEquals(zclass.hashCode(), zclass2.hashCode());
    assertNotEquals(zclass.getZObjectPropertyList().hashCode(),
        zclass2.getZObjectPropertyList().hashCode());
    assertFalse(zclass.getZObjectPropertyList().equals(zclass2.getZObjectPropertyList()));
    assertTrue(Zobj.equals(Zobj));
  }

  @Test
  public void testzZClassEqualsParent() throws NoSuchFieldException, IllegalAccessException {
    // given
    String parentiriStr = "http://www.test.com#Parent";
    IRI iriParetn = IRI.create(parentiriStr);
    ZClass Zparent = new ZClass(iriParetn);
    String iriStr = "http://www.test.com#test";
    String object = "http://www.test.com#ObjectProperty";
    String data1 = "http://www.test.com#DataProperty1";
    IRI iriObject = IRI.create(object);
    IRI iridata1 = IRI.create(data1);
    ZObjectProperty Zobj = new ZObjectProperty(iriObject);
    ZDataProperty zdata1 = new ZDataProperty(iridata1);
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);
    zclass2.setComments("this is same");
    zclass.setComments(null);
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    zclass.setLabel(null);
    zclass2.setLabel("not the same");
    zclass.add_ObjectProperty(Zobj);
    zclass.add_ParentClass(Zparent);
    zclass2.add_DataProperty(zdata1);
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    // then
    assertFalse(zclass.getParentToExtend().equals(zclass2.getParentToExtend()));
    assertTrue(zclass.getImportsDeclarations().equals(zclass2.getImportsDeclarations()));
    assertTrue(zclass.getListOfImports().equals(zclass2.getListOfImports()));
    assertTrue(zclass.getImportsDeclarationsFromObjectPropertiesInstances()
        .equals(zclass2.getImportsDeclarationsFromObjectPropertiesInstances()));
    assertTrue(zclass.hasSubClass());
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
  }

  @Test
  public void testzClassImportDec() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    String iriStr1 = "http://www.test.com#class";
    IRI iri1 = IRI.create(iriStr1);
    ZClass zclass1 = new ZClass(iri1);

    String instance = "http://www.test.com#Instance";
    IRI iriInstance = IRI.create(instance);
    ZInstance ZInstance = new ZInstance(iriInstance);

    String instance1 = "http://www.test.com#Instance1";
    IRI iriInstance1 = IRI.create(instance1);
    ZInstance ZInstance1 = new ZInstance(iriInstance1);

    zclass.add_ZInstanceClass(ZInstance);
    zclass1.add_ZInstanceClass(ZInstance1);
    // then
    assertFalse(zclass.getzClassName().equals(zclass1.getzClassName()));
    assertFalse(zclass.getListZInstanceIRI().equals(zclass1.getListZInstanceIRI()));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertNotEquals(zclass.hashCode(), zclass1.hashCode());
  }

  @Test
  public void testzClassPathEquals() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    String iriStr1 = "http://www.test.com#test1";
    IRI iri1 = IRI.create(iriStr1);
    ZClass zclass1 = new ZClass(iri1);
    zclass.setPath(iriStr);
    zclass1.setPath(iriStr1);
    // then
    assertFalse(zclass.getPath().equals(zclass1.getPath()));
    assertTrue(zclass.getClass().equals(zclass1.getClass()));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertNotEquals(zclass.hashCode(), zclass1.hashCode());
  }

  @Test
  public void testzInstanceEquals() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);

    String Data = "http://www.test.com#dataProp";
    IRI iriData = IRI.create(Data);
    ZDataProperty zData = new ZDataProperty(iriData);

    String iriStr1 = "http://www.test.com#test1";
    IRI iri1 = IRI.create(iriStr1);
    ZClass zclass1 = new ZClass(iri1);

    String iriInstance = "http://www.test.com#Instance";
    IRI iriInsta = IRI.create(iriInstance);
    ZInstance zInstance = new ZInstance(iriInsta);

    String iriInstance1 = "http://www.test.com#Instance1";
    IRI iriInsta1 = IRI.create(iriInstance1);
    ZInstance zInstance1 = new ZInstance(iriInsta1);

    zInstance.setComments("the same comment");
    zInstance1.setComments("the same comment");

    zInstance.setLabel("the same label");
    zInstance1.setLabel("the same label");

    zInstance.setPath("path");
    zInstance1.setPath("path");

    zInstance.add_DataProperty(zData);
    zInstance1.add_DataProperty(zData);

    zclass.add_ZInstanceClass(zInstance);
    zclass1.add_ZInstanceClass(zInstance1);
    zclass.getListZInstanceIRI().hashCode();
    // then
    assertTrue(
        zInstance.getListZObjectPropertyList().equals(zInstance1.getListZObjectPropertyList()));
    assertTrue(zInstance.getListZDataPropertyList().equals(zInstance1.getListZDataPropertyList()));
    assertTrue(zInstance.getPath().equals(zInstance1.getPath()));
    assertTrue(zInstance.getPackageName().equals(zInstance1.getPackageName()));
    assertTrue(zInstance.getComments().equals(zInstance1.getComments()));
    assertTrue(zInstance.getLabel().equals(zInstance1.getLabel()));
    assertFalse(zInstance.getzInstanceName().equals(zInstance1.getzInstanceName()));
    assertFalse(zclass.equals(zclass1));
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertNotEquals(zclass.hashCode(), zclass1.hashCode());
  }

  @Test
  public void testzZClassObject() throws NoSuchFieldException, IllegalAccessException {
    // given
    String iriStr = "http://www.test.com#test";
    String object = "http://www.test.com#ObjectProperty";
    String object1 = "http://www.test.com#ObjectProperty1";
    String data1 = "http://www.test.com#DataProperty1";
    IRI iriObject = IRI.create(object);
    IRI iriObject1 = IRI.create(object1);
    ZObjectProperty Zobj1 = new ZObjectProperty(iriObject1);
    IRI iridata1 = IRI.create(data1);
    ZObjectProperty Zobj = new ZObjectProperty(iriObject);
    ZDataProperty zdata1 = new ZDataProperty(iridata1);
    IRI iri = IRI.create(iriStr);
    ZClass zclass = new ZClass(iri);
    ZClass zclass2 = new ZClass(iri);
    Zobj1.setLabel(" ");
    Zobj.setLabel("object label");
    Zobj1.setComments("object comment");
    Zobj.setComments(" ");
    zclass2.setComments("this is same");
    zclass.setComments(" ");
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    zclass.setLabel(" ");
    zclass2.setLabel("not the same");
    zclass.add_ObjectProperty(Zobj);

    zclass2.add_DataProperty(zdata1);
    zclass.setHasSubClass();
    zclass2.setHasSubClass();
    // then
    assertTrue(zclass.getImportsDeclarations().equals(zclass2.getImportsDeclarations()));
    assertTrue(zclass.getListOfImports().equals(zclass2.getListOfImports()));
    assertTrue(zclass.getImportsDeclarationsFromObjectPropertiesInstances()
        .equals(zclass2.getImportsDeclarationsFromObjectPropertiesInstances()));
    assertTrue(zclass.hasSubClass());
    assertEquals("Fields didn't match", zclass.getNamespace(), "http://www.test.com#");
    assertFalse(Zobj.getComments().equals(Zobj1.getComments()));
    assertFalse(Zobj.getLabel().equals(Zobj1.getLabel()));
    assertNotEquals(Zobj.getComments().hashCode(), Zobj1.getComments().hashCode());
    assertNotEquals(Zobj.getLabel().hashCode(), (Zobj1.getLabel().hashCode()));
  }
}
