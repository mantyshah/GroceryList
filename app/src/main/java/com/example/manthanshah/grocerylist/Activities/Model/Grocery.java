package com.example.manthanshah.grocerylist.Activities.Model;

/**
 * Created by Manthan.Shah on 18-12-2017.
 */

public class Grocery {

    private String name;
    private String quatity;
    private String dateItemAdded;
    private int id;

    public Grocery() {

    }

    public Grocery(String name, String quatity, String dateItemAdded, int id) {
        this.name = name;
        this.quatity = quatity;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuatity() {
        return quatity;
    }

    public void setQuatity(String quatity) {
        this.quatity = quatity;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
