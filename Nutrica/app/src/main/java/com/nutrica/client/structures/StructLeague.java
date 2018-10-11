package com.nutrica.client.structures;

/**
 * Created by root on 7/9/2017.
 */

public class StructLeague {

    public StructLeague()
    {

    }

    public StructLeague(int id, String name)
    {
        this.setId(id);
        this.setName(name);
    }

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
