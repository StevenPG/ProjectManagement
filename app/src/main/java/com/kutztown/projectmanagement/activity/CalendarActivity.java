package com.kutztown.projectmanagement.activity;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.CalendarView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.controller.ListViewAdapter;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.LogRecord;
import java.util.zip.Inflater;

import static com.kutztown.projectmanagement.controller.ActivityController.openReminderActivity;

public class CalendarActivity extends AppCompatActivity {
  private   ArrayList<String> taskList = null;
   private ArrayList<String> dates = null;
    private ListView taskView = null;
    private ListViewAdapter myadapter;
   private CalendarView myCalendar;
   private ArrayList<String> dueTask = null;
   private ArrayList<String> datesTask = null;
    private Handler handler = new Handler();

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
         myCalendar = (CalendarView) findViewById(R.id.CalendarView);
        myCalendar.setShownWeekCount(4);



       if (this.taskList == null) {
            this.taskList = new ArrayList<>();

        }
        if (this.dates == null)
        {
            this.dates = new ArrayList<>();
        }
        new Thread(new gettingTask()).start();

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
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
            header.setText("No tasks have been assigned");
            return null;
        } else {
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
                     if (entry != null && entry.getTaskDueDate() !=null) {
                         Log.d("DEBUG: ", "Task due date: " + entry.getTaskDueDate());
                         String trimDate = String.valueOf(entry.getTaskDueDate());
                         if (trimDate.equals("u''")) {
                             trimDate = trimDate.substring(2, trimDate.length() - 1);
                         } else {
                             trimDate = trimDate.substring(2, trimDate.length() - 2);
                             dates.add(trimDate);
                         }
                     }
                        else {
                           // this.dates = new ArrayList<>();
                        }
                        if (entry != null && entry.getTaskName() != null) {
                            taskArray.add(entry.getTaskName().substring(2,
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

    public void dueDatessetter(String date)
    {   List<Long> dates1 = new ArrayList<Long>();

        //String date = "22/3/2014";
        String parts[] = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        dates1.add(calendar.getTimeInMillis());
         myCalendar.setSelected(true);

    }

    public void checkDueTask(ArrayList<String> d, ArrayList<String> t){
         dueTask = new ArrayList<>();
         datesTask = new ArrayList<>();
       final int five = 5;
        Calendar c = Calendar.getInstance();
       final int pd = c.get(Calendar.DAY_OF_MONTH);
       final int pm = c.get(Calendar.MONTH) + 1;
        final int py = c.get(Calendar.YEAR);
        for (int i = 0; i < d.size(); i++)
        {
            String cleanD = d.get(i);
            String parts[] = cleanD.split("-");
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            if (py == year && pm == month && (day - pd <= five && day - pd > 0))
            {
                dueTask.add(t.get(i));
                datesTask.add(d.get(i));

            }
            Log.d("Debug", "dueTask:" + dueTask.size());
            Log.d("Debug", "dateTask:" + datesTask.size());
            Log.d("Debug", " due date:" + datesTask.toString());
            Log.d("Debug", "task Name:" + dueTask.toString());

        }

    }
    private class gettingTask implements Runnable {

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    taskList = getTasksFromProject();
                    checkDueTask(dates, taskList);
                    taskView = (ListView) findViewById(R.id.list);
                    myadapter = new ListViewAdapter(CalendarActivity.this,dates,taskList);
                    taskView.setAdapter(myadapter);
                    Log.d ("Apdater", "Apdater count:" + myadapter.getCount());
                    if (!(dueTask.isEmpty() && datesTask.isEmpty()))
                    {
                        startActivity(openReminderActivity(getApplicationContext(),dueTask,datesTask));
                    }

                    }
                });
            }
}


}
