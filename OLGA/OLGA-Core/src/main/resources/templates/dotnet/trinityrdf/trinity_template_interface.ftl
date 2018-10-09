using System.CodeDom.Compiler;<#if Zclass.getZDataPropertyList()??><#list Zclass.getZDataPropertyList() as packageName><#if packageName.getPackageName()??>
using ${packageName.getPackageName()};</#if></#list></#if><#if hasCollections == true>
using System.Collections.Generic;</#if><#if Zclass.getSetOfImportPackages()?? && preserveNameSpaceOfOntology==true><#list Zclass.getSetOfImportPackages() as importedPackage>
using ${importedPackage};</#list></#if>

namespace ${packageName}
{
    <#if Zclass.getLabel()??>
    <#noautoesc>
    ${'/// <remarks>'}
    </#noautoesc>
    /// Class ${Zclass.getLabel()}
    <#noautoesc>
    ${'/// </remarks>'}
    </#noautoesc>
    </#if>
    <#if Zclass.getComments()??>
    /// <summary>
    <#list Zclass.getComments()?split("\n") as line>
    /// ${line}
    </#list>
    /// </summary>
    </#if><#list motherClassList as SuperZClassCurrentElementOfList>
    /// <inheritdoc cref="I${SuperZClassCurrentElementOfList.getzClassName()}"/>
    </#list>
    [GeneratedCode("OLGA Generator", "${OLGAVersion}")]
    <#assign interfaceName = 'I' + Zclass.getzClassName()/>
    public<#if generatePartial!false> partial</#if> interface ${interfaceName}<#if motherClassList??><#if motherClassList?has_content> : <#list motherClassList as SuperZClassCurrentElementOfList><#if SuperZClassCurrentElementOfList.getzClassName() == "Resource" && preserveNameSpaceOfOntology==true>${SuperZClassCurrentElementOfList.getPackageName()}.I${SuperZClassCurrentElementOfList.getzClassName()}<#else>I${SuperZClassCurrentElementOfList.getzClassName()}</#if><#sep>, </#list><#else> : Semiodesk.Trinity.IResource</#if></#if>
    {
     <#if objectPropertyList??>
        <#list objectPropertyList as ObjectProperty>
        <#assign i = ObjectProperty.getRangeListZClasses()?size>
            <#list ObjectProperty.getRangeListZClasses() as Range>
            <#if Range.getValue()[0] == true>
      <#if ObjectProperty.getLabel()??>
      /// <remarks>
      /// ${ObjectProperty.getLabel()}
      /// </remarks>
      </#if>
      <#if ObjectProperty.getComments()??>
      <#noautoesc>
        ${'/// <summary>'} function Add${ObjectProperty.getObjectPropertyShortFormCS()?cap_first}_${ObjectProperty.getObjectPropertyType()}_<#if ObjectProperty.getObjectPropertyCardinality()?has_content>${ObjectProperty.getObjectPropertyCardinality()?lower_case}</#if>${Range.getKey().getzClassName()}
        <#list ObjectProperty.getComments()?split("\n") as line>
        /// ${line}
        </#list>
        /// <seealso cref="${Range.getKey().getzClassName()}}"/>
        ${'/// </summary>'}
        ${'/// <param name="I${Range.getKey().getzClassName()}"></param>'}
      </#noautoesc>
      </#if>
      <#if ObjectProperty.getObjectPropertyType()?contains("Some") || ObjectProperty.getObjectPropertyType()?contains("Only")>
        <#if Range.getValue()[1] == true>new </#if>List<${Range.getKey().getzClassName()}> ${ObjectProperty.getObjectPropertyShortFormCS()?cap_first}<#if (ObjectProperty.isOverridable()!false) || (i>1)>${Range.getKey().getzClassName()?cap_first}</#if> { get; set; }
      <#else>
        <#if Range.getValue()[1] == true>new </#if>${Range.getKey().getzClassName()} ${ObjectProperty.getObjectPropertyShortFormCS()?cap_first}<#if (ObjectProperty.isOverridable()!false) || (i>1) >${Range.getKey().getzClassName()?cap_first}</#if> { get; set; }</#if>
        </#if>
        </#list>
    </#list>
    </#if>
       <#if dataPropertyList??>
        <#list dataPropertyList as DataProperty>
        <#if DataProperty.isGeneratable() == true>
      <#if DataProperty.getLabel()??>
        /// <remarks>
        /// ${DataProperty.getLabel()}
        /// </remarks>
      </#if>
       <#noautoesc>
      <#if DataProperty.getComments()??>
        ${'/// <summary>'}
      <#list DataProperty.getComments()?split("\n") as line>
        /// ${line}
        </#list>
        ${'/// </summary>'}
        </#if>
      </#noautoesc>
        <#if DataProperty.isHidingParentProperty()>new </#if>${DataProperty.getRangeXSDType()} ${DataProperty.getDataPropertyShortFormCS()?cap_first} { get; set; }
        </#if></#list>
    </#if>
    }
}