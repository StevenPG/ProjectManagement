package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.CommaListParser;
import com.kutztown.projectmanagement.data.TableEntry;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class CreateTask extends AppCompatActivity {

    public Spinner spinner1 = null;
    public Spinner spinner2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu09);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        this.spinner1 = (Spinner) findViewById(R.id.spinner01);

        this.spinner2 = (Spinner) findViewById(R.id.spinner02);

        TextView getSeverity = (TextView)spinner2.getSelectedView();
        TextView getName = (TextView) spinner1.getSelectedView();
        String pickedName = getName.getText().toString();
        final String pickedPriority = getSeverity.getText().toString();


        final TextView taskName = (TextView) findViewById(R.id.name_task);

        // this textview will be set whith the project name
         final TextView projectName = (TextView) findViewById(R.id.name_project);
        projectName.setText(ApplicationData.currentProject.getProjectName());

        // this textview will get a description of the task
         final EditText taskDesc = (EditText) findViewById(R.id.task_description);

        Button createTask = (Button) findViewById(R.id.create_task);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Wipe out python stuff from projectName
                String projectName = String.valueOf(ApplicationData.currentProject.getProjectId());

                TaskTableEntry entry = new TaskTableEntry(
                        String.valueOf(ApplicationData.currentUser.getUserId()),
                        projectName,
                        taskName.getText().toString().replace(" ", ""),
                        taskDesc.getText().toString().replace(" ", ""),
                        pickedPriority,
                        "",
                        "",
                        "",
                        "");

                HTTPHandler handler = new HTTPHandler();
                try {
                    // Insert the new task
                    handler.insert(entry, "TaskTable");

                    Toast.makeText(getApplicationContext(), "task successfully created", Toast.LENGTH_SHORT).show();
                    // after inserting into db, pull back out with unique id
                    TaskTableEntry currentTask = (TaskTableEntry)
                            handler.select(taskName.getText().toString(), "TaskName",
                                    new TaskTableEntry(), "TaskTable");

                    Log.d("debug2", "CurrentProjectId: " + String.valueOf(ApplicationData.currentProject.getProjectId()));
                    String currentTaskList = ApplicationData.currentProject.getTaskList();
                    currentTaskList = currentTaskList.replace("u'", "");
                    currentTaskList = currentTaskList.replace("'", "");
                    handler.update(
                            "UPDATE%20ProjectTable%20SET%20tasklist=\"" +
                            currentTaskList + "--" +
                            currentTask.getTaskID(),
                            "ProjectTable");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("debug", "Error creating new task ");
                }

                // head on back to task view
                startActivity(ActivityController.openTaskActivity(getApplicationContext()));
            }
        });

        final DatePicker taskDate = (DatePicker)findViewById(R.id.datepicker01);
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        taskDate.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear, dayOfMonth);
                // here will go code to get the date due of the task and passed that info to the
                // database
            }
        });

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
