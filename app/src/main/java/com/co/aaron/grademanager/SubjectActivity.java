package com.co.aaron.grademanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

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
    private int auxIndex;
    private int subjectIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        //Gets Subject from Intent
        Intent getSubject = getIntent();

        subjectIndex = (int) getSubject.getSerializableExtra("SUBJECT_INDEX");

        //subjectSelected = (Subject) getSubject.getSerializableExtra("SUBJECT");
        subjectSelected = MainActivity.subjects.get(subjectIndex);

        //Initialize Subject Name, Average, & Adapter & ListView
        TextView subjectName = findViewById(R.id.subject_name_text_view);
        final TextView subjectAvg = findViewById(R.id.subject_avg_text_view);

        criteriaListView = findViewById(R.id.criteria_list_view);
        criteriaAdapter = new CriteriaAdapter(this, MainActivity.subjects.get(subjectIndex).getCriterias());
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

                final int result = 1;
                auxIndex = i;

                Intent sendCriteria = new Intent(SubjectActivity.this, CriterionActivity.class);

                sendCriteria.putExtra("SUBJECT_INDEX", subjectIndex);
                sendCriteria.putExtra("CRITERIA_INDEX", auxIndex);
                /*
                Criteria criteria =  subjectSelected.getCriterias().get(i);

                sendCriteria.putExtra("CRITERIA", (Serializable) criteria);*/

                startActivityForResult(sendCriteria, result);
            }
        });

        //Long Click Action to modify or delete an item
        criteriaListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * Displays a dialog where the name can be edited or the subject deleted
                 */
                auxIndex  = i;  //Saves the index of the selected element for later use

                //Defines float format
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);

                //Initialize Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SubjectActivity.this);
                builder.setTitle(R.string.modify_subject_title_dialog);

                // Inflates layout and mounts it to dialog
                final Context context = builder.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.criteria_modify, null, false);
                builder.setView(dialogView);

                //Set the name of the subject to EditText
                final EditText newCriteriaName = dialogView.findViewById(R.id.modify_criteria_name_edit_text);
                final EditText newCriteriaValue = dialogView.findViewById(R.id.modify_criteria_value_edit_text);

                Criteria selectedCriteria  = (Criteria) adapterView.getItemAtPosition(auxIndex);
                newCriteriaName.setText(selectedCriteria.getName());
                newCriteriaValue.setText(df.format(selectedCriteria.getValue()));

                //Save button
                builder.setPositiveButton(R.string.save_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**
                         * Updates name and value of the criterion
                         */

                        //Define Float Format
                        DecimalFormat df = new DecimalFormat("#.###");
                        df.setRoundingMode(RoundingMode.CEILING);

                        //Validates empty name
                        if (newCriteriaName.getText().toString().equals("")){
                            Toast.makeText(SubjectActivity.this, R.string.enter_name_toast, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Validates empty value
                        if (newCriteriaValue.getText().toString().equals("")) {
                            Toast.makeText(SubjectActivity.this, R.string.enter_value_toast, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Validates value and updates elements
                        try {

                            float aux = Float.valueOf(newCriteriaValue.getText().toString());

                            //Updates name, value and points
                             /* subjectSelected.getCriterias().get(auxIndex).setValue(Float.valueOf(df.format(aux)));
                            subjectSelected.getCriterias().get(auxIndex).setName(newCriteriaName.getText().toString());
                            subjectSelected.getCriterias().get(auxIndex).setPoints();*/

                            MainActivity.subjects.get(subjectIndex).getCriterias().get(auxIndex).setValue(Float.valueOf(df.format(aux)));
                            MainActivity.subjects.get(subjectIndex).getCriterias().get(auxIndex).setName(newCriteriaName.getText().toString());
                            MainActivity.subjects.get(subjectIndex).getCriterias().get(auxIndex).setPoints();
                            criteriaAdapter.notifyDataSetChanged();
                            saveContent();


                        }catch (NumberFormatException e){
                            Toast.makeText(SubjectActivity.this, "Enter a valid decimal number for value", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                builder.setNegativeButton(R.string.delete_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**
                         * Deletes the selected criteria from the list
                         */
                        //subjectSelected.getCriterias().remove(auxIndex);
                        MainActivity.subjects.get(subjectIndex).getCriterias().remove(auxIndex);
                        criteriaAdapter.notifyDataSetChanged();
                        Toast.makeText(SubjectActivity.this, R.string.subject_deleted_toast, Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();

                return true;
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
                EditText mName = dialogView.findViewById(R.id.criterion_name_edit_text);
                EditText mValue = dialogView.findViewById(R.id.criterion_value_edit_text);

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

                    MainActivity.subjects.get(subjectIndex).getCriterias().add(new Criteria(name, Float.valueOf(value)));
                    //subjectSelected.getCriterias().add(new Criteria(name, Float.valueOf(value)));
                    criteriaAdapter.notifyDataSetChanged();
                    setAverage();

                    Toast.makeText(SubjectActivity.this, R.string.criterion_added_toast, Toast.LENGTH_SHORT).show();
                    saveContent();
                    return;

                }catch (NumberFormatException e){
                    Toast.makeText(SubjectActivity.this, R.string.valid_value_toast, Toast.LENGTH_SHORT).show();
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

    public void setAverage(){

        int i;
        float sum = 0.0f;
        final TextView averageTextView;

        if (subjectSelected.getCriterias().size() == 0) {
            averageTextView  = findViewById(R.id.subject_avg_text_view);
            averageTextView.setText("00.00");
            return;
        }

        //Defines decimal format
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        for (i = 0; i < subjectSelected.getCriterias().size();i++){
            sum += subjectSelected.getCriterias().get(i).getPoints();
        }

        subjectSelected.setAverage(sum);

        averageTextView  = findViewById(R.id.subject_avg_text_view);
        averageTextView.setText(df.format(sum));

        return;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * Gets the selected object and replace it on the list to save modifications
         */


        //Updates the subject in the list
        criteriaAdapter.notifyDataSetChanged();

        //Gets the average TextView for the selected element
        TextView points = getViewByPosition(auxIndex, criteriaListView).findViewById(R.id.criteria_points_text_view);

        //Defines decimal format
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        //Updates TextView
        points.setText(df.format( MainActivity.subjects.get(subjectIndex).getCriterias().get(auxIndex).getPoints()));

        //Updates total average
        setAverage();

        return;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
