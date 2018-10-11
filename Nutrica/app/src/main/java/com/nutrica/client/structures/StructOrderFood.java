package com.nutrica.client.structures;

/**
 * Created by root on 6/11/2017.
 */

public class StructOrderFood {

    private int id;
    private String name;
    private int price;
    private int count;

    public StructOrderFood() {
    }

    public StructOrderFood(int id, String name, int price, int count)
    {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setCount(count);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
