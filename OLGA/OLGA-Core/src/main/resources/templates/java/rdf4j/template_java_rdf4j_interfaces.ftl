package ${packageName};

import org.eclipse.rdf4j.model.IRI;<#if objectPropertyList??><#if objectPropertyList?size gt 0>
import java.util.Set;</#if></#if><#if Zclass.getListOfImports()??><#list (Zclass.getListOfImports()?keys) as importedPackageKey><#if (Zclass.getListOfImports()[importedPackageKey])?? && preserveNameSpaceOfOntology==true><#list (Zclass.getListOfImports()[importedPackageKey]) as importedPackageValue>
import ${importedPackageKey?lower_case}.I${importedPackageValue};</#list></#if></#list></#if><#if Zclass.getZDataPropertyList()??><#list Zclass.getZDataPropertyList() as packageName><#if packageName.getPackageName()??>
import ${packageName.getPackageName()}.${packageName.getRangeXSDType()};</#if></#list></#if>
<#if Zclass.getLabel()?? || Zclass.getComments()??>/**<#if Zclass.getLabel()??>
* Class ${Zclass.getLabel()}</#if> <#if Zclass.getComments()??><#list Zclass.getComments()?split("\n") as line>
* ${line}</#list></#if>
*/</#if>
<#assign interfaceName = 'I' + Zclass.getzClassName()/>
public interface ${interfaceName}<#if motherClassList??><#if motherClassList?has_content> extends <#list motherClassList as SuperZClassCurrentElementOfList><#if SuperZClassCurrentElementOfList.getzClassName() == "Resource" && preserveNameSpaceOfOntology==true>${SuperZClassCurrentElementOfList.getPackageName()?lower_case}.I${SuperZClassCurrentElementOfList.getzClassName()}<#else>I${SuperZClassCurrentElementOfList.getzClassName()}</#if><#sep>, </#list><#else> extends IRI</#if></#if>{

	public IRI iri();		
	<#if objectPropertyList??>
	<#list objectPropertyList as ObjectProperty> 	
	<#assign i = ObjectProperty.getRangeListZClasses()?size>
	<#list ObjectProperty.getRangeListZClasses() as Range>
	<#if Range.getValue()[0] == true> 
	<#if (ObjectProperty.isOverridable()!false) || (i>1)>
	<#assign varName = ObjectProperty.getObjectPropertyShortFormCS()?cap_first+Range.getKey().getzClassName()?cap_first>
	<#else>
	<#assign varName = ObjectProperty.getObjectPropertyShortFormCS()?cap_first>
	</#if>
	<#if ObjectProperty.getLabel()?? || ObjectProperty.getComments()??>    
   
    /**
	<#if ObjectProperty.getLabel()??>* Property ${ObjectProperty.getLabel()}</#if><#if ObjectProperty.getComments()??>* function add${varName?cap_first}
	<#list ObjectProperty.getComments()?split("\n") as line>* ${line}</#list></#if>
	*/</#if>     	
    public void add${varName?cap_first} (I${Range.getKey().getzClassName()} parameter);
	
	public Set<I${Range.getKey().getzClassName()}> get${varName?cap_first}<#if Range.getValue()[1] == true>From${Zclass.getzClassName()}</#if>();
    </#if></#list></#list></#if>    
    
    <#if Zclass.getZDataPropertyList()??>
    <#list Zclass.getZDataPropertyList() as DataPropertyList>
    <#if DataPropertyList.isGeneratable() == true>
    <#if DataPropertyList.getLabel()?? || DataPropertyList.getComments()??>    
    /**<#if DataPropertyList.getLabel()??>
    * Property ${DataPropertyList.getLabel()}</#if> <#if (DataPropertyList.getComments())??><#list (DataPropertyList.getComments())?split("\n") as line>
    * ${line}</#list></#if>
	*/</#if>
	public void <#if DataPropertyList.getDataPropertyShortFormCS()?cap_first == "ID">set${Zclass.getzClassName()}<#else>set</#if>${DataPropertyList.getDataPropertyShortFormCS()?cap_first} (${DataPropertyList.getRangeXSDType()} parameter);
	
	public String <#if DataPropertyList.getDataPropertyShortFormCS()?cap_first == "ID">get${Zclass.getzClassName()}<#else>get</#if>${DataPropertyList.getDataPropertyShortFormCS()?cap_first}<#if DataPropertyList.isHidingParentProperty() == true>From${Zclass.getzClassName()}</#if> ();
	</#if></#list></#if>	
}
