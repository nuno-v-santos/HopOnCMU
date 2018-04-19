package EndPoints;

import Model.Monument;
import Model.Question;
import Model.Quiz;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import static spark.Spark.*;

public class AndroidEndPoints {

    private static User validateUser(String token) {

        System.out.println("TOKEN");
        System.out.println(token);

        if (token == null) {
            return null;
        }

        //split do token
        String[] s = token.split("\\|");
        User user = User.get(Integer.parseInt(s[1]));

        if (user == null)
            return null;

        if (!user.getSession().getToken().equals(token))
            return null;

        return user;
    }

    public static void mount() {

        get("/android/monuments/", ((request, response) -> {

            System.out.println("ok");
            try {
                //obter o token
                String token = request.headers("token");
                User user = validateUser(token);

                if (user == null)
                    return false;

                Gson gson = new Gson();

                String result = gson.toJson(user.getTicket().getTour().getMonuments());
                System.out.println("Monuments =======> ok");
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }));


        get("/android/userInfo/", ((request, response) -> {

            try {

                //obter o token
                String token = request.headers("token");
                User user = validateUser(token);


                System.out.println(token);

                if (user == null)
                    return false;

                Gson gson = new Gson();

                String ticket = gson.toJson(user.getTicket());
                String tour = gson.toJson(user.getTicket().getTour());
                JsonObject responsePrams = new JsonObject();

                return "" + '{' +
                        '"' + "tiket" + '"' + ':' + ticket + ',' +
                        '"' + "tour" + '"' + ':' + tour +
                        '}';

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));


        get("/android/monumentQuestions/", ((request, response) -> {

            try {

                //obter o token
                String token = request.headers("token");
                User user = validateUser(token);
                if (user == null)
                    return false;

                int monID = Integer.parseInt(request.headers("monumentID"));
                Gson gson = new Gson();
                Monument monument = Monument.get(monID);
                Quiz quiz = monument.getQuiz();


                StringBuilder quizQuestions = new StringBuilder();
                quizQuestions.append("[");

                List<Question> questions = quiz.getQuestions();

                for (Question question : questions) {
                    String answersString = gson.toJson(question.getAnswers());

                    quizQuestions.append(
                            "{" +
                                    "\"id\" : " + "\"" + question.getId() + "\"" + "," +
                                    "\"question\" : " + "\"" + question.getQuestion() + "\"" + "," +
                                    "\"answers\" : " + answersString +
                                    "}"
                    );
                }

                quizQuestions.append("]");

                return quizQuestions.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));
    }

}
