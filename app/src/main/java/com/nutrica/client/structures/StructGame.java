package com.nutrica.client.structures;

/**
 * Created by root on 6/14/2017.
 */

public class StructGame {

    private int id;
    private String name;

    public StructGame() {}

    public StructGame(int id, String name)
    {
        this.setId(id);
        this.setName(name);
    }

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
