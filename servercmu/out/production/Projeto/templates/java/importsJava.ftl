<#assign importList = [
{'key': '1', 'type': 'Date' , 'import': 'java.util.Date'},
{'key': '2', 'type': 'Date' , 'import': 'java.text.SimpleDateFormat'}
] />

<#assign imported = [] >


<#macro import type>
    <#compress>

        <#list importList as importItem>

            <#if type?starts_with(importItem['type']) &&  imported?seq_contains(importItem['key']) == false >
                <#assign imported = imported + [importItem['key']]  >
                <#t>import ${importItem['import']};
            </#if>

        </#list>

    </#compress>
</#macro>

<#function filter things name value>
    <#local result = []>
    <#list things as thing>
        <#if thing[name] == value>
            <#local result = result + [thing]>
        </#if>
    </#list>
    <#return result>
</#function>




