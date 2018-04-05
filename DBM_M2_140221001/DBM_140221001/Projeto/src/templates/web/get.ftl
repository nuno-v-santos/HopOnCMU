<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Get ${r"${"+name?lower_case+".name}"}</title>
</head>
<body>
<h1>${name}'s details</h1>
<dl>
<#list attributes as attribute>
    <dt>${attribute.name?cap_first}</dt>
    <dd>${r"${"+name?lower_case+"."+attribute.name+"}"}</dd>
</#list>
</dl>

<a href="update?id=${r"${"+name?lower_case+".id}"}">Update</a>
</body>
</html>