# OLGA: an Ontology Library GenerAtor

## Welcome !
OLGA is a generic tool aiming to accelerate the adoption of [Standard W3C Semantic technology](https://www.w3.org/standards/semanticweb/) among developers.

OLGA is licensed under the MIT License.
Schneider Electric requests contributions to be provided back to benefit the community.

- [OLGA: an Ontology Library GenerAtor](#olga--an-ontology-library-generator)
  * [Welcome !](#welcome--)
    + [Why? : Enhance Developer Experience](#why----enhance-developer-experience)
    + [What? : An Ontology Library Generator](#what----an-ontology-library-generator)
    + [How? : A Model-based Engineering Approach](#how----a-model-based-engineering-approach)
      - [Published work](#published-work)
    + [Current Features](#current-features)
      - [Trinity](#trinity)
      - [Coming soon: RDF4J support](#coming-soon--rdf4j-support)
      - [Supported Ontologies](#supported-ontologies)
  * [Getting Started](#getting-started)
  * [Asking Questions and Reporting Bugs](#asking-questions-and-reporting-bugs)
  * [Pull Requests](#pull-requests)
  * [Acknowledgments](#acknowledgments)
    + [Developers](#developers)
    + [Contributors](#contributors)
      - [How to cite this work](#how-to-cite-this-work)
    + [Dependencies](#dependencies)

### Why? : Enhance Developer Experience
1. Reduce friction barrier for developers when working with an ontology model.
2. Accelerates development of ontology based systems.
3. Eliminates complexity by providing Object Oriented libraries for developers.

### What? : An Ontology Library Generator
OLGA is based on a model driven approach taking as input an ontology file expressed in one of the supported W3C supported standards ([RDF](https://www.w3.org/2001/sw/wiki/RDF), [OWL](https://www.w3.org/OWL)) and generating a library conform to the ontology model.

The generated library is then imported and used to programmatically:
1. Generate an ontology instance conform to the ontology model.
2. Query the generated ontology instance by relying on Object Oriented Classes.

### How? : A Model-based Engineering Approach
OLGA complements existing OWL, RDF serializers such as [RDF4J](http://rdf4j.org/) or RDF Object Relational Mappers such as [Trinity](https://bitbucket.org/semiodesk/trinity), and [RDFAlchemy](http://rdfalchemy.readthedocs.io/en/latest/index.html) in Python by automatically generating compliant code with such libraries. 

OLGA takes as input the following:
1. One or more Ontology
2. A parameter indicating the dependency of the generated library.
	1. Trinity - C#
	2. RDF4J - Java
	3. DotNetRDF - C#
	3. RDFAlchemy - Python

The output of OLGA is a generated library ready to be used by IoT developers. The generated library is conform to the ontology and is dependent on available serializers and ORMs in various languages (C#, Java, and Python).

#### Published work
For more info, refer to the [OLGA paper](./docs/OLGA-Semantics.pdf) presented at SEMANTICS'13- SIS-IoT Workshop, 2017.

### Current Features
The current version of OLGA supports the following:

#### Trinity 
Code generation compliant with [Trinity](https://bitbucket.org/semiodesk/trinity) ORM
* Lambda Expressions (LINQ) to SPARQL transformation thanks to Trinity
* Object Relational Mapping from C# classes to RDF/OWL
* In Memory Persistence
* Persistence Database based on DotNetRDF (Stardog, Virtuoso, and others)

#### Coming soon: RDF4J support
The PoC in [OLGA paper](./docs/OLGA-Semantics.pdf) supported various libraries, we decided to rewrite OLGA and started with Trinity. Other libraries will be added soon such as RDF4J. 

#### Supported Ontologies
OLGA has been tested with the following ontologies:
- [x] [Saref](http://ontology.tno.nl/saref/)
- [x] [OneM2M](http://www.onem2m.org/ontology/Base_Ontology/oneM2M_Base_Ontology-V_3_2_0.owl)
- [x] [Provenance](https://www.w3.org/TR/prov-o/)
- [x] [Semantic Sensor Network](https://www.w3.org/TR/vocab-ssn/)
- [x] [Brick](https://brickschema.org/download/)

and many other internal ontologies.

## Getting Started
To get started with using OLGA you may want to check out the following resources:

[User Guide](https://github.com/EcoStruxure/OLGA/wiki/User-Guide) - how to use various features of the library

[Hello World](https://github.com/EcoStruxure/OLGA/wiki/Hello-World) - a hello world example 

[Developer Guide](https://github.com/EcoStruxure/OLGA/wiki/DeveloperGuide) - how to extend OLGA


## Asking Questions and Reporting Bugs
Bugs and feature requests can be submitted to our [issues list](https://github.com/EcoStruxure/OLGA/issues) on GitHub. When submitting a bug report, please include as much detail as possible. Code and/or data that reproduces the problem you are reporting will make it much more likely that your issue gets addressed quickly.

## Pull Requests
We are always pleased to receive pull requests that fix bugs or add features. When fixing a bug, please make sure that it has been reported on the issues list first. If you plan to work on a new feature for OLGA, it would be good to raise that on the issues list before you commit too much time to it.

## Acknowledgments
OLGA became open source thanks to the support of Schneider Electric and EcoStruxure leadership.

### Developers
OLGA is developed by the following people:
* [Charbel Kaed](https://github.com/charbull) (Architect & Dev)
* [Andre Ponnouradjane](https://github.com/ponnou) (Dev, 2017-2018)
* Alber Tadrous (Dev)
* Farrukh Bek (QA)

Those who contribute over time will be invited to join the project as developers.

### Contributors
* [Mark Stemmler](https://github.com/DarthStem) for his advice regarding C# code generation along with his help with Trinity.
* SemioDesk Trinity team (Sebastian and Moritz) for their development effort to extend Trinity in addition to their guidance and support.

#### How to cite this work
```
@inproceedings{Kaed2017AMD,
  title={A Model Driven Approach Accelerating Ontology-based IoT Applications Development},
  author={Charbel El Kaed and Andre Ponnouradjane},
  booktitle={SEMANTICS'13- SIS-IoT Workshop},
  year={2017}
}
```

### Dependencies 
OLGA uses the following 3rd party libraries:

| Library           |	Licenses  |
|-------------------|-------------|
| Trinity 			| MIT License |
| DotNetRDF			| MIT License |
| commons-cli 		| Apache License, Version 2.0 | 
| junit 			| Eclipse Public License 1.0 | 
| owlapi-api 		| Apache License Version 2.0| 
| commons-lang3 	| Apache License, Version 2.0 | 
| commons-text 		| Apache License, Version 2.0 | 
| maven-model 		| Apache License, Version 2.0 | 
| maven-invoker-plugin | Apache License, Version 2.0 | 
| evo-inflector 	| Apache License, Version 2.0 | 
| freemarker		| Apache License, Version 2.0 | 
| slf4j-api | -  | 
