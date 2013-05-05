package com.manuelpeinado.multichoiceadapter.demo;

import java.util.ArrayList;

import android.content.Context;

public class Building {
    public String name;
    public String height;

    public Building(String name, String height) {
        this.name = name;
        this.height = height;
    }

    public static ArrayList<Building> createList(Context ctx) {
        String[] names = ctx.getResources().getStringArray(R.array.names);
        String[] heights = ctx.getResources().getStringArray(R.array.heights);
        ArrayList<Building> items = new ArrayList<Building>(names.length);
        for (int i = 0; i < names.length; ++i) {
            items.add(new Building(names[i], heights[i]));
        }
        return items;
    }
}
