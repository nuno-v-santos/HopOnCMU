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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

/**
 * Created by tiago on 28-Mar-18.
 */

public class MonumentListAdapter extends ArrayAdapter<MonumentData> {


    private ArrayList<MonumentData> monuments;

    public MonumentListAdapter(Context context, ArrayList<MonumentData> monuments) {
        super(context, 0, monuments);
        this.monuments = monuments;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        MonumentData monument = monuments.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.monument_list_item, parent, false);
        }


        TextView status = convertView.findViewById(R.id.status);
        ImageView imageView = convertView.findViewById(R.id.imageViewMonumentListAdapter);
        TextView monumentName = convertView.findViewById(R.id.monumentNameListAdapter);

        status.setText(monument.getStatus().toUpperCase());
        status.setBackgroundColor(getContext().getResources().getColor(getColor(monument.getStatus())));

        // todo colocar imagem correct
        imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.belem));

        monumentName.setText(monument.getMonumentName());


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
