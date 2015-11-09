package com.kkanchamreddy.todo;

/**
 * Created by kkanchamreddy on 11/09/2015.
 */
public class Item {
    public String text;
    public String id;

    public Item(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
