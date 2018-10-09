<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  	<groupId>OLGA-RdfAlchemy</groupId>
	<artifactId>${ontologyName}-RDFAlchemy</artifactId>
	<version>${OntologyVersion}</version>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <python_package>${ontologyName}</python_package>
    <description>${ontologyName} RdfAlchemy for python</description>
  </properties>
  <build>
    <plugins>
     <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>1.6.0</version>
        <executions>
            <execution>
                <phase>package</phase>
                <configuration>
                    <executable>python</executable>
                    <workingDirectory>${path}</workingDirectory>
                    <arguments>
                    	<argument>setup.py</argument>
                    	<argument>sdist</argument>
                    	<argument>bdist_wheel</argument>
                   	</arguments>
                </configuration>
                <goals>
                    <goal>exec</goal>
                </goals>
            </execution>
        </executions>
     </plugin>
    </plugins>
   </build>
  </project>
