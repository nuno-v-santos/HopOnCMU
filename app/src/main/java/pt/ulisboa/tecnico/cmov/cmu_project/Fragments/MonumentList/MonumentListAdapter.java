package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.R;

/**
 * Created by tiago on 28-Mar-18.
 */

public class MonumentListAdapter extends ArrayAdapter<Monument> {


    private ArrayList<Monument> monuments;

    public MonumentListAdapter(Context context, ArrayList<Monument> monuments) {
        super(context, 0, monuments);
        this.monuments = monuments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.monument_list_item, parent, false);
        }


        Monument monument = monuments.get(position);

        TextView status = convertView.findViewById(R.id.status);
        status.setText(monument.getStatus().toUpperCase());
        status.setBackgroundColor(getContext().getResources().getColor(getColor(monument.getStatus())));

        View viewBefore = convertView.findViewById(R.id.viewBefore);
        if (monument.getStatus().equals(Monument.VISITED) || monument.getStatus().equals(Monument.QUIZ)) {
            viewBefore.setBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
        }else {
            viewBefore.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightGray));
        }

        View viewAfter = convertView.findViewById(R.id.viewAfter);
        if (monument.getStatus().equals(Monument.VISITED)) {
            viewAfter.setBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
        }else {
            viewAfter.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightGray));
        }

        CardView statusCardView = convertView.findViewById(R.id.statusCardView);
        if (monument.getStatus().equals(Monument.VISITED) || monument.getStatus().equals(Monument.QUIZ)) {
            statusCardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
        }else {
            statusCardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorGray));
        }


        return convertView;
    }


    public int getColor(String status) {

        switch (status) {
            case Monument.VISITED:
                return R.color.colorVisited;
            case Monument.NOT_VISITED:
                return R.color.colorNotVisited;
            case Monument.QUIZ:
                return R.color.colorQuiz;

            default:
                return R.color.colorVisited;
        }

    }
}
