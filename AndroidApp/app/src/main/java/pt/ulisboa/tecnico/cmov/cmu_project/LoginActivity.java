package pt.ulisboa.tecnico.cmov.cmu_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private Random rand = new Random();
    public final static String SHARED_PREF_TOKEN = "SHARED_PREF_TOKEN";
    public final static String SESSION_TOKEN = "token";
    public final static String SEND_USERNAME = "SEND_USERNAME";
    private SharedPreferences.Editor editor;
    final private static String RANDOM = "random";
    final private static String USERNAME = "username";
    final private static String TICKET = "ticket";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE);
        this.editor = sp.edit();
        if (sp.getString(LoginActivity.SESSION_TOKEN, null) != null){
            //TODO: check if session is still valid
            Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setupParent(findViewById(R.id.relativeLayout));

        DatabaseHelper.getInstance(getBaseContext()).deleteDataBase(getBaseContext());
        Toast.makeText(this, "apagado", Toast.LENGTH_LONG).show();


    }

    protected void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Method that responds to the event of the click in the login event
     *
     * @param v
     * @throws IOException
     * @throws JSONException
     */
    public void login(View v) throws IOException, JSONException {

        EditText usrName = findViewById(R.id.txtLogin);
        final String userName = usrName.getText().toString();
        EditText ticketNum = findViewById(R.id.txtTicketNum);
        final String ticketNumber = ticketNum.getText().toString();

        if (ticketNum.length() > 0 && userName.length() > 0) {
            int randInt = this.rand.nextInt();
            loginPost(userName, ticketNumber, randInt);


        } else {

            Toast.makeText(this, R.string.short_login, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Function that sends to the server the username and ticketNumber
     *
     * @param userName     user username
     * @param ticketNumber user ticket number
     * @param randInt      a random int for security purposes
     * @throws IOException
     * @throws JSONException
     */
    private void loginPost(final String userName, final String ticketNumber, int randInt) throws JSONException {

        JSONObject postParams = new JSONObject();
        postParams.put(LoginActivity.USERNAME, userName);
        postParams.put(LoginActivity.TICKET, Integer.parseInt(ticketNumber));
        postParams.put(LoginActivity.RANDOM, randInt);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URLS.URL_LOGIN, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getString("error").equals("")) {
                        String token = response.getString(LoginActivity.SESSION_TOKEN);
                        LoginActivity.this.editor.putString(LoginActivity.SESSION_TOKEN, token);
                        LoginActivity.this.editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(SEND_USERNAME, userName);

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.getString("error"),
                                Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), R.string.server_connection_error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), R.string.server_connection_error, Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(jsonObjReq);
    }

}







