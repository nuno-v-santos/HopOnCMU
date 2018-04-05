<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List all ${name}'s instances</title>
${r"<#include"} "../imports.html"${r">"}
</head>
<body>
<div class="main-panel" style="width : 100%; float : none ; height: 100%">
${r"<#include"} "../menu.html">


    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="header">
                            <h4 class="title">${name} List</h4>
                            <a href="create">Create a ${name?lower_case}</a>
                        </div>

                        <div class="content table-responsive table-full-width">
                            <table class="table table-hover table-striped">
                                <thead>
                                <th>Id</th>
                                <#list attributes as attribute>
                                <th>${attribute.name}</th>
                                </#list>
                                <#list relations as relation>
                                    <#switch relation.type>
                                        <#case "OneToOne">
                                            <#if name?contains(relation.base.name)>
                                            <th>${relation.target.name?lower_case}Id</th>
                                            <#else>
                                            <th>${relation.base.name?lower_case}Id</th>
                                            </#if>
                                            <#break>
                                        <#case "ManyToOne">
                                            <#if name?contains(relation.base.name)>
                                            <th>${relation.target.name?lower_case}Id</th>
                                            <#else>
                                            <th>${relation.base.name?lower_case}Id</th>
                                            </#if>
                                            <#break >
                                    </#switch>
                                </#list>
                                <th>actions</th>
                                </thead>
                                <tbody>
                                ${r"<#list list as "+name?lower_case+">"}
                                <tr>
                                    <td>${r"${"+name?lower_case+".id}"}</td>
                                <#list attributes as attribute >
                                    <td>${r"${"+name?lower_case+"."+attribute.name+"}"}</td>
                                </#list>
                                <#list relations as relation>
                                    <#switch relation.type>
                                        <#case "OneToOne">
                                            <#if name?contains(relation.base.name)>
                                                <td> ${r"<#if "+name?lower_case+"."+relation.target.name?lower_case+"??>"}${r"${"+name?lower_case+"."+relation.target.name?lower_case+".id}"}${r"</#if>"}</td>
                                            <#else>
                                                <td>${r"${"+name?lower_case+"."+relation.base.name?lower_case+".id}"}</td>
                                            </#if>
                                            <#break>
                                        <#case "ManyToOne">
                                            <td> ${r"<#if "+name?lower_case+"."+relation.target.name?lower_case+"??>"}${r"${"+name?lower_case+"."+relation.target.name?lower_case+".id}"}${r"</#if>"}</td>
                                            <#break >
                                    </#switch>
                                </#list>
                                    <td>
                                        <a href="update?id=${r"${"+name?lower_case+".id}"}">
                                            <button type="submit" class="btn btn-warning btn-fill ">Update</button>
                                        </a>
                                        <a href="delete?id=${r"${"+name?lower_case+".id}"}">
                                            <button type="submit" class="btn btn-danger btn-fill ">Remove</button>
                                        </a>
                                    </td>
                                </tr>
                                ${r"</#list>"}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>