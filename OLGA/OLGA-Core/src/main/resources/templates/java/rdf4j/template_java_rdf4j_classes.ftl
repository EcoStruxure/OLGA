package ${packageName};

import ${ontologyName}.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;<#if listObjectPropertiesToImplement??>
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;</#if><#if Zclass.getListOfImports()??><#list (Zclass.getListOfImports()?keys) as importedPackageKey><#if (Zclass.getListOfImports()[importedPackageKey])?? && preserveNameSpaceOfOntology==true><#list (Zclass.getListOfImports()[importedPackageKey]) as importedPackageValue>
import ${importedPackageKey?lower_case}.I${importedPackageValue};
import ${importedPackageKey?lower_case}.${importedPackageValue};</#list></#if></#list></#if><#if Zclass.getZDataPropertyList()??><#list Zclass.getZDataPropertyList() as packageName><#if packageName.getPackageName()??>
import ${packageName.getPackageName()}.${packageName.getRangeXSDType()};</#if></#list></#if>

<#if Zclass.getLabel()?? || Zclass.getComments()??>
/**<#if Zclass.getLabel()??>
* Class ${Zclass.getLabel()}</#if> <#if Zclass.getComments()??>
* ${Zclass.getComments()}</#if>
*/</#if>
@SuppressWarnings("serial")
public ${Zclass.getGenerate()?string("","abstract ")}class ${Zclass.getzClassName()}<#if motherToExtend??> extends <#if preserveNameSpaceOfOntology==true>${motherToExtend.getPackageName()?lower_case}.${motherToExtend.getzClassName()}<#else>${motherToExtend.getzClassName()}</#if></#if> implements I${Zclass.getzClassName()}{

	IRI newInstance;
	public ${Zclass.getzClassName()}(String namespace, String instanceId) {<#if motherToExtend??>
		super(namespace, instanceId);
		<#else>
		super();</#if>
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("${Zclass.iri}"));
	}

	public IRI iri()
	{
		return newInstance;
	}

	<#if listDataPropertiesToImplement??><#list listDataPropertiesToImplement as DataPropertyList>
	<#if DataPropertyList.isGeneratable() == true>
	<#if DataPropertyList.getLabel()?? || DataPropertyList.getComments()??>
	/**<#if DataPropertyList.getLabel()??>
    * Property ${DataPropertyList.getLabel()}</#if> <#if (DataPropertyList.getComments())??><#list (DataPropertyList.getComments())?split("\n") as line>
    * ${line}</#list></#if>
	*/</#if>	
	public void <#if DataPropertyList.getDataPropertyShortFormCS()?cap_first == "ID">set${Zclass.getzClassName()}<#else>set</#if>${DataPropertyList.getDataPropertyShortFormCS()?cap_first}(${DataPropertyList.getRangeXSDType()} param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("${DataPropertyList.getDataProperty()}"), GLOBAL.factory.createLiteral(param));
	}
	
	public String <#if DataPropertyList.getDataPropertyShortFormCS()?cap_first == "ID">get${Zclass.getzClassName()}<#else>get</#if>${DataPropertyList.getDataPropertyShortFormCS()?cap_first}<#if DataPropertyList.isHidingParentProperty() == true>From${Zclass.getzClassName()}</#if>(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("${DataPropertyList.getDataProperty()}"), null).objects().iterator().next()).stringValue();	
	}
	</#if></#list></#if>		
	<#if listObjectPropertiesToImplement??>
	<#list listObjectPropertiesToImplement as ObjectPropertyList>
	<#assign i = ObjectPropertyList.getRangeListZClasses()?size>
    <#list ObjectPropertyList.getRangeListZClasses() as Range>
	<#if Range.getValue()[0] == true> 
    <#if ObjectPropertyList.getLabel()?? || ObjectPropertyList.getComments()??>    
    /**<#if ObjectPropertyList.getLabel()??>
    * ${ObjectPropertyList.getLabel()}</#if><#if ObjectPropertyList.getComments()??>
    * ${ObjectPropertyList.getComments()}</#if>
	*/</#if>
	<#if (ObjectPropertyList.isOverridable()!false) || (i>1)>
	<#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first+Range.getKey().getzClassName()?cap_first>
	<#else>
	<#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first></#if>
    public void add${varName?cap_first} (I${Range.getKey().getzClassName()} parameter)
	{
		GLOBAL.model.add(this, GLOBAL.factory.createIRI("${ObjectPropertyList.getObjectProperty()}"), parameter);
	}
	
	public Set<I${Range.getKey().getzClassName()}> get${varName?cap_first}<#if Range.getValue()[1] == true>From${Zclass.getzClassName()}</#if> (){
		Set<I${Range.getKey().getzClassName()}> ${varName} = new HashSet<I${Range.getKey().getzClassName()}>();
		GLOBAL.model.filter(this, GLOBAL.factory.createIRI("${ObjectPropertyList.getObjectProperty()}"), null).objects().forEach(action->{
			if(action instanceof ${Range.getKey().getzClassName()}) {
				${varName}.add((${Range.getKey().getzClassName()})action);			
			}
		});
		return ${varName};	
	}
	
	</#if></#list></#list></#if>		
	<#if motherToExtend??>
	<#else>
	@Override
	public String stringValue() {
		return this.newInstance.getLocalName();
	}

	@Override
	public String getNamespace() {
		return this.newInstance.getNamespace();
	}

	@Override
	public String getLocalName() {
		return this.newInstance.getLocalName();
	}
	
	@Override
	public String toString() {
		return this.newInstance.toString();
	}
	
	</#if>	
	public static Set<I${Zclass.getzClassName()}> getAll${Zclass.getzClassName()}sObjectsCreated(){
		Set<I${Zclass.getzClassName()}> objects = new HashSet<I${Zclass.getzClassName()}>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("${Zclass.iri}")).subjects().stream().map(mapper->(I${Zclass.getzClassName()})mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
