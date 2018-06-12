# OLGA Developers Guide

This page contains information for developers on how to extend OLGA. This developers guide is intended for advanced users who want to extend OLGA further and the project developers. If you are new to OLGA you should start with the [User Guide](https://github.com/EcoStruxure/OLGA/wiki/User-Guide) before moving onto this guide.

## I. Architecture & Design
This section depicts the architecture of OLGA. 

OLGA is a multi-library code generator, it takes four mandatory parameters as input: 
* Code: java, cs, or py
* Library: the generated library will depend either on a serializer or a an object relational mapper, such as RDF4J, Trinity
* Name: Give name by the user for the generated library
* Path to a folder containing one or more ontologies since an ontology can depend on other ontologies.

Thus, OLGA completes already existing libraries ([RDF4J](http://rdf4j.org/), [Trinity](https://bitbucket.org/semiodesk/trinity), and others).

The architecture of OLGA is outlined in the figure below:
![OLGA Architecture](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/OLGA.png)

The architecture consists of the following modules:

### I) a. Menu
OLGA relies [Apache Commons Cli](https://commons.apache.org/proper/commons-cli/) to support user input for the various options.

### I) b. Importer
Relies on the [OWL API](https://github.com/owlcs/owlapi) to load into memory one or more ontologies and merges them into one ontology easier to visit.

### c. Visitor
OLGA implements a visitor pattern to traverse all the elements of a given ontology provided by the Importer. The visitor crosses the following elements: Classes, ObjectProperties, DataProperties, Individuals, Literals, and the various
axioms to populate the internal model. The visitor module relies on OWL API.

### I) d. Internal Model
The Model allows capturing the ontology information (T-Box) independent of any targeted library or programming language. Separating the model from any targeted implementation offers OLGA a huge flexibility making the support for an additional language or a dependent library simply a matter of adding templates. The model is populated by the visitor, consists of a representation layer which captures all the elements of an ontology, it is inspired by the work of [Kalyanpur et al](https://pdfs.semanticscholar.org/5af1/38779ab343a802aa29e93ca96d347f393f7f.pdf).

The following figure outline the various elements of the model:
![](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/OLGAModel.png)

* Class: Each Class can have none (owl:Thing) or multiple super classes populated by the visitor based on the owl:SubClassOf. The namespace, packageName, and className are extracted based on the Class IRI for the code generator.
A Class may have none or several ObjectProperties. 

The visitor populates the following parameters for each ObjectProperty: a restriction type (owl:AllValuesFrom), an optional restriction cardinality (owl:minCardinality), a restriction number associated with the cardinality, a possible one or more characteristic of the property (owl:TransitiveProperty), and an optional expression (owl:UnionOf). 

* An ObjectProperty associates the following concepts:
  * Class-to-Class(es): one or several range classes depending
on the restriction type and the expression. For example, a
Building contains Some Floor.
  * Class-to-Individual(s): one or several range individuals depending on the restriction type and the Expression. For example, a TemperatureMeasure Class hasUnit Degree_Celsius.
  * Individual-to-Individual: the restriction type, cardinality, expression, and characteristic are not populated by the visitor when an individual is associated to another individual. For example, a Building1 contains Floor1. Only the name parameter is used in the ObjectProperty for the code generation.

* An Individual is an instance of a class. The visitor fills the namespace and the packageName for the code generator based on its IRI.

* DataProperty is associated with an Individual, the visitor populates only the range (Literal). However, when the DataProperty is associated with a class, the visitor populates in addition to the previous parameters, the restriction type and the restriction cardinality.

### I) e. Templates
They are arranged by library dependency, a serializer (RDF4J) or ORM (Trinity). Each template contains a
code snippet written according to a programming language syntax (Java, C#, Python, or others) and holds several information awaiting to be filled from the model such as the imports declaration of packages related to the dependent library, name of the class/interface to be generated. In addition, each of the data and object property will be transformed into a parameter with getter/setter functions. These templates will be loaded in memory by the Generator and
the code snippets will be completed according to the information populated in the model. OLGA relies on [FreeMarker](https://freemarker.apache.org) as a template engine. The figure below shows a simple example of a template.

```csharp
namespace ${packageName}
{
    <#if Zclass.getLabel()??>
    <#noautoesc>
    ${'/// <remarks>'}
    </#noautoesc>
    /// Class ${Zclass.getLabel()}
    <#noautoesc>
    ${'/// </remarks>'}
    </#noautoesc>
    </#if>
    <#if Zclass.getComments()??>
    /// <summary>
    <#list Zclass.getComments()?split("\n") as line>
    /// ${line}
    </#list>
    /// </summary>
    </#if><#list motherClassList as SuperZClassCurrentElementOfList>
    /// <inheritdoc cref="I${SuperZClassCurrentElementOfList.getzClassName()}"/>
    </#list>
    [GeneratedCode("OLGA Generator", "${OLGAVersion}")]
    <#assign interfaceName = 'I' + Zclass.getzClassName()/>
    public<#if generatePartial!false> partial</#if> interface ${interfaceName} ... </#if>
    { 
      ... 
    }
```

### I) f. Generator
Based on the selected library dependency, adequate templates are loaded into memory to initiate the code generation
from the populated model. The Generator injects the information from the model into the templates. The separation between the model and the templates provides flexibility and makes supporting an additional library a matter of templates extension. 

In the case of a selected ORM dependency, each Class and Individual of the ontology will be generated into an interface. In fact, an ORM library provides a factory allowing developers to instantiate the interfaces into objects. OLGA handles multiple inheritance and composition by generating interfaces extending other interfaces. 

This makes the code generation simpler since only the interfaces need to be generated. Developers will rely on the factory to instantiate their objects based on the generated interfaces, therefore, the ORM’s factory will handle multiple extensions of the interfaces and their declared functions.

### I) g. Compiler & Packager
Once the code is injected into the templates, the generator creates files containing the expected code.
Then, the compilation and packaging phase can start. According to the selected library and its programming language a compiler is loaded. Once the compilation ends successfully, the packaging is triggered to prepare the adequate format (.jar, .dll, .whl, or others).

OLGA relies on the following maven plugins to compile and package:
* DotNet: [dotnet-maven-plugin](https://github.com/kaspersorensen/dotnet-maven-plugin)
* Java: [maven-compiler-plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
* Python: [exec-maven-plugin](https://www.mojohaus.org/exec-maven-plugin/) requires a working installation of Python on the host.

## II. General Coding Information
This section covers how to contribute to OLGA and is primarily intended for existing/prospective contributors.

### II) a. Compiling
Compiling OLGA may be done using Maven only from:
* Command line
* IDE (Eclipse or IntelliJ with embedded maven)

### II) b. Coding Style
We adopt the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) to implement and extend OLGA.

For the generated code, we adopted the following Style Guides:
* Java: [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
* C#: [Microsoft Style Guide](https://docs.microsoft.com/en-us/dotnet/csharp/programming-guide/inside-a-program/coding-conventions)
* Python: [PEP 8 Style Guide](https://www.python.org/dev/peps/pep-0008/)


### II) c. Deprecation Policy
We follow the [Oracle Deprecation Policy](https://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/deprecation/deprecation.html) for OLGA code.

For the generated code, we adopted the following deprecation policies:
##### Java
We follow the [Oracle Policy](https://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/deprecation/deprecation.html)

##### C#
For C# Our deprecation policy is a very simple three step process.
* Step 1 - Obsolete but Usable
Firstly APIs to be removed must be marked with the Obsolete attribute with the boolean parameter set to false such that the code can still be used. The warning message should direct users to alternative/replacement APIs that are available and when we expect to remove the API.
There must be at least one release where this is present and the change must be noted in the Change Log for that release.
If the API is included in any documentation on the website it should be updated to indicate that the API is being deprecated or to use the alternative APIs

* Step 2 - Obsolete
In a release subsequent to the Obsolete attribute having been added the boolean parameter must be set to true so that using this code now results in a compiler error. The warning message may be updated to further clarify alternative/replacement APIs and when we will remove the API.
Again there must be at least one release where this is present and the change must be noted in the Change Log for that release. Ideally an API should only remain in this state for a single release.
If the API is included in any documentation on the website it must be updated to indicate that the API is obsolete and documentation is preserved only for historical reference.

* Step 3 - Remove
The API must be fully removed from the library and this must be noted in the Change Log of the release where this happens.
Documentation pertaining to this API must be removed or clearly marked as pertaining to an obsolete unsupported API.

##### Python
Policy To Be Determined 

### II) d. Test Environment
Our test environment is internal for now, it has 3 levels of tests:

* Level 1: Unit Tests: with JUnit for OLGA
* Level 2: Generated Code: OLGA generates code from a model, so we need to put in place, tests to make sure the generated code is what is expected to be. 
* Level 3: Integration with a [TripleStore](stardog.com): this generated code will be used by an application to instantiate an ontology and run queries on an ontology database

So far, Level 1 and part of Level 2 are in place.
