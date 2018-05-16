
package Model;

import EndPoints.AndroidEndPoints;
import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tour {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String name;

    // public constructor
    public Tour() {

        this.id = -1;
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


    public void save() {


        String query = "";

        if (this.id == -1) {


            query = "Insert into Tour(name ) values (?)";

            List<String> args = new ArrayList<>();
            args.add(this.name + "");
            this.id = con.executeUpdate(query, args);
        } else {

            query = "Update Tour set name = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.name + "");
            args.add(this.id + "");
            con.executeUpdate(query, args);

        }


        AndroidEndPoints.VERSION++;

    }


    public static List<Tour> all() {
        String query = String.format("SELECT * FROM Tour;");
        ResultSet result = con.executeQuery(query);

        List<Tour> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Tour p = getTourClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Tour> where(String clause) {

        String query = String.format("SELECT * FROM Tour where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Tour> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Tour p = getTourClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Tour get(int i) {
        String query = String.format("SELECT * FROM Tour where id = ? ;");
        List<String> args = new ArrayList<>();
        args.add(i + "");
        ResultSet result = con.executeQuery(query, args);

        try {
            while (result.next()) {

                Tour p = getTourClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void delete() {

        List<String> args = new ArrayList<>();
        args.add(this.id + "");
        con.executeUpdate("DELETE FROM Tour WHERE id = ? ", args);
        System.out.println("Success");


    }


    public static Tour getTourClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String name = result.getString("name");

        //Manel p = new Manel(Person.get(id));
        Tour p = new Tour();
        p.setId(id);
        p.setName(name);
        return p;

    }


   /* public List<Ticket> getTickets() {
        // select * from Ticket where tour_id = this.id;
    }*/

    public List<Ticket> getTickets() {
        // select * from Person where manel_id = this.id;
        String query = String.format("select * from Ticket where tour_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Ticket> lista = new ArrayList<>();

        try {

            while (result.next()) {

                Ticket p = (Ticket) Ticket.getTicketClass(result);
                lista.add(p);

            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    //ok  Relation_Person_Manel
    //"select b.* from Relation_Tour_Monument as a inner join Monument as b on a.monument_id = b.id where a.tour_id = " + this.id;

    public List<Monument> getMonuments() {

        String query = String.format("select b.* from Relation_Tour_Monument as a inner join Monument as b on a.monument_id = b.id where a.tour_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Monument> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Monument p = (Monument) Monument.getMonumentClass(result);
                lista.add(p);
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addMonument(Monument monument) {
        String query = String.format("Insert into Relation_Tour_Monument(tour_id, monument_id) values (%d, %d)", this.id, monument.getId());

        con.executeUpdate(query);
    }

    public void removeMonument(Monument monument) {
        String query = String.format("Delete from Relation_Tour_Monument where tour_id = %d and  monument_id = %d", this.id, monument.getId());

        con.executeUpdate(query);
    }


    @Override
    public String toString() {
        return "Tour{" +
                " name=" + name +
                '}';
    }

}
