package com.co.aaron.grademanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by aaron on 27/12/2017.
 */

public class CriterionActivity extends Activity {

    private Criteria criteriaSelected;
    private AssignmentAdapter assignmentAdapter;
    private ListView assignmentListView;
    private int auxIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria);

        //Gets Subject from Intent
        Intent getCriteria = getIntent();
        criteriaSelected = (Criteria) getCriteria.getSerializableExtra("CRITERIA");

        //Initialize TextView, ListView and Adapter
        TextView criteriaName = (TextView) findViewById(R.id.criteria_name_text_view);
        TextView criteriaValue = (TextView)  findViewById(R.id.criterion_value_text_view);
        TextView criteriaAssignmentsNumber = (TextView) findViewById(R.id.number_assignments_text_view);
        final TextView criteriaPoints = (TextView) findViewById(R.id.criteria_points_text_view);

        assignmentListView = (ListView) findViewById(R.id.assignment_list_view);
        assignmentAdapter =  new AssignmentAdapter(this, criteriaSelected.getAssignments());
        assignmentListView.setAdapter(assignmentAdapter);

        //Set TextView contents
        criteriaName.setText(criteriaSelected.getName());
        criteriaValue.setText(String.valueOf(criteriaSelected.getValue()));
        criteriaAssignmentsNumber.setText(String.valueOf(criteriaSelected.getAssignments().size()));
        criteriaPoints.setText(String.valueOf(criteriaSelected.getPoints()));

        //Click to open assignments
        assignmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        //Long click to modify assignment

        assignmentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return true;
            }
        });
    }

    public void addAssignmentButton(View view) {
        /**
         * Button to add a new assignment to the list
         * Creates a dialog from a layout
         * Reads the name and grade of the assignment
         * If input is valid adds the element to the list(assignment)
         * Updates the ListView
         * */

        AlertDialog.Builder builder = new AlertDialog.Builder(CriterionActivity.this);
        builder.setTitle(R.string.create_assignment_title_dialog);

        // inflates layout and mounts it to dialog
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.add_assignment, null, false);
        builder.setView(dialogView);
    }
}
