package ${pkg?lower_case};
<#function filter things name value>
    <#local result = []>
    <#list things as thing>
        <#if thing[name] == value>
            <#local result = result + [thing]>
        </#if>
    </#list>
    <#return result>
</#function>
<#assign requiredList = filter(attributes, "required", true) >
import utils.sqlite.SQLiteConn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
<#assign uses_date = false>
<#assign uses_list = false>
<#list attributes as attribute>
    <#if attribute.type == "Date" && uses_date == false>
        <#assign uses_date = true>
        <#lt>import java.util.Date;
    </#if>
    <#if attribute.type?starts_with("List") && uses_list == false>
        <#assign uses_list = true>
        <#lt>import java.util.List;
    </#if>
</#list>

public class ${name} {
    private int id;
    <#list attributes as attribute>
    private ${attribute.type} ${attribute.name};
    </#list>


    public ${name}(<#compress><#list requiredList as attribute>${attribute.type} ${attribute.name}<#sep>, </#sep></#list></#compress> ) {
        this.id=-1;
    <#list requiredList as attribute>
        this.${attribute.name} = ${attribute.name};
    </#list>

    }

    <#if (requiredList?size > 0) >
    private ${name}() {
        this.id=-1;
    }
    </#if>

    public int getId(){
        return this.id;
    }


    public void setId(int id){
        this.id = id;
    }

    <#list attributes as attribute>
    <#-- Getter -->
    public ${attribute.type} get${attribute.name?cap_first}() {
        return ${attribute.name};
    }

    <#-- Setter -->
    public void set${attribute.name?cap_first}(${attribute.type} ${attribute.name}) {
        this.${attribute.name} = ${attribute.name};
    }

    </#list>

    public void save() {

    SQLiteConn c = new SQLiteConn("src/${pkg}/${name}.db");
    String query = "";

        if (this.id == -1) {


            query = String.format("Insert into %s(<#list attributes as attribute>${attribute.name}<#sep>,</#sep> </#list>) values (<@compress single_line=true>
                <#list attributes as attribute>
                    <#if attribute.type == "String" >
                        <#lt>'%s'
                    </#if>
                    <#if attribute.type?starts_with("int")>
                        <#lt>%d
                    </#if>
                    <#sep>,</#sep>
                </#list></@compress>)", "${name}", <#list attributes as attribute>this.${attribute.name}<#sep>,</#sep> </#list>);
            this.id = c.executeUpdate(query);

        } else {

            query = String.format("Update %s set <@compress single_line=true><#list attributes as attribute>
                <#if attribute.type == "String">
                    <#t>${attribute.name} = '%s'
                </#if>
                <#if attribute.type?starts_with("int")>
                    <#t>${attribute.name} = %d
                </#if><#sep>,</#sep></#list></@compress> where id = %d", "${name}", <#list attributes as attribute>this.${attribute.name}, </#list>this.id);

            c.executeUpdate(query);
        }


    }


    public static List<${name}> all() {
        SQLiteConn con = new SQLiteConn("src/${pkg}/${name}.db");
        String query = String.format("SELECT * FROM ${name};");
        ResultSet result = con.executeQuery(query);

        List<${name}> lista = new ArrayList<>();
        try {
            while (result.next()) {

                int id = result.getInt("id");
                <#list attributes as attribute>
                    <#if attribute.type == "String">
                    String ${attribute.name} = result.getString("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("Date")>
                    Date ${attribute.name} = result.getDate("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("double")>
                    double ${attribute.name} = result.getDouble("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("int")>
                    int ${attribute.name} = result.getInt("${attribute.name}");
                    </#if></#list>


                ${name} p = new ${name}();

                p.setId(id);
                <#list attributes as attribute>
                p.set${attribute.name?cap_first}(${attribute.name});
                </#list>

                lista.add(p);
            }

        } catch (SQLException e) {
        e.printStackTrace();
        }
        return lista;
    }

    public static List<${name}> where(String clause) {
        SQLiteConn con = new SQLiteConn("src/${pkg}/${name}.db");
        String query = String.format("SELECT * FROM ${name} where "+clause+";");
        ResultSet result = con.executeQuery(query);

        List<${name}> lista = new ArrayList<>();
        try {
            while (result.next()) {

                int id = result.getInt("id");
                <#list attributes as attribute>
                    <#if attribute.type == "String">
                    String ${attribute.name} = result.getString("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("Date")>
                    Date ${attribute.name} = result.getDate("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("double")>
                    double ${attribute.name} = result.getDouble("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("int")>
                    int ${attribute.name} = result.getInt("${attribute.name}");
                    </#if></#list>


                ${name} p = new ${name}();

                p.setId(id);
                <#list attributes as attribute>
                p.set${attribute.name?cap_first}(${attribute.name});
                </#list>

                lista.add(p);
            }

        } catch (SQLException e) {
        e.printStackTrace();
        }
        return lista;
    }

    public static ${name} get(int i) {
        SQLiteConn con = new SQLiteConn("src/${pkg}/${name}.db");
        String query = String.format("SELECT * FROM ${name} where id ="+i+";");
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                int id = result.getInt("id");
                <#list attributes as attribute>
                    <#if attribute.type == "String">
                    String ${attribute.name} = result.getString("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("Date")>
                    Date ${attribute.name} = result.getDate("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("double")>
                    double ${attribute.name} = result.getDouble("${attribute.name}");
                    </#if>
                    <#if attribute.type?starts_with("int")>
                    int ${attribute.name} = result.getInt("${attribute.name}");
                    </#if></#list>


                ${name} p = new ${name}();

                p.setId(id);
                <#list attributes as attribute>
                p.set${attribute.name?cap_first}(${attribute.name});
                </#list>

                return p;
            }

        } catch (SQLException e) {
        e.printStackTrace();
        }
        return null;
    }


    public static void delete(int id)  {
        SQLiteConn sqLiteConn = new SQLiteConn("src/person/Person.db");

        sqLiteConn.executeUpdate("DELETE FROM Person WHERE id = " + id);
        System.out.println("Success");

        sqLiteConn.close();

    }

    public void delete(){
        ${name}.delete(this.id);
    }


}