   <#include "ResultSetToClass">
    <#if extend??>@Override</#if>
    public void save() {


        String query = "";

        if (this.id == -1) {

            <#if extend??>super.save();</#if>
            query = String.format("Insert into %s(<#if extend??>id, </#if><#list attributes as attribute>${attribute.name}<#sep>,</#sep> </#list>) values (<#if extend??>%d, </#if><@compress single_line=true>
    <#list attributes as attribute>
    <#if attribute.type == "String" >
        <#lt>'%s'
    </#if>
    <#if attribute.type?starts_with("int")>
        <#lt>%d
    </#if>
    <#if attribute.type?starts_with("Date")>
        <#lt>'%s'
    </#if>
    <#sep>,</#sep>
</#list></@compress>)", "${name}",<#if extend??>this.id, </#if><#list attributes as attribute><#if attribute.type?starts_with("Date")><#if bdname?contains("Sqlite3")>new java.sql.Timestamp(this.${attribute.name}.getTime())<#else>new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this.${attribute.name})</#if><#else>this.${attribute.name}</#if><#sep>,</#sep> </#list>);
            <#if !extend??>this.id = </#if>con.executeUpdate(query);

        } else {
            <#if extend??>super.save();</#if>
            query = String.format("Update %s set <@compress single_line=true><#list attributes as attribute>
        <#if attribute.type == "String">
            <#t>${attribute.name} = '%s'
        </#if>
        <#if attribute.type?starts_with("int")>
            <#t>${attribute.name} = %d
        </#if>
       <#if attribute.type?starts_with("Date")>
           <#lt>${attribute.name} = '%s'
       </#if><#sep>,</#sep></#list></@compress> where id = %d", "${name}", <#list attributes as attribute><#if attribute.type?starts_with("Date")><#if bdname?contains("Sqlite3")>new java.sql.Timestamp(this.${attribute.name}.getTime())<#else>new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this.${attribute.name})</#if><#else>this.${attribute.name}</#if>, </#list>this.id);
            con.executeUpdate(query);

        }



    }


    public static List<<#if extend??><@superName class=extend/><#else>${name}</#if>> all() {
        String query = String.format("SELECT * FROM ${name};");
        ResultSet result = con.executeQuery(query);

        List<<#if extend??><@superName class=extend/><#else>${name}</#if>> lista = new ArrayList<>();
        try {
            while (result.next()) {

                ${name} p = get${name}Class(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<<#if extend??><@superName class=extend/><#else>${name}</#if>> where(String clause) {

        String query = String.format("SELECT * FROM ${name} where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<<#if extend??><@superName class=extend/><#else>${name}</#if>> lista = new ArrayList<>();

        try {
            while (result.next()) {

                ${name} p = get${name}Class(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static <#if extend??>${extend.name}<#else>${name}</#if> get(int i) {
        String query = String.format("SELECT * FROM ${name} where id =" + i + ";");
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                ${name} p = get${name}Class(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    <#if extend??>@Override</#if>
    public void delete() {

        con.executeUpdate("DELETE FROM Person WHERE id = " + this.id);
        System.out.println("Success");


    }


   public static ${name} get${name}Class(ResultSet result) throws SQLException {

   <@resultSetToClass att=attributes name=name />
       return p;

   }