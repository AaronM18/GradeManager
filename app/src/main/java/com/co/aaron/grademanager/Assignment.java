package com.co.aaron.grademanager;

import java.io.Serializable;

/**
 * Created by aaron on 20/12/2017.
 *
 * Represents ans assignment for a subject
 * Is classified in a criteria
 *
 * Contents:
 * Name
 * Grade
 * Date of delivery
 *
 * Main Methods:
 * Modify contents
 */

public class Assignment implements Serializable{

    private String name;
    private float grade;
    private String deliverDate;

    public Assignment(String name, float grade, String deliverDate) {
        this.name = name;
        this.grade = grade;
        this.deliverDate = deliverDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    float getGrade() {
        return grade;
    }

    void setGrade(float grade) {
        this.grade = grade;
    }

    String getDeliverDate() {
        return deliverDate;
    }

    void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }
}
