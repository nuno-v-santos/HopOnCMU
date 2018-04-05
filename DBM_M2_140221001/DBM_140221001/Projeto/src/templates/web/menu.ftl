
<nav class="navbar navbar-default navbar-fixed">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-example-2">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Home</a>
        </div>
        <ul class="nav navbar-nav ">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    Entities
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <#list classes as class>
                    <li><a href="/${class.name?lower_case}/list">${class.name}</a></li>
                    </#list>
                </ul>
            </li>
        </ul>
    </div>


</nav>