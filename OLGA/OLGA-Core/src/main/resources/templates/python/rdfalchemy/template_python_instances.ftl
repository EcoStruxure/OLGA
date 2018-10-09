from rdflib import Namespace
from rdfalchemy.rdfSubject import rdfSubject
from rdfalchemy import rdfSingle, rdfMultiple, rdfList

<#-- Import dependent parents -->
<#list Zclass.getSuperZClassList() as Zclass>
<#if Zclass??>from ${ontologyName}.${Zclass.packageName}.${Zclass.zClassName} import ${Zclass.zClassName}</#if></#list>

class ${ZInstance.getzInstanceName()}(<#if (Zclass.getSuperZClassList()?size < 1)>rdfSubject</#if><#list Zclass.getSuperZClassList() as Zclass><#if Zclass??>${Zclass.zClassName}<#sep>,</#if></#list>):
    rdf_type = Namespace('${Zclass.getNamespace()}').${Zclass.zClassName}
<#if listDataPropertiesToImplement??><#list listDataPropertiesToImplement as DataPropertyList>
    set${DataPropertyList.getDataPropertyShortFormCS()?cap_first} = rdfSingle(Namespace('${DataPropertyList.getDataPropertyNamespace()}').${DataPropertyList.getDataPropertyShortForm()})
</#list></#if>

<#if listObjectPropertiesToImplement??><#list listObjectPropertiesToImplement as ObjectPropertyList><#assign i = ObjectPropertyList.getRangeListZClasses()?size>
<#if ObjectPropertyList.getRangeListZClasses()??><#list ObjectPropertyList.getRangeListZClasses() as Range><#if Range.getValue()[0] == true><#if (ObjectPropertyList.isOverridable()!false) || (i>1)><#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first+Range.getKey().getzClassName()?cap_first><#else><#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first></#if>
    ${varName?cap_first}${Range.getKey().getzClassName()} = rdfList(Namespace('${ObjectPropertyList.getObjectPropertyNamespace()}').${varName?cap_first}${Range.getKey().getzClassName()})
    listOf${varName?cap_first}${Range.getKey().getzClassName()} = []

    def add${varName?cap_first}(self, parameter):
        self.listOf${varName?cap_first}${Range.getKey().getzClassName()}.append(parameter)
        self.${varName?cap_first}${Range.getKey().getzClassName()} = self.listOf${varName?cap_first}${Range.getKey().getzClassName()}
</#if></#list></#if></#list></#if>