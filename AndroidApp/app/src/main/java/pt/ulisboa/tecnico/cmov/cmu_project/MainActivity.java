package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MainFragment;
import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList.MonumentListFragment;
import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.Ranking.RankingFragment;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SEND_MONUMENT_LIST = "MONUMENT_LIST";
    private LinkedList<MonumentData> monumentDataLinkedList = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to set our main fragment
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        // not sure if we need a tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //configurations of the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation configurations
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(LoginActivity.SEND_USERNAME);

        if (DatabaseHelper.getInstance(getBaseContext()).tableIsEmpty(DatabaseHelper.TABLE_MONUMENTS))
            getMonuments();
        else {

            this.monumentDataLinkedList = DatabaseHelper.getInstance(getBaseContext()).buildMonumentsFromDB();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function that logs out the user.
     */
    private void logOutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString(LoginActivity.SESSION_TOKEN, "");

        if (!sessionToken.equals("")) {
            JSONObject jsonParams = new JSONObject();
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, URLS.URL_LOGOUT, jsonParams,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("token", sessionToken);
                    return headers;
                }
            };

            VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(myRequest);
        }
    }

    /**
     * Simple function that builds MonumentData object from JSONObject
     *
     * @param jObject
     * @return MonumentData object
     * @throws JSONException
     */
    private void addMonumentToDB(JSONObject jObject, DatabaseHelper databaseHelper) throws JSONException {
        String imURL = jObject.getString("imageURL");
        String name = jObject.getString("name");
        String wifiId = jObject.getString("wifiId");
        int id = jObject.getInt("id");
        String sampleDesc = getResources().getString(R.string.sample_desc);
        databaseHelper.insertMonument(id, MonumentData.NOT_VISITED, imURL, name, sampleDesc, wifiId);
    }


    /**
     * Method that asks the server for the monuments
     *
     * @return
     */
    private void getMonuments() {
        final LinkedList<MonumentData> monumentsList = new LinkedList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString(LoginActivity.SESSION_TOKEN, "");
        Toast.makeText(this, sessionToken, Toast.LENGTH_SHORT).show();
        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getBaseContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, URLS.URL_GET_MONUMENTS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        addMonumentToDB(response.getJSONObject(i), databaseHelper);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                MainActivity.this.monumentDataLinkedList = databaseHelper.buildMonumentsFromDB();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, R.string.server_connection_error, Toast.LENGTH_SHORT).show();

            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", sessionToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new MainFragment();
        } else if (id == R.id.nav_ranking) {
            fragment = new RankingFragment();
        } else if (id == R.id.nav_monuments) {

            Bundle bundle = new Bundle();
            bundle.putSerializable(MainActivity.SEND_MONUMENT_LIST, monumentDataLinkedList);

            fragment = new MonumentListFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_share_results) {
            //todo: wi-fi direct
        }
        if (id == R.id.nav_logout) {
            logOutUser();
            finish();
            return true;
        }

        //if we have a fragment selected than we make a transition
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
