
package Model;
import EndPoints.AndroidEndPoints;
import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Monument {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String name;
    private String wifiId;
    private String imageURL;

    // public constructor
    public Monument() {
        
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

    public String getWifiId() {
        return wifiId;
    }

    public void setWifiId(String wifiId) {

        this.wifiId = wifiId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {

        this.imageURL = imageURL;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into Monument(name, wifiId, imageURL ) values (? , ? , ?)";

            List<String> args = new ArrayList<>();
            args.add(this.name+"");
            args.add(this.wifiId+"");
            args.add(this.imageURL+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update Monument set name = ? , wifiId = ? , imageURL = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.name+"");
            args.add(this.wifiId+"");
            args.add(this.imageURL+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }


        AndroidEndPoints.VERSION++;

    }


    public static List<Monument> all() {
        String query = String.format("SELECT * FROM Monument;");
        ResultSet result = con.executeQuery(query);

        List<Monument> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Monument p = getMonumentClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Monument> where(String clause) {

        String query = String.format("SELECT * FROM Monument where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Monument> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Monument p = getMonumentClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Monument get(int i) {
        String query = String.format("SELECT * FROM Monument where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                Monument p = getMonumentClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    
    public void delete() {

        List<String> args = new ArrayList<>();
        args.add(this.id+"");
        con.executeUpdate("DELETE FROM Monument WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static Monument getMonumentClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String name = result.getString("name");
        String wifiId = result.getString("wifiId");
        String imageURL = result.getString("imageURL");

        //Manel p = new Manel(Person.get(id));
         Monument p = new Monument();
        p.setId(id);
        p.setName(name);
        p.setWifiId(wifiId);
        p.setImageURL(imageURL);
       return p;

   }


            //not me
        //"select b.* from Relation_Tour_Monument as a inner join Tour as b on a.tour_id = b.id where a.monument_id = " + this.id;

    public List<Tour> getTours() {

        String query = String.format("select b.* from Relation_Tour_Monument as a inner join Tour as b on a.tour_id = b.id where a.monument_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Tour> lista = new ArrayList<>();
            try {
                while (result.next()) {

                    Tour p = (Tour) Tour.getTourClass(result);
                    lista.add(p);

                }
                return lista;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void addTour(Tour tour){

        String query = String.format("Insert into Relation_Tour_Monument(tour_id, monument_id) values (%d, %d)",tour.getId(),this.id);
        con.executeUpdate(query);

    }

    public void removeTour(Tour tour){

        String query = String.format("Delete from Relation_Tour_Monument where tour_id = %d and monument_id = %d",tour.getId(),this.id);
        con.executeUpdate(query);

    }





    public Quiz getQuiz() {
    // select * from Quiz as a inner join Monument as b on a.id = b.quiz_id where b.id = this.id;

        String query = String.format("select a.* from Quiz as a inner join Monument as b on a.id = b.quiz_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    Quiz p = (Quiz) Quiz.getQuizClass(result);
                    return p;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void setQuiz(Quiz quiz) {

        String query = String.format("Update %s set quiz_id = %s  where id = %d", "Monument", quiz != null ? "'"+quiz.getId()+"'" :"null", this.id);
        con.executeUpdate(query);

    }



    @Override
    public String toString() {
        return "Monument{" +
                " name=" + name +
                " wifiId=" + wifiId +
                " imageURL=" + imageURL +
                '}';
    }

}
