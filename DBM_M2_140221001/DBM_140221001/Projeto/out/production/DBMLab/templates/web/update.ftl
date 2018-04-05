<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update ${r"${"+name?lower_case+".name}"}</title>
</head>
<body>
<h1>Update ${name?lower_case}</h1>
<form action="update" method="post">
    <input name="id" type="hidden" value="${r"${"+name?lower_case+".id}"}"/>
<#list attributes as attribute>
    <label for="${attribute.name}">${attribute.name?cap_first}</label>
    <input id="${attribute.name}" name="${attribute.name}" type="text" value="${r"${"+name?lower_case+"."+attribute.name+"}"}"/>
</#list >
    <input type="submit" value="Update"/>
</form>
</body>
</html>