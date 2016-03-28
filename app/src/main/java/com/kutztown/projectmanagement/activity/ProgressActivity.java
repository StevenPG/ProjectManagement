package com.kutztown.projectmanagement.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.graphing.PieGraph;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.util.ArrayList;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      //  getSupportActionBar().setDisplayShowHomeEnabled(false);
        //getSupportActionBar().setTitle(null);

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
    protected void displayProjectProgress() {
        // Try to guarantee that yData.length == xData.length
        // This is dummy data that will be filled correctly later

        float progress = 0;

        // Retrieve all tasks from this user within the project to display
        // Make generic call to retrieve everything
        HTTPHandler handler = new HTTPHandler();
        String dataFromDB = "";
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("table=" + "TaskTable");
            builder.append("&");
            builder.append("where=");
            builder.append("project=");
            builder.append(ApplicationData.currentProject.getProjectId());
            dataFromDB = handler.genericCall(builder.toString(), "genericSelect");
        } catch (Exception e) {
            dataFromDB = null;
            e.printStackTrace();
        }

        // Remove brackets from front and back
        dataFromDB = dataFromDB.substring(1, dataFromDB.length() - 1);

        // Split string by end of internalList
        String[] dataFromDBArray = dataFromDB.split("\\), ");

        Log.d("debug", "Data from DB: " + dataFromDB);

        if("".equals(dataFromDB)){
            Log.d("debug", "No tasks");
            TextView chartname = (TextView) findViewById(R.id.ChartName);
            chartname.setText("No tasks in project");
            return;
        }

        // Holds all of the objects created
        ArrayList<TaskTableEntry> objectList = new ArrayList<>();

        // Used multiple times inside the for loop
        ArrayList<String> dataList;
        for (String s : dataFromDBArray) {
            // remove open and close parenthesis to prep for adding into object
            s = s.replace("(", "");
            s = s.replace(")", "");

            // Break into arraylist to build objects
            dataList = new ArrayList<>();

            String[] internalList = s.split(",");

            for (String iS : internalList) {
                // clear spaces before adding
                dataList.add(iS.replace(" ", ""));
            }

            TaskTableEntry entry = new TaskTableEntry(dataList);

            objectList.add(entry);

        }

        // yData is an array of individual values
        float[] yData = new float[objectList.size() + 1];

        // xData is an array of individual labels
        String[] xData = new String[objectList.size() + 1];

        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) != null && objectList.get(i).getTaskName() != null) {
                xData[i] = objectList.get(i).getTaskName().substring(2,
                        objectList.get(i).getTaskName().length()-1);
                yData[i] = Float.parseFloat(
                        objectList.get(i).getTaskProgress());
            }
        }

        float remainingPercentage = 0;

        // get remaining percentage
        for (TaskTableEntry entry : objectList) {
            remainingPercentage = remainingPercentage + (100 - Float.parseFloat(entry.getTaskProgress()));
        }

        xData[objectList.size()] = "Remaining";
        yData[objectList.size()] = remainingPercentage;

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
        if ("".equals(projectName) || projectName == null) {
            projectName = "Error in Project Name";
        }
        projectName = projectName.substring(2, ApplicationData.currentProject.getProjectName().length() - 1);
        textView.setText("Task progress in " + projectName);

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
