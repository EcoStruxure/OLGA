<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>OLGA</groupId>
	<artifactId>${ontologyName}-trinity</artifactId>
	<version>${OntologyVersion}</version>
	<packaging>dotnet-library</packaging>

	<build>
		<plugins>
			<plugin>
				<groupId>org.eobjects.build</groupId>
				<artifactId>dotnet-maven-plugin</artifactId>
				<version>0.23</version>
				<extensions>true</extensions>
				<configuration>
					<buildConfiguration>Release</buildConfiguration>
					<buildEnabled>true</buildEnabled>
					<cleanEnabled>true</cleanEnabled>
					<restoreEnabled>true</restoreEnabled>
					<packEnabled>true</packEnabled>
					<packOutput>bin</packOutput> 
   <#if buildRepo??><repository>${buildRepo}</repository>
   					<publishEnabled>false</publishEnabled>
   <#if keyRepo??>  <repositoryKey>${keyRepo}</repositoryKey></#if></#if>
					<restoreEnabled>true</restoreEnabled>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>