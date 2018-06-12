using DemoExample.Rdf.Model;
using Semiodesk.Trinity;
using System;
using System.IO;


namespace DemoExample
{
    class Program
    {
        static string ns = "http://example.com/sample/";
        static IModel context;

        public static void InitContextInMemory()
        {
            IStore _entityStore = StoreFactory.CreateStore("provider=dotnetrdf"); // in memory store
            context = _entityStore.GetModel(new Uri(ns));
        }

        public static void InitContextTripleStore()
        {
            IStore _entityStore = StoreFactory.CreateStore("provider=stardog;host=http://localhost:5820;uid=admin;pw=admin;sid=DemoExample");
            context = _entityStore.GetModel(new Uri(ns));
        }

        public static void Serialize()
        {
            try
            {
                FileStream stream = new FileStream("C:\\demoExample.rdf", FileMode.Create);
                context.Write(stream, RdfSerializationFormat.RdfXml);
                Console.WriteLine("Ontology Instance Generated at C:\\demoExample.rdf");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }



        private static void CreateTopology()
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


        static void InMemoryExample()
        {
            Console.WriteLine("Memory Example ... start");
            InitContextInMemory();
            CreateTopology();
            Serialize();
            Console.WriteLine("Memory Example ... done");
            Console.ReadLine();
        }

        static void InTripleStoreExample()
        {
            Console.WriteLine("TripleStore Example ... start");
            InitContextTripleStore();
            //CreateTopology(); //Comment when querying
            Query();
            Console.WriteLine("TripleStore Example ... done");
            Console.ReadLine();
        }

        private static void Query()
        {
            QueryAllFloors();
            QueryAllSensors();
        }

  

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

        static void Main(string[] args)
        {
            //InMemoryExample();
            InTripleStoreExample();
        }
    }
}
