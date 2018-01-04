package com.co.aaron.grademanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by aaron on 20/12/2017.
 *
 * Defines how the adapter will work with the ListView
 * Shows subjects's name
 * Shows subject's average
 */

public class SubjectAdapter extends ArrayAdapter<Subject>{

    SubjectAdapter(@NonNull Context context, @NonNull ArrayList<Subject> objects) {
        super(context, R.layout.subject_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Initialize the view
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.subject_row, parent, false);

        //Reads the item
        Subject item = getItem(position);

        //Initialize the widgets for that item
        TextView subjectNameTextView = view.findViewById(R.id.subject_text_view);
        TextView averageTextView = view.findViewById(R.id.average_text_view);

        //Defines float format
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        //Fills the widgets for that item
        assert item != null;
        subjectNameTextView.setText(item.getName());
        averageTextView.setText( df.format(item.getAverage() ) );

        return view;
    }
}
