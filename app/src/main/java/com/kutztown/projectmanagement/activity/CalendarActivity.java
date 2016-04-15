package com.kutztown.projectmanagement.activity;

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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.Inflater;

public class CalendarActivity extends AppCompatActivity {
    ArrayList<String> taskList = null;
    ListView taskView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);

        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu01);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        final CalendarView myCalendar = (CalendarView) findViewById(R.id.CalendarView);
        myCalendar.setShownWeekCount(4);
        // Retrieve tasks of current user
        this.taskList = getTasksFromProject();
        taskView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.taskList);
        taskView.setAdapter(listAdapter);

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        final TextView myText = (TextView) findViewById(R.id.date_display);
        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {


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
    protected void onResume(){
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);
    }

    /**
     * Retrieve the tasks from the selected project and parse them into an arraylist
     *
     * @return null if no tasks, else arraylist of tasks
     */
    protected ArrayList<String> getTasksFromProject() {
        String taskList = null;

        if (ApplicationData.currentProject == null) {
            Log.d("debug", "There is no current project");
            taskList = "";
        } else {
            taskList = ApplicationData.currentProject.getTaskList();
        }
        ArrayList taskArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.header);
        Log.d("debug", "Task List: " + taskList);
        if ("None".equals(taskList)) {
            //header.setText("No tasks have been assigned");
            return null;
        } else {
          //  header.setText("All Tasks");
            String[] tasks = taskList.split("--");

            for (String task : tasks) {
                // Wipe leftover python structures
                task = task.replaceAll("u'", "");
                task = task.replaceAll("'", "");
                if (!"".equals(task)) {
                    if (!"None".equals(task)) {
                        // Get the project name of project
                        TaskTableEntry entry = null;
                        try {
                            Log.d("debug", task);
                            entry = (TaskTableEntry) new HTTPHandler().select(
                                    task, "TaskId", new TaskTableEntry(), "TaskTable"
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (entry != null && entry.getTaskName() != null) {
                            taskArray.add(task + ": " + entry.getTaskName().substring(2,
                                    entry.getTaskName().length() - 1));
                        } else {
                            taskArray.add(task);
                        }
                    }
                }
            }
        }

        return taskArray;
    }


}
