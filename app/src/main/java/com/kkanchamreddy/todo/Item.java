package com.kkanchamreddy.todo;

/**
 * Created by kkanchamreddy on 11/09/2015.
 */
public class Item {
    public String text;
    public String id;
    public String priority;

    public Item(String id, String text, String priority) {
        this.id = id;
        this.text = text;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return text;
    }

}
