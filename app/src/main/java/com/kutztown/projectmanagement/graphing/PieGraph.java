package com.kutztown.projectmanagement.graphing;

// Android imports
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;

// Import library
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

// Java imports
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Steven Gantz on 1/28/2016.
 *
 * This class will encapsulate the creation
 * of a pie graph using the open source library
 * https://github.com/PhilJay/MPAndroidChart
 *
 * Tutorial used: http://www.ssaurel.com/blog/learn-to-
 *  create-a-pie-chart-in-android-with-mpandroidchart/
 *
 * It is added to the project using Gradle.
 */
public class PieGraph {

    // Attributes
    /**
     * The label for each pieces of the pie graph
     */
    private String[] xData;

    /**
     * The values assigned to each label of the pie graph
     */
    private float[] yData;

    /**
     * General layout for controlling
     */
    private RelativeLayout mainLayout;

    /**
     * Main graph pointer
     */
    private PieChart chart;

    /**
     * How long the animation should last
     */
    private int animationLength;

    /**
     * Calling activity context
     */
    protected static Context context;

    /**
     * This class takes a list of data points
     * and their corresponding labels as its
     * input and generates a pie graph using
     * a third party library.
     * @param xData String array of x values (Can be null)
     * @param yData float array of y values (cannot be null)
     * @param layout main activity layout object
     */
    public PieGraph(String[] xData, float[] yData, RelativeLayout layout,
             Context context, int animationLength) {
        PieGraph.context = context;
        this.animationLength = animationLength;

        // Assign values
        this.mainLayout = layout;
        this.chart = new PieChart(context);

        // Get dimensions
        WindowManager windowManager =
                (WindowManager) PieGraph.context.getSystemService(
                    Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        // add chart to main layout
        layout.addView(this.chart, display.getHeight(), display.getWidth());

        // Assign the values
        this.xData = xData;
        this.yData = yData;

        // configure chart values
        this.chart.setUsePercentValues(true);

        // enable and configure hole
        this.chart.setDrawHoleEnabled(true);
        this.chart.setHoleColorTransparent(true);
        this.chart.setHoleRadius(30);
        this.chart.setTransparentCircleRadius(10);

        // enable rotation by touch
        this.chart.setRotationAngle(0);
        this.chart.setRotationEnabled(true);

        // set a chart value selected listener
        this.chart.setOnChartValueSelectedListener(
                new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry entry, int i, Highlight highlight) {
                    }
                    @Override
                    public void onNothingSelected() {
                    }
                }
        );

        // Add general data
        addChartData();

        // customize legends
        this.chart.getLegend().setEnabled(false);
    }

    /**
     * Sets the background color of the layout
     * @param colorHex hex representation of desired background color
     */
    public void setBackgroundColor(String colorHex){
        this.mainLayout.setBackgroundColor(
                Color.parseColor(colorHex));
    }

    /**
     * Set the chart description
     * @param description chart description
     */
    public void setDescription(String description){
        this.chart.setDescription(description);
    }

    private void addChartData() {

        // Add the y value data directly
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < yData.length; i++) {
            yVals.add(new Entry(this.yData[i], i));
        }

        // Add the x value data directly (can be null)
        ArrayList<String> xVals = new ArrayList<>();
        Collections.addAll(xVals, xData);

        // create the pie data set
        PieDataSet dataSet = new PieDataSet(yVals, "Pie Chart Legend");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(10);
        dataSet.setValueTextSize(42f);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.GRAY);

        this.chart.setData(data);

        this.chart.animateXY(this.animationLength,
                this.animationLength);

        // undo all highlights
        this.chart.highlightValues(null);

        // update pie chart
        this.chart.invalidate();
    }
}
