package com.kutztown.projectmanagement.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.graphing.PieGraph;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        // Try to guarantee that yData.length == xData.length
        // This is dummy data that will be filled correctly later
        float[] yData = {
                0.7F, 15.7F, 16.9F,
                36.1F, 3.5F, 12.2F,
                9.0F, 2.7F, 3.0F, 0.2F
        };
        String[] xData = {
                "6.0 Marshmallow",
                "5.1 Lollipop",
                "5.0 Lollipop",
                "4.4 KitKat",
                "4.3 Jelly Bean",
                "4.2.x Jelly Bean",
                "4.1.x Jelly Bean",
                "4.0.3-4 Ice Cream Sandwich",
                "2.3.3-7 Gingerbread",
                "2.2 Froyo",
        };


        PieGraph chart = new PieGraph(
                xData,
                yData,
                (RelativeLayout) findViewById(R.id.PieChartLayout),
                this,
                3500);
        chart.setBackgroundColor("#CCFFFF");
        chart.setDescription("Relative Percent of Android OS Usage");

        // end of dummy data

        // Set title of chart
        TextView textView = (TextView) findViewById(R.id.ChartName);
        textView.setText("Android OS Usage Breakdown");

        // Build text arrays for list view output
        String[] legend = buildTexts(yData, xData);

        // TODO - Created a bullet listview with correct colors by asking graph for colors
        // TODO - Tutorial at this location:
        // TODO -   http://www.technotalkative.com/android-bullets-in-listview/

        // Display xData in list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.activity_progress_listview, legend);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public String[] buildTexts(float[] yData, String[] xData){
        String[] legend = new String[yData.length];
        for(int i = 0; i < legend.length; i++){
            legend[i] = Float.toString(yData[i]) + "% - " + xData[i];
        }
        return legend;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        onBackPressed();
        return true;

    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
    }
}
