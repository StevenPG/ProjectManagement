package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class TaskViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView taskName = (TextView) findViewById(R.id.task_name);
        taskName.setText("Task Name: " +
                ApplicationData.currentTask.getTaskName().
                        substring(2, ApplicationData.currentTask.getTaskName().length() - 1));

        // Set progress to 0
        SeekBar progressBar = (SeekBar) findViewById(R.id.seekBar);
        progressBar.setProgress(0);

        // Assign the textfield to 0
        final TextView progressText = (TextView) findViewById(R.id.seekbarProgress);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // TODO MAGIC DEFAULT VALUE TO 100
        final String progress = "100";

        // Send the value from the seekbar to the database to update progress
        Button submitButton = (Button) findViewById(R.id.submit);
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
                                    String.valueOf(progress) +
                                    "\"_WHERE_taskname=\"" +
                                    taskName + "\""
                            , "TaskTable");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("debug", "Failed to update progress...");
                }
            }
        });

    }

}
