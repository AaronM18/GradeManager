package com.co.aaron.grademanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by aaron on 23/12/2017.
 *
 * Displays the contents of a subject:
 * Subject Name
 * Subject Average
 * Subjects criteria with its average
 */

public class SubjectActivity extends Activity {

    private Subject subjectSelected;
    private CriteriaAdapter criteriaAdapter;
    private ListView criteriaListView;
    private float average;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        //Gets Subject from Intent
        Intent getSubject = getIntent();
        subjectSelected = (Subject) getSubject.getSerializableExtra("SUBJECT");

        //Initialize Subject Name, Average, & Adapter & ListView
        TextView subjectName = findViewById(R.id.subject_name_text_view);
        TextView subjectAvg = findViewById(R.id.subject_avg_text_view);

        criteriaListView = (ListView) findViewById(R.id.criteria_list_view);
        criteriaAdapter = new CriteriaAdapter(this, subjectSelected.getCriterias());
        criteriaListView.setAdapter(criteriaAdapter);

        //Set Subject Name, Average
        subjectName.setText(subjectSelected.getName());
        subjectAvg.setText(String.valueOf(subjectSelected.getAverage()));

        //Click Action for an item in list
        criteriaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * Opens Criteria Activity and display its details
                 */
            }
        });
    }

    public void addCriteriaButton(View view) {
        /**
         * Button to add a new criteria to the list
         * Creates a dialog from a layout
         * Reads the name and % value of the criteria
         * If inout is valid adds the element to the list(criteria)
         * Updates the ListView
         * */

        AlertDialog.Builder builder = new AlertDialog.Builder(SubjectActivity.this);
        builder.setTitle(R.string.create_criterion_title_dialog);

        // inflates layout and mounts it to dialog
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.add_criterion, null, false);
        builder.setView(dialogView);

        //Create Button from dialog
        builder.setPositiveButton(R.string.create_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Initialize EditTexts
                EditText mName = (EditText) dialogView.findViewById(R.id.criterion_name_edit_text);
                EditText mValue = (EditText) dialogView.findViewById(R.id.criterion_value_edit_text);

                //Define Float Format
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);

                //validate not null object
                if ( mName.getText() == null || mValue.getText() == null) {
                    Toast.makeText(SubjectActivity.this, R.string.error_criterion_creation_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Get values from EditTexts
                String name = mName.getText().toString();

                //validate not empty string
                if (name.equals("")) {
                    Toast.makeText(SubjectActivity.this, R.string.enter_name_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mValue.getText().toString().equals("")) {
                    Toast.makeText(SubjectActivity.this, R.string.enter_value_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    String value = df.format(Float.valueOf(mValue.getText().toString()));

                    subjectSelected.getCriterias().add(new Criteria(name, Float.valueOf(value)));
                    criteriaAdapter.notifyDataSetChanged();
                    setAverage();

                    Toast.makeText(SubjectActivity.this, R.string.criterion_added_toast, Toast.LENGTH_SHORT).show();
                    return;

                }catch (NumberFormatException e){
                    Toast.makeText(SubjectActivity.this, "Enter a valid decimal number for value", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //Cancel button from dialog
        builder.setNegativeButton(R.string.cancel_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /**
                 * Cancels criterion creation
                 */
                return;
            }
        });
        builder.show();

    }

    public void setAverage() {

    }
}
