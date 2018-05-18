package pt.ulisboa.tecnico.cmov.cmu_project.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.cmu_project.LoginActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.MainActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.R;
import pt.ulisboa.tecnico.cmov.cmu_project.URLS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment} interface
 * to handle interaction events.
 * Use the {@link MainFragment} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private int random;
    private String userName;
    private String ticket;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        getActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, getActivity().MODE_PRIVATE);
        this.random = sharedPreferences.getInt(LoginActivity.RANDOM, 0);
        this.userName = sharedPreferences.getString(LoginActivity.USERNAME, "");
        this.ticket = sharedPreferences.getString(LoginActivity.TICKET, "");

        Switch sw = view.findViewById(R.id.switch1);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    MainActivity.wifiDirect.wifiOn();
                }else {
                    MainActivity.wifiDirect.wifiOff();
                }

            }
        });


        TextView userNameTextView = view.findViewById(R.id.usernameFragmentMain);
        userNameTextView.setText(userName);

        return view;

    }


}
