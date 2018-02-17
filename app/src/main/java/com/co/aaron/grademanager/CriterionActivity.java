package com.co.aaron.grademanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aaron on 27/12/2017.
 * Activity that shows te contents of criteria
 * Value
 * Points
 * Assignments
 */

public class CriterionActivity extends Activity {

    private Criteria criteriaSelected;
    private AssignmentAdapter assignmentAdapter;
    private int subjectIndex;
    private int criteriaIndex;
    private int auxIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria);

        //Gets Subject from Intent
        Intent getCriteria = getIntent();
        //criteriaSelected = (Criteria) getCriteria.getSerializableExtra("CRITERIA");
        subjectIndex = (int) getCriteria.getSerializableExtra("SUBJECT_INDEX");
        criteriaIndex = (int) getCriteria.getSerializableExtra("CRITERIA_INDEX");

        //Gets selected criteria shortcut
        criteriaSelected = MainActivity.subjects.get(subjectIndex).getCriterias().get(criteriaIndex);

        //Initialize TextView, ListView and Adapter
        TextView criteriaName = findViewById(R.id.criteria_name_text_view);
        TextView criteriaValue = findViewById(R.id.criterion_value_text_view);
        final TextView criteriaAssignmentsNumber = findViewById(R.id.number_assignments_text_view);
        final TextView criteriaPoints = findViewById(R.id.criteria_points_text_view);

        ListView assignmentListView = findViewById(R.id.assignment_list_view);
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
                /**
                 * Displays a dialog where the assignment can be edited
                 */

                auxIndex  = i;  //Saves the index of the selected element for later use

                //Defines float format
                final DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);

                //Initialize Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(CriterionActivity.this);
                builder.setTitle(R.string.modify_assignment_title_dialog);

                // Inflates layout and mounts it to dialog
                final Context context = builder.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.assignment_modify, null, false);
                builder.setView(dialogView);

                //Set the name of the subject to EditText
                final EditText newAssignmentName = dialogView.findViewById(R.id.modify_assignment_name_edit_text);
                final EditText newAssignmentGrade = dialogView.findViewById(R.id.modify_assignment_grade_edit_text);
                final EditText setDateText = dialogView.findViewById(R.id.enter_assignment_date_edit_text);

                //Sets the contents of the widgets
                final Assignment selectedAssignment  = (Assignment) adapterView.getItemAtPosition(auxIndex);
                newAssignmentName.setText(selectedAssignment.getName());
                newAssignmentGrade.setText(df.format(selectedAssignment.getGrade()));
                setDateText.setText(selectedAssignment.getDeliverDate());

                //Set date button
                setDateText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            //Gets the date saved so the calendar is init there
                            String[] date = selectedAssignment.getDeliverDate().split("/");
                            final int year = Integer.valueOf(date[2]);
                            final int month = Integer.valueOf(date[1])-1;
                            final int day = Integer.valueOf(date[0]);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(CriterionActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    String date = i2 + "/" + (i1+1) + "/" + i;
                                    setDateText.setText(date);
                                }
                            }, year, month, day);

                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.MONTH, -4);
                            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

                            datePickerDialog.show();

                        }catch (NumberFormatException e){

                            //Exception validates date format, if invalid, min date on calendar is shown
                            Date today = new Date(); // current date
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(today);


                            Toast.makeText(CriterionActivity.this, R.string.date_error_toast, Toast.LENGTH_SHORT).show();
                            //If date was not valid
                            DatePickerDialog datePickerDialog = new DatePickerDialog(CriterionActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    String date = i2 + "/" + (i1+1) + "/" + i;
                                    setDateText.setText(date);
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                            //Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.MONTH, -4);
                            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

                            datePickerDialog.show();
                        }
                    }
                });

                //Save button
                builder.setPositiveButton(R.string.save_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**
                         * Updates name, value and date of the assignment
                         */

                        //Define Float Format
                        DecimalFormat df = new DecimalFormat("#.###");
                        df.setRoundingMode(RoundingMode.CEILING);

                        //Define Date Format
                        SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/yy");

                        //Validates empty name
                        if (newAssignmentName.getText().toString().equals("")){
                            Toast.makeText(CriterionActivity.this, R.string.enter_name_toast, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Validates empty value
                        if (newAssignmentGrade.getText().toString().equals("")) {
                            Toast.makeText(CriterionActivity.this, R.string.enter_grade_toast , Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Validates empty date
                        if (setDateText.getText().toString().equals("")) {
                            Toast.makeText(CriterionActivity.this, R.string.enter_date_toast, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Validates value and updates elements
                        try {

                            float aux = Float.valueOf(newAssignmentGrade.getText().toString());

                            //Updates name, value and points
                            criteriaSelected.getAssignments().get(auxIndex).setGrade(Float.valueOf(df.format(aux)));
                            criteriaSelected.getAssignments().get(auxIndex).setName(newAssignmentName.getText().toString());
                            criteriaSelected.getAssignments().get(auxIndex).setDeliverDate(setDateText.getText().toString());

                            //Updates points
                            criteriaSelected.setPoints();
                            assignmentAdapter.notifyDataSetChanged();
                            updatePoints();
                            saveContent();


                        }catch (NumberFormatException e){
                            Toast.makeText(CriterionActivity.this, R.string.valid_assignment_grade_toast, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                //Delete Button
                builder.setNegativeButton(R.string.delete_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**
                         * Deletes selected assignment from the list
                         */
                        criteriaSelected.getAssignments().remove(auxIndex);
                        assignmentAdapter.notifyDataSetChanged();
                        Toast.makeText(CriterionActivity.this, R.string.assignment_deleted_toast, Toast.LENGTH_SHORT).show();
                        updatePoints();

                        TextView numberOfAssignments = findViewById(R.id.number_assignments_text_view);
                        numberOfAssignments.setText(String.valueOf(criteriaSelected.getAssignments().size()));
                        saveContent();

                    }
                });

                builder.show();
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

        //Initialize Date edit text
        final EditText assignmentDate = (EditText) dialogView.findViewById(R.id.delivery_date_edit_text);

        //DatePicker dialog from date edit text
        assignmentDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Date today = new Date(); // current date
                Calendar cal = Calendar.getInstance();
                cal.setTime(today);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CriterionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        assignmentDate.setText(i2 + "/" + (i1+1) + "/" + i);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                cal.add(Calendar.MONTH, -4);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        //Create button
        builder.setPositiveButton(R.string.save_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Initialize EditTexts
                EditText assignmentName = (EditText) dialogView.findViewById(R.id.assignment_name_text_view);
                EditText assignmentGrade = (EditText) dialogView.findViewById(R.id.assignment_grade_text_view);

                //Define Float and date Format
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/yy");

                //validate not null object
                if (assignmentName.getText() == null || assignmentGrade.getText() == null) {
                    Toast.makeText(CriterionActivity.this, R.string.error_assignment_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Get values from EditTexts
                String name = assignmentName.getText().toString();
                String date = assignmentDate.getText().toString();

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
                    TextView pointsTextView = findViewById(R.id.number_assignments_text_view);
                    pointsTextView.setText(String.valueOf(criteriaSelected.getAssignments().size()));

                    Toast.makeText(CriterionActivity.this, R.string.assignment_created_toast, Toast.LENGTH_SHORT).show();
                    saveContent();
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
            pointsTextView  = findViewById(R.id.criteria_points_text_view);
            criteriaSelected.setPoints();
            pointsTextView.setText(R.string.empty_average_text_view);
            return;
        }

        pointsTextView = findViewById(R.id.criteria_points_text_view);
        criteriaSelected.setPoints();
        pointsTextView.setText(String.valueOf(criteriaSelected.getPoints()));

        return;
    }

    void saveContent() {

        SharedPreferences appSharedPrefs  = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.subjects);

        prefsEditor.putString("Subjects", json);
        prefsEditor.commit();
    }
}


