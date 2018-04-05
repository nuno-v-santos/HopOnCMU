package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentScreenActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class MonumentListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Monument List");

        View view = inflater.inflate(R.layout.fragment_monument_list, container, false);

        ArrayList<Monument> arrayOfUsers = new ArrayList<Monument>();

        arrayOfUsers.add(new Monument(Monument.VISITED));
        arrayOfUsers.add(new Monument(Monument.QUIZ));
        arrayOfUsers.add(new Monument(Monument.NOT_VISITED));
        arrayOfUsers.add(new Monument(Monument.NOT_VISITED));

// Create the adapter to convert the array to views
        MonumentListAdapter adapter = new MonumentListAdapter(getContext(), arrayOfUsers);
// Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.monumentsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), MonumentScreenActivity.class);
                MonumentData monData = new MonumentData(R.drawable.ic_launcher_background, "TORRE DE CMU", "Este Monumento é Bue Fixe");
                intent.putExtra(MonumentScreenActivity.MONUMENT_DATA, monData);
                startActivity(intent);
            }
        });

        return view;
    }

}
