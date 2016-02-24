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
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class TaskActivity extends Activity implements AppCompatCallback{

    /**
     * List of tasks
     */
    ArrayList<String> taskList = null;
    ListView taskView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //NOTE: I don't know why there are erros
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Retrieve tasks of current user
        this.taskList = getTasksFromProject();

        if(this.taskList == null){
            this.taskList = new ArrayList<>();
        }

        // Retrieve listview and add tasks
        taskView = (ListView) findViewById(R.id.TaskListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.taskList);
        taskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked Text contains the task name
                String clickedText = (String) parent.getItemAtPosition(position);
                // TODO - Set the ApplicationData.currentTask value
                // Retrieve the task from the DB and store it globally
                HTTPHandler handler = new HTTPHandler();
                try {
                    ApplicationData.currentTask = (TaskTableEntry) handler.
                            select(clickedText, "TaskName", new TaskTableEntry(), "TaskTable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("debug", ApplicationData.currentTask.writeAsGet());

                //NOTE: TaskViewActivity has not been created yet
                //startActivity(ActivityController.openTaskViewActivity(getApplicationContext()));
            }
        });
        Log.d("debug", listAdapter.toString());
        taskView.setAdapter(listAdapter);
    }

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

    /**
     * Retrieve the tasks from the selected project and parse them into an arraylist
     * @return null if no tasks, else arraylist of tasks
     */
    protected ArrayList<String> getTasksFromProject(){
        String taskList = ApplicationData.currentProject.getTaskList();
        ArrayList taskArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.header);
        Log.d("debug", "Task List: " + taskList);
        if("None".equals(taskList)){
            header.setText("No tasks have been assigned");
            return null;
        }
        else{
            header.setText("Tasks");
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
