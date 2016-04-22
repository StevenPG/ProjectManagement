package com.kutztown.projectmanagement.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.kutztown.projectmanagement.activity.AddMemberstoProject;
import com.kutztown.projectmanagement.activity.CalendarActivity;
import com.kutztown.projectmanagement.activity.CreateAccountActivity;
import com.kutztown.projectmanagement.activity.CreateProject;
import com.kutztown.projectmanagement.activity.CreateTask;
import com.kutztown.projectmanagement.activity.InboxActivity;
import com.kutztown.projectmanagement.activity.LeaderView;
import com.kutztown.projectmanagement.activity.LoginActivity;
import com.kutztown.projectmanagement.activity.MainActivity;
import com.kutztown.projectmanagement.activity.MemberList;
import com.kutztown.projectmanagement.activity.MemberProgressActivity;
import com.kutztown.projectmanagement.activity.MemberView;
import com.kutztown.projectmanagement.activity.MessageActivity;
import com.kutztown.projectmanagement.activity.ProfileActivity;
import com.kutztown.projectmanagement.activity.ProgressActivity;
import com.kutztown.projectmanagement.activity.ProjectDescriptionActivity;
import com.kutztown.projectmanagement.activity.Reminder;
import com.kutztown.projectmanagement.activity.TaskActivity;
import com.kutztown.projectmanagement.activity.TaskViewActivity;
import com.kutztown.projectmanagement.activity.ThemeActivity;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.graphing.GraphingTest;

import java.util.ArrayList;

/**
 * Created by Steven Gantz on 1/26/2016.
 * HELLO
 * This class controls which activity is being brought to
 * the forefront next.
 */
public class ActivityController extends Application{

    public static Intent openLoginActivity(Context appContext){
        return new Intent(appContext, LoginActivity.class);
    }

    public static Intent openCreateAccountActivity(Context appContext){
        return new Intent(appContext, CreateAccountActivity.class);
    }

    public static Intent openMainActivity(Context appContext){
        return new Intent(appContext, MainActivity.class);
    }

    /**
     * Debug class that opens the graphing test activity
     * @return graphing test activity intent
     */
    public static Intent openGraphingTest(Context appContext){
        return new Intent(appContext, GraphingTest.class);
    }
    public static Intent openCreateProject(Context appContext){
        return new Intent(appContext, CreateProject.class);
    }
    public static Intent openCreateAccount(Context appContext) {
        return new Intent(appContext, CreateAccountActivity.class);
    }
    public static Intent openMemberList(Context appContext){
        return new Intent(appContext,MemberList.class);
    }
    public static Intent openProfileActivity(Context appContext) {
        return new Intent(appContext, ProfileActivity.class);
    }
    public static Intent openTaskActivity(Context appContext) {
        return new Intent(appContext, TaskActivity.class);
    }
    public static Intent openCalendarActivity(Context appContext) {
        return new Intent(appContext, CalendarActivity.class);
    }
    public static Intent openProgressActivity(Context appContext) {
        return new Intent(appContext, ProgressActivity.class);
    }
    public static Intent openMemberProgressActivity(Context appContext) {
        return new Intent(appContext, MemberProgressActivity.class);
    }
    public static Intent openAddMembersToProjectActivity(Context appContext, String message)
    {
        Intent myIntent = new Intent(appContext,AddMemberstoProject.class);
        myIntent.putExtra(ApplicationData.ProjectName, message);
        return myIntent;
    }

    public static Intent openMemberViewActivity(Context appContext){
        Intent myIntent = new Intent(appContext,MemberView.class);
        return myIntent;
    }

    public static Intent openLeaderViewActivity(Context appContext){
        Intent myIntent = new Intent(appContext,LeaderView.class);
        return myIntent;
    }
    public static Intent openCreateTaskActivity(Context appContext){
        Intent myIntent = new Intent(appContext,CreateTask.class);
        return myIntent;
    }
    public static Intent openActivityTaskView(Context appContext){
        Intent myIntent = new Intent(appContext, TaskViewActivity.class);
        return myIntent;
    }
    public static Intent openActivityMessage(Context appContext, String receiver)
    {
        Intent myIntent = new Intent(appContext, MessageActivity.class);
        myIntent.putExtra("receiver", receiver);
        return  myIntent;
    }
    public static Intent openActivityInbox(Context appContext){
        Intent myIntent = new Intent(appContext, InboxActivity.class);
        return myIntent;
    }

    public static Intent openThemeActivity(Context appContext){
        Intent myIntent = new Intent(appContext, ThemeActivity.class);
        return myIntent;
    }

    public static Intent openProjectDescriptionActivity(Context appContext) {
        Intent myIntent = new Intent(appContext, ProjectDescriptionActivity.class);
        return myIntent;
    }
    public static Intent openReminderActivity(Context appContext,ArrayList<String> dates, ArrayList<String> tasks)
    {
        Intent myIntent = new Intent(appContext, Reminder.class);
        myIntent.putStringArrayListExtra("Dates", dates);
        myIntent.putStringArrayListExtra("Tasks", tasks);
        return myIntent;
    }
}
