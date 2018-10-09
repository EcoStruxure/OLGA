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
package semanticstore.ontology.library.generator.model.xsd;

import java.util.HashMap;
import java.util.Map;
import semanticstore.ontology.library.generator.model.ZPair;

public class XsdMapConfig {

  /**
   * Usage / Design: - We map xsd types to java type names; - Then for each java type name the
   * corresponding method to get the value can be derived. This method is used to get the method
   * name, type... from the templates.
   */

  private final static Map<String, ZPair<String, String>> xsd2cSharpName =
      new HashMap<String, ZPair<String, String>>();

  private final static Map<String, ZPair<String, String>> xsd2JavaName =
      new HashMap<String, ZPair<String, String>>();

  private final static Map<String, ZPair<String, String>> xsd2pythonName =
      new HashMap<String, ZPair<String, String>>();

  public synchronized static Map<String, ZPair<String, String>> getXsd2cSharpName() {
    return xsd2cSharpName;
  }

  public synchronized static Map<String, ZPair<String, String>> getXsd2JavaName() {
    return xsd2JavaName;
  }

  public synchronized static Map<String, ZPair<String, String>> getXsd2PythonName() {
    return xsd2pythonName;
  }

  static {
    xsd2cSharpName.put("xsd:ENTITY", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:ID", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:IDREF", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:IDREFS", new ZPair<String, String>("string[]", null));
    xsd2cSharpName.put("xsd:NCName", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:NMTOKEN", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:NMTOKENS", new ZPair<String, String>("string[]", null));
    xsd2cSharpName.put("xsd:NOTATION", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:Name", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:QName", new ZPair<String, String>("XmlQualifiedName", "System.Xml"));
    xsd2cSharpName.put("xsd:anyURI", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:base64Binary", new ZPair<String, String>("Byte[]", "System"));
    xsd2cSharpName.put("xsd:boolean", new ZPair<String, String>("bool", null));
    xsd2cSharpName.put("xsd:byte", new ZPair<String, String>("SByte", "System"));
    xsd2cSharpName.put("xsd:date", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:dateTime", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:dateTimeStamp", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:decimal", new ZPair<String, String>("decimal", null));
    xsd2cSharpName.put("xsd:double", new ZPair<String, String>("double", null));
    xsd2cSharpName.put("xsd:duration", new ZPair<String, String>("TimeSpan", "System"));
    xsd2cSharpName.put("xsd:float", new ZPair<String, String>("float", null));
    xsd2cSharpName.put("xsd:gDay", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:gMonth", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:gMonthDay", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:gYear", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:gYearMonth", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:hexBinary", new ZPair<String, String>("Byte[]", "System"));
    xsd2cSharpName.put("xsd:int", new ZPair<String, String>("int", null));
    xsd2cSharpName.put("xsd:integer", new ZPair<String, String>("decimal", null));
    xsd2cSharpName.put("xsd:language", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:long", new ZPair<String, String>("long", null));
    xsd2cSharpName.put("xsd:negativeInteger", new ZPair<String, String>("decimal", null));
    xsd2cSharpName.put("xsd:nonNegativeInteger", new ZPair<String, String>("decimal", null));
    xsd2cSharpName.put("xsd:nonPositiveInteger", new ZPair<String, String>("decimal", null));
    xsd2cSharpName.put("xsd:normalizedString", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:positiveInteger", new ZPair<String, String>("decimal", null));
    xsd2cSharpName.put("xsd:short", new ZPair<String, String>("Int16", "System"));
    xsd2cSharpName.put("xsd:string", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:time", new ZPair<String, String>("DateTime", "System"));
    xsd2cSharpName.put("xsd:token", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("xsd:unsignedByte", new ZPair<String, String>("Byte", "System"));
    xsd2cSharpName.put("xsd:unsignedInt", new ZPair<String, String>("UInt32", "System"));
    xsd2cSharpName.put("xsd:unsignedLong", new ZPair<String, String>("UInt64", "System"));
    xsd2cSharpName.put("xsd:unsignedShort", new ZPair<String, String>("int", null));
    xsd2cSharpName.put("xsd:Literal", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("http://www.w3.org/2000/01/rdf-schema#Literal",
        new ZPair<String, String>("string", null));
    xsd2cSharpName.put("rdfs:Literal", new ZPair<String, String>("string", null));
    xsd2cSharpName.put("rdf:PlainLiteral", new ZPair<String, String>("string", null));
  }

  static {
    xsd2JavaName.put("xsd:ENTITY", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:ID", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:IDREF", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:IDREFS", new ZPair<String, String>("String[]", null));
    xsd2JavaName.put("xsd:NCName", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:NMTOKEN", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:NMTOKENS", new ZPair<String, String>("String[]", null));
    xsd2JavaName.put("xsd:NOTATION", new ZPair<String, String>("QName", "javax.xml.namespace"));
    xsd2JavaName.put("xsd:Name", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:QName", new ZPair<String, String>("QName", "javax.xml.namespace"));
    xsd2JavaName.put("xsd:anyURI", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:base64Binary", new ZPair<String, String>("byte[]", null));
    xsd2JavaName.put("xsd:boolean", new ZPair<String, String>("boolean", null));
    xsd2JavaName.put("xsd:byte", new ZPair<String, String>("byte", null));
    xsd2JavaName.put("xsd:date",
        new ZPair<String, String>("XMLGregorianCalendar", "javax.xml.datatype"));
    xsd2JavaName.put("xsd:dateTime",
        new ZPair<String, String>("XMLGregorianCalendar", "javax.xml.datatype"));
    xsd2JavaName.put("xsd:dateTimeStamp", new ZPair<String, String>("Date", "java.util"));
    xsd2JavaName.put("xsd:decimal", new ZPair<String, String>("BigDecimal", "java.math"));
    xsd2JavaName.put("xsd:double", new ZPair<String, String>("double", null));
    xsd2JavaName.put("xsd:duration", new ZPair<String, String>("Duration", "javax.xml.datatype"));
    xsd2JavaName.put("xsd:float", new ZPair<String, String>("float", null));
    xsd2JavaName.put("xsd:gDay", new ZPair<String, String>("Date", "java.util"));
    xsd2JavaName.put("xsd:gMonth", new ZPair<String, String>("Date", "java.util"));
    xsd2JavaName.put("xsd:gMonthDay", new ZPair<String, String>("Date", "java.util"));
    xsd2JavaName.put("xsd:gYear", new ZPair<String, String>("Date", "java.util"));
    xsd2JavaName.put("xsd:gYearMonth", new ZPair<String, String>("Date", "java.util"));
    xsd2JavaName.put("xsd:hexBinary", new ZPair<String, String>("byte[]", null));
    xsd2JavaName.put("xsd:int", new ZPair<String, String>("int", null));
    xsd2JavaName.put("xsd:integer", new ZPair<String, String>("BigInteger", "java.math"));
    xsd2JavaName.put("xsd:language", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:long", new ZPair<String, String>("long", null));
    xsd2JavaName.put("xsd:negativeInteger", new ZPair<String, String>("BigDecimal", "java.math"));
    xsd2JavaName.put("xsd:nonNegativeInteger",
        new ZPair<String, String>("BigDecimal", "java.math"));
    xsd2JavaName.put("xsd:nonPositiveInteger",
        new ZPair<String, String>("BigDecimal", "java.math"));
    xsd2JavaName.put("xsd:normalizedString", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:positiveInteger", new ZPair<String, String>("BigDecimal", "java.math"));
    xsd2JavaName.put("xsd:short", new ZPair<String, String>("short", null));
    xsd2JavaName.put("xsd:string", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:time",
        new ZPair<String, String>("XMLGregorianCalendar", "javax.xml.datatype"));
    xsd2JavaName.put("xsd:token", new ZPair<String, String>("String", null));
    xsd2JavaName.put("xsd:unsignedByte", new ZPair<String, String>("Short", null));
    xsd2JavaName.put("xsd:unsignedInt", new ZPair<String, String>("long", null));
    xsd2JavaName.put("xsd:unsignedLong", new ZPair<String, String>("long", null));
    xsd2JavaName.put("xsd:unsignedShort", new ZPair<String, String>("int", null));
    xsd2JavaName.put("xsd:Literal", new ZPair<String, String>("String", null));
    xsd2JavaName.put("http://www.w3.org/2000/01/rdf-schema#Literal",
        new ZPair<String, String>("String", null));
    xsd2JavaName.put("rdfs:Literal", new ZPair<String, String>("String", null));
    xsd2JavaName.put("rdf:PlainLiteral", new ZPair<String, String>("String", null));
  }

  static {
    xsd2pythonName.put("xsd:ENTITY", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:ID", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:IDREF", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:IDREFS", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:NCName", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:NMTOKEN", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:NMTOKENS", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:NOTATION", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:Name", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:QName", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:anyURI", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:base64Binary", new ZPair<String, String>("base64", null));
    xsd2pythonName.put("xsd:boolean", new ZPair<String, String>("boolean", null));
    xsd2pythonName.put("xsd:byte", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:date", new ZPair<String, String>("date", null));
    xsd2pythonName.put("xsd:dateTime", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:decimal", new ZPair<String, String>("Decimal", null));
    xsd2pythonName.put("xsd:double", new ZPair<String, String>("Double", null));
    xsd2pythonName.put("xsd:duration", new ZPair<String, String>("TimeSpan", null));
    xsd2pythonName.put("xsd:float", new ZPair<String, String>("Single", null));
    xsd2pythonName.put("xsd:gDay", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:gMonth", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:gMonthDay", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:gYear", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:gYearMonth", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:dateTimeStamp", new ZPair<String, String>("datetime", null));
    xsd2pythonName.put("xsd:hexBinary", new ZPair<String, String>("Byte[]", null));
    xsd2pythonName.put("xsd:int", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:integer", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:language", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:long", new ZPair<String, String>("long", null));
    xsd2pythonName.put("xsd:negativeInteger", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:nonNegativeInteger", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:nonPositiveInteger", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:normalizedString", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:positiveInteger", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:short", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:string", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:time", new ZPair<String, String>("time", null));
    xsd2pythonName.put("xsd:token", new ZPair<String, String>("None", null));
    xsd2pythonName.put("xsd:unsignedByte", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:unsignedInt", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:unsignedLong", new ZPair<String, String>("long", null));
    xsd2pythonName.put("xsd:unsignedShort", new ZPair<String, String>("int", null));
    xsd2pythonName.put("xsd:Literal", new ZPair<String, String>("None", null));
    xsd2pythonName.put("http://www.w3.org/2000/01/rdf-schema#Literal",
        new ZPair<String, String>("None", null));
    xsd2pythonName.put("rdf:PlainLiteral", new ZPair<String, String>("None", null));
  }

}
