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
            try {
                JsonElement body = new JsonParser().parse(request.body());
                String ticket = body.getAsJsonObject().get("ticket").getAsString();
                String username = body.getAsJsonObject().get("username").getAsString();
                String random = body.getAsJsonObject().get("random").getAsString();
                if (username.length() < 3) {
                    return "short name";
                }

                Ticket ticketObject = null;
                if (!Ticket.where("number = " + ticket).isEmpty())
                    ticketObject = Ticket.where("number = " + ticket).get(0);

                //username already taken
                if (User.where("username= '" + username + "'").size() > 0) {
                    return "{" +
                            "\"token\" : \"" + "\"," +
                            "\"error\" : \"" + "Username taken. Please choose another" + "\"" +
                            "}";
                }

                //ticket não existe
                if (ticketObject == null) {
                    return "{" +
                            "\"token\" : \"" + "\"," +
                            "\"error\" : \"" + "Ticket doesnt exist" + "\"" +
                            "}";
                }

                //ticket já foi obtido
                if (ticketObject.getTaken() == 1) {
                    return "{" +
                            "\"token\" : \"" + "\"," +
                            "\"error\" : \"" + "Ticket already taken" + "\"" +
                            "}";
                }


                User newUser = new User();
                newUser.setUsername(username);
                newUser.save();
                ticketObject.setTaken(1);
                ticketObject.save();
                newUser.setTicket(ticketObject);

                String token = UUID.randomUUID().toString().toUpperCase()
                        + "|" + newUser.getId() + "|"
                        + System.currentTimeMillis() + "|" + Integer.parseInt(random);

                String sharedToken = UUID.randomUUID().toString().toUpperCase()
                        + "|" + newUser.getId() + "|"
                        + System.currentTimeMillis() + "|" + Integer.parseInt(random);


                Session session = new Session();
                session.setToken(token);
                session.setRandom(Integer.parseInt(random));
                session.setSharedToken(sharedToken);
                session.save();

                newUser.setSession(session);

                return "{" +
                        "\"token\" : \"" + token + "\"," +
                        "\"error\" : \"" + "\"" +
                        "}";

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
                System.out.println(username + " " + ticket);
                List<User> userList = User.where("username ='" + username + "'");

                if (!userList.isEmpty()) {
                    User user = userList.get(0);

                    if (user.getSession() != null) {
                        return false;
                    }

                    if (user.getTicket().getNumber() != Integer.parseInt(ticket)) {
                        return "{" +
                                "\"token\" : \"" + "\"," +
                                "\"error\" : \"" + "Bus ticket and Username does not match" + "\"" +
                                "}";
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
                            "\"error\" : \"" + "\"" +
                            "}";

                } else {

                    return "{" +
                            "\"token\" : \"" + "\"," +
                            "\"error\" : \"" + "User does not exist" + "\"" +
                            "}";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        })));

        post("/logout", (((request, response) -> {

            String token = request.headers("token");
            String id = token.split("\\|")[1];

            User user = User.get(Integer.parseInt(id));
            if (user != null) {
                Session session = user.getSession();
                session.delete();
                return true;
            }


            return false;
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
