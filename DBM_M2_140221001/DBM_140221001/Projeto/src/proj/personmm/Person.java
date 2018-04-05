package proj.personmm;

import utils.sqlite.BdConnection;
import utils.sqlite.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private int id;
    private static BdConnection con = MysqlConnection.getInstace();
    private String Name;
    private int age;

    // public constructor
    public Person() {
        
        this.id = -1;
    }



    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {

        this.Name = Name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {

        this.age = age;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = String.format("Insert into %s(Name, age ) values ('%s' , %d)", "Person",this.Name, this.age );
            this.id = con.executeUpdate(query);

        } else {

            query = String.format("Update %s set Name = '%s' , age = %d where id = %d", "Person", this.Name, this.age, this.id);
            con.executeUpdate(query);

        }



    }


    public static List<Person> all() {
        String query = String.format("SELECT * FROM Person;");
        ResultSet result = con.executeQuery(query);

        List<Person> lista = new ArrayList<>();
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

    public static List<Person> where(String clause) {

        String query = String.format("SELECT * FROM Person where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Person> lista = new ArrayList<>();

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


    public static Person get(int i) {
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


    
    public void delete() {

        con.executeUpdate("DELETE FROM Person WHERE id = " + this.id);
        System.out.println("Success");


    }


   public static Person getPersonClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String Name = result.getString("Name");
        int age = result.getInt("age");

        //Manel p = new Manel(Person.get(id));
         Person p = new Person();
        p.setId(id);
        p.setName(Name);
        p.setAge(age);
       return p;

   }



    @Override
    public String toString() {
        return "Person{" +
                " Name=" + Name +
                " age=" + age +
                '}';
    }

}
