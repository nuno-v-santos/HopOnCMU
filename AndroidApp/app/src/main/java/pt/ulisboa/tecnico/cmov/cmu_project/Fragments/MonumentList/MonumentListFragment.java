package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

import pt.ulisboa.tecnico.cmov.cmu_project.DatabaseHelper;
import pt.ulisboa.tecnico.cmov.cmu_project.MainActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentScreenActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class MonumentListFragment extends Fragment {


    private MonumentListAdapter adapter;
    private ArrayList<MonumentData> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Monument List");


        LinkedList<MonumentData>
                monumentDataList = (LinkedList<MonumentData>) getArguments().getSerializable(MainActivity.SEND_MONUMENT_LIST);

        View view = inflater.inflate(R.layout.fragment_monument_list, container, false);

        // Create the adapter to convert the array to views
        this.data.addAll(monumentDataList);
        this.adapter = new MonumentListAdapter(getContext(), data);

        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.monumentsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), MonumentScreenActivity.class);
                MonumentData monData = data.get(i);
                intent.putExtra(MonumentScreenActivity.MONUMENT_DATA, monData);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinkedList<MonumentData> x = DatabaseHelper.getInstance(getContext()).buildMonumentsFromDB();
        this.data = new ArrayList<MonumentData>();
        this.data.addAll(x);
        this.adapter.clear();
        this.adapter.addAll(this.data);
        this.adapter.notifyDataSetChanged();

    }


}
