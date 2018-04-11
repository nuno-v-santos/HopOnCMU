package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private Random rand = new Random();
    public static String SESSION_TOKEN;
    public static String SHARED_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_login);
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Method that responds to the event of the click in the login event
     * @param v
     * @throws IOException
     * @throws JSONException
     */
    public void login(View v) throws IOException, JSONException {

        EditText usrName = findViewById(R.id.txtLogin);
        String userName = usrName.getText().toString();
        EditText ticketNum = findViewById(R.id.txtTicketNum);
        String ticketNumber = ticketNum.getText().toString();

        if (ticketNum.length() > 1 && userName.length() > 1) {
            int randInt = this.rand.nextInt();
            loginPost(userName, ticketNumber, randInt);

            if (LoginActivity.SHARED_TOKEN != null && LoginActivity.SESSION_TOKEN != null) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        } else {

            Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Function that sends to the server the username and ticketNumber
     * @param userName user username
     * @param ticketNumber user ticket number
     * @param randInt a random int for security purposes
     * @throws IOException
     * @throws JSONException
     */
    private void loginPost(String userName, String ticketNumber, int randInt) throws IOException, JSONException {

        JSONObject postParams = new JSONObject();
        postParams.put("username", "tiago");
        postParams.put("ticket", 123456789);
        postParams.put("random", 25);

        /*
        postParams.put("username", userName);
        postParams.put("ticket", Integer.parseInt(ticketNumber));
        postParams.put("random", randInt);
        */

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URLS.URL_LOGIN, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LoginActivity.SESSION_TOKEN = response.getString("token");
                    LoginActivity.SHARED_TOKEN = response.getString("sharedToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}





