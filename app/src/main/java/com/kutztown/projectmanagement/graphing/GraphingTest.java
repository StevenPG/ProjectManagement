package com.kutztown.projectmanagement.graphing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.kutztown.project.projectmanagement.R;

/**
 * This class is a test class for performing 3 tasks
 *
 * First: To implement a generic pie chart from a tutorial
 * Second: To help build and test an API for building the pie chart
 * Third: To allow testing of specific data in the Pie graph
 */
public class GraphingTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing_test);

        // Try to guarantee that yData.length == xData.length
        float[] yData = { 5, 10, 15, 30, 40 };
        String[] xData = { "Sony", "Huawei", "LG", "Apple", "Samsung" };

        PieGraph chart = new PieGraph(xData, yData,
                (RelativeLayout) findViewById(R.id.layout),
                this, "Market Average of Phones");
        chart.setBackgroundColor("#CCFFFF");
        chart.setDescription("Market Average of Cell Phones");
    }
}
