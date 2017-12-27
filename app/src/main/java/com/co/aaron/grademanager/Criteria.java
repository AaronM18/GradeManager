package com.co.aaron.grademanager;

import java.io.Serializable;
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

public class Criteria implements Serializable{

    private String name;
    private float value;   //Percentage value
    private float points;  //Total points of the criterion
    private ArrayList<Assignment> assignments;

    public Criteria(String name, float value) {
        this.name = name;
        this.value = value;
        this.points = 0;
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float grade) {
        this.value = grade;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints() {

        int i;
        float sum = 0;

        if (this.assignments.isEmpty()){
            this.points = 0;
            return;
        }

        for (i = 0; i < this.assignments.size(); i++) {
            sum += this.assignments.get(i).getGrade();
        }
        sum = sum/this.assignments.size();

        this.points = sum*this.value;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }
}
