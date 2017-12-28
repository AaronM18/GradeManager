package com.co.aaron.grademanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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

        //Create button
        builder.setPositiveButton(R.string.save_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Initialize EditTexts
                EditText assignmentName = (EditText) dialogView.findViewById(R.id.assignment_name_text_view);
                EditText assignmentGrade = (EditText) dialogView.findViewById(R.id.assignment_grade_text_view);
                CalendarView assignmentDate = (CalendarView) dialogView.findViewById(R.id.delivery_date_calendar_view);

                //Define Float and date Format
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/yy");

                //validate not null object
                if (assignmentName.getText() == null || assignmentGrade.getText() == null || assignmentDate == null) {
                    Toast.makeText(CriterionActivity.this, R.string.error_assignment_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Get values from EditTexts
                String name = assignmentName.getText().toString();
                String date = sdf.format(assignmentDate.getDate());

                //validate not empty string
                if (name.equals("")) {
                    Toast.makeText(CriterionActivity.this, R.string.enter_name_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (assignmentGrade.getText().toString().equals("")) {
                    Toast.makeText(CriterionActivity.this, R.string.enter_value_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (date.equals("")) {
                    Toast.makeText(CriterionActivity.this, R.string.enter_date_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String value = df.format(Float.valueOf(assignmentGrade.getText().toString()));

                    criteriaSelected.getAssignments().add(new Assignment(name, Float.valueOf(value), date));
                    assignmentAdapter.notifyDataSetChanged();
                    updatePoints();

                    //Updates number of assignments
                    TextView pointsTextView = (TextView) findViewById(R.id.number_assignments_text_view);
                    pointsTextView.setText(String.valueOf(criteriaSelected.getAssignments().size()));

                    Toast.makeText(CriterionActivity.this, R.string.assignment_created_toast, Toast.LENGTH_SHORT).show();
                    return;

                } catch (NumberFormatException e) {
                    Toast.makeText(CriterionActivity.this, R.string.valid_grade_toast, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        builder.show();
    }

    public void updatePoints(){
        /**
         * Updates the points ob the object
         * Updates the points TextView
         */
        final TextView pointsTextView;

        if (criteriaSelected.getAssignments().size() == 0) {
            pointsTextView  = (TextView) findViewById(R.id.criteria_points_text_view);
            pointsTextView.setText("00.00");
            return;
        }

        pointsTextView = (TextView) findViewById(R.id.criteria_points_text_view);
        criteriaSelected.setPoints();
        pointsTextView.setText(String.valueOf(criteriaSelected.getPoints()));

        return;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * Back button press action
         * Return the object selected
         */
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent returnObject = new Intent();
            returnObject.putExtra("OBJECT", (Serializable) criteriaSelected);
            setResult(RESULT_OK, returnObject);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
