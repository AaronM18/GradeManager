package com.co.aaron.grademanager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aaron on 20/12/2017.
 *
 * Represents a Subject
 *
 * Contents:
 * Name
 * Average (from the criteria and assignments)
 * List of Criteria
 *
 * Main methods:
 * Modify contents
 * Get the average
 */

public class Subject implements Serializable {

    private String name;
    private float average;
    private ArrayList<Criteria> criterias;

    public Subject(String name) {
        this.name = name;
        this.average = 0.0f;
        this.criterias = new ArrayList<>();
    }

    public Subject(String name, Float average) {
        // !!! Only for testing !!!
        this.name = name;
        this.average = average;
        this.criterias = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public ArrayList<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(ArrayList<Criteria> criterias) {
        this.criterias = criterias;
    }

    @Override
    public String toString() {
        return getName();
    }
}
