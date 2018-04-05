<#include "java_sqlite_datatype_converter.ftl">
PRAGMA foreign_keys = 0;
<#list classes as class>

CREATE TABLE IF NOT EXISTS ${class.name}(
    id INTEGER <#if !class.extend?? >PRIMARY KEY AUTOINCREMENT<#else>REFERENCES ${class.extend.name}(id)</#if>,
    <#list class.attributes as attribute >
    ${attribute.name} ${JAVA_TO_SQLITE_CONVERTER[attribute.type]}<#sep>, </#sep>
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
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ${relation.base.name?lower_case}_id INTEGER REFERENCES ${relation.base.name} (id),
    ${relation.target.name?lower_case}_id INTEGER REFERENCES ${relation.target.name} (id)
);
            <#break>

        <#case "OneToOne">
            <#t>ALTER TABLE ${relation.base.name} ADD COLUMN [${relation.target.name?lower_case}_id] INTEGER REFERENCES ${relation.target.name} (id);
            <#t>create unique index unique_${relation.target.name} on ${relation.base.name}(${relation.target.name?lower_case}_id);
            <#break>

        <#default>
    </#switch>

</#list>


PRAGMA foreign_keys = 1;