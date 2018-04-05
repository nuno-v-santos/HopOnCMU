<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update ${r"${"+name?lower_case+".name}"}</title>
${r"<#include"} "../imports.html"${r">"}
</head>
<body>

<div class="main-panel" style="width : 100%; float : none ; height: 100%">
${r"<#include"} "../menu.html">


    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6 ">
                    <div class="card">
                        <div class="header">
                            <h4>Edit</h4>
                        </div>

                        <div class="content">

                            <form id="myForm" action="update" method="post">
                                <input name="id" type="hidden" value="${r"${"+name?lower_case+".id}"}"/>

                            <#list attributes as attribute>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="${attribute.name}">${attribute.name?cap_first}</label>
                                        <input class="form-control" id="${attribute.name}" name="${attribute.name}"
                                               type="text" value="${r"${"+name?lower_case+"."+attribute.name+"}"}"
                                               <#if attribute.required>required</#if>
                                               <#if attribute.maxLength??>data-maxlength="${attribute.maxLength?replace(',','')}"</#if>
                                               <#if attribute.minLength??>data-minlength="${attribute.minLength?replace(',','')}"</#if>
                                               <#if attribute.min??>min="${attribute.min?replace(',','')}"</#if>
                                               <#if attribute.max??>max="${attribute.max?replace(',','')}"</#if>
                                               <#if attribute.type?contains('int')>type="number"<#else>type="text"</#if>/>
                                    </div>
                                </div>
                            </#list >
                            <#list relations as relation>
                                <#switch relation.type>
                                    <#case "ManyToOne">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>${relation.target.name?lower_case}</label>
                                                <select class="form-control"
                                                        name="${relation.target.name?lower_case}Id">
                                                    <option value="-1">none</option>
                                                ${r"<#list "+relation.target.name?lower_case+" as p>"}
                                                    <option ${r"<#if p.id == "+name?lower_case+""+relation.target.name+".id >"}selected="selected"
                                                            ${r"</#if>"}value="${r"${p.id}"}"> ${r"${p}"}</option>
                                                ${r"</#list>"}
                                                </select>
                                            </div>
                                        </div>
                                        <#break>
                                    <#case "OneToOne">
                                        <#if relation.base.name?contains(name) >
                                            <div class="col-md-6">
                                                <div class="form-group">
                                                    <label>${relation.target.name?lower_case}</label>
                                                    <select class="form-control"
                                                            name="${relation.target.name?lower_case}Id">
                                                        <option value="-1">none</option>
                                                    ${r"<#list "+relation.target.name?lower_case+" as p>"}
                                                        <option ${r"<#if p.id == "+name?lower_case+""+relation.target.name+".id >"}selected="selected"
                                                                ${r"</#if>"}value="${r"${p.id}"}"> ${r"${p}"}</option>
                                                    ${r"</#list>"}
                                                    </select>
                                                </div>
                                            </div>
                                        <#else>
                                            <div class="col-md-6">
                                                <div class="form-group">
                                                    <label>${relation.base.name?lower_case}</label>
                                                    <select class="form-control"
                                                            name="${relation.base.name?lower_case}Id">
                                                        <option value="-1">none</option>
                                                    ${r"<#list "+relation.base.name?lower_case+" as p>"}
                                                        <option ${r"<#if p.id == "+name?lower_case+""+relation.base.name+".id >"}selected="selected"
                                                                ${r"</#if>"}value="${r"${p.id}"}"> ${r"${p}"}</option>
                                                    ${r"</#list>"}
                                                    </select>
                                                </div>
                                            </div>
                                        </#if>

                                        <#break>
                                </#switch>
                            </#list>

                                <br>
                                <div class="col-md-12">
                                    <button type="submit" class="btn btn-success btn-fill pull-right">Update</button>
                                    <a href="list" class="btn btn-fill ">
                                        <i class="pe-7s-back"></i>
                                    </a>
                                </div>
                                <div class="clearfix"></div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card">
                        <div class="header">
                            <h4>Relations</h4>
                        </div>


                    <#list relations as relation>
                        <#switch relation.type>
                            <#case "OneToMany">
                                <div class="content table-responsive table-full-width">
                                    <div class="header"
                                    <p class="category">${relation.target.name}</p></div>
                                <table class="table table-hover table-striped">
                                    <thead>
                                    <th>Id</th>
                                        <#list relation.target.attributes as attribute>
                                        <th>${attribute.name}</th>
                                        </#list>
                                    <th>actions</th>
                                    </thead>
                                ${r"<#list "+relation.target.name?lower_case+" as a>"}
                                ${r"<#assign tem=false>"}
                                ${r"<#list "+relation.base.name?lower_case+""+relation.target.name+" as aa>"}
                                ${r"<#if aa.id == a.id><#assign tem=true></#if>"}
                                ${r"</#list>"}
                                    <tr ${r"<#if"} tem> class="success"${r"<#else>"}class="danger"${r"</#if>"}>
                                        <td>${r"${a.id}"}</td>
                                        <#list relation.target.attributes as attribute>
                                            <td> ${r"${a."+attribute.name+"}"}</td>
                                        </#list>
                                        <td>

                                        ${r" <#if tem>"}
                                            <a href="remove${relation.target.name}?id=${r"${"+relation.base.name?lower_case+".id}"}&rmv=${r"${a.id}"}">
                                                <button class="btn btn-danger btn-fill ">remove</button>
                                            </a>
                                        ${r"<#else>"}
                                            <form method="post" action="add${relation.target.name}">
                                                <input name="id"
                                                       value="${r"${"+relation.base.name?lower_case+".id}"}"
                                                       type="hidden">
                                                <input name="add" value="${r"${a.id}"}" type="hidden">
                                                <button class="btn btn-success btn-fill ">add</button>
                                            </form>

                                        ${r" </#if>"}
                                        </td>
                                    </tr>
                                ${r"</#list>"}
                                </table>
                                <#break>

                            <#case "ManyToMany">

                                <#if relation.base.name?contains(name) >
                                    <div class="content table-responsive table-full-width">
                                        <div class="header"
                                        <p class="category">${relation.target.name}</p></div>
                                    <table class="table table-hover table-striped">
                                        <thead>
                                        <th>Id</th>
                                            <#list relation.target.attributes as attribute>
                                            <th>${attribute.name}</th>
                                            </#list>
                                        <th>actions</th>
                                        </thead>
                                    ${r"<#list "+relation.target.name?lower_case+" as a>"}
                                    ${r"<#assign tem=false>"}
                                    ${r"<#list "+relation.base.name?lower_case+""+relation.target.name+" as aa>"}
                                    ${r"<#if aa.id == a.id><#assign tem=true></#if>"}
                                    ${r"</#list>"}
                                        <tr ${r"<#if"} tem> class="success"${r"<#else>"}class="danger"${r"</#if>"}>
                                            <td>${r"${a.id}"}</td>
                                            <#list relation.target.attributes as attribute>
                                                <td> ${r"${a."+attribute.name+"}"}</td>
                                            </#list>
                                            <td>

                                            ${r" <#if tem>"}
                                                <a href="remove${relation.target.name}?id=${r"${"+relation.base.name?lower_case+".id}"}&rmv=${r"${a.id}"}">
                                                    <button class="btn btn-danger btn-fill ">remove</button>
                                                </a>
                                            ${r"<#else>"}
                                                <form method="post" action="add${relation.target.name}">
                                                    <input name="id"
                                                           value="${r"${"+relation.base.name?lower_case+".id}"}"
                                                           type="hidden">
                                                    <input name="add" value="${r"${a.id}"}" type="hidden">
                                                    <button class="btn btn-success btn-fill ">add</button>
                                                </form>

                                            ${r" </#if>"}
                                            </td>
                                        </tr>
                                    ${r"</#list>"}
                                    </table>

                                <#else>
                                    <div class="content table-responsive table-full-width">
                                        <div class="header"
                                        <p class="category">${relation.base.name}</p></div>
                                    <table class="table table-hover table-striped">
                                        <thead>
                                        <th>Id</th>
                                            <#list relation.base.attributes as attribute>
                                            <th>${attribute.name}</th>
                                            </#list>
                                        <th>actions</th>
                                        </thead>
                                    ${r"<#list "+relation.base.name?lower_case+" as a>"}
                                    ${r"<#assign tem=false>"}
                                    ${r"<#list "+relation.target.name?lower_case+""+relation.base.name+" as aa>"}
                                    ${r"<#if aa.id == a.id><#assign tem=true></#if>"}
                                    ${r"</#list>"}
                                        <tr ${r"<#if"} tem> class="success"${r"<#else>"}class="danger"${r"</#if>"}>
                                            <td>${r"${a.id}"}</td>
                                            <#list relation.base.attributes as attribute>
                                                <td> ${r"${a."+attribute.name+"}"}</td>
                                            </#list>
                                            <td>

                                            ${r" <#if tem>"}
                                                <a href="remove${relation.base.name}?id=${r"${"+relation.target.name?lower_case+".id}"}&rmv=${r"${a.id}"}">
                                                    <button class="btn btn-danger btn-fill ">remove</button>
                                                </a>
                                            ${r"<#else>"}
                                                <form method="post" action="add${relation.base.name}">
                                                    <input name="id"
                                                           value="${r"${"+relation.target.name?lower_case+".id}"}"
                                                           type="hidden">
                                                    <input name="add" value="${r"${a.id}"}" type="hidden">
                                                    <button class="btn btn-success btn-fill ">add</button>
                                                </form>

                                            ${r" </#if>"}
                                            </td>
                                        </tr>
                                    ${r"</#list>"}
                                    </table>

                                </#if>
                                <#break>
                        </#switch>
                    </#list>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>

<script>
    $('#myForm').validator({
        custom: {
            'maxlength': function ($el) {
                var maxlength = $el.data('maxlength')
                return !$el.val() || $el.val().length <= maxlength
            }
        },
        errors: {
            'maxlength': "Too long"
        }
    });
</script>