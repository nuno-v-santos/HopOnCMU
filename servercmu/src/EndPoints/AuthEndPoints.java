package EndPoints;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.UUID;

import static spark.Spark.*;

public class AuthEndPoints {

    public static void mount() {

        post("/register", ((request, response) -> {

            //todo : send json responses
            try {
                JsonElement body = new JsonParser().parse(request.body());
                String ticket = body.getAsJsonObject().get("ticket").getAsString();
                String username = body.getAsJsonObject().get("username").getAsString();

                if (username.length() < 3) {
                    return "short name";
                }

                Ticket ticketObject = Ticket.where("number = " + ticket).get(0);

                //username already taken
                if (User.where("username= '" + username + "'").size() > 0) {
                    return "username taken";
                }

                //ticket não existe
                if (ticketObject == null) {
                    return "no such ticket";
                }

                //ticked já foi obtido
                if (ticketObject.getTaken() == 1) {
                    return "ticket taken";
                }


                User newUser = new User();
                newUser.setUsername(username);
                newUser.save();
                ticketObject.setTaken(1);
                ticketObject.save();
                newUser.setTicket(ticketObject);
                //todo generate token session
                //todo: read the random number
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));

        post("/login", (((request, response) -> {

            try {


                JsonElement body = new JsonParser().parse(request.body());
                String ticket = body.getAsJsonObject().get("ticket").getAsString();
                String username = body.getAsJsonObject().get("username").getAsString();
                String random = body.getAsJsonObject().get("random").getAsString();

                User user = User.where("username ='" + username + "'").get(0);

                if (user == null) {
                    return false;
                }

                if (user.getTicket().getNumber() != Integer.parseInt(ticket)) {
                    return false;
                }

                if (user.getSession() != null) {
                    return false;
                }

                String token = UUID.randomUUID().toString().toUpperCase()
                        + "|" + user.getId() + "|"
                        + System.currentTimeMillis() + "|" + Integer.parseInt(random);

                String sharedToken = UUID.randomUUID().toString().toUpperCase()
                        + "|" + user.getId() + "|"
                        + System.currentTimeMillis() + "|" + Integer.parseInt(random);


                Session session = new Session();
                session.setToken(token);
                session.setRandom(Integer.parseInt(random));
                session.setSharedToken(sharedToken);
                session.save();

                user.setSession(session);

                return "{" +
                        "\"token\" : \"" + token + "\"," +
                        "\"sharedToken\" : \"" + sharedToken + "\"" +
                        "}";

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        })));

        get("test", (((request, response) -> {

            String token = request.headers("token");
            System.out.println(token);
            String[] s = token.split("\\|");
            System.out.println(s);
            System.out.println(s[1]);
            System.out.println(User.get(Integer.parseInt(s[1])));

            Quiz quiz = Quiz.get(1);
            Gson gson = new Gson();
            String quizJson = gson.toJson(quiz);

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

            response.type("application/json");

            return "{" +
                    "\"Quiz\" : " + quizJson + "," +
                    "\"QuizQuestions\" : " + quizQuestions.toString() +
                    "}";

        })));

    }
}
