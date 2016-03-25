package com.kutztown.projectmanagement.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.data.TaskDatePicker;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static String dateString = "";
    public Spinner spinner1 = null;
    public Spinner spinner2 = null;
    public Spinner spinner3 = null;

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

        String[] itemArray = ApplicationData.currentProject.getMemberList().split("--");

        // Remove very first two chars from first item
        itemArray[0] = itemArray[0].substring(2, itemArray[0].length());

        // Remove very last char from last item
        itemArray[itemArray.length - 1] = itemArray[itemArray.length - 1].substring(0, itemArray[itemArray.length - 1].length() - 1);

        this.spinner1 = (Spinner) findViewById(R.id.spinner01);
        this.spinner1.setAdapter(
                new ArrayAdapter<String>(this,
                        R.layout.mainactivityrow, itemArray)
        );


        this.spinner2 = (Spinner) findViewById(R.id.spinner02);
        this.spinner3 = (Spinner) findViewById(R.id.spinner03);

        final TextView taskName = (TextView) findViewById(R.id.name_task);

        // this textview will be set whith the project name
        final TextView projectName = (TextView) findViewById(R.id.name_project);
        projectName.setText(ApplicationData.currentProject.getProjectName().substring(2,
                ApplicationData.currentProject.getProjectName().length() - 1));
        Button taskDate = (Button) findViewById(R.id.datepicker01);
        final Calendar c = Calendar.getInstance();

        // this textview will get a description of the task
        final EditText taskDesc = (EditText) findViewById(R.id.task_description);

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TaskDatePicker();
                newFragment.show(getFragmentManager(), "datePicker");
            }

        });

        Button createTask = (Button) findViewById(R.id.create_task);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView getSeverity = (TextView) spinner2.getSelectedView();
                TextView getName = (TextView) spinner1.getSelectedView();
                TextView getDependecy = (TextView) spinner3.getSelectedView();
                String pickedName = getName.getText().toString();

                String pickedPriority = getSeverity.getText().toString();
                String dependency = getDependecy.getText().toString();
                String task_name = taskName.getText().toString().replace(" ", "");
                String task_desc = taskDesc.getText().toString().replace(" ", "");
                String severity = getSeverity.getText().toString();

                Log.d("debug", "Chosen Priority: " + pickedPriority);
                Log.d("debug", "Chosen member:" + getName.getText().toString());

                // Wipe out python stuff from projectName
                String projectName = String.valueOf(ApplicationData.currentProject.getProjectId());
                if (projectName.equals("") || task_desc.equals("") || task_name.equals("") || pickedPriority.equals("") ||
                        dateString.equals("") || dependency.equals("") || pickedName.equals("")) {
                    missingInfo();

                } else {
                    TaskTableEntry entry = new TaskTableEntry(
                            String.valueOf(ApplicationData.currentUser.getUserId()),
                            projectName,
                            task_name,
                            task_desc,
                            "0",
                            "",
                            pickedPriority.replace(" ", ""),
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
                        currentTaskList = currentTaskList.substring(2, currentTaskList.length() - 1);

                        Log.d("debug", currentTaskList);
                        Log.d("debug", currentTaskList + "--" + currentTask.getTaskID());

                        // updateString
                        Log.d("debug", "tasklist=\"" +
                                currentTaskList + "--" + currentTask.getTaskID() +
                                "\"_WHERE_ProjectID=\"" + ApplicationData.currentProject.getProjectId() + "\"");

                        // add the created task to project's tasklist
                        handler.update(
                                "tasklist=\"" +
                                        currentTaskList + "--" + currentTask.getTaskID() +
                                        "\"_WHERE_ProjectID=\"" +
                                        ApplicationData.currentProject.getProjectId() + "\""
                                , "ProjectTable");

                        // Retrieve the project with the new tasklist
                        ApplicationData.currentProject = (ProjectTableEntry)
                                handler.select(
                                        String.valueOf(ApplicationData.currentProject.getProjectId()),
                                        "projectid",
                                        new ProjectTableEntry(),
                                        "ProjectTable");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("debug", "Error creating new task ");
                    }

                    // head on back to task view
                    startActivity(ActivityController.openTaskActivity(getApplicationContext()));
                }
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

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        ApplicationData.year = year;
        ApplicationData.month = monthOfYear;
        ApplicationData.day = dayOfMonth;
        updateDisplay();

    }

    private void updateDisplay() {
        StringBuilder myString = new StringBuilder();
        // Month is 0 based so add 1
        myString.append(ApplicationData.month + 1).append("-").append(ApplicationData.day).append("-")
                .append(ApplicationData.year).append(" ");
        dateString = myString.toString();
        Toast.makeText(getApplicationContext(), myString, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);
    }

    private void missingInfo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTask.this);

        // Setting Dialog Title
        alertDialog.setTitle("Missing task information");

        // Setting Dialog Message
        alertDialog.setMessage("Please fill all fields before creating task");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.icon);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
