package com.nutrica.client.structures;

import java.util.ArrayList;

/**
 * Created by root on 6/14/2017.
 */

public class StructMultipleData {

    private int id;
    private ArrayList<String> data;

    public StructMultipleData() {
    }

    public StructMultipleData(int id, ArrayList<String> data) {
        this.setId(id);
        this.setData(data);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
