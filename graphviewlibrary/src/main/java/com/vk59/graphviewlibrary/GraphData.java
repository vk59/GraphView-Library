package com.vk59.graphviewlibrary;


import android.util.Log;

import java.util.ArrayList;

public class GraphData {
    private int color;
    private String label;
    private ArrayList<Moment> data;
    private float minX = Float.MAX_VALUE;
    private float maxX = -Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float maxY = -Float.MAX_VALUE;

    public GraphData(ArrayList<Moment> data, int color, String label) {
        setData(data);
        setColor(color);
        setLabel(label);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setData(ArrayList<Moment> data) {
        this.data = data;
        for (Moment moment : data) {
            float x = moment.getX();
            float y = moment.getY();
            minX = Math.min(x, minX);
            maxX = Math.max(x, maxX);
            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);
        }
    }

    public void addData(float x, float y) {
        this.data.add(new Moment(x, y));
        minX = Math.min(x, minX);
        maxX = Math.max(x, maxX);
        minY = Math.min(y, minY);
        maxY = Math.max(y, maxY);
    }

    public ArrayList<Moment> getData() {
        return data;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
