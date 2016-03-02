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
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class TaskViewActivity extends AppCompatActivity {

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
        SeekBarLabel = SeekBarLabel.substring(2,ApplicationData.currentTask.getTaskProgress().length()-1);
        if(SeekBarLabel.equals(""))
            SeekBarLabel = "0";
        progressBar.setProgress(Integer.parseInt(SeekBarLabel));

        // Assign the textfield to 0
        final TextView progressText = (TextView) findViewById(R.id.seekbarProgress);
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

                progressText.setText(String.valueOf(progress));

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
