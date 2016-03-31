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
import android.widget.SeekBar;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class TaskViewActivity extends AppCompatActivity {

    int currentProgressValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu10);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        TextView taskName = (TextView) findViewById(R.id.task_name);
        String taskNameText ="Task Name: " +
                ApplicationData.currentTask.getTaskName().
                        substring(2, ApplicationData.currentTask.getTaskName().length() - 1);
        taskName.setText(taskNameText);

        // Set progress to 0
        SeekBar progressBar = (SeekBar) findViewById(R.id.seekBar);

        String SeekBarLabel = ApplicationData.currentTask.getTaskProgress();
        SeekBarLabel = SeekBarLabel.replaceAll("'", "");
        SeekBarLabel = SeekBarLabel.replaceAll("u", "");
        if(SeekBarLabel.equals(""))
            SeekBarLabel = "0";
        progressBar.setProgress((int) Float.parseFloat(SeekBarLabel));

        // Assign the textfield to 0
        final TextView progressText = (TextView) findViewById(R.id.seekbarProgress);
        progressText.setText(SeekBarLabel + "%");
        final Button buttonText = (Button) findViewById(R.id.updateSubmitButton);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                // Retrieve value globally
                currentProgressValue = progress;

                progressText.setText(String.valueOf(progress) + ".0%");

                if(String.valueOf(progress).equals("100"))
                {
                    buttonText.setText("Submit");
                }
                else
                {
                    buttonText.setText("Update");
                }

            }
        });

        // Send the value from the seekbar to the database to update progress
        Button submitButton = (Button) findViewById(R.id.updateSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPHandler handler = new HTTPHandler();

                // Retrieve the taskName and substring away pytohn stuff
                String taskName = ApplicationData.currentTask.getTaskName();
                taskName = taskName.substring(2, taskName.length() - 1);

                try {
                    handler.update(
                            "taskprogress=\"" +
                                    String.valueOf(currentProgressValue) +
                                    "\"_WHERE_taskname=\"" +
                                    taskName + "\""
                            , "TaskTable");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("debug", "Failed to update progress...");
                }

                // Go back to the updated taskview screen
                startActivity(ActivityController.openTaskActivity(getApplicationContext()));
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
