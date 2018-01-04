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
 * Created by aaron on 27/12/2017.
 * Adapter for Assignments in Criteria Activity
 */

public class AssignmentAdapter extends ArrayAdapter<Assignment> {

    AssignmentAdapter(@NonNull Context context, @NonNull ArrayList<Assignment> objects) {
        super(context, R.layout.assignment_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Initialize the view
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.assignment_row, parent, false);

        //Reads item
        Assignment item = getItem(position);

        //Initialize widget for item
        TextView assignmentNameTextView = view.findViewById(R.id.assignment_text_view);
        TextView assignmentGrade = view.findViewById(R.id.grade_text_view);
        TextView assignmentDate = view.findViewById(R.id.date_text_view);

        //Float format
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        //Fill widgets
        assert item != null;
        assignmentNameTextView.setText(item.getName());
        assignmentGrade.setText(df.format(item.getGrade()));
        assignmentDate.setText(item.getDeliverDate());

        return view;
    }
}
