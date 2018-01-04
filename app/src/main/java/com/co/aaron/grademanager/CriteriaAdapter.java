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
 * Created by aaron on 23/12/2017.
 * Adapter for Criteria in Subject Activity
 */

public class CriteriaAdapter extends ArrayAdapter<Criteria>{

    CriteriaAdapter(@NonNull Context context, @NonNull ArrayList<Criteria> objects) {
        super(context, R.layout.criteria_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Initialize the view
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.criteria_row, parent, false);

        //Reads item
        Criteria item = getItem(position);

        //Initialize widget for item
        TextView criteriaNameTextView = view.findViewById(R.id.criteria_list_view);
        TextView criteriaAverage = view.findViewById(R.id.criteria_value_text_view);
        TextView criteriaPoints = view.findViewById(R.id.criteria_points_text_view);

        //Float format
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        //Fill widgets
        assert item != null;
        criteriaNameTextView.setText(item.getName());
        String average = df.format(item.getValue()) + "%";
        criteriaAverage.setText(average);
        criteriaPoints.setText(df.format(item.getPoints()));

        return view;
    }
}
