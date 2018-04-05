<#import bdincludefunctions as bd >
<#macro superName class><@compress single_line=true>
    <#if class.extend??><@superName class=class.extend/><#else>${class.name}</#if>
</@compress></#macro>
<#if pkg?contains("sim")>package proj.${pkg?lower_case};</#if>

<@bd.bd_imports />
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
<#include "importsJava.ftl" >
<#compress >
    <#list attributes as attribute>
        <#t><@import type=attribute.type/>
    </#list>
</#compress>
<#assign requiredList = filter(attributes, "required", true) >

public class ${name}<#if extend??> extends ${extend.name}</#if> {
    <#if !extend??><#if superClass >protected <#else >private </#if>int id;</#if>
    <#if superClass >protected <#else >private </#if>static BdConnection con = <@compress single_line=true ><@bd.bd_instance/></@compress>
    <#list attributes as attribute>
    <#if superClass >protected <#else >private </#if>${attribute.type} ${attribute.name};
    </#list>

    // public constructor
    public ${name}(<#compress><#list requiredList as attribute>${attribute.type} ${attribute.name}<#sep>, </#sep></#list></#compress>) {
        <#if extend??>super();</#if>
        this.id = -1;
        <#list requiredList as attribute>
        this.${attribute.name} = ${attribute.name};
        </#list>
    }

    <#if (requiredList?size > 0) >
    //protected construtor for child and internal consume
    protected ${name}() {
        <#if extend??>super();</#if>
        this.id=-1;
    }
    </#if>

    <#if extend?? >
    public ${name}(${extend.name} e) {
        //<@superName class=extend/>
        super();
        this.id = e.id;
        <#list extend.attributes as attribute>
        this.${attribute.name} = e.${attribute.name};
        </#list>
    }
    </#if>

    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    <#list attributes as attribute>
    <#-- Getter -->
    public ${attribute.type} get${attribute.name?cap_first}() {
        return ${attribute.name};
    }

    <#-- Setter -->
    public void set${attribute.name?cap_first}(${attribute.type} ${attribute.name}) {
        <#if attribute.max??>

        if(${attribute.name} > ${attribute.max?replace(',','')}){
            throw  new RuntimeException("${attribute.name} tem de ser inferior ou igual a ${attribute.max}");
        }
        </#if>
        <#if attribute.min??>

        if(${attribute.name} < ${attribute.min?replace(',','')}){
            throw  new RuntimeException("${attribute.name} tem de ser maior ou igual a ${attribute.min}");
        }
        </#if>
        <#if attribute.notEmpty>

        if(${attribute.name}.isEmpty()){
        throw  new RuntimeException("${attribute.name} não pode ser vazio");
        }
        </#if>
        <#if attribute.contains??>

        if(!${attribute.name}.contains("${attribute.contains}")){
            throw  new RuntimeException("${attribute.name} deve conter a string ${attribute.contains}");
        }
        </#if>
        <#if attribute.maxLength??>

        if(${attribute.name}.length() > ${attribute.maxLength?replace(',','')}){
            throw  new RuntimeException("${attribute.name} tem de ter menos de ${attribute.maxLength+1} carateres");
        }
        </#if>
        <#if attribute.minLength??>

        if(${attribute.name}.length() < ${attribute.minLength?replace(',','')}){
            throw  new RuntimeException("${attribute.name} tem de ter mais de ${attribute.minLength-1} carateres");
        }
        </#if>

        this.${attribute.name} = ${attribute.name};
    }

    </#list>


<#include bdinclude>
<#include "java_relation_sql.ftl" >

    <#if protectedMethods??>
    //@Protected Start
        ${protectedMethods}
    //@Protected End
    </#if>

    @Override
    public String toString() {
        return <#if extend??>super.toString() + </#if>"${name}{" +
                <#list attributes as attribute>
                " ${attribute.name}=" + ${attribute.name} +
                </#list>
                '}';
    }

}