using DemoExample.Rdf.Model;
using Ontologies;
using Semiodesk.Trinity;
using Semiodesk.Trinity.Store;
using System;
using System.IO;
using System.Linq;

namespace DemoOLGA
{
    class Program
    {
        static string ns = "http://www.example.com/";
        static IModel context;

        static void Main(string[] args)
        {
            //Use one of the two options (I or II) at a time
            //I- Create Topology in Memory
            MemoryStoreExample();

            //II-Create Topology in a TripleStore
            //Load manually your model into the store
            //TripleStoreExample();

            Console.WriteLine("Hit enter to continue");
            Console.ReadLine();
        }

        private static void MemoryStoreExample()
        {
            Console.WriteLine("MemoryStore Example ... start");
            InitContextInMemory();
            CreateTopology();
            AllQueries();
            SerializeToFile();
            Console.WriteLine("MemoryStore Example ... done");
        }

        public static void InitContextInMemory()
        {
            SemiodeskDiscovery.Discover();
            //In memory store with an ontology
            IStore _entityStore = 
                new dotNetRDFStore(new []{ "C:\\ExampleDemoOntology.owl" }); //Replace with your path
                
            // In memory Empty store
            //IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf");
            context = _entityStore.GetModel(new Uri(ns));
        }

        public static void SerializeToFile()
        {
            try
            {
                FileStream stream = new FileStream("C:\\demoExample.rdf" //replace with your path
                    , FileMode.Create);
                context.Write(stream, RdfSerializationFormat.RdfXml);
                Console.WriteLine("Ontology Instance Generated at C:\\demoExample.rdf");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        private static void TripleStoreExample()
        {
            Console.WriteLine("TripleStore Example ... start");
            InitContextTripleStore();
            //Create Topology should be call only once
            //It should be commented when querying to avoid creating same instances.
            CreateTopology();
            AllQueries();
            Console.WriteLine("TripleStore Example ... done");
        }
        public static void InitContextTripleStore()
        {
            SemiodeskDiscovery.Discover();
            IStore _entityStore = StoreFactory.
                CreateStore("provider=stardog;host=http://localhost:5820;uid=admin;pw=admin;sid=DemoExample");
            context = _entityStore.GetModel(new Uri(ns));
        }

        private static void CreateTopology()
        {
            Console.WriteLine("Creating Topology ... start");

            //Making sure it has not been already added
            if (context.AsQueryable<Floor>().FirstOrDefault() == null)
            {
                //Create a floor 1
                Floor floor1 = context.CreateResource<Floor>(ns + "f1");
                floor1.Name = "floor 1";
                floor1.Commit();

                //Create a floor 2
                Floor floor2 = context.CreateResource<Floor>(ns + "f2");
                floor2.Name = "floor 2";
                floor2.Commit();

                //Create a Building 1
                Building building1 = context.CreateResource<Building>(ns + "b1");
                building1.Description = "North face Building";
                building1.Name = "b1";
                building1.Floors.Add(floor1);
                building1.Floors.Add(floor2);
                building1.Commit();

                //Create a Temperature measurement
                Temperature temp1 = context.CreateResource<Temperature>(ns + "t1");
                temp1.Description = "this is indoor temperature";
                temp1.UnitOfMeasure = TemperatureUnit.Celsius;
                temp1.Name = "temp1";
                temp1.TimeStamp = DateTime.UtcNow;
                temp1.Value = 32;
                temp1.Commit();

                //Create a Temperature Sensor
                Sensor temperatureSensor = context.CreateResource<Sensor>(ns + "s1");
                temperatureSensor.Description = "This is sensor s1";
                temperatureSensor.Name = "TempSensor1";
                temperatureSensor.Measures.Add(temp1);
                temperatureSensor.PhysicalLocation = floor1;
                temperatureSensor.Commit();

                //Create a Humidity measurement
                Humidity humidity1 = context.CreateResource<Humidity>(ns + "h1");
                humidity1.Description = "this is indoor humidity";
                humidity1.UnitOfMeasure = HumidityUnit.RelativeHumidity;
                humidity1.Name = "h1";
                humidity1.TimeStamp = DateTime.UtcNow;
                humidity1.Value = 64;
                humidity1.Commit();

                //Create a Humidity Sensor
                Sensor humiditySensor = context.CreateResource<Sensor>(ns + "hum1");
                humiditySensor.Description = "This is humidity sensor 1";
                humiditySensor.Name = "hum1";
                humiditySensor.Measures.Add(humidity1);
                humiditySensor.PhysicalLocation = floor2;
                humiditySensor.Commit();
            }
            else Console.WriteLine("Topology already created, not re-adding it");
            Console.WriteLine("Creating Topology ... done");
        }

        private static void AllQueries()
        {
            QueryAllFloors();
            QueryAllSensors();
            QueryPhysicalLocation();
            QuerySPARQLPhysicalLocation();
        }



        private static void QueryAllFloors()
        {
            Console.WriteLine("QueryAllFloors .. start");
            var floors = context.AsQueryable<Floor>();
            foreach (Floor floor in floors)
            {
                Console.WriteLine("    - Found: " + floor.Name);
            }
            Console.WriteLine("QueryAllFloors .. done");
        }

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
            else Console.WriteLine("====> No inferred results found, did you upload the ontology model?");
            Console.WriteLine("QueryPhysicalLocation with Inference .. done");
        }

        private static void QuerySPARQLPhysicalLocation()
        {
            string sparql = "Select ?a from <http://www.example.com/> " +
                "{ ?a a<http://www.example.com/PhysicalLocation>. }";

            ISparqlQuery sparqlQuery = new SparqlQuery(sparql, false);
            Console.WriteLine("SPARQL QueryPhysicalLocation .. start");
            //Executing Query with inference, hence the true statement
            ISparqlQueryResult locations = context.ExecuteQuery(sparqlQuery, true);

            var bindings = locations.GetBindings().ToList();
            if (bindings.FirstOrDefault() != null)
            {
                foreach (var binding in bindings)
                {
                    Console.WriteLine("    - Found: " + binding.Values.FirstOrDefault().ToString());
                }
            }
            else Console.WriteLine("====> No inferred results found, did you upload the ontology model?");
            Console.WriteLine("SPARQL QueryPhysicalLocation .. done");
        }

    }
}
