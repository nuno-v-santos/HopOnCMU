package proj.person;

import utils.sqlite.BdConnection;
import utils.sqlite.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Manel extends Person {
    
    private static BdConnection con = MysqlConnection.getInstace();
    private String coi;
    private int man;

    // Empty constructor
    public Manel() {
        super();
        this.id = -1;
    }

    public Manel(Person e) {
        //Super
        super();
        this.id = e.id;
        this.name = e.name;
        this.age = e.age;
    }

    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getCoi() {
        return coi;
    }

    public void setCoi(String coi) {

        if(!coi.contains("a")){
            throw  new RuntimeException("coi deve conter a string a");
        }

        if(coi.isEmpty()){
            throw  new RuntimeException("coi não pode ser vazio");
        }

        if(coi.length() > 9){
            throw  new RuntimeException("coi tem de ter menos de 10 carateres");
        }

        if(coi.length() < 4){
            throw  new RuntimeException("coi tem de ter mais de 3 carateres");
        }

        this.coi = coi;
    }

    public int getMan() {
        return man;
    }

    public void setMan(int man) {

        this.man = man;
    }



   
    @Override
    public void save() {


        String query = "";

        if (this.id == -1) {

            super.save();
            query = String.format("Insert into %s(id, coi, man ) values (%d, '%s' , %d)", "Manel",this.id, this.coi, this.man );
            con.executeUpdate(query);

        } else {

            query = String.format("Update %s set coi = '%s' , man = %d where id = %d", "Manel", this.coi, this.man, this.id);
            con.executeUpdate(query);

        }



    }


    public static List<Super> all() {
        String query = String.format("SELECT * FROM Manel;");
        ResultSet result = con.executeQuery(query);

        List<Super> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Manel p = getManelClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Super> where(String clause) {

        String query = String.format("SELECT * FROM Manel where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Super> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Manel p = getManelClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Person get(int i) {
        String query = String.format("SELECT * FROM Manel where id =" + i + ";");
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                Manel p = getManelClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void delete() {

        con.executeUpdate("DELETE FROM Person WHERE id = " + this.id);
        System.out.println("Success");


    }


   public static Manel getManelClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String coi = result.getString("coi");
        int man = result.getInt("man");

        //Manel p = new Manel(Person.get(id));
        Manel p = new Manel((Person)Person.get(id));
        p.setId(id);
        p.setCoi(coi);
        p.setMan(man);
       return p;

   }


            //not me
        //"select b.* from Relation_Person_Manel as a inner join Person as b on a.person_id = b.id where a.manel_id = " + this.id;

    public List<Person> getPersons() {

        String query = String.format("select b.* from Relation_Person_Manel as a inner join Person as b on a.person_id = b.id where a.manel_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Person> lista = new ArrayList<>();
            try {
                while (result.next()) {

                    Person p = (Person) Person.getPersonClass(result);
                    lista.add(p);

                }
                return lista;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void addPerson(Person person){

        String query = String.format("Insert into Relation_Person_Manel(person_id, manel_id) values (%d, %d)",person.getId(),this.id);
        con.executeUpdate(query);

    }



    //@Protected Start
                                                                                                                                                                                
    public void test2() {

    }


    public void test() {

    }
























    //@Protected End

    @Override
    public String toString() {
        return super.toString() + "Manel{" +
                " coi=" + coi +
                " man=" + man +
                '}';
    }

}
