package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.CommaListParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    /**
     * List of projects
     */
    ArrayList<String> projectList = null;
    ListView projectView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieve projects of current user
        this.projectList = getProjectsFromUser();

        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.ProjectListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.projectList);
        projectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedText = (String) parent.getItemAtPosition(position);
                // TODO - Set the ApplicationData.currentProject value
                // TODO - send to projectactivity
            }
        });

        projectView.setAdapter(listAdapter);
    }

    /**
     * Retrieve the projects from the logged in user and parse them into an arraylist
     * @return null if no projects, else arraylist of projects
     */
    protected ArrayList<String> getProjectsFromUser(){
        String projectList = ApplicationData.currentUser.getProjectList();
        ArrayList projectArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.Header);
        Log.d("debug", "Project List: " + projectList);
        if("None".equals(projectList)){
            header.setText("You are not a part of any projects.");
            return null;
        }
        else{
            header.setText("Projects");
            String[] projects = projectList.split("--");

            for(String project : projects){
                // Wipe leftover python structures
                project = project.replaceAll("u'", "");
                project = project.replaceAll("'", "");
                projectArray.add(project);
            }
        }

        return projectArray;
    }



}
