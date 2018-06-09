## Hello World Example in C#


### 1. Generate a Library from the Demo Ontology
First, retrieve the [ExampleDemoOntology.owl](./helloWorld/ExampleDemoOntology.owl) and generate a library to a given ${Path Of Your Choice}:

```console
java -jar target\OLGA-0.0.3-with-dependencies.jar --code cs --library trinity --name DemoExample --path .../ExampleDemoOntology.owl --out ${Path Of Your Choice}
```

The following sections will go through the steps need to instantiate an ontology, persist it and query it.
The source code for these steps can be found [here](./Example.zip).

In case you want to try another ontology make you sure you take a look at this URI Normalization at the user guide section.

### 2. Create a .NetFramework project

First create a .NetFramework console project.

To instantiate an ontology, the following dependencies are required:

* [Trinity](https://www.nuget.org/packages/Semiodesk.Trinity) Library provided by SemioDesk available at Nuget.org. It can be added as a Nuget package
* The generated library by OLGA available on your machine, it must added as a reference from the DemoExample-Trinity.dll which is located in the following path:

> `${Path Of Your Choice}\DemoExample-dotnetTrinity\bin\Release\net461`

### 3. Initialization

First, create a namespace to be used for the instantiated individuals and theirs properties:

```csharp 
static string ns = "http://example.com/test/";
```
Trinity is based on the factory pattern which can be configured to work in memory of with a persistent triple store.

#### a. In Memory usage
To use Trinity in memory the `StoreFactory` can be configured as follows:

```csharp 
IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf"); 
```

#### b. Persistence Triple Store usage
To use Trinity with a persistence store, the `StoreFactory` can be configured as follows:

```csharp
IStore _entityStore = StoreFactory.CreateStore("provider=stardog;host=http://localhost:5820;uid=admin;pw=admin;sid=DemoExample");
```
Other persistence triple stores can be used with the StoreFactory which is based on DotNetRDF library as shown in [DotNetRDF documentation](https://github.com/dotnetrdf/dotnetrdf/wiki/UserGuide-Triple-Store-Integration) and [Trinity documentation](https://bitbucket.org/semiodesk/trinity/wiki/FirstSteps)

Once the StoreFactory is configured, an `IModel` is used to create the instances of the model:

 ```csharp
 context = _entityStore.GetModel(new Uri(ns));
 ```

The `InitContext()` function is provided below:
```csharp
 static string ns = "http://example.com/test/";
 static IModel context;
        
 public static void InitContext()
 {
  IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf"); // in memory store
  context = _entityStore.GetModel(new Uri(ns));
 }
```
 
### 4. Implementation using the Generated Library

Now that the context is initialized, the Example Ontology instantiation can take place.


#### a. In Memory

#### b. Serialize to file

#### c. Persistence into a Triple Store

### 5. LINQ Queries