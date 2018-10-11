package com.nutrica.client.structures;

/**
 * Created by root on 6/11/2017.
 */

public class StructFoodReport {

    private int id;
    private String name;
    private String food;
    private int count;

    public StructFoodReport() {
    }

    public StructFoodReport(int id, String name, String food, int count) {
        this.setId(id);
        this.setName(name);
        this.setFood(food);
        this.setCount(count);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
