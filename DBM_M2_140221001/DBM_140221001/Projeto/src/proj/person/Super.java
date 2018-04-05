package proj.person;

import utils.sqlite.BdConnection;
import utils.sqlite.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Super {
    protected int id;
    protected static BdConnection con = MysqlConnection.getInstace();
    protected int ok;
    protected Date da;

    // Empty constructor
    public Super() {
        
        this.id = -1;
    }


    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {

        if(ok > 5){
            throw  new RuntimeException("ok tem de ser inferior ou igual a 5");
        }

        if(ok < 0){
            throw  new RuntimeException("ok tem de ser maior ou igual a 0");
        }

        this.ok = ok;
    }

    public Date getDa() {
        return da;
    }

    public void setDa(Date da) {

        this.da = da;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = String.format("Insert into %s(ok, da ) values (%d , '%s')", "Super",this.ok, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this.da) );
            this.id = con.executeUpdate(query);

        } else {

            query = String.format("Update %s set ok = %d , where id = %d", "Super", this.ok, this.da, this.id);
            con.executeUpdate(query);

        }



    }


    public static List<Super> all() {
        String query = String.format("SELECT * FROM Super;");
        ResultSet result = con.executeQuery(query);

        List<Super> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Super p = getSuperClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Super> where(String clause) {

        String query = String.format("SELECT * FROM Super where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Super> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Super p = getSuperClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Super get(int i) {
        String query = String.format("SELECT * FROM Super where id =" + i + ";");
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                Super p = getSuperClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    
    public void delete() {

        con.executeUpdate("DELETE FROM Person WHERE id = " + this.id);
        System.out.println("Success");


    }


   public static Super getSuperClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        int ok = result.getInt("ok");
        Date da = result.getDate("da");

        //Manel p = new Manel(Person.get(id));
         Super p = new Super();
        p.setId(id);
        p.setOk(ok);
        p.setDa(da);
       return p;

   }



    @Override
    public String toString() {
        return "Super{" +
                " ok=" + ok +
                " da=" + da +
                '}';
    }

}
