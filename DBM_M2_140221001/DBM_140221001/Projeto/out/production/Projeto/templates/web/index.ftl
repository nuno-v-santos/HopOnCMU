<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Panel</title>
${r"<#include"} "./imports.html">
</head>
<body>

<div class="main-panel" style="width : 100%; float : none ; height: 100%">
${r"<#include"} "./menu.html">
    <div class="content">
        <div class="container-fluid">
            <div class="col-md-4 col-md-offset-4">

                <div class="card">
                    <div class="header">
                        <h4 class="title">Entities</h4>
                    </div>
                    <div class="content">
                        <ul class="list-group">
                        <#list classes as class>
                            <li class="list-group-item"><a href="/${class.name?lower_case}/list">${class.name}</a></li>
                        </#list>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>