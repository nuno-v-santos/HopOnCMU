<#include "java_mysql_datatype_converter.ftl">
DROP DATABASE IF EXISTS ORM;
CREATE DATABASE IF NOT EXISTS ORM;
USE ORM;
<#list classes as class>

CREATE TABLE IF NOT EXISTS ${class.name}(
    id INTEGER PRIMARY KEY<#if !class.extend?? > AUTO_INCREMENT<#else> NOT NULL</#if>,
    <#list class.attributes as attribute >
    ${attribute.name} ${JAVA_TO_MYSQL_CONVERTER[attribute.type]}<#sep>, </#sep>
    </#list>
);
</#list>


<#list relations as relation >

    <#switch relation.type>

        <#case "OneToMany">
        <#t>ALTER TABLE ${relation.target.name} ADD COLUMN ${relation.base.name?lower_case}_id INTEGER REFERENCES ${relation.base.name} (id);
            <#break>

        <#case "ManyToOne">
            <#t>ALTER TABLE ${relation.base.name} ADD COLUMN ${relation.target.name?lower_case}_id INTEGER REFERENCES ${relation.target.name} (id);
            <#break>

        <#case  "ManyToMany" >
CREATE TABLE IF NOT EXISTS Relation_${relation.base.name}_${relation.target.name}(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    ${relation.base.name?lower_case}_id INTEGER REFERENCES ${relation.base.name} (id),
    ${relation.target.name?lower_case}_id INTEGER REFERENCES ${relation.target.name} (id)
);
            <#break>

        <#case "OneToOne">
ALTER TABLE ${relation.base.name} ADD COLUMN `${relation.target.name?lower_case}_id` INTEGER REFERENCES ${relation.target.name} (id);
ALTER TABLE ${relation.base.name} ADD CONSTRAINT constraint_${relation.target.name} UNIQUE (${relation.target.name?lower_case}_id);

        <#break>

        <#default>
    </#switch>

</#list>

<#list classes as classe>
    <#if classe.extend??>
ALTER TABLE ${classe.name} ADD CONSTRAINT fk_${classe.extend.name}_${classe.name}_id FOREIGN KEY (id) REFERENCES ${classe.extend.name}(id);
    </#if>
</#list>