package pt.ulisboa.tecnico.cmov.cmu_project.Monument;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.cmu_project.DatabaseHelper;
import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList.Monument;
import pt.ulisboa.tecnico.cmov.cmu_project.LoginActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.MainActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizQuestion;
import pt.ulisboa.tecnico.cmov.cmu_project.R;
import pt.ulisboa.tecnico.cmov.cmu_project.URLS;
import pt.ulisboa.tecnico.cmov.cmu_project.VolleyRequests.JsonArrayRequestV;
import pt.ulisboa.tecnico.cmov.cmu_project.VolleySingleton;


public class MonumentScreenActivity extends AppCompatActivity {

    private MonumentData monData;
    public static final String MONUMENT_DATA = "MONUMENT_DATA";
    private ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_monument_screen);


        Intent intent = getIntent();
        dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        this.monData = (MonumentData) intent.getSerializableExtra(MONUMENT_DATA);
        this.updateUIWithMonumentInfo();

        if (DatabaseHelper.getInstance(getApplicationContext()).questionForMonumentDownload(this.monData.getMonumentID())) {

            Button button = findViewById(R.id.btnDownloadQuiz);
            button.setText(R.string.play_quiz);
        }

    }


    /**
     * Função que actualiza a interface de acordo com o objecto MonumentData
     */
    private void updateUIWithMonumentInfo() {

        ImageView imageView = findViewById(R.id.imgViewMonument);
        VolleySingleton.getInstance(getBaseContext()).requestImage(monData.getImURL(), imageView, getBaseContext());
        TextView monumentNameTxtView = findViewById(R.id.txtMonumentName);
        TextView monumentInfoTxtView = findViewById(R.id.txtMonumentInfo);

        monumentNameTxtView.setText(this.monData.getMonumentName());
        monumentInfoTxtView.setText(this.monData.getMonumentDescription());
    }

    /**
     * Funcão que trata do evento onClick no botão que faz o download do Quiz
     *
     * @param view
     */
    public void btnDownloadQuizOnClick(View view) {

        //String monDataWifiId = this.monData.getWifiId();

        final int monID = this.monData.getMonumentID();


        if (dbHelper.monQuestionAnswered(monID)) {
            Toast.makeText(getBaseContext(), R.string.quiz_answered, Toast.LENGTH_SHORT).show();
            return;
        }


        if (dbHelper.questionForMonumentDownload(monID)) {
            startQuizActivity(monID);

        /*} else if (!dbHelper.questionForMonumentDownload(monID)) {
            Toast.makeText(getBaseContext(), R.string.txt_down, Toast.LENGTH_SHORT).show();
            this.downloadQuestions();*/

        } else if (!dbHelper.questionForMonumentDownload(monID)) {
            //scans nearby peers to check if near the monument
            System.out.println(MainActivity.wifiDirect.getDevicesList().size());
            for (SimWifiP2pDevice device : MainActivity.wifiDirect.getPeerList()) {

                if (device.deviceName.equals(this.monData.getWifiId())) {
                    Toast.makeText(getBaseContext(), R.string.txt_down, Toast.LENGTH_SHORT).show();
                    this.downloadQuestions();
                    return;
                }
            }
            Toast.makeText(getBaseContext(), "Not in the monument", Toast.LENGTH_SHORT).show();
        }
    }


    private void startQuizActivity(int monumentID) {

        MonumentScreenActivity.this.quizQuestions =
                DatabaseHelper.getInstance(getApplicationContext()).buildQuizQuestionFromDB(monumentID);

        Intent intent = new Intent(getBaseContext(), QuizActivity.class);
        intent.putExtra(QuizActivity.QUIZ_QUESTIONS, quizQuestions);
        intent.putExtra(QuizActivity.MON_ID, monumentID);
        startActivity(intent);

    }

    /**
     * Function that given JSONArray and String builds object QuizQuestion
     *
     * @param questionObj
     * @param monumentID
     * @return
     * @throws JSONException
     */

    private void insertQuestionInDb(JSONObject questionObj, int monumentID) throws JSONException {

        String question = questionObj.getString("question");
        int questionID = questionObj.getInt("id");
        JSONArray answers = questionObj.getJSONArray("answers");
        dbHelper.insertQuestion(questionID, monumentID, question, 0);

        for (int j = 0; j < answers.length(); j++) {
            JSONObject tmp = answers.getJSONObject(j);
            String answer = tmp.getString("answer");
            int answerID = tmp.getInt("id");
            int correct = tmp.getInt("correct");
            dbHelper.insertAnswer(answerID, questionID, answer, correct);
        }
    }


    /**
     * Function that downloads the questions and insert it in the database
     */
    private void downloadQuestions() {
        final int monumentID = this.monData.getMonumentID();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE);
        final String token = sharedPreferences.getString(LoginActivity.SESSION_TOKEN, "");

        if (!token.equals("")) {
            JsonArrayRequestV jsonObjectRequest = new JsonArrayRequestV(Request.Method.GET, URLS.URL_GET_QUESTIONS, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {


                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "INTEGRITY FAIL", Toast.LENGTH_LONG);
                        return;

                    }

                    System.out.println(response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject tmpObj = response.getJSONObject(i);
                            MonumentScreenActivity.this.insertQuestionInDb(tmpObj, monumentID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    dbHelper.updateMonumentQuizStatus(monumentID, MonumentData.DOWNLOADED);
                    dbHelper.updateMonumentStatus(monumentID, Monument.QUIZ);

                    Button btn = findViewById(R.id.btnDownloadQuiz);
                    btn.setText(R.string.play_quiz);

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
                    headers.put("monumentID", "" + monumentID);
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
