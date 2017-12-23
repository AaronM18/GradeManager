package com.co.aaron.grademanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;

/**
 * Created by aaron
 *
 * Main activity
 * Main activity shows the list of subjects saved
 * Displays the total average of all the subjects
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<Subject> subjects;    // list of subjects
    private float average;                  // Average of all the active subjects

    private ListView subjectListView;       // ListView for subjects
    private  SubjectAdapter subjectAdapter; // Adapter for subjects

    private int auxIndex;                   //aux index


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //subjects = new ArrayList<Subject>();

        //Get subject for testing !!!
        subjects = test();
        setAverage();

        // Initialize the ListView and the adapter
        subjectAdapter = new SubjectAdapter(this, subjects);
        subjectListView = (ListView) findViewById(R.id.subject_list_view);
        subjectListView.setAdapter(subjectAdapter);

        // Click Action for a click on an item of the list
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * Opens new activity where details fo the subject are displayed
                 */
                final int result = 1;
                Intent openSubjectDetail = new Intent(MainActivity.this, SubjectActivity.class);
                startActivityForResult(openSubjectDetail, result);
            }
        });

        //Long Click Action for a click on an item on the list
        subjectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * Displays a dialog where the name can be edited or the subject deleted
                 */
                auxIndex  = i;  //Saves the index of the selected element for later use

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.modify_subject_title_dialog);

                // inflates layout and mounts it to dialog
                final Context context = builder.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.subject_modify, null, false);
                builder.setView(dialogView);

                //Set the name of the subject to EditText
                final EditText newSubjectName = (EditText) dialogView.findViewById(R.id.modify_subject_dame_edit_text);
                Subject test  = (Subject) adapterView.getItemAtPosition(auxIndex);
                newSubjectName.setText(test.getName());

                //Save button from dialogs
                builder.setPositiveButton(R.string.save_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**
                         * Saves the new name for a subject
                         */
                        if (newSubjectName.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, R.string.enter_name_toast, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        subjects.get(auxIndex).setName(newSubjectName.getText().toString());
                        subjectAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, R.string.name_changed_toast, Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                //Negative button from dialog
                builder.setNegativeButton(R.string.delete_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**
                         * Deletes the selected subject from the list
                         */

                        subjects.remove(auxIndex);
                        subjectAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, R.string.subject_deleted_toast, Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
                return true;
            }
        });
    }

    public void addSubjectButton(View view) {
        /**
         *Button to add a new subject to the list
         * Creates a dialog from a layout
         * Reads the name input for the new subject
         * If inout is valid adds the element to the list(subjects)
         * Updates the ListView
         * */

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.create_subject_dialog_title);

        // inflates layout and mounts it to dialog
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.add_subject, null, false);
        builder.setView(dialogView);

        //Create Button from dialog
        builder.setPositiveButton(R.string.create_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                EditText mName = (EditText) dialogView.findViewById(R.id.subject_name_edit_text);

                //validate not null object
                if ( mName.getText() == null) {
                    Toast.makeText(MainActivity.this, R.string.error_creation_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = mName.getText().toString();

                //validate not empty string
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, R.string.enter_name_toast, Toast.LENGTH_SHORT).show();
                    return;
                }

                subjects.add(new Subject(name));
                subjectAdapter.notifyDataSetChanged();
                setAverage();

                Toast.makeText(MainActivity.this, R.string.subject_added_toast, Toast.LENGTH_SHORT).show();
                return;
            }
        });

        //Cancel button from dialog
        builder.setNegativeButton(R.string.cancel_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /**
                 * Cancels subject creation
                 */
                return;
            }
        });
        builder.show();
    }

    public ArrayList<Subject> test() {
        /**
         * Method for testing the app
         * Fills subjects with subjects
         * !!!!! Wont be included in final version !!!!
         */
        int i;
        Subject item;
        ArrayList<Subject> list = new ArrayList<Subject>();

        for (i = 0; i < 6; i++) {
            list.add(new Subject("Subject " + String.valueOf(i), 6.0f));
        }

        return list;
    }

    public void setAverage(){

        int i;
        float sum = 0.0f;
        final TextView averageTextView;

        if (subjects.size() == 0) {
            return;
        }

        //Defines decimal format
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        for (i = 0; i < subjects.size();i++){
            sum += subjects.get(i).getAverage();
        }

        sum = sum/subjects.size();

        averageTextView  = (TextView) findViewById(R.id.avgf_text_view);
        averageTextView.setText(df.format(sum));

        return;

    }

}
