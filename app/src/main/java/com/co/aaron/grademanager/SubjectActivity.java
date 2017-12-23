package com.co.aaron.grademanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by aaron on 23/12/2017.
 *
 * Displays the contents of a subject:
 * Subject Name
 * Subject Average
 * Subjects criteria with its average
 */

public class SubjectActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);


    }

    public void addCriteriaButton(View view) {
    }
}
