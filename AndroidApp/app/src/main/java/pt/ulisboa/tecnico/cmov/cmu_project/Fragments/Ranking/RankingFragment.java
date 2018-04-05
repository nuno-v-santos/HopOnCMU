package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.Ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList.Monument;
import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList.MonumentListAdapter;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentScreenActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankingFragment} interface
 * to handle interaction events.
 * Use the {@link RankingFragment} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Ranking");

        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        ArrayList<User> arrayOfUsers = new ArrayList<User>();
        arrayOfUsers.add(new User("Manel das coubes", 400));
        arrayOfUsers.add(new User("Manel das coubes", 300));
        arrayOfUsers.add(new User("Manel das coubes", 200));
        arrayOfUsers.add(new User("Manel das coubes", 100));
        arrayOfUsers.add(new User("Manel das coubes", 100));
        arrayOfUsers.add(new User("Manel das coubes", 100));
        arrayOfUsers.add(new User("Manel das coubes", 100));
        arrayOfUsers.add(new User("Manel das coubes", 100));
// Create the adapter to convert the array to views
        RankingListAdapter adapter = new RankingListAdapter(getContext(), arrayOfUsers);
// Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.list_ranking);
        listView.setAdapter(adapter);

        return view;
    }

}
