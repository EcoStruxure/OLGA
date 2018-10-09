from rdflib import Namespace
from rdfalchemy.rdfSubject import rdfSubject
from rdfalchemy import rdfSingle, rdfMultiple, rdfList

<#-- Import dependent parents -->
<#if Zclass.getSuperZClassList()??>
<#list Zclass.getSuperZClassList() as Zclass>
<#if Zclass??>from ${ontologyName}.${Zclass.packageName}.${Zclass.zClassName} import ${Zclass.zClassName}
</#if>
</#list>
</#if>
<#if Zclass.getLabel()??># Class ${Zclass.getLabel()}</#if><#if Zclass.getComments()??><#list Zclass.getComments()?split("\n") as line>
# ${line}</#list></#if>


class ${Zclass.getzClassName()}(<#if (Zclass.getSuperZClassList()?size < 1)>rdfSubject</#if><#list Zclass.getSuperZClassList() as Zclass><#if Zclass??>${Zclass.zClassName}<#sep>,</#if></#list>):
    rdf_type = Namespace('${Zclass.getNamespace()}').${Zclass.zClassName}
    
    <#if listDataPropertiesToImplement??><#list listDataPropertiesToImplement as DataPropertyList><#if DataPropertyList.getLabel()??>
    # Property ${DataPropertyList.getLabel()}</#if><#if DataPropertyList.getComments()??><#list DataPropertyList.getComments()?split("\n") as line>
    # ${line}
</#list></#if>
    set${DataPropertyList.getDataPropertyShortFormCS()?cap_first} = rdfSingle(Namespace('${DataPropertyList.getDataPropertyNamespace()}').${DataPropertyList.getDataPropertyShortForm()})
    </#list></#if><#if listObjectPropertiesToImplement??>

<#list listObjectPropertiesToImplement as ObjectPropertyList><#assign i = ObjectPropertyList.getRangeListZClasses()?size>
<#list ObjectPropertyList.getRangeListZClasses() as Range><#if Range.getValue()[0] == true><#if ObjectPropertyList.getLabel()??>
    # Property ${ObjectPropertyList.getLabel()}</#if><#if ObjectPropertyList.getComments()??><#list ObjectPropertyList.getComments()?split("\n") as line>
    # ${line}
</#list>
</#if><#if (ObjectPropertyList.isOverridable()!false) || (i>1)><#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first+Range.getKey().getzClassName()?cap_first><#else><#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first></#if>
    ${varName?cap_first}${Range.getKey().getzClassName()} = rdfList(Namespace('${ObjectPropertyList.getObjectPropertyNamespace()}').${varName?cap_first}${Range.getKey().getzClassName()})
    listOf${varName?cap_first}${Range.getKey().getzClassName()} = []
    
    def add${varName?cap_first}(self, parameter):
        self.listOf${varName?cap_first}${Range.getKey().getzClassName()}.append(parameter)
        self.${varName?cap_first}${Range.getKey().getzClassName()} = self.listOf${varName?cap_first}${Range.getKey().getzClassName()}
        </#if></#list></#list></#if>