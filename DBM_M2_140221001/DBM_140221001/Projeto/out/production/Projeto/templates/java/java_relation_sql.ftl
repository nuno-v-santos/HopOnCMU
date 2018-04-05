

<#list relations as relation>

<#switch relation.type>

    <#case "OneToMany">
   /* public List<${relation.target.name}> get${relation.target.name}s() {
        // select * from ${relation.target.name} where ${relation.base.name?lower_case}_id = this.id;
    }*/

    public List<${relation.target.name}> get${relation.target.name}s() {
        // select * from Person where manel_id = this.id;
        String query = String.format("select * from ${relation.target.name} where ${relation.base.name?lower_case}_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<${relation.target.name}> lista = new ArrayList<>();

        try {

            while (result.next()) {

                ${relation.target.name} p = (${relation.target.name}) ${relation.target.name}.get${relation.target.name}Class(result);
                lista.add(p);

            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

        <#break>

    <#case "ManyToOne">
    public ${relation.target.name} get${relation.target.name}() {
        // select * from ${relation.target.name} as a inner join ${relation.base.name} as b on a.id = b.${relation.target.name?lower_case}_id where b.id = this.id;

        String query = String.format("select a.* from ${relation.target.name} as a inner join ${relation.base.name} as b on a.id = b.${relation.target.name?lower_case}_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                ${relation.target.name} p = (${relation.target.name}) ${relation.target.name}.get${relation.target.name}Class(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void set${relation.target.name}(${relation.target.name} ${relation.target.name?lower_case}) {

        String query = String.format("Update %s set ${relation.target.name?lower_case}_id = '%d'  where id = %d", "${relation.base.name}", ${relation.target.name?lower_case} != null ? ${relation.target.name?lower_case}.getId() : -1, this.id);
        con.executeUpdate(query);

    }
        <#break>

    <#case  "ManyToMany" >
        <#if relation.base.name?contains(name) >
            //ok  Relation_Person_Manel
        //"select b.* from Relation_${relation.base.name}_${relation.target.name} as a inner join ${relation.target.name} as b on a.${relation.target.name?lower_case}_id = b.id where a.${relation.base.name?lower_case}_id = " + this.id;

    public List<${relation.target.name}> get${relation.target.name}s() {

        String query = String.format("select b.* from Relation_${relation.base.name}_${relation.target.name} as a inner join ${relation.target.name} as b on a.${relation.target.name?lower_case}_id = b.id where a.${relation.base.name?lower_case}_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<${relation.target.name}> lista = new ArrayList<>();
            try {
                while (result.next()) {

                    ${relation.target.name} p = (${relation.target.name}) ${relation.target.name}.get${relation.target.name}Class(result);
                    lista.add(p);
                }
                return lista;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void add${relation.target.name}(${relation.target.name} ${relation.target.name?lower_case}){
        String query = String.format("Insert into Relation_${relation.base.name}_${relation.target.name}(${relation.base.name?lower_case}_id, ${relation.target.name?lower_case}_id) values (%d, %d)",this.id,${relation.target.name?lower_case}.getId());

        con.executeUpdate(query);
    }

    public void remove${relation.target.name}(${relation.target.name} ${relation.target.name?lower_case}){
        String query = String.format("Delete from Relation_${relation.base.name}_${relation.target.name} where ${relation.base.name?lower_case}_id = %d and  ${relation.target.name?lower_case}_id = %d",this.id,${relation.target.name?lower_case}.getId());

        con.executeUpdate(query);
    }


        <#else >
            //not me
        //"select b.* from Relation_${relation.base.name}_${relation.target.name} as a inner join ${relation.base.name} as b on a.${relation.base.name?lower_case}_id = b.id where a.${relation.target.name?lower_case}_id = " + this.id;

    public List<${relation.base.name}> get${relation.base.name}s() {

        String query = String.format("select b.* from Relation_${relation.base.name}_${relation.target.name} as a inner join ${relation.base.name} as b on a.${relation.base.name?lower_case}_id = b.id where a.${relation.target.name?lower_case}_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<${relation.base.name}> lista = new ArrayList<>();
            try {
                while (result.next()) {

                    ${relation.base.name} p = (${relation.base.name}) ${relation.base.name}.get${relation.base.name}Class(result);
                    lista.add(p);

                }
                return lista;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void add${relation.base.name}(${relation.base.name} ${relation.base.name?lower_case}){

        String query = String.format("Insert into Relation_${relation.base.name}_${relation.target.name}(${relation.base.name?lower_case}_id, ${relation.target.name?lower_case}_id) values (%d, %d)",${relation.base.name?lower_case}.getId(),this.id);
        con.executeUpdate(query);

    }

    public void remove${relation.base.name}(${relation.base.name} ${relation.base.name?lower_case}){

        String query = String.format("Delete from Relation_${relation.base.name}_${relation.target.name} where ${relation.base.name?lower_case}_id = %d and ${relation.target.name?lower_case}_id = %d",${relation.base.name?lower_case}.getId(),this.id);
        con.executeUpdate(query);

    }

        </#if>
        <#break>

        <#case "OneToOne">

        <#if relation.base.name?contains(name)>

    public ${relation.target.name} get${relation.target.name}() {
    // select * from ${relation.target.name} as a inner join ${relation.base.name} as b on a.id = b.${relation.target.name?lower_case}_id where b.id = this.id;

        String query = String.format("select a.* from ${relation.target.name} as a inner join ${relation.base.name} as b on a.id = b.${relation.target.name?lower_case}_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    ${relation.target.name} p = (${relation.target.name}) ${relation.target.name}.get${relation.target.name}Class(result);
                    return p;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void set${relation.target.name}(${relation.target.name} ${relation.target.name?lower_case}) {

        String query = String.format("Update %s set ${relation.target.name?lower_case}_id = %s  where id = %d", "${relation.base.name}", ${relation.target.name?lower_case} != null ? "'"+${relation.target.name?lower_case}.getId()+"'" :"null", this.id);
        con.executeUpdate(query);

    }

        <#else>

    public ${relation.base.name} get${relation.base.name}() {
    // select * from ${relation.target.name} as a inner join ${relation.base.name} as b on a.id = b.${relation.target.name?lower_case}_id where b.id = this.id;

        String query = String.format("select * from ${relation.base.name}  where ${relation.target.name?lower_case}_id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    ${relation.base.name} p = (${relation.base.name}) ${relation.base.name}.get${relation.base.name}Class(result);
                    return p;

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void set${relation.base.name}(${relation.base.name} ${relation.base.name?lower_case}) {

        String query = String.format("Update %s set ${relation.target.name?lower_case}_id = '%d'  where id = %d", "${relation.base.name}",this.id,  ${relation.base.name?lower_case} != null ? ${relation.base.name?lower_case}.getId() : -1);
        if(${relation.base.name?lower_case} == null){
            query = String.format("Update %s set ${relation.target.name?lower_case}_id = null  where ${relation.target.name?lower_case}_id = %d", "${relation.base.name}",this.id);
        }
        con.executeUpdate(query);

    }


        </#if>


        <#break>

    <#default>

</#switch>

</#list>
