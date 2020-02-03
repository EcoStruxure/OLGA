[![Build Status](https://struxurewarecloud.visualstudio.com/_apis/public/build/definitions/05ca61f1-5a19-4a29-8b05-35664499dffe/490/badge)](https://struxurewarecloud.visualstudio.com/Digital%20Services%20Platform/_build/index?definitionId=490)
[![GitHub stars](https://img.shields.io/github/stars/EcoStruxure/OLGA.svg)](https://github.com/EcoStruxure/OLGA/stargazers)
[![HitCount](http://hits.dwyl.io/EcoStruxure/OLGA.svg)](http://hits.dwyl.io/EcoStruxure/OLGA)


# OLGA: an Ontology SDK

## Welcome !
OLGA (Ontology Library GenerAtor) is a generic tool aiming to accelerate the adoption of [Standard W3C Semantic technology](https://www.w3.org/standards/semanticweb/) among developers. 

OLGA provides a better development experience by focusing on:

* Reducing friction barrier for developers when working with an ontology model.

* Accelerating development of ontology based systems.

* Eliminating complexity by providing Object Oriented libraries for developers.

OLGA is based on a model driven approach taking as input an ontology file expressed in one of the supported W3C supported standards ([RDF](https://www.w3.org/2001/sw/wiki/RDF), [OWL](https://www.w3.org/OWL)) and generating a library conform to the ontology model.

The generated library is then imported and used to programmatically to:
1. Generate an ontology instance conform to the ontology model.
2. Query the generated ontology instance by relying on Object Oriented Model instead of [SPARQL](https://www.w3.org/TR/sparql11-query/).

OLGA is licensed under the [MIT License](./LICENSE.TXT).
Schneider Electric requests contributions to be provided back to benefit the community.

## Getting Started
To get started with OLGA, please check the following resources:

* [Home](https://github.com/EcoStruxure/OLGA/wiki/Home) - general information about OLGA and its supported features

* [User Guide](https://github.com/EcoStruxure/OLGA/wiki/User-Guide) - how to use various features of the library

* [Hello World C#](https://github.com/EcoStruxure/OLGA/wiki/Hello-World) - a hello world example in C# (the most advanced in features) to get you started

* [Hello World Java](https://github.com/EcoStruxure/OLGA/wiki/Hello-World-(Java-RDF4J)) - a hello world example in Java

* [Hello World Python](https://github.com/EcoStruxure/OLGA/wiki/Hello-World-(Python-RDFAlchemy)) - a hello world example in Python

* [Developer Guide](https://github.com/EcoStruxure/OLGA/wiki/Developer-Guide) - how to extend OLGA

* [Webservice User Guide](https://github.com/EcoStruxure/OLGA/wiki/Webservice-User-Guide) - how to use OLGA through a WebService


## Docker

You can build a docker image for hosting the OLGA web service.

To build a new image, run the following command:

```
$ ./build-docker-image.sh
```

There are various environment variable you can set for the build script:

|Environment Variable|Default Value|Description|
|---|---|---|
|OLGA_REPO_URL|https://github.com/EcoStruxure/OLGA.git|OLGA Source Code Repo|
|OLGA_GIT_BRANCH|master|Git branch in source repo to use when cloning OLGA repo|
|OLGA_PROJECT_NAME|OLGA|Project name, used by the Dockerfile to generate artifact paths|
|OLGA_SUBPROJECTS|OLGA-Core,OLGA-Ws|What subprojects we want to build|
|OLGA_ARTIFACT_ID|OLGA-Ws|Maven Artifact ID, used by Dockerfile to generate artifact paths|
|OLGA_VERSION|0.0.5|OLGA Version|
|OLGA_DOCKER_TAG|ecostruxure/olga:latest|Docker tag for image|

**N.B.** the build script skips the tests.

To run the resulting image, run the following command:

```
$ ./docker-run.sh
```

There is a environment variable you can set for the run script:

|Environment Variable|Default Value|Description|
|---|---|---|
|OLGA_DOCKER_TAG|ecostruxure/olga:latest|Docker tag for image|

You can access the web service at `http://localhost:9090`

An additional script, `docker-cleanup.sh` is included to cleanup intermediate docker images created by the build script.
