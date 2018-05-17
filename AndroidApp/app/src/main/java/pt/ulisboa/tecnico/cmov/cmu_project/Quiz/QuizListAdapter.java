package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

import android.app.Activity;

/**
 * Created by espada on 26-03-2018.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class QuizListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemNames;

    public QuizListAdapter(Activity context, ArrayList<String> itemNames) {
        super(context, R.layout.question_btn_layout, itemNames);
        this.context = context;
        this.itemNames = itemNames;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.question_btn_layout, null, true);
        TextView label = rowView.findViewById(R.id.monumentNameListAdapter);
        TextView a = rowView.findViewById(R.id.a);
        label.setText(itemNames.get(position));
        char alfabeth = (char) (65 + position);
        a.setText(alfabeth + "");
        return rowView;

    }

}
