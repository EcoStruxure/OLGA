using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using Semiodesk.Trinity;<#if !motherToExtend??>
using Olga.Trinity;</#if><#if Zclass.hasSubClass() == true>

using Olga.Trinity.Attriubtes;</#if><#if Zclass.getSetOfImportPackages()?? && preserveNameSpaceOfOntology==true><#list Zclass.getSetOfImportPackages() as importedPackage>
using ${importedPackage};</#list></#if>

namespace ${packageName}
{
    <#if Zclass.getLabel()??>
    /// <remarks>
    /// Class ${Zclass.getLabel()}
    /// </remarks>
    </#if>
    <#if Zclass.getComments()??>
    /// <inheritdoc cref="I${Zclass.getzClassName()}"/>>
    </#if>
    [GeneratedCode("OLGA Generator", "${OLGAVersion}")]
    [RdfClass("${Zclass.getIri()}")]<#if Zclass.hasSubClass() == true>
    [QueryWithReasoning]</#if>
    public<#if generatePartial!false> partial</#if> class ${Zclass.getzClassName()}<#if Zclass.getSuperZClassList()?? || motherToExtend??> : <#if motherToExtend??><#if motherToExtend.getzClassName() == "Resource" && preserveNameSpaceOfOntology==true>${motherToExtend.getPackageName()}.${motherToExtend.getzClassName()}<#else>${motherToExtend.getzClassName()}</#if><#else>ContextResource</#if></#if>, I${Zclass.getzClassName()}
    {
        public ${Zclass.getzClassName()}(Uri uri) : base(uri)
        {
              <#if listObjectPropertiesToImplement??>
                <#list listObjectPropertiesToImplement as ObjectPropertyList>
                <#assign i = ObjectPropertyList.getRangeListZClasses()?size>
                    <#list ObjectPropertyList.getRangeListZClasses() as Range>
                    <#if Range.getValue()[0] == true>
                      <#if ObjectPropertyList.getObjectPropertyType()?contains("Some") || ObjectPropertyList.getObjectPropertyType()?contains("Only")>
            ${ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first}<#if (ObjectPropertyList.isOverridable()!false) || (i>1) >${Range.getKey().getzClassName()?cap_first}</#if> = new List<${Range.getKey().getzClassName()}>();
                     </#if>
                     </#if>
                    </#list>
                    </#list>
            </#if>
        }
         <#if listObjectPropertiesToImplement??>
            <#list listObjectPropertiesToImplement as ObjectPropertyList>
            <#assign i = ObjectPropertyList.getRangeListZClasses()?size>
                <#list ObjectPropertyList.getRangeListZClasses() as Range>
                <#if Range.getValue()[0] == true>                
        <#if ObjectPropertyList.getLabel()??>
        /// <remarks>
        ///${ObjectPropertyList.getLabel()}
        /// </remarks>
        </#if>
        <#if ObjectPropertyList.getComments()??>
        /// <inheritdoc cref="I${Zclass.getzClassName()}"/>>
        </#if>
        <#if (ObjectPropertyList.isOverridable()!false) || (i>1)>
         <#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first+Range.getKey().getzClassName()?cap_first>
        <#else>
         <#assign varName = ObjectPropertyList.getObjectPropertyShortFormCS()?cap_first>
        </#if>
        <#if ObjectPropertyList.getObjectPropertyType()?contains("Some") || ObjectPropertyList.getObjectPropertyType()?contains("Only")>
         <#assign rangeName = "List<"+Range.getKey().getzClassName()+">" >
        <#else>
         <#assign rangeName = Range.getKey().getzClassName()>
        </#if>
        <#if Range.getValue()[1] == true>new </#if>protected readonly PropertyMapping<${rangeName}> _${varName?uncap_first} = new PropertyMapping<${rangeName}>("${varName?cap_first}${rangeName}${Zclass.getzClassName()}", "${ObjectPropertyList.getObjectProperty()}");
        [RdfProperty("${ObjectPropertyList.getObjectProperty()}")]
        <#if Range.getValue()[1] == true>new </#if>public ${rangeName} ${varName?cap_first}
        {
            get => GetValue(_${varName?uncap_first});
            set => SetValue(_${varName?uncap_first}, value);
        }        
        </#if>
                </#list>
            </#list>
        </#if>
        <#if listDataPropertiesToImplement??>
            <#list listDataPropertiesToImplement as DataPropertyList>
            <#if DataPropertyList.isGeneratable() == true>
          <#noautoesc>
          <#if DataPropertyList.getLabel()??>
        ${'/// <remarks>'}
        /// ${DataPropertyList.getLabel()}
        ${'/// </remarks>'}
          </#if>
          <#if DataPropertyList.getComments()??>
        /// <inheritdoc cref="I${Zclass.getzClassName()}"/>>
          </#if>
          </#noautoesc>
        <#if DataPropertyList.isHidingParentProperty()==true>new </#if>protected readonly PropertyMapping<${DataPropertyList.getRangeXSDType()}> _${DataPropertyList.getDataPropertyShortFormCS()?uncap_first} = new PropertyMapping<${DataPropertyList.getRangeXSDType()}>("${DataPropertyList.getDataPropertyShortFormCS()?cap_first}${DataPropertyList.getRangeXSDType()}${Zclass.getzClassName()}", "${DataPropertyList.getDataProperty()}");
        [RdfProperty("${DataPropertyList.getDataProperty()}")]
        <#if DataPropertyList.isHidingParentProperty()==true>new </#if>public ${DataPropertyList.getRangeXSDType()} ${DataPropertyList.getDataPropertyShortFormCS()?cap_first}
        {
            get => GetValue(_${DataPropertyList.getDataPropertyShortFormCS()?uncap_first});
            set => SetValue(_${DataPropertyList.getDataPropertyShortFormCS()?uncap_first}, value);
        }
        </#if></#list>
        </#if>
        public override IEnumerable<Class> GetTypes()
        {
            yield return new Class(new Uri("${Zclass.getIri()}"));
        }
        <#if (Zclass.getListZInstanceIRI())??>
            <#list Zclass.getListZInstanceIRI() as Zinstance>
        public static ${Zclass.getzClassName()} ${Zinstance.getzInstanceName()?cap_first} => new ${Zclass.getzClassName()}(new Uri("${Zinstance.getIri()}"))
        {
            <#if (Zinstance.getListZDataPropertyList())??>
                <#list Zinstance.getListZDataPropertyList() as DataPropertyList>
            ${DataPropertyList.getDataPropertyShortFormCS()?cap_first} = <#if (DataPropertyList.getRangeXSDType() == "string")>"${DataPropertyList.getValue()}"<#else>${DataPropertyList.getValue()}</#if><#sep>,</#list>
          </#if>
            <#if (Zinstance.getListZObjectPropertyList())??>
                <#list Zinstance.getListZObjectPropertyList() as ObjectPropertyItem>
                    <#if (ObjectPropertyItem.getRangeListZInstances())??>                            
                               /*                           
                               ${ObjectPropertyItem.getObjectPropertyShortFormCS()?capitalize}
                                = new System.Collections.Generic.List<${ObjectPropertyItem.getInstancesClass()}>()
                            {
                                <#list ObjectPropertyItem.getRangeListZInstances() as Range>
                                InstancesOf${Range.getParentClassIRI().getShortForm()}.${Range.getzInstanceName()} <#if (Range_has_next)>,</#if>
                                </#list>
                            }
                            */        
                    </#if>
                </#list>
            </#if>
        };
            </#list>
        </#if>
    }
}