package com.vk59.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vk59.graphviewlibrary.GraphData;
import com.vk59.graphviewlibrary.Moment;

import java.util.ArrayList;

public class DataView extends View {
    private ArrayList<GraphData> allGraphData = new ArrayList<>();
    private ArrayList<ArrayList<Moment>> moments = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    private float screenWidth;
    private float screenHeight;

    public DataView(Context context) {
        super(context);
    }

    public DataView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void addGraphData(GraphData data) {
        try {
            moments.add(data.getData());
            colors.add(data.getColor());
            labels.add(data.getLabel());
            allGraphData.add(data);
        } catch (NullPointerException e) {

        }
    }

    public void addMomentToGraphData(Moment moment) {
        allGraphData.get(moments.size() - 1).getData().add(moment);
        moments.get(moments.size() - 1).add(moment);
        invalidate();
    }

    public void drawData() {
        invalidate();
    }

    public long getCountGraphs() {
        return moments.size();
    }

    public void removeGraphData(int index) {
        moments.remove(index);
        allGraphData.remove(index);
        colors.remove(index);
        labels.remove(index);
        invalidate();
    }

    public void clear() {
        allGraphData.clear();
        moments.clear();
        colors.clear();
        labels.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("ON DRAW", "draw numbers");
        canvas.drawColor(Color.WHITE);
        drawNumbers(canvas);
    }

    private void drawNumbers(Canvas canvas) {
        try {
            screenWidth = canvas.getWidth();
            screenHeight = canvas.getHeight();
            Paint textPaint = new Paint();
            textPaint.setTextSize(10);
            textPaint.setColor(Color.BLACK);
            long i = 0;
            long j = 0;
            for (ArrayList<Moment> graph : moments) {
                for (Moment moment : graph) {
                    canvas.drawText(Float.toString(moment.getX()), j * 30, i * 15, textPaint);
                    i++;
                    if (i * 15 > screenHeight) {
                        j++;
                        i = 1;
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}