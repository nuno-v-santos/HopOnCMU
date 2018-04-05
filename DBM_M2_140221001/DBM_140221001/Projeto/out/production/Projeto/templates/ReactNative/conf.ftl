{
    "url": "http://192.168.1.68:1234",
    "classes": [
        <#list classes as class >
        {
        "name": "${class.name}",
        "Attributes": [
            <#list class.attributes as attribute >
            { "name" : "${attribute.name}" , "type" : "${attribute.type}"}<#sep>,</#sep>
            </#list>
        ]
        }<#sep>,</#sep>
        </#list>
    ]
}
