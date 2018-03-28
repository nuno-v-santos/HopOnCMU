package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.Ranking;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList.Monument;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

/**
 * Created by tiago on 28-Mar-18.
 */

public class RankingListAdapter extends ArrayAdapter<User> {


    private ArrayList<User> users;

    public RankingListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ranking_list_item, parent, false);
        }

        User user = users.get(position);

        ImageView placeImage = convertView.findViewById(R.id.imageRanking);
        TextView placeText = convertView.findViewById(R.id.textRanking);
        TextView nameText = convertView.findViewById(R.id.ranking_user_name);
        TextView scoreText = convertView.findViewById(R.id.rankingPoints);


        if (position < 3) {
            placeText.setVisibility(View.GONE);
            placeImage.setVisibility(View.VISIBLE);
            placeImage.setImageDrawable(getImage(position));
        } else if (position >= 3) {
            placeImage.setVisibility(View.GONE);
            placeText.setVisibility(View.VISIBLE);
            placeText.setText(position + 1 + "");
        }

        Log.i("POSITION", "dsdsa " + position);

        nameText.setText(user.getName());
        scoreText.setText(user.getPoints() + " pts.");

        return convertView;
    }

    private Drawable getImage(int position) {
        if (position == 0) {
            return getContext().getResources().getDrawable(R.drawable.ic_gold_medal);
        } else if (position == 1) {
            return getContext().getResources().getDrawable(R.drawable.ic_silver_medal);
        } else {
            return getContext().getResources().getDrawable(R.drawable.ic_bronze_medal);
        }
    }
}
