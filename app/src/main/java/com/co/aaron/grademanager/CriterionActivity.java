package com.co.aaron.grademanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

/**
 * Created by aaron on 27/12/2017.
 */

public class CriterionActivity extends Activity {

    private Criteria criteriatSelected;
    private AssignmentAdapter assignmentAdapter;
    private ListView assignmentListView;
    private int auxIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria);


    }
}
