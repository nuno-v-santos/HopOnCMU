   <#include "ResultSetToClass">
    <#if extend??>@Override</#if>
    public void save() {


        String query = "";

        if (this.id == -1) {

            <#if extend??>super.save();</#if>
            query = "Insert into ${name}(<#if extend??>id, </#if><#list attributes as attribute>${attribute.name}<#sep>,</#sep> </#list>) values (<#if extend??>?, </#if><@compress single_line=true>
    <#list attributes as attribute>
    <#if attribute.type == "String" >
        <#lt>?
    </#if>
    <#if attribute.type?starts_with("int")>
        <#lt>?
    </#if>
    <#if attribute.type?starts_with("Date")>
        <#lt>?
    </#if>
    <#sep>,</#sep>
</#list></@compress>)";

            List<String> args = new ArrayList<>();
  <#if extend??>
            args.add(this.id+"");
  </#if>
   <#list attributes as attribute>
       <#if attribute.type?starts_with("Date")>
           <#if bdname?contains("Sqlite3")>
            args.add(new java.sql.Timestamp(this.${attribute.name}.getTime())+"");
           <#else>
            args.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this.${attribute.name})+"");
           </#if>
       <#else>
            args.add(this.${attribute.name}+"");
       </#if>
   </#list>
            <#if !extend??>this.id = </#if> con.executeUpdate(query,args);
        } else {
            <#if extend??>super.save();</#if>
            query = "Update ${name} set <@compress single_line=true><#list attributes as attribute>
        <#if attribute.type == "String">
            <#t>${attribute.name} = ?
        </#if>
        <#if attribute.type?starts_with("int")>
            <#t>${attribute.name} = ?
        </#if>
       <#if attribute.type?starts_with("Date")>
           <#lt>${attribute.name} = ?
       </#if><#sep>,</#sep></#list></@compress> where id = ?";

            List<String> args = new ArrayList<>();
   <#list attributes as attribute>
       <#if attribute.type?starts_with("Date")>
           <#if bdname?contains("Sqlite3")>
            args.add(new java.sql.Timestamp(this.${attribute.name}.getTime())+"");
           <#else>
            args.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this.${attribute.name})+"");
           </#if>
       <#else>
            args.add(this.${attribute.name}+"");
       </#if>
   </#list>
            args.add(this.id+"");
            con.executeUpdate(query,args);

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
        String query = String.format("SELECT * FROM ${name} where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

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

        List<String> args = new ArrayList<>();
        args.add(this.id+"");
        con.executeUpdate("DELETE FROM ${name} WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static ${name} get${name}Class(ResultSet result) throws SQLException {

   <@resultSetToClass att=attributes name=name />
       return p;

   }