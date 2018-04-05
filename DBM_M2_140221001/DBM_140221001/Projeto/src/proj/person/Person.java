package proj.person;

import utils.sqlite.BdConnection;
import utils.sqlite.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Person extends Super {
    
    protected static BdConnection con = MysqlConnection.getInstace();
    protected String name;
    protected int age;

    // Empty constructor
    public Person() {
        super();
        this.id = -1;
    }

    public Person(Super e) {
        //Super
        super();
        this.id = e.id;
        this.ok = e.ok;
        this.da = e.da;
    }

    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {

        this.age = age;
    }



   
    @Override
    public void save() {


        String query = "";

        if (this.id == -1) {

            super.save();
            query = String.format("Insert into %s(id, name, age ) values (%d, '%s' , %d)", "Person",this.id, this.name, this.age );
            con.executeUpdate(query);

        } else {

            query = String.format("Update %s set name = '%s' , age = %d where id = %d", "Person", this.name, this.age, this.id);
            con.executeUpdate(query);

        }



    }


    public static List<Super> all() {
        String query = String.format("SELECT * FROM Person;");
        ResultSet result = con.executeQuery(query);

        List<Super> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Person p = getPersonClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Super> where(String clause) {

        String query = String.format("SELECT * FROM Person where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Super> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Person p = getPersonClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Super get(int i) {
        String query = String.format("SELECT * FROM Person where id =" + i + ";");
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                Person p = getPersonClass(result);
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


   public static Person getPersonClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String name = result.getString("name");
        int age = result.getInt("age");

        //Manel p = new Manel(Person.get(id));
        Person p = new Person((Super)Super.get(id));
        p.setId(id);
        p.setName(name);
        p.setAge(age);
       return p;

   }


            //ok  Relation_Person_Manel
        //"select b.* from Relation_Person_Manel as a inner join Manel as b on a.manel_id = b.id where a.person_id = " + this.id;

    public List<Manel> getManels() {

        String query = String.format("select b.* from Relation_Person_Manel as a inner join Manel as b on a.manel_id = b.id where a.person_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Manel> lista = new ArrayList<>();
            try {
                while (result.next()) {

                    Manel p = (Manel) Manel.getManelClass(result);
                    lista.add(p);
                }
                return lista;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void addManel(Manel manel){
        String query = String.format("Insert into Relation_Person_Manel(person_id, manel_id) values (%d, %d)",this.id,manel.getId());

        con.executeUpdate(query);
    }





    @Override
    public String toString() {
        return super.toString() + "Person{" +
                " name=" + name +
                " age=" + age +
                '}';
    }

}
