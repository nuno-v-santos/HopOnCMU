package pt.ulisboa.tecnico.cmov.cmu_project.Monument;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pt.ulisboa.tecnico.cmov.cmu_project.LoginActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizQuestion;
import pt.ulisboa.tecnico.cmov.cmu_project.R;
import pt.ulisboa.tecnico.cmov.cmu_project.URLS;
import pt.ulisboa.tecnico.cmov.cmu_project.VolleySingleton;

public class MonumentScreenActivity extends AppCompatActivity {

    private MonumentData monData;
    public static final String MONUMENT_DATA = "MONUMENT_DATA";
    private ArrayList<QuizQuestion> quizQuestions;
    private int nClick = 0;
    private boolean validSSID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_monument_screen);
        Intent intent = getIntent();
        this.monData = (MonumentData) intent.getSerializableExtra(MONUMENT_DATA);

        this.quizQuestions = new ArrayList<>();
        this.downloadQuestions();
        this.updateUIWithMonumentInfo();


    }


    /**
     * Função que actualiza a interface de acordo com o objecto MonumentData
     */
    private void updateUIWithMonumentInfo() {
        ImageView imageView = findViewById(R.id.imgViewMonument);
        TextView monumentNameTxtView = findViewById(R.id.txtMonumentName);
        TextView monumentInfoTxtView = findViewById(R.id.txtMonumentInfo);

        //imageView.setBackground(getResources().getDrawable(this.monData.getImURL()));
        monumentNameTxtView.setText(this.monData.getMonumentName());
        monumentInfoTxtView.setText(this.monData.getMonumentDescription());
    }

    /**
     * Funcão que trata do evento onClick no botão que faz o download do Quiz
     *
     * @param view
     */
    public void btnDownloadQuizOnClick(View view) throws InterruptedException, TimeoutException, JSONException {

        String monDataWifiId = this.monData.getWifiId();
        this.downloadQuestions();

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(QuizActivity.QUIZ_QUESTIONS, this.quizQuestions);
        intent.putExtra(QuizActivity.MONUMENT_IMG, this.monData.getImURL());
        startActivity(intent);
    }

    /**
     * Function that given JSONArray and String buils object QuizQuestion
     *
     * @param answers
     * @param question
     * @return
     * @throws JSONException
     */
    private QuizQuestion buildQuizQuestion(JSONArray answers, String question) throws JSONException {

        ArrayList<String> allAnswers = new ArrayList<>();
        String correctAnswer = null;

        for (int j = 0; j < answers.length(); j++) {

            JSONObject tmp = answers.getJSONObject(j);
            String answer = tmp.getString("answer");
            int correct = tmp.getInt("correct");

            if (correct == 1)
                correctAnswer = answer;

            allAnswers.add(answer);
        }

        return new QuizQuestion(question, correctAnswer, allAnswers);
    }

    /**
     * Function that downloads the questions and builds the list of quiz questions
     */
    private void downloadQuestions() {
        final int id = this.monData.getMonumentID();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE);
        final String token = sharedPreferences.getString(LoginActivity.SESSION_TOKEN, "");

        if (!token.equals("")) {

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, URLS.URL_GET_QUESTIONS, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println(response.toString());
                    for (int i = 0; i < response.length(); i++) {

                        try {

                            JSONObject tmpObj = response.getJSONObject(i);
                            String question = tmpObj.getString("question");
                            JSONArray answers = tmpObj.getJSONArray("answers");
                            quizQuestions.add(buildQuizQuestion(answers, question));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (QuizQuestion q : quizQuestions) {

                        System.out.println(q.getQuestion());
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MonumentScreenActivity.this, R.string.server_connection_error, Toast.LENGTH_SHORT).show();

                }

            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("token", token);
                    headers.put("monumentID", "" + id);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
        }
    }

    /**
     * Function that verifies the network SSDI that the device is connected and compares it with
     * the SSDI that allows the user to download the quiz for a determined monument
     *
     * @param monSSID
     * @return
     */
    private Boolean checkNetworkSSDI(String monSSID) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        return ssid.equals(monSSID);
    }

}
