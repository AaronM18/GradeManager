package com.co.aaron.grademanager;

import java.util.ArrayList;

/**
 * Created by aaron on 20/12/2017.
 *
 * Represents a Criteria from a Subject
 *
 * Contents:
 * Name
 * Grade (average from assignment grades)
 * List of assignments
 *
 * Main Methods:
 * Modify contents
 * Get grade
 */

public class Criteria {

    private String name;
    private float grade;
    private ArrayList<Assignment> assignments;

    public Criteria(String name) {
        this.name = name;
        this.grade = 0.0f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }
}
