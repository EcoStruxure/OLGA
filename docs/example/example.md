## Hello World Example in C#

In this example, we will go through the instantiation of an ontology model (classes and relationships) programmatically. 
We will rely on a set of annotated classes and an Objet-Mapping-Library for RDF to generate a topology instance specific to a site.
The generated topology can be in memory, or stored in an RDF persistent store. It can also be queried in memory or from a persistent store.

![](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/yourLibrary.png)

This library can be part of an IoT application, embedded in a commissioning tool or deployed on a system.

## Table of Contents
  * [1. Generate a Library from the Demo Ontology](#1-generate-a-library-from-the-demo-ontology)
  * [2. Create a .Net project](#2-create-a-net-project)
  * [3. Initialization](#3-initialization)
    + [In Memory usage](#in-memory-usage)
      - [Empty in Memory Store](#empty-in-memory-store)
      - [Memory Store with an Ontology](#memory-store-with-an-ontology)
  * [4. Instantiate the Ontology using the Generated Library](#4-instantiate-the-ontology-using-the-generated-library)
    + [a. Create the following topology `static void CreateTopology()`:](#a-create-the-following-topology--static-void-createtopology----)
    + [b. Serialize to file](#b-serialize-to-file)
    + [c. Persistence into a Triple Store](#c-persistence-into-a-triple-store)
    + [d. View Your Instantiated Topology](#d-view-your-instantiated-topology)
    + [d. Upload your Ontology Model (T-Box)](#d-upload-your-ontology-model--t-box-)
  * [5. LINQ Queries](#5-linq-queries)
  * [6. Inference with LINQ Queries](#6-inference-with-linq-queries)
  * [7. SPARQL Queries](#7-sparql-queries)
  * [8. Next](#8-next)


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

##### Empty in Memory Store
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
6:  SemiodeskDiscovery.Discover();
7:  IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf");
8:  context = _entityStore.GetModel(new Uri(ns));
9: }
```

Line 6, is mandatory to allow Trinity to discover the created mappings `SemiodeskDiscovery.Discover();`

##### Memory Store with an Ontology
The previous example, we created an in Memory store which was empty. It is also possible to create an in Memory Store with an ontology as follows:

```csharp
 public static void InitContextTripleStore()
 {
  SemiodeskDiscovery.Discover();
  IStore _entityStore = 
  	new dotNetRDFStore(new []{ "C:\\ExampleDemoOntology.owl" });
  
  context = _entityStore.GetModel(new Uri(ns));
 }
```

### 4. Instantiate the Ontology using the Generated Library

Now that the context is initialized, the Example Ontology instantiation can take place. 
Let's start by instantiating a Floor as follows:

* Invoke the Factory to create an instance of Floor
* Fill the name parameter
* Commit the changes

```csharp
//Create a floor 1
1: Floor floor1 = context.CreateResource<Floor>(ns + "f1");
2: floor1.Name = "floor 1";
3: floor1.Commit();
```

In line 1, we forced the Uri creation, however, it is also possible to let Trinity automatically create Uris as follows:
```csharp
Floor floor1 = context.CreateResource<Floor>();
```

You will need to add the following declaration import:
```csharp
using DemoExample.Rdf.Model;
```

#### a. Create the following topology `static void CreateTopology()`:
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

#### b. Serialize to file

The content of the InMemory ContextFactory can be serialized into an RDF file as following:
```csharp
1: public static void SerializeToFile()
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

Line 6, the developer can choose from many other RdfSerializationFormats.

#### c. Persistence into a Triple Store

As mentioned earlier, the Context can be configured to use a persistence Triple Store. 

For this example, we will be using [Stardog](https://www.stardog.com/) Community Edition Triple Store. Other persistence triple stores can be used with the StoreFactory which is based on DotNetRDF library as shown in [DotNetRDF documentation](https://github.com/dotnetrdf/dotnetrdf/wiki/UserGuide-Triple-Store-Integration) and [Trinity documentation](https://bitbucket.org/semiodesk/trinity/wiki/FirstSteps).

To use Trinity with a persistence store, the `StoreFactory` can be configured as follows:

* DB provider, example Stardog
* Host where the Triple Store is hosted, http://localhost:5820
* UserId, uid=admin
* Password, pw=admin
* DB Name (sid=DemoExample), which needs to be created before running your code. 

These parameters are used as following:

```csharp
IStore _entityStore = StoreFactory.CreateStore("provider=stardog;host=http://localhost:5820;uid=admin;pw=admin;sid=DemoExample");
```

Make sure to create a new database in Stardog with the name `DemoExample` then build and start your program.

![](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/stardogNewDB.png)

#### d. View Your Instantiated Topology
Once you run the program, the database will be populated with the created topology as shown in the figure below:
![Instantiated Ontology](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/OntologyInstanceInStardog.png)

#### d. Upload your Ontology Model (T-Box)

The DB has for now only the instantiated ontology (A-box), therefore, we will add the ontology model (T-box) to start executing queries with inference based on the model.

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

[SPARQL 1.1](https://www.w3.org/TR/sparql11-query/) is the W3C standard query language for RDF and OWL. However, some developers prefer to rely on LINQ-like expressions to query the model.

With Trinity ORM, it is also possible to use the Language Integrated Query. LINQ has become a de-facto standard in .Net to allow developers to query data in an expressive manner. Trinity transforms a subset of the LINQ queries into SPARQL.

An example to query all floors is given below:
```csharp
private static void QueryAllFloors()
 {
  Console.WriteLine("QueryAllFloors .. start");
  var floors = context.AsQueryable<Floor>();
  foreach (Floor floor in floors)
  {
   Console.WriteLine("    - " + floor.Name);
  }
  Console.WriteLine("QueryAllFloors .. done");
 }
```

Or all the sensors, their physical locations and their description:
```csharp
private static void QueryAllSensors()
 {
  Console.WriteLine("QueryAllSensors .. start");
  var sensors = context.AsQueryable<Sensor>();
  foreach (Sensor sensor in sensors)
  {
   Console.WriteLine("    - Found: " + sensor.Name);
   Console.WriteLine("    - Description: " + sensor.Description);
   Console.WriteLine("    - Located at:" + sensor.PhysicalLocation.Name);
   Console.WriteLine();
  }
  Console.WriteLine("AllSensors .. done");
 }
```

LINQ Queries **without Inference** can be executed both:
* In Memory
* In a Triple Store


### 6. Inference with LINQ Queries
[Inference](https://www.w3.org/standards/semanticweb/inference) allows to extract additional information based on what is already represented.

The Inference mechanism is dependent on the Persistent Store.

* DotNetRDF in memory store provide inference capabilities based on [RDFS](https://www.w3.org/TR/rdf-schema/).
* Other Triple stores such as Stardog provides inference capabilities based on [OWL](https://www.w3.org/OWL/). [Stardog](https://www.stardog.com) DB supports many flavors of [OWL reasoning](https://www.stardog.com/docs/#_owl_rule_reasoning).

**OWL based inference offers much more than an RDFS based inference**

This step might not work if you are using another Triple Store with non or different inference support.

Trinity ORM supports LINQ to SPARQL generation, in order to invoke inference in queries, the parameter `true` must be set at the `AsQueryable` method.

In the Ontology used in this example, `PhysicalLocation` has two subclasses `Building` and `Floor` as shown in the figure below:

![](https://github.com/EcoStruxure/OLGA/blob/master/docs/figures/PhysicalLocation.png)

The following code snippet is querying for all PhysicalLocation items with inference set to true.

```csharp
private static void QueryPhysicalLocation()
{
 Console.WriteLine("QueryPhysicalLocation with Inference .. start");
 var locations = context.AsQueryable<PhysicalLocation>(true).ToList();
 if (locations.FirstOrDefault() != null)
 {
  foreach (PhysicalLocation loc in locations)
  {
   Console.WriteLine("    - Found: " + loc.Name);
  }
 }
}
```        

The result of the query will return the instances of two floors and a building.

This inference will be successful in memory since [rdfs:subClassOf](https://www.w3.org/TR/rdf-schema/#ch_subclassof) is part of [RDFS](https://www.w3.org/TR/rdf-schema).

In order to apply the inference in memory, make you sure you loaded the ExampleDemoOntology.owl in memory when you initialize the store, see the `InitContextInMemory` method in [Program.cs](https://github.com/EcoStruxure/OLGA/tree/master/docs/example/Program.cs)

### 7. SPARQL Queries

It is also possible to write SPARQL queries through Trinity and DotNetRDF or at the Triple Store Query UI level.

First, the Sparql query:
```sql
Select ?a from <http://www.example.com/>
{ 
 ?a a<http://www.example.com/PhysicalLocation>. 
}
``` 

The SPARQL query can used with the context to execute it as show in line 8 where the inference is set to true. The SPARQL query result returns bindings object conform to the [SPARQL standard](https://www.w3.org/TR/sparql11-results-json/#select-results-form)

```csharp
1: private static void QuerySPARQLPhysicalLocation()
2:  {
3:   string sparql = "Select ?a from <http://www.example.com/> " +
4:   "{ ?a a<http://www.example.com/PhysicalLocation>. }";

5:   ISparqlQuery sparqlQuery = new SparqlQuery(sparql, false);
6:   Console.WriteLine("SPARQL QueryPhysical .. start");
 
7:   //Executing Query with inference, hence the true statement
8:   ISparqlQueryResult locations = context.ExecuteQuery(sparqlQuery, true);

9:   var bindings = locations.GetBindings().ToList();
10:  foreach (var binding in bindings)
11:  {
12:   Console.WriteLine("Found: " + binding.Values.FirstOrDefault().ToString());
13:  }
14:  Console.WriteLine("SPARQL QueryPhysical .. done");
15: }
```

The returned results are the three subclasses of `PhysicalLocation`:
* `http://www.example.com/f1`
* `http://www.example.com/f2`
* `http://www.example.com/b1`


### 8. Next
The complete code example is available [here](https://github.com/EcoStruxure/OLGA/tree/master/docs/example/DemoExample.zip)

You can now instantiate your own ontology or a public ontology.
