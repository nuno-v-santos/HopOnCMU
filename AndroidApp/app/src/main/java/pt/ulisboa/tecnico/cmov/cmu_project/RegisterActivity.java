package pt.ulisboa.tecnico.cmov.cmu_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    public final static String SHARED_PREF_TOKEN = "SHARED_PREF_TOKEN";
    public final static String SESSION_TOKEN = "token";
    public final static String SEND_USERNAME = "SEND_USERNAME";

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_register);
        this.editor = getSharedPreferences(RegisterActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE).edit();
        this.editor.remove(SESSION_TOKEN);
        this.editor.commit();
        setupParent(findViewById(R.id.relativeLayout));

        DatabaseHelper.getInstance(getBaseContext()).deleteDataBase(getBaseContext());
    }

    protected void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
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

    public void registerOnClick(View v) throws IOException, JSONException {

        EditText usrName = findViewById(R.id.txtRegister);
        String userName = usrName.getText().toString();
        EditText ticketNum = findViewById(R.id.txtTicketNum);
        String ticketNumber = ticketNum.getText().toString();

        if (ticketNum.length() > 0 && userName.length() > 0) {
            int randInt = (new Random()).nextInt();
            registerPost(userName, ticketNumber, randInt);
        } else {
            Toast.makeText(this, R.string.invalid_register, Toast.LENGTH_LONG).show();
        }

    }

    private void registerPost(final String userName, final String ticketNumber, int randInt) throws IOException, JSONException {

        JSONObject postParams = new JSONObject();

        postParams.put("username", userName);
        postParams.put("ticket", ticketNumber);
        postParams.put("random", randInt);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URLS.URL_REGISTER, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("token").equals(""))
                        Toast.makeText(getBaseContext(), response.getString("error"), Toast.LENGTH_LONG).show();
                    else {
                        String token = response.getString("token");
                        RegisterActivity.this.editor.putString(RegisterActivity.SESSION_TOKEN, token);
                        RegisterActivity.this.editor.commit();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.putExtra(SEND_USERNAME,userName);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), R.string.server_connection_error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), R.string.server_connection_error, Toast.LENGTH_LONG).show();
                        error.printStackTrace();

                    }
                });
        VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(jsonObjReq);
    }
}
