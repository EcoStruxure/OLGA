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

### Importer
Relies on the [OWL API](https://github.com/owlcs/owlapi) to load into memory one or more ontologies and merges them into one ontology easier to visit.

### Visitor
OLGA implements a visitor pattern to traverse all the elements of a given ontology provided by the Importer. The visitor crosses the following elements: Classes, ObjectProperties, DataProperties, Individuals, Literals, and the various
axioms to populate the internal model.

### Internal Model
The Model allows capturing the ontology information (T-Box) independent of any targeted library or programming language. Separating the model from any targeted implementation offers OLGA a huge flexibility making the support for an additional language or a dependent library simply a matter of adding templates. The model is populated by the visitor, consists of a representation layer which captures all the elements of an ontology, it is inspired by the work of [Kalyanpur et al](https://pdfs.semanticscholar.org/5af1/38779ab343a802aa29e93ca96d347f393f7f.pdf).



## II. Developing for OLGA
This section covers how to contribute to OLGA and is primarily intended for existing/prospective contributors.

### Menu & Options
OLGA relies [Apache Commons Cli](https://commons.apache.org/proper/commons-cli/) to support user input for the various options.

## III. General Coding Information
### Compiling
### Code Style
### Deprecation Policy
### Test Environment
