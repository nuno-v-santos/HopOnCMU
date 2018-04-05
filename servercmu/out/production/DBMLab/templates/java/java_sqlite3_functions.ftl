<#macro bd_imports >
import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
</#macro>

<#macro bd_instance >
SQLiteConn.getInstace("src/ORM.db");
</#macro>