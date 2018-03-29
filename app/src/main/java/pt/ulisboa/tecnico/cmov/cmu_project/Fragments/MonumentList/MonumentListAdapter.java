package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int pos) {

        return monuments.get(pos) == null ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Monument monument = monuments.get(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(getItemViewType(position) == 1 ?
                    R.layout.monument_list_start : R.layout.monument_list_item, parent, false);
        }

        if (monument == null) {

            TextView textView = convertView.findViewById(R.id.startMonumentListText);
            LinearLayout linearLayout = convertView.findViewById(R.id.startLinear);

            if (position == 0) {
                textView.setText("START");
                linearLayout.setPadding(convertDpToPixel(5), convertDpToPixel(5), 0, 0);
                textView.setBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
            } else {
                textView.setText("END");
                Monument prevMonument = monuments.get(position - 1);
                linearLayout.setPadding(convertDpToPixel(5), 0, 0, convertDpToPixel(5));
                Log.i("COLOR", (prevMonument != null && !prevMonument.getStatus().equals(Monument.VISITED)) + "");
                Log.i("COLOR", (prevMonument != null) + "");
                Log.i("COLOR", (!(prevMonument.getStatus().equals(Monument.VISITED))) + "");
                textView.setBackgroundColor(getContext().getResources().getColor(prevMonument != null && !prevMonument.getStatus().equals(Monument.VISITED) ?
                        R.color.colorGray : R.color.colorQuizPath));
            }

            return convertView;
        }


        TextView status = convertView.findViewById(R.id.status);

        status.setText(monument.getStatus().toUpperCase());
        status.setBackgroundColor(getContext().getResources().getColor(getColor(monument.getStatus())));

        View viewBefore = convertView.findViewById(R.id.viewBefore);
        if (monument.getStatus().equals(Monument.VISITED) || monument.getStatus().equals(Monument.QUIZ)) {
            viewBefore.setBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
        } else {
            viewBefore.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightGray));
        }

        View viewAfter = convertView.findViewById(R.id.viewAfter);
        if (monument.getStatus().equals(Monument.VISITED)) {
            viewAfter.setBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
        } else {
            viewAfter.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightGray));
        }

        CardView statusCardView = convertView.findViewById(R.id.statusCardView);
        if (monument.getStatus().equals(Monument.VISITED)) {
            statusCardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorQuizPath));
        } else if (monument.getStatus().equals(Monument.QUIZ)) {
            statusCardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorQuiz));
        } else {
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

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
