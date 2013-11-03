package com.manuelpeinado.multichoiceadapter.samples;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import com.manuelpeinado.multichoiceadapter.samples.stock.R;
import com.manuelpeinado.multichoiceadapter.samples.stock.R.array;

public class Building {
    public String name;
    public String height;
    public Drawable photo;

    public Building(String name, String height, Drawable photo) {
        this.name = name;
        this.height = height;
        this.photo = photo;
    }

    public static ArrayList<Building> createList(Context ctx) {
        Resources res = ctx.getResources();
        String[] names = res.getStringArray(R.array.names);
        String[] heights = res.getStringArray(R.array.heights);
        TypedArray icons = res.obtainTypedArray(R.array.photos);
        ArrayList<Building> items = new ArrayList<Building>(names.length);
        for (int i = 0; i < names.length; ++i) {
            items.add(new Building(names[i], heights[i], icons.getDrawable(i)));
        }
        icons.recycle();
        return items;
    }
}
