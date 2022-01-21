package com.example.textadventure;

import android.util.Log;

import java.util.List;

public class Item {
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

    private int id;
    private String name;

    public Item(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
    }

}
