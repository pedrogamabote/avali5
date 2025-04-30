package com.example.avali5;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<String> items;

    public Category(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public Category(String name, List<String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }
    public void setItems(List<String> items) {
        this.items = items;
    }
}
