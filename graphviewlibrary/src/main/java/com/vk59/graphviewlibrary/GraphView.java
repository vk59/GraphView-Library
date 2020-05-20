package com.vk59.graphviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class GraphView extends View {
    private ArrayList<GraphData> allGraphData = new ArrayList<>();
    private ArrayList<ArrayList<Moment>> moments = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    private boolean isInitialized = false;
    private boolean isLegendEnable = true;

    private String xName = "";
    private String yName = "";

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    private float screenWidth;
    private float screenHeight;
    private float padding;
    private float paddingY;
    private float paddingLegend;
    private float paddingBottom;

    private int countOfDivisions;

    private float stepGridX;
    private float stepGridY;


    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void minMaxInit(GraphData data) {
        if (!isInitialized) {
            minX = data.getMinX();
            maxX = data.getMaxX();
            minY = data.getMinY();
            maxY = data.getMaxY();
            isInitialized = true;
        } else {
            if (minX > data.getMinX()) minX = data.getMinX();
            if (maxX < data.getMaxX()) maxX = data.getMaxX();
            if (minY > data.getMinY()) minY = data.getMinY();
            if (maxY < data.getMaxY()) maxY = data.getMaxY();
        }
    }

    public void addGraphData(GraphData data) {
        try {
            moments.add(data.getData());
            colors.add(data.getColor());
            labels.add(data.getLabel());
            allGraphData.add(data);
            minMaxInit(data);
        } catch (NullPointerException e) {

        }
    }

    public void addMomentToGraphData(Moment moment) {
        allGraphData.get(moments.size() - 1).getData().add(moment);
        moments.get(moments.size() - 1).add(moment);
        invalidate();
    }

    public void drawGraph() {
        invalidate();
    }

    public void setLegendEnable(boolean legendEnable) {
        this.isLegendEnable = legendEnable;
    }

    public void setAxisName(String xName, String yName) {
        this.xName = xName;
        this.yName = yName;
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
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {
        try {
            canvas.drawColor(Color.WHITE);
            if (moments != null && moments.size() > 0) {
                getMinMaxValues();
                setScreenSettings(canvas);
                drawGrid(canvas);
                drawText(canvas);
                drawNamesOfAxis(canvas);
                drawGraphs(canvas);
                if (isLegendEnable) {
                    drawLegend(canvas);
                }
            } else {
                setNoDataText(canvas);
            }
        } finally {
        }
    }

    private void drawLegend(Canvas canvas) {
        float paddingForLabel = paddingLegend / moments.size();
        float sideOfSquare = 0.9f * paddingForLabel;
        float textSize = 0.9f * paddingForLabel;
        for (int i = 0; i < moments.size(); i++) {
            Paint legendRectanglePaint = new Paint();
            legendRectanglePaint.setColor(colors.get(i));
            int bottom = (int) (screenHeight + paddingY + padding + paddingForLabel * (i + 1));
            int left = (int) (padding * 1.5);
            int top = (int) (bottom - sideOfSquare);
            int right = (int) (left + sideOfSquare);
            Rect square = new Rect(left, top, right, bottom);
            canvas.drawRect(square, legendRectanglePaint);

            Paint legendTextPaint = new Paint();
            legendTextPaint.setTextSize(textSize);
            legendTextPaint.setColor(getResources().getColor(R.color.text));
            int startOfTextX = (int) (right + sideOfSquare * 0.5f);
            int startOfTextY = (int) (bottom);
            canvas.drawText(labels.get(i), startOfTextX, startOfTextY, legendTextPaint);
        }
    }

    private void setScreenSettings(Canvas canvas) {
        padding = (float) 0.025 * (canvas.getWidth() + canvas.getHeight());
        paddingY = padding * 0.5f;
        paddingLegend = 0;

        if (isLegendEnable) {
            paddingLegend += moments.size() * 0.5f * padding;
        }

        paddingBottom = padding * 0.5f;
        screenWidth = canvas.getWidth() - 2 * padding;
        screenHeight = canvas.getHeight() - padding - paddingY - max(paddingLegend, padding) -
                paddingBottom;

        countOfDivisions = 5;
    }

    private void drawGraphs(Canvas canvas) {
        float stepX = screenWidth / (maxX - minX);
        float stepY = screenHeight / (maxY - minY);

        Paint paintGraph = new Paint();
        paintGraph.setStrokeWidth(3);
        paintGraph.setAntiAlias(true);

        int points = getMaximumOfSizes();
        for (int thisPoint = 0; thisPoint < points - 1; thisPoint++) {
            for (ArrayList<Moment> graph : moments) {
                if (graph.size() > thisPoint + 1) {
                    Moment moment = graph.get(thisPoint);
                    Moment nextMoment = graph.get(thisPoint + 1);

                    int graphNum = moments.indexOf(graph);
                    paintGraph.setColor(colors.get(graphNum));

                    float startX = (moment.getX() - minX) * stepX + padding;
                    float startY = normalY((moment.getY() - minY) * stepY)
                            - max(padding, paddingLegend) - paddingY - paddingBottom;
                    float stopX = (nextMoment.getX() - minX) * stepX + padding;
                    float stopY = normalY((nextMoment.getY() - minY) * stepY)
                            - max(padding, paddingLegend) - paddingY - paddingBottom;

                    canvas.drawLine(startX, startY, stopX, stopY, paintGraph);
                }
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        Paint paintGrid = new Paint();
        paintGrid.setColor(getResources().getColor(R.color.grid));
        paintGrid.setStrokeWidth(1f);
        paintGrid.setAntiAlias(true);

        stepGridX = screenWidth / countOfDivisions;
        stepGridY = screenHeight / countOfDivisions;
        for (int i = countOfDivisions; i >= 0; i--) {
            canvas.drawLine(padding, i * stepGridY + padding, padding + screenWidth,
                    padding + i * stepGridY, paintGrid);
            canvas.drawLine(padding + (countOfDivisions - i) * stepGridX,
                    padding, padding + (countOfDivisions - i) * stepGridX,
                    padding + screenHeight, paintGrid);
        }
    }

    private void drawText(Canvas canvas) {
        int textSize = (int) (padding * 0.5);
        Paint paintTextHorizontal = new Paint();
        paintTextHorizontal.setColor(getResources().getColor(R.color.text));
        paintTextHorizontal.setStrokeWidth(1f);
        paintTextHorizontal.setAntiAlias(true);
        paintTextHorizontal.setTextAlign(Paint.Align.CENTER);
        paintTextHorizontal.setTextSize(textSize);

        Paint paintTextVertical = new Paint();
        paintTextVertical.setColor(getResources().getColor(R.color.text));
        paintTextVertical.setStrokeWidth(1f);
        paintTextVertical.setAntiAlias(true);
        paintTextVertical.setTextSize(textSize);

        float cStepTextX = (maxX - minX) / 5;
        float cStepTextY = (maxY - minY) / 5;

        for (int i = countOfDivisions - 1; i > 0; i--) {

            float valueX = (countOfDivisions - i) * cStepTextX + minX;
            canvas.drawText(
                    roundFloat(valueX, cStepTextX),
                    (countOfDivisions - i) * stepGridX + padding,
                    screenHeight + padding + paddingY,
                    paintTextHorizontal);
            // Y Axis
            float valueY = (countOfDivisions - i) * cStepTextY + minY;
            canvas.drawText(
                    roundFloat(valueY, cStepTextY),
                    0 + padding,
                    i * stepGridY + padding,
                    paintTextVertical);
        }
    }

    private void drawNamesOfAxis(Canvas canvas) {
        int nameAxisSize = (int) ((int) padding * 0.7f);

        Paint paintAxis = new Paint();
        paintAxis.setColor(getResources().getColor(R.color.text));
        paintAxis.setStrokeWidth(1f);
        paintAxis.setAntiAlias(true);
        paintAxis.setTextAlign(Paint.Align.CENTER);
        paintAxis.setTextSize(nameAxisSize);

        // X
        canvas.drawText(xName,
                padding + screenWidth / 2,
                screenHeight + 2 * padding + paddingY * 0.5f,
                paintAxis);

        // Y
        float rotate_center_x = padding * 0.5f;
        float rotate_center_y = screenHeight / 2 + padding;
        float degrees = -90;

        canvas.rotate(degrees, rotate_center_x, rotate_center_y);
        canvas.drawText(yName,
                padding * 0.5f,
                screenHeight / 2 + padding,
                paintAxis);
        canvas.rotate(-degrees, rotate_center_x, rotate_center_y);
    }

    private void setNoDataText(Canvas canvas) {
        Paint p = new Paint();
        p.setTextSize(getWidth() * 0.1f);
        p.setColor(Color.BLACK);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("There is no data", getWidth() / 2, getHeight() / 2, p);
    }

    private float max(float a, float b) {
        if (a > b) return a;
        return b;
    }

    private int getMaximumOfSizes() {
        int maximumOfSizes = 0;
        for (ArrayList<Moment> data : moments) {
            if (maximumOfSizes < data.size()) {
                maximumOfSizes = data.size();
            }
        }
        return maximumOfSizes;
    }

    private float normalY(float y) {
        return getHeight() - y;
    }

    private String roundFloat(float a, float decimalPlaces) {
        int base = 0;
        String result = Float.toString(a);
        if (decimalPlaces > 0.0001f) {
            if (decimalPlaces < 1) {
                while (decimalPlaces < 10) {
                    decimalPlaces *= 10;
                    base++;
                }
            } else {
                if (decimalPlaces >= 1 && decimalPlaces < 10) {
                    base++;
                }
            }
            switch (base)
            {
                case 1:
                    result = String.format("%.1f", a);
                    break;
                case 2:
                    result = String.format("%.2f", a);
                    break;
                case 3:
                    result = String.format("%.3f", a);
                    break;
                case 4:
                    result = String.format("%.4f", a);
                    break;
                default:
                    result = String.format("%.0f", a);
                    break;
            }
        }
        return result;
    }

    private void getMinMaxValues() {
        maxX = -Float.MAX_VALUE;
        minX = Float.MAX_VALUE;
        maxY = -Float.MAX_VALUE;
        minY = Float.MAX_VALUE;
        for (GraphData graph : allGraphData) {
            if (graph.getMaxX() > maxX) maxX = graph.getMaxX();
            if (graph.getMinX() < minX) minX = graph.getMinX();
            if (graph.getMaxY() > maxY) maxY = graph.getMaxY();
            if (graph.getMinY() < minY) minY = graph.getMinY();
        }
    }
}