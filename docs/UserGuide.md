# OLGA User Guide

## Getting Started
Welcome to OLGA user guide, this provides an introduction to OLGA and how it can be used to instantiate an Ontology Model

## Table Of Content
* [Prerequisite](#prerequisite)
  * [Compile & Package](#compile---package)
  * [Run](#run)
  * [Options](#options)
  * [Restrictions](#restrictions)
      - [1. Reserved keywords](#1-reserved-keywords)
      - [2. Special Characters](#2-special-characters)
      - [3. Empty Strings](#3-empty-strings)
      - [4. Ontology Interdependencies](#4-ontology-interdependencies)
      - [5. Constraints levels](#5-constraints-levels)
      - [6. Individuals not conform to their Class Object/Data Properties](#6-individuals-not-conform-to-their-class-object-data-properties)
      - [7. OWL Unions and Intersections](#7-owl-unions-and-intersections)
      - [8. URI Normalization in .Net](#8-uri-normalization-in-net)
      - [9. Code Generation](#9-code-generation)
        * [Naming Conventions](#naming-conventions)
        * [Axioms](#axioms)
  * [Hello World Example](#hello-world-example)
    
    
## Prerequisite
OLGA is Java 8 based and relies on Maven to compile the generated code.
Therefore, the following must be installed:
* Java 8 (JDK)
* [Maven](https://maven.apache.org/install.html) with **M2_HOME** must be set as an environment variable name
* [.Net SDK](https://www.microsoft.com/net/learn/get-started/windows) for C# libraries build.

## Compile & Package
After the prerequisites are installed and the **M2_home** is set, use the following maven command to compile the source code:
```console
mvn clean package
```
To skip the tests, use the following command:
```console
mvn clean package -DskipTests=true
```

##  Run
The previous phase packages the dependencies in a single jar ready to be used to start generating libraries from ontology files.
 
The following command generates a library from [Saref](http://ontology.tno.nl/saref.ttl) ontology:
```console
java -jar target\OLGA-0.0.3-with-dependencies.jar --code cs --library trinity --name Saref --path .\src\test\resources\saref\
``` 

This command will generate a saref-Trinity.dll at the following path:

> `.\OLGA\generated\Saref-dotnetTrinity\bin\Release\netstandard2.0\`

## Options

OLGA can be configured with the following options:

| Option    | Argument | Description | Mandatory |
|-----------|----------|-------------|-----------|
| --path | path | a folder path to one or more ontology | Mandatory |
| --code | code label | generated code: cs, java, or py  | Mandatory |
| --library | library label | dependency on a library: Trinity, RDF4J  | Mandatory |
| --name | ${string} | Desired Generated Library Name | Mandatory |
| --out | path | a path to output directory | Optional |
| -preserve | None | relies on the Ontology URI when generating folder structure. When two or more ontologies are given as an input to OLGA it is preferable to use "-preserve" option to avoid overwriting the identical names entities | Optional |
| --version | `<major>.<minor>.<build>.<revision>` | By default OLGA assigns the Ontology version to the generated library. When the version is missing, OLGA version is assigned to the generated library. This option allows the user to force the version of the generated library | Optional |
| -partial | None | Relevant for C# code generation only, appends the keyword `partial` to the generated code | Optional |

 
## Restrictions
OLGA is a powerful tool aiming to generate libraries from ontologies. However, some restrictions apply:

#### 1. Reserved keywords

The following keywords are reserved in both C# and Java as class, package or namespace name, therefore they cannot be used in the Ontology Model. OLGA will detect those keywords and stop the code generation.

These keywords are: 

>* **abstract**, **as**, **assert**, **base**, **bool**, **boolean**, **break**
>* **byte**, **case**, **catch**, **char**, **checked**, **class**, **const**,
>* **continue**, **decimal**, **default**, **delegate**, **do**, **double**, **else**
>* **enum**, **extends**, **event**, **explicit**, **extern**, **false**, **final**
>* **finally**, **fixed**, **float**, **for**, **foreach**, **goto**, **if**
>* **implicit**, **implements**, **import**, **in**, **int**
 **interface**, **internal**
>* **instanceof**, **is**, **lock**, **long**, **native**, **namespace**, **new**
>* **null**, **object**, **operator**, **out**, **override**, **params**, **package**
>* **private**, **protected**, **public**, **readonly**, **ref**, **return**, **sbyte**
>* **sealed**, **short**, **sizeof**, **stackalloc**, **strictfp**, **super**, **static**
>* **string**, **struct**, **switch**, **this**, **transient**, **throw**, **throws**
>* **true**, **try**, **typeof**, **uint**, **ulong**, **unchecked**, **unsafe**
>* **ushort**, **using**, **using static**, **virtual**, **void**, **volatile**, **while**
      
#### 2. Special Characters

Cannot use special characters in the URI or class name. 
These characters are: {`~!@#$%^&*()-+={}[]\|:;"'?/<>,.}.

#### 3. Empty Strings

Cannot start any entity name "Class, object property or data property" with number or empty string.

#### 4. Ontology Interdependencies

When an ontology is dependent on one or other ontologies:

* The ontology and its dependencies need to be placed in the same folder.
* If Protégé is used, make sure to remove the file path dependencies.
* When two or more ontologies are given as an input to OLGA it is advisable to use "-preserve" option to avoid overwriting the identical names entities.


#### 5. Constraints levels 
When an Object/Data property is identified more than once OLGA will prioritize according to the following priorities (higher to lower):

* exactly
* max
* min
* only 
* some


In the following example:

(1) `Alice knows Bob`

(2) `Alice knows only Bob`

OLGA will take into consideration the more restrictive case (2) since `owl:Restriction` is more restrictive than `owl:ObjectProperty`.

 
#### 6. Individuals not conform to their Class Object/Data Properties 
Individuals with new Object/Data properties which are non existent on the Class will not be generated by OLGA.

#### 7. OWL Unions and Intersections 
The Ontology language offers more expressivity than the object oriented model. Therefore, the code generation for union and intersection expressions require a special handling.

Currently, OLGA supports these expressions by splitting the union/intersections into several expressions.
For example, the following expression:

` ComfortSensors measures only (Temperature or Humidity) `

Will be split into the two expressions and code generated as following:

(1) ` ComfortSensors measures only Temperature `

(2) ` ComfortSensors measures only Humidity `

#### 8. URI Normalization in .Net

According to the [RFC3986](https://tools.ietf.org/html/rfc3986) the generic URI syntax consists of a hierarchical sequence of components referred to as the scheme, authority, path, query, and fragment.
```
URI = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
hier-part   = "//" authority path-abempty
               / path-absolute
               / path-rootless
               / path-empty 
```

In simple terms the .Net implementation will transform the following URI:
 `http://www.example.com#MyConcept` to `http://www.example.com/#MyConcept` by adding an extra **/** before **#MyConcept**.
 
 This can cause problems depending whether a persistence triple store will consider both forms the same or not. If they are not considered the same, SPARQL queries or LINQ queries will not return any result. 
 
#### 9. Code Generation 
OLGA generates code based on an Ontology model. 

##### Naming Conventions
The Semantic community established [best practices](http://perfectsemanticweb.appspot.com/documentation/SemanticWebBestPracticesForDummies.pdf?_sm_au_=iNVQTQZ7rPt4Dsw5) when creating models which are very different from the developers best practices and code conventions. 
Therefore, since OLGA is about enabling developers, the code generation has to take the developers best practices into account.

Below is an example where a transformation is applied:
 ObjectProperty convention: `Building hasFloor some Floor` will be transformed to the following C# method
 `List<Floor> Floors`. OLGA will transform `hasFloor some` to a list and transform Floor to its plural form.   
 
##### Axioms

The Ontology axioms are generated in C# as follows:
* `A hasX some B` --> `List<B> X()`
* `A hasX only B` --> `List<B> X()`
* `A hasX max 1 B` --> `B X()`

 
 ## Hello World Example
 Next, try a [hello world](https://github.com/EcoStruxure/OLGA/wiki/Hello-World) example to get started.
