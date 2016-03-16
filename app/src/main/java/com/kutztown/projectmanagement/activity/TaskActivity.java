package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.exception.ServerNotRunningException;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TaskActivity extends Activity implements AppCompatCallback{

    /**
     * List of tasks
     */
    ArrayList<String> taskList = null;
    ListView taskView = null;
    ListView completedTaskView = null;

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ApplicationData.delegate = AppCompatDelegate.create(this, this);
        ApplicationData.delegate.installViewFactory();
        super.onCreate(savedInstanceState);
        ApplicationData.delegate.onCreate(savedInstanceState);
        ApplicationData.delegate.setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ApplicationData.delegate.getSupportActionBar();
        ApplicationData.delegate.setSupportActionBar(toolbar);

        //NOTE: I don't know why there are erros
        ApplicationData.delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ApplicationData.delegate.getSupportActionBar().setDisplayShowHomeEnabled(false);
        ApplicationData.delegate.getSupportActionBar().setTitle(null);

        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu08);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
         final ImageButton createTaskB = (ImageButton) findViewById(R.id.plus_buttom1);
         createTaskB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(ActivityController.openCreateTaskActivity(getApplicationContext()));
             }
         });

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        // Retrieve tasks of current user
        this.taskList = getTasksFromProject();

        // Retrieve completed tasks
        ArrayList<String> completedTaskList = getCompletedTasksFromProject(this.taskList);

        if(this.taskList == null){
            this.taskList = new ArrayList<>();
        }

        // Retrieve listview and add tasks
        taskView = (ListView) findViewById(R.id.TaskListView);
        completedTaskView = (ListView) findViewById(R.id.CompletedTaskListView);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.taskList);
        ArrayAdapter<String> completeAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, completedTaskList);

        taskView.setAdapter(listAdapter);
        completedTaskView.setAdapter(completeAdapter);

        // for completed items
        completedTaskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked Text contains the project name
                String clickedText = (String) parent.getItemAtPosition(position);

                // Retrieve the project from the DB and store it globally
                HTTPHandler handler = new HTTPHandler();
                try {
                    ApplicationData.currentTask = (TaskTableEntry) handler.
                            select(clickedText, "TaskId", new TaskTableEntry(), "TaskTable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (ApplicationData.currentTask == null) {

                } else {
                    Log.d("debug", ApplicationData.currentTask.writeAsGet());
                    startActivity(ActivityController.openActivityTaskView(getApplicationContext()));
                }
            }
        });

        // for all items
        taskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked Text contains the project name
                String clickedText = (String) parent.getItemAtPosition(position);

                // Retrieve the project from the DB and store it globally
                HTTPHandler handler = new HTTPHandler();
                try {
                    ApplicationData.currentTask = (TaskTableEntry) handler.
                            select(clickedText, "TaskId", new TaskTableEntry(), "TaskTable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (ApplicationData.currentTask == null) {

                } else {
                    Log.d("debug", ApplicationData.currentTask.writeAsGet());
                    startActivity(ActivityController.openActivityTaskView(getApplicationContext()));
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

    protected ArrayList<String> getCompletedTasksFromProject(ArrayList<String> taskList){

        ArrayList<TaskTableEntry> taskTableEntries = new ArrayList<>();
        ArrayList<String> tasks = new ArrayList<>();


        // exit if tasklist is empty
        if(taskList == null){
            return new ArrayList<>();
        }

        for(String task : taskList){
            HTTPHandler handler = new HTTPHandler();
            try {
                TaskTableEntry currententry = (TaskTableEntry) handler.select(task, "TaskId", new TaskTableEntry(), "TaskTable");
                Log.d("debug", currententry.getTaskProgress() + "Task progressLLLLL:");
                Log.d("debug", currententry.writeAsGet());
                if(currententry.getTaskProgress().equals("100.0")){
                    taskTableEntries.add(currententry);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(TaskTableEntry t : taskTableEntries){
            tasks.add(String.valueOf(t.getTaskID()));
        }

        return tasks;
    }

    /**
     * Retrieve the tasks from the selected project and parse them into an arraylist
     * @return null if no tasks, else arraylist of tasks
     */
    protected ArrayList<String> getTasksFromProject(){
        String taskList = null;

        if(ApplicationData.currentProject == null){
            Log.d("debug", "There is no current project");
            taskList = "";
        } else {
            taskList = ApplicationData.currentProject.getTaskList();
        }
        ArrayList taskArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.header);
        Log.d("debug", "Task List: " + taskList);
        if("None".equals(taskList)){
            header.setText("No tasks have been assigned");
            return null;
        }
        else{
            header.setText("All Tasks");
            String[] tasks = taskList.split("--");

            for(String task : tasks){
                // Wipe leftover python structures
                task = task.replaceAll("u'", "");
                task = task.replaceAll("'", "");
                taskArray.add(task);
            }
        }

        return taskArray;
    }

}
