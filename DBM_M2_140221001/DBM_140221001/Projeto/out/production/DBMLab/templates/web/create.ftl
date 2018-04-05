<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create a ${name}</title>
</head>
<body>
<h1>Create a ${name?lower_case}</h1>
<form action="create" method="post">
<#list attributes as attribute>
    <label for="${attribute.name}">${attribute.name?cap_first}</label>
    <input id="${attribute.name}" name="${attribute.name}" type="text"/>
</#list >
    <input type="submit" value="Create"/>
</form>
</body>
</html>