package Model;

import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String question;

    // public constructor
    public Question() {
        
        this.id = -1;
    }



    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {

        this.question = question;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into Question(question ) values (?)";

            List<String> args = new ArrayList<>();
            args.add(this.question+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update Question set question = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.question+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<Question> all() {
        String query = String.format("SELECT * FROM Question;");
        ResultSet result = con.executeQuery(query);

        List<Question> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Question p = getQuestionClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Question> where(String clause) {

        String query = String.format("SELECT * FROM Question where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Question> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Question p = getQuestionClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Question get(int i) {
        String query = String.format("SELECT * FROM Question where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                Question p = getQuestionClass(result);
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
        con.executeUpdate("DELETE FROM Question WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static Question getQuestionClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String question = result.getString("question");

        //Manel p = new Manel(Person.get(id));
         Question p = new Question();
        p.setId(id);
        p.setQuestion(question);
       return p;

   }


    public Quiz getQuiz() {
        // select * from Quiz as a inner join Question as b on a.id = b.quiz_id where b.id = this.id;

        String query = String.format("select a.* from Quiz as a inner join Question as b on a.id = b.quiz_id where b.id ="+ this.id);
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

        String query = String.format("Update %s set quiz_id = '%d'  where id = %d", "Question", quiz != null ? quiz.getId() : -1, this.id);
        con.executeUpdate(query);

    }


   /* public List<Answer> getAnswers() {
        // select * from Answer where question_id = this.id;
    }*/

    public List<Answer> getAnswers() {
        // select * from Person where manel_id = this.id;
        String query = String.format("select * from Answer where question_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<Answer> lista = new ArrayList<>();

        try {

            while (result.next()) {

                Answer p = (Answer) Answer.getAnswerClass(result);
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
        return "Question{" +
                " question=" + question +
                '}';
    }

}
