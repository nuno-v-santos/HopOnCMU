<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create a ${name}</title>
${r"<#include"} "../imports.html"${r">"}
</head>
<div class="main-panel" style="width : 100%; float : none ; height: 100%">
  ${r"<#include"} "../menu.html">


    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="card">
                        <div class="header">
                            <h4>Create a ${name?lower_case}</h4>
                        </div>

                        <div class="content">

                            <form id="myForm" action="create" method="post">
                                <input type="hidden" />
                            <#list attributes as attribute>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="${attribute.name}">${attribute.name?cap_first}</label>
                                        <input class="form-control" id="${attribute.name}" name="${attribute.name}"
                                               <#if attribute.required>required</#if>
                                               <#if attribute.maxLength??>data-maxlength="${attribute.maxLength?replace(',','')}"</#if>
                                               <#if attribute.minLength??>data-minlength="${attribute.minLength?replace(',','')}"</#if>
                                               <#if attribute.min??>min="${attribute.min?replace(',','')}"</#if>
                                               <#if attribute.max??>max="${attribute.max?replace(',','')}"</#if>
                                               <#if attribute.type?contains('int')>type="number"<#else>type="text"</#if>/>
                                    </div>
                                </div>
                            </#list >
                                <div class="col-md-12">
                                    <button type="submit" class="btn btn-success btn-fill pull-right">Create</button>
                                    <a class="btn btn-fill " href="list">
                                        <i class="pe-7s-back"></i>
                                    </a>
                                </div>
                                <div class="clearfix"></div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<body>
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