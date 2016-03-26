package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     private ActionMenuView amvMenu1;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu04);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });


       ImageButton myButton = (ImageButton) findViewById(R.id.plus_button);
         myButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(ActivityController.openCreateProject(getApplicationContext()));
             }
         });


        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

         // Retrieve projects of current user;
        this.projectList = getProjectsFromUser();

        if(this.projectList == null){
            this.projectList = new ArrayList<>();
        }

        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.ProjectListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.projectList);
        projectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked Text contains the project name
                String clickedText = (String) parent.getItemAtPosition(position);
                // TODO - Set the ApplicationData.currentProject value
                // Retrieve the project from the DB and store it globally
                HTTPHandler handler = new HTTPHandler();
                try {
                    ApplicationData.currentProject = (ProjectTableEntry) handler.
                            select(clickedText, "ProjectId", new ProjectTableEntry(), "ProjectTable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(ApplicationData.currentProject == null){

                } else {
                    Log.d("debug", ApplicationData.currentProject.writeAsGet());
                    // Get info and decide where to send the user if leader or not
                    String projectLeader = ApplicationData.currentProject.getLeaderList().replaceAll("\\s+","");
                    if(projectLeader.equals(ApplicationData.currentUser.getEmail())){
                        startActivity(ActivityController.openLeaderViewActivity(getApplicationContext()));
                    } else {
                        startActivity(ActivityController.openMemberViewActivity(getApplicationContext()));
                    }
                }
            }
        });

        projectView.setAdapter(listAdapter);
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

        // Also do this on activity resume
        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.ProjectListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.projectList);
        projectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked Text contains the project name
                String clickedText = (String) parent.getItemAtPosition(position);
                // TODO - Set the ApplicationData.currentProject value
                // Retrieve the project from the DB and store it globally
                HTTPHandler handler = new HTTPHandler();
                try {
                    ApplicationData.currentProject = (ProjectTableEntry) handler.
                            select(clickedText, "ProjectId", new ProjectTableEntry(), "ProjectTable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(ApplicationData.currentProject == null){

                } else {
                    Log.d("debug", ApplicationData.currentProject.writeAsGet());
                    // Get info and decide where to send the user if leader or not
                    String projectLeader = ApplicationData.currentProject.getLeaderList().replaceAll("\\s+","");
                    if(projectLeader.equals(ApplicationData.currentUser.getEmail())){
                        startActivity(ActivityController.openLeaderViewActivity(getApplicationContext()));
                    } else {
                        startActivity(ActivityController.openMemberViewActivity(getApplicationContext()));
                    }
                }
            }
        });
    }

    /**
     * Retrieve the projects from the logged in user and parse them into an arraylist
     * @return null if no projects, else arraylist of projects
     */
    protected ArrayList<String> getProjectsFromUser(){

        String projectList = ApplicationData.currentUser.getProjectList();
        if(projectList == null){
            projectList = "";
        }

        ArrayList projectArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.Header);
        Log.d("debug", "Project List: " + projectList);
        if("None".equals(projectList) || "".equals(projectList)){
            header.setText("Click \"+\" to create a project");
            return null;
        } else {
            header.setText("Project List");
            String[] projects = projectList.split("--");

            for(String project : projects){
                // Wipe leftover python structures
                project = project.replaceAll("u'", "");
                project = project.replaceAll("'", "");
                if(!"".equals(project)) {
                    if(!"None".equals(project)) {
                        // Get the project name of project
                        ProjectTableEntry entry = null;
                        try {
                            Log.d("debug", project);
                            entry = (ProjectTableEntry) new HTTPHandler().select(
                                    project, "ProjectId", new ProjectTableEntry(), "ProjectTable"
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (entry != null && entry.getProjectName() != null) {
                            projectArray.add(project + ": " + entry.getProjectName().substring(2,
                                    entry.getProjectName().length()-1));
                        } else {
                            projectArray.add(project);
                        }
                    }
                }
            }
        }

        return projectArray;
    }


}
