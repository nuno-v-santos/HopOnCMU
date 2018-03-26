package pt.ulisboa.tecnico.cmov.cmu_project;

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

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemNames;
    private final String[] itemAlpha;

    public CustomListAdapter(Activity context, ArrayList<String> itemNames, String[] itemAlpha) {
        super(context, R.layout.question_btn_layout, itemNames);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemNames = itemNames;
        this.itemAlpha = itemAlpha;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.question_btn_layout, null, true);
        TextView label = rowView.findViewById(R.id.label);
        TextView a = rowView.findViewById(R.id.a);
        label.setText(itemNames.get(position));
        a.setText(this.itemAlpha[position]);
        return rowView;

    }

}
