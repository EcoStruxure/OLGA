## Hello World Example in C#


In this session, we will go through the instantiation of an ontology model (classes and relationships) programmatically. 
We will rely on a set of annotated classes and an Objet-Mapping-Library for RDF to generate a topology instance specific to a site.
The generated topology can be in memory, or stored in an RDF persistent store. It can also be queried in memory or from a peristent store.

![](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/yourLibrary.png)

This library can be part of an IoT application, embedded in a commissioning tool or deployed on a system.

## Table of Contents
- [1. Generate a Library from the Demo Ontology](#1-generate-a-library-from-the-demo-ontology)
- [2. Create a .Net project](#2-create-a-net-project)
- [3. Initialization](#3-initialization)
  * [In Memory usage](#in-memory-usage)
- [4. Instantiate the Ontology using the Generated Library](#4-instantiate-the-ontology-using-the-generated-library)
  * [a. Serialize to file](#a-serialize-to-file)
  * [b. Persistence into a Triple Store](#b-persistence-into-a-triple-store)
  * [c. View Your Instantiated Topology](#c-view-your-instantiated-topology)
  * [d. Upload your Ontology Model (T-Box)](#d-upload-your-ontology-model--t-box-)
- [5. LINQ Queries](#5-linq-queries)

### 1. Generate a Library from the Demo Ontology
First, retrieve the [ExampleDemoOntology.owl](https://github.com/EcoStruxure/OLGA/blob/master/docs/example/ExampleDemoOntology.owl) and generate a library to a given ${Path Of Your Choice}:

```console
java -jar target\OLGA-0.0.3-with-dependencies.jar --code cs --library trinity --name DemoExample --path .../ExampleDemoOntology.owl --out ${Path Of Your Choice}
```

The following sections will go through the steps need to instantiate an ontology, persist it and query it.
The source code for these steps can be found [here](https://github.com/EcoStruxure/OLGA/blob/master/docs/example/DemoExample.zip).

In case you want to try another ontology make you sure you take a look at this URI Normalization at the user guide section.

### 2. Create a .Net project

First create a .Net (Core or Framework) console project.

To instantiate an ontology, the following dependencies are required:

* Trinity Library provided by SemioDesk available at Nuget.org. It can be added as a [Nuget package](https://www.nuget.org/packages/Semiodesk.Trinity)
* The generated library by OLGA available on your machine, it must added as a reference from the DemoExample-Trinity.dll which is located in the following path:

> `${Path Of Your Choice}\DemoExample-dotnetTrinity\bin\Release\net461`

### 3. Initialization

First, create a namespace to be used for the instantiated individuals and theirs properties:

```csharp 
static string ns = "http://www.example.com/";
```
Trinity is based on the factory pattern which can be configured to work in memory of with a persistent triple store.

#### In Memory usage
To use Trinity in memory the `StoreFactory` can be configured as follows:

```csharp 
IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf"); 
```

Once the StoreFactory is configured, an `IModel` is used to create the instances of the model:

 ```csharp
 context = _entityStore.GetModel(new Uri(ns));
 ```

The `InitContextInMemory()` function is provided below:
```csharp
1: static string ns = "http://example.com/sample/";
2: static IModel context;
3:        
4: public static void InitContextInMemory()
5: {
6:  IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf");
7:  context = _entityStore.GetModel(new Uri(ns));
8: }
```
 
### 4. Instantiate the Ontology using the Generated Library

Now that the context is initialized, the Example Ontology instantiation can take place. Let's start by instantiating a Floor:

* Invoke the Factory to create an instance of Floor
* Fill the name parameter
* Commit the changes

```csharp
//Create a floor 1
1: Floor floor1 = context.CreateResource<Floor>(ns + "f1");
2: floor1.Name = "floor 1";
3: floor1.Commit();
```

You will need to add the following declaration import:
```csharp
using DemoExample.Rdf.Model;
```

Create the following topology `static void CreateTopology()`:
* A Floor `f1`
* A Floor `f2`
* A Building `b1` with the two previous floors
* A Temperature Measurement `temp1`
* A Sensor `TempSensor1` which measures the previous Temperature measurement `temp1`
* Sensor `TempSensor1` located on Floor `f1`
* A Humidity Measurement `h1`
* A Sensor `hum1` which measures the previous Humidity measurement `h1`
* Sensor `hum1` located on Floor `f2`

The code for the topology is [here](https://github.com/EcoStruxure/OLGA/tree/master/docs/example/Program.cs)

#### a. Serialize to file

The content of the InMemory ContextFactory can be flushed into an RDF file as following:
```csharp
1: public static void Serialize()
2: {
3:  try
4:    {
5:     FileStream stream = new FileStream("C:\\demoExample.rdf", FileMode.Create);
6:     context.Write(stream, RdfSerializationFormat.RdfXml);
7:	   Console.WriteLine("Ontology Instance Generated at C:\\demoExample.rdf");
8:    }
9:    catch (Exception ex)
10:   {
11:    Console.WriteLine(ex.Message);
12:   }
12:
13: }
```        

Line 6, the developer can choose from many other RdfSerializationFormats

#### b. Persistence into a Triple Store

As mentioned earlier, the Context can be configured to use persistence Triple Store. 

For this example, we will be using [Stardog](https://www.stardog.com/) Community Edition Triple Store.

Other persistence triple stores can be used with the StoreFactory which is based on DotNetRDF library as shown in [DotNetRDF documentation](https://github.com/dotnetrdf/dotnetrdf/wiki/UserGuide-Triple-Store-Integration) and [Trinity documentation](https://bitbucket.org/semiodesk/trinity/wiki/FirstSteps)

Persistence Triple Store usage
To use Trinity with a persistence store, the `StoreFactory` can be configured as follows:

```csharp
IStore _entityStore = StoreFactory.CreateStore("provider=stardog;host=http://localhost:5820;uid=admin;pw=admin;sid=DemoExample");
```

Make sure to create a new database in Stardog with the name `DemoExample` then build and start your program.

![](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/stardogNewDB.png)

#### c. View Your Instantiated Topology
Once you run the program, the database will be populated with the created topology as shown in the figure below:
![Intantiated Ontology](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/OntologyInstanceInStardog.png)

#### d. Upload your Ontology Model (T-Box)

The DB has the instantiated ontology (A-box) only for now, therefore, we will add the ontology model (T-box) to start executing queries based on the model.

Add the [Ontology Model](https://github.com/EcoStruxure/OLGA/tree/master/docs/example/ExampleDemoOntology.owl) to the DB as shown in the image below:

![Upload the Ontology Model](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/addOntologyModel.png)

 To upload the Ontology model follow these steps:

1.	+Add
2.	Browse
3.	And choose the ExampleDemoOntology.owl
4.	Upload
5.	Refresh your browser

Stardog offers an ingestion REST API which can be used to upload the Ontology Model. 

### 5. LINQ Queries

[SPARQL 1.1](https://www.w3.org/TR/sparql11-query/) is the W3C standard query language for RDF and OWL. However, some developers prefer to rely on LINQ like expressions to query the model.

With Trinity ORM, it is also possible to use the Language Integrated Query. LINQ has become a de-facto standard in .Net to allow programmers to query data in an expressive manner. Trinity transforms a subset of the LINQ queries into SPARQL.

An example to query all floors is given below:
```csharp
private static void QueryAllFloors()
 {
  Console.WriteLine("QueryAllFloors .. start");
  var floors = context.AsQueryable<Floor>();
  foreach (Floor floor in floors)
  {
  Console.WriteLine("Found: " + floor.Name);
  }
  Console.WriteLine("QueryAllFloors .. done");
 }
```

Or all the sensors, their physical locations and their description:
```csharp
private static void QueryAllSensors()
 {
  Console.WriteLine("AllSensors .. start");
  var sensors = context.AsQueryable<Sensor>();
  foreach (Sensor sensor in sensors)
  {
   Console.WriteLine("Found: " + sensor.Name);
   Console.WriteLine("Description: " + sensor.Description);
   Console.WriteLine("Located at:" + sensor.PhysicalLocation.Name);
   }
   Console.WriteLine("AllSensors .. done");
 }
```

The complete code example is available [here](https://github.com/EcoStruxure/OLGA/tree/master/docs/example/DemoExample.zip)