/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.global;

/**
 * CONFIG is a class contains all of the static paths OLGA is going to use as fall back options.
 */

public class CONFIG {
  public static final String PROJECT_DIRECTORY = "./";
  public static final String RESOURCES = "/";
  public static final String GENERATED_LIBRARY = "OLGA/generated/";
  public static final String OLGA_Dependencies = "src/main/resources/Dependencies";
  public static final String DOTNET_TRINITYRDF_TEMPLATES =
      RESOURCES + "templates/dotnet/trinityrdf";
  public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
}
