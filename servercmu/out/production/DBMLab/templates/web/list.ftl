<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List all ${name}'s instances</title>
</head>
<body>
<h1>List all ${name}'s instances</h1>
<a href="create">Create a ${name?lower_case}</a>
<ul>
${r"<#list list as "+name?lower_case+">"}
    <li><a href="get?id=${r"${"+name?lower_case+".id}"}">${r"${"+name+"}"}</a><a
            href="delete?id=${r"${"+name?lower_case+".id}"}">[DELETE]</a></li>
${r"</#list>"}
</ul>
</body>
</html>