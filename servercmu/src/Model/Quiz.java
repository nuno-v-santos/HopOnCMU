
package Model;
import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String name;

    // public constructor
    public Quiz() {
        
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

            
            query = "Insert into Quiz(name ) values (?)";

            List<String> args = new ArrayList<>();
            args.add(this.name+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update Quiz set name = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.name+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<Quiz> all() {
        String query = String.format("SELECT * FROM Quiz;");
        ResultSet result = con.executeQuery(query);

        List<Quiz> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Quiz p = getQuizClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Quiz> where(String clause) {

        String query = String.format("SELECT * FROM Quiz where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Quiz> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Quiz p = getQuizClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Quiz get(int i) {
        String query = String.format("SELECT * FROM Quiz where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                Quiz p = getQuizClass(result);
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
        con.executeUpdate("DELETE FROM Quiz WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static Quiz getQuizClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String name = result.getString("name");

        //Manel p = new Manel(Person.get(id));
         Quiz p = new Quiz();
        p.setId(id);
        p.setName(name);
       return p;

   }




    public Monument getMonument() {
    // select * from Quiz as a inner join Monument as b on a.id = b.quiz_id where b.id = this.id;

        String query = String.format("select * from Monument  where quiz_id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    Monument p = (Monument) Monument.getMonumentClass(result);
                    return p;

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void setMonument(Monument monument) {

        String query = String.format("Update %s set quiz_id = '%d'  where id = %d", "Monument",this.id,  monument != null ? monument.getId() : -1);
        if(monument == null){
            query = String.format("Update %s set quiz_id = null  where quiz_id = %d", "Monument",this.id);
        }
        con.executeUpdate(query);

    }






   /* public List<Question> getQuestions() {
        // select * from Question where quiz_id = this.id;
    }*/

    public List<Question> getQuestions() {
        // select * from Person where manel_id = this.id;
        String query = String.format("select * from Question where quiz_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Question> lista = new ArrayList<>();

        try {

            while (result.next()) {

                Question p = (Question) Question.getQuestionClass(result);
                lista.add(p);

            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



   /* public List<QuizResponse> getQuizResponses() {
        // select * from QuizResponse where quiz_id = this.id;
    }*/

    public List<QuizResponse> getQuizResponses() {
        // select * from Person where manel_id = this.id;
        String query = String.format("select * from QuizResponse where quiz_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<QuizResponse> lista = new ArrayList<>();

        try {

            while (result.next()) {

                QuizResponse p = (QuizResponse) QuizResponse.getQuizResponseClass(result);
                lista.add(p);

            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




    @Override
    public String toString() {
        return "Quiz{" +
                " name=" + name +
                '}';
    }

}
