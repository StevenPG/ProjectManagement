package com.kutztown.projectmanagement.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);

        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn) {
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        // Display project progress based on Appdata.currentProject
        displayProjectProgress();
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
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, ApplicationData.amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);

    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
    }

    /**
     * This method displays the current project's progress
     */
    protected void displayProjectProgress(){
        // Try to guarantee that yData.length == xData.length
        // This is dummy data that will be filled correctly later

        float progress = 0;

        String projectProgress = ApplicationData.currentProject.getProjectProgress();
        if("u''".equals(projectProgress)){
            projectProgress = String.valueOf(0);
        }
        if("".equals(projectProgress)){
            progress = 0;
        } else {
            progress = Float.parseFloat(projectProgress);
        }

        float[] yData = {
                (float) progress, (float) (100 - progress)
        };

        String[] xData = {
                "Current project progress",
                "Remaining progress"
        };

        PieGraph chart = new PieGraph(
                xData,
                yData,
                (RelativeLayout) findViewById(R.id.PieChartLayout),
                this,
                3500);
        chart.setBackgroundColor("#CCFFFF");
        chart.setDescription("Total progress");

        // end of dummy data

        // Set title of chart
        TextView textView = (TextView) findViewById(R.id.ChartName);
        String projectName = ApplicationData.currentProject.getProjectName();
        if("".equals(projectName) || projectName == null){
            projectName = "Error in Project Name";
        }
        textView.setText(projectName + " Progress");

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
}
