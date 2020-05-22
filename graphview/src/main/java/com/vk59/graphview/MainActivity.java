package com.vk59.graphview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.vk59.graphview.test.CurrentTest;
import com.vk59.graphview.test.MomentTest;
import com.vk59.graphview.test.simulation.TestSimulation;
import com.vk59.graphview.test.simulation.TestSimulationCallback;
import com.vk59.graphviewlibrary.GraphData;
import com.vk59.graphviewlibrary.GraphView;
import com.vk59.graphviewlibrary.Moment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements TestSimulationCallback {
    private TestSimulation testSimulation;

    private GraphData graphData;

    private GraphData graphTP;
    private GraphData graphTC;
    private GraphData graphPC;
    private GraphView graphView;
    private int currentAxes = 0;

    private RadioGroup switcherAxises;
    private Button buttonStop;
    private Button buttonStart;
    private DataView dataView;

    private int testIndex = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActivity();
    }

    @Override
    protected void onStop() {
        testSimulation.stopSimulation();
        CurrentTest.results.clear();
        super.onStop();
    }

    private void initializeActivity() {
        initializeViews();
        customizeActivity();
        customizeGraphView();
    }

    private void initializeViews() {
        switcherAxises = findViewById(R.id.radio_group);
        buttonStop = findViewById(R.id.buttonStop);
        buttonStart = findViewById(R.id.buttonStart);
        graphView = findViewById(R.id.graphView);
        dataView = findViewById(R.id.dataView);
        int color = Color.rgb(61, 165, 244);
        graphPC = new GraphData(new ArrayList<Moment>(), color, "Sinusoid");
        graphTC = new GraphData(new ArrayList<Moment>(), color, "Sinusoid");
        graphTP = new GraphData(new ArrayList<Moment>(), color, "Sinusoid");
        graphData = graphTP;
    }

    private void customizeActivity() {
        buttonStop.setOnClickListener(onClickStop);
        buttonStart.setOnClickListener(onClickStart);
        switcherAxises.setOnCheckedChangeListener(onSwitchAxises);
    }

    private View.OnClickListener onClickStart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startSimulation();
        }
    };

    private View.OnClickListener onClickStop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            testSimulation.stopSimulation();
            dataView.clear();
            graphView.clear();
        }
    };

    private RadioGroup.OnCheckedChangeListener onSwitchAxises = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioGraphPT:
                currentAxes = 0;
                break;
            case R.id.radioGraphCT:
                currentAxes = 1;
                break;
            case R.id.radioGraphCP:
                currentAxes = 2;
                break;
            case R.id.radioNumbers:
                currentAxes = 3;
                break;
        }
        setLabelAxises();
        drawChart();
        }
    };

    private void startSimulation() {
        testSimulation = new TestSimulation();
        testSimulation.startSimulation(this, this, testIndex);
    }

    @Override
    public void onGetSimulationData(MomentTest testData) {
        prepareNewData(testData);
        drawChart();
    }

    private void prepareNewData(MomentTest testData) {
        Log.d("PREPARING", "added" + testData.getTime() + " " +  testData.getVoltage());
        graphTP.addData(testData.getTime(), testData.getVoltage());
        graphTC.addData(testData.getTime(), testData.getAmperage());
        graphPC.addData(testData.getVoltage(), testData.getAmperage());
        CurrentTest.results.add(testData);
    }

    private void customizeGraphView() {
        graphView.setLegendEnable(true);
        graphView.addGraphData(graphData);
        dataView.addGraphData(graphData);
        setLabelAxises();
    }

    private void drawChart() {
        if (currentAxes == 3) {
            dataView.drawData();
        }
        else {
            graphView.drawGraph();
        }
    }

    private void setLabelAxises() {
        String labelXAxis = "";
        String labelYAxis = "";
        switch (currentAxes) {
            case 0:
                graphData = graphTP;
                labelXAxis = getString(R.string.chartAxisTime);
                labelYAxis = getString(R.string.chartAxisPotential);
                graphView.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.INVISIBLE);
                graphView.clear();
                graphView.addGraphData(graphData);
                graphView.setAxisName(labelXAxis, labelYAxis);
                break;
            case 1:
                graphData = graphTC;
                labelXAxis = getString(R.string.chartAxisTime);
                labelYAxis = getString(R.string.chartAxisCurrent);
                graphView.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.INVISIBLE);
                graphView.clear();
                graphView.addGraphData(graphData);
                graphView.setAxisName(labelXAxis, labelYAxis);
                break;
            case 2:
                graphData = graphPC;
                labelXAxis = getString(R.string.chartAxisPotential);
                labelYAxis = getString(R.string.chartAxisCurrent);
                graphView.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.INVISIBLE);
                graphView.clear();
                graphView.addGraphData(graphData);
                graphView.setAxisName(labelXAxis, labelYAxis);
                break;
            case 3:
                graphData = graphTP;
                graphView.setVisibility(View.INVISIBLE);
                dataView.setVisibility(View.VISIBLE);
                dataView.clear();
                dataView.addGraphData(graphData);
        }
        drawChart();
    }
}
