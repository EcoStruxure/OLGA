<Project Sdk="Microsoft.NET.Sdk">

  <PropertyGroup>
    <OutputType>Library</OutputType>
    <RootNamespace>${ontologyName}-Trinity</RootNamespace>
    <AssemblyName>${ontologyName}-Trinity</AssemblyName>
    <AssemblyVersion>${ontologyVersion}</AssemblyVersion>
    <Version>${ontologyVersion}-alpha</Version>
    <TargetFramework>netstandard2.0</TargetFramework>
  </PropertyGroup>
 <#if dependency??> 
  <Target Name="PostBuild" AfterTargets="PostBuildEvent">
    <Exec Command="&quot;${dependency}&quot; -i $(TargetPath) -o $(TargetPath) --no-symbols" />
  </Target>
</#if>


  <ItemGroup>
    <PackageReference Include="Semiodesk.Trinity" Version="1.0.*" />
  </ItemGroup>
</Project>
