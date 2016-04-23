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
import android.widget.Button;
import android.widget.EditText;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class CreateProject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu02);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if (!loggedIn) {
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        //NOTE: here puh completed project into the database

        // Retrieve data from fields
        final EditText projectNameText = (EditText) findViewById(R.id.project_name);
        final EditText projectDescText = (EditText) findViewById(R.id.projectDescField);

        String leaderWholeName = "";
        String first = ApplicationData.currentUser.getFirstName();
        String last = ApplicationData.currentUser.getLastName();

        if(!first.equals("None") && !last.equals("None"))
        {
            first = first.substring(2,first.length()-1);
            last = last.substring(2,last.length()-1);
            leaderWholeName = first + " " +last;
        }

        EditText projectLeaderText = (EditText) findViewById(R.id.projectLeaderEditText);
        projectLeaderText.setText(leaderWholeName);


        // Add onclick to button
        Button projectB = (Button) findViewById(R.id.createProject);
        projectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve values
                String projectName = projectNameText.getText().toString();
                String projectDesc = projectDescText.getText().toString();

                // Short circuit method if entries contain '**'
                if(ApplicationData.checkIfContainsDoubleStar(getApplicationContext(), projectName, true)){
                    return;
                }
                if(ApplicationData.checkIfContainsDoubleStar(getApplicationContext(), projectDesc, true)){
                    return;
                }

                // print values for debugging
                Log.d("debug", "ProjectName: " + projectName);
                Log.d("debug", "Project Desc:" + projectDesc);

                // Enter project into database
                String currentUserEmail = ApplicationData.currentUser.getEmail();
                currentUserEmail = currentUserEmail.substring(2, currentUserEmail.length() - 1);
                projectName = projectName.replace(" ", "_");
                projectDesc = projectDesc.replace(" ", "_");
                HTTPHandler handler = new HTTPHandler();
                ProjectTableEntry entry = new ProjectTableEntry(
                        currentUserEmail,
                        projectName,
                        projectDesc);

                // Assign entry as the current project
                ApplicationData.currentProject = entry;

                // Retrieve current projectlist
                String currentProjectList = ApplicationData.currentUser.getProjectList();

                // If the projectlist is a python None, set it to empty.
                if (currentProjectList.equals("None")) {
                    currentProjectList = "";
                } else if(currentProjectList.equals("")){
                    // If it is already empty, just do nothing
                } else {
                    // It is something other than none or empty, so it has something
                    // Remove any duplicates that exist
                    // TODO currentProjectList = ApplicationData.removeDuplicates(currentProjectList);
                }

                try {
                    // Add the project
                    handler.insert(entry, "ProjectTable");

                    // Get the unique id from the newly added project
                    ApplicationData.currentProject = (ProjectTableEntry)
                            handler.select(entry.getProjectName(),
                                    "projectname",
                                    new ProjectTableEntry(),
                                    "ProjectTable");

                    // Adding the first project
                    if (currentProjectList.length() == 0) {
                        handler.update("projectlist=\"" +
                                ApplicationData.currentProject.getProjectId() + "\"**WHERE**UserID=\"" +
                                ApplicationData.currentUser.getUserId() + "\""
                                , "UserTable");
                        Log.d("debug", "ProjectID onCreateProject: " + String.valueOf(ApplicationData.currentProject.getProjectId()));
                    } else {
                        Log.d("createbugcheck", "user's projectlist=\"" +
                                currentProjectList.substring(2, currentProjectList.length() - 1) + "--" +
                                ApplicationData.currentProject.getProjectId() + "\"%20WHERE%20UserID=\"" +
                                ApplicationData.currentUser.getUserId() + "\"");

                        // Concat the project if projectlist is not empty
                        if (currentProjectList.equals("")) {
                            handler.update(
                                    "projectlist=\"" +
                                            ApplicationData.currentProject.getProjectId() + "\"**WHERE*UserID=\"" +
                                            ApplicationData.currentUser.getUserId() + "\""
                                    , "UserTable");
                        } else {

                            // If projectlist is not empty, concat the new project
                            // add the current project to the user's projectlist
                            handler.update(
                                    "projectlist=\"" +
                                            currentProjectList.substring(2, currentProjectList.length() - 1) + "--" +
                                            ApplicationData.currentProject.getProjectId() + "\"**WHERE**UserID=\"" +
                                            ApplicationData.currentUser.getUserId() + "\""
                                    , "UserTable");
                        }
                    }

                    // Retrieve the project again to get the projectId
                    UserTableEntry currentUser = (UserTableEntry)
                            handler.select(String.valueOf(ApplicationData.currentUser.getUserId()),
                                    "userid",
                                    new UserTableEntry(),
                                    "usertable"
                            );

                    ApplicationData.currentUser = currentUser;

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("debug", "Error inserting new project into project table");
                }

                startActivity(ActivityController.openLeaderViewActivity(getApplicationContext()));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if (!loggedIn) {
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
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
}
