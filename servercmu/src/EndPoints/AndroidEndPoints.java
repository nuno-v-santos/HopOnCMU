package EndPoints;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

                if (user == null) return false;

                Gson gson = new Gson();

                String result = gson.toJson(user.getTicket().getTour().getMonuments());
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

                if (user == null) return false;

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


    }
}
