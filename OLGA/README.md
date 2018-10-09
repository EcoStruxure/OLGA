## Overview  
### Build
This is the new version of OLGA as modularized project so now the user able to build OLGA as a service by running this Maven command on OLGA (directory or project) "--projects OLGA-Core,OLGA-Ws clean install", or able to build it as a desktop application using this command "--projects OLGA-Core,OLGA-Cli clean install"

### Run
In the situation of running OLGA as desktop application the user have to go to run the jar file in OLGA-Cli/target with proper input options. On the other hand when the user wants to run it as web service user have to run jar file in OLGA-Ws/target on server and interact with this server by sending post request to "{Domain}/Ontology/OLGAsaas/1.0.0/1.0/generate?code=cs&name=Saref&library=trinity&preserve=true&version=0.0.3&partial=true".

Note:
the first 3 options in the query string is mandatory.

### Build An Container Image for Any Module
OLGA uses Google Container Tool called "JIB" which makes the containerization of any module like a breeze, just we have to add our image hub info in configuration clause under JIB plug in pom.xml and run mvn command [https://github.com/GoogleContainerTools/jib/blob/master/jib-maven-plugin/README.md].

<configuration>
	<to>
		<image>MyImage</image>
	</to>
</configuration>

or we can generate the image locally as .tar file by running this command "mvn compile jib:buildTar".