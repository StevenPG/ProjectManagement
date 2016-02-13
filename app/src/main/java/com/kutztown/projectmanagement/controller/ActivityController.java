package com.kutztown.projectmanagement.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.kutztown.projectmanagement.activity.CalendarActivity;
import com.kutztown.projectmanagement.activity.CreateAccountActivity;
import com.kutztown.projectmanagement.activity.CreateProject;
import com.kutztown.projectmanagement.activity.LoginActivity;
import com.kutztown.projectmanagement.activity.MainActivity;
import com.kutztown.projectmanagement.activity.MemberList;
import com.kutztown.projectmanagement.activity.ProfileActivity;
import com.kutztown.projectmanagement.activity.ProgressActivity;
import com.kutztown.projectmanagement.activity.TaskActivity;
import com.kutztown.projectmanagement.graphing.GraphingTest;

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
     * TODO - THIS IS A DEBUG METHOD
     * Open a progress activity
     * @param appContext
     * @return
     */
    public static Intent openProgressActivity(Context appContext){
        return new Intent(appContext, ProgressActivity.class);
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
    public static Intent openPregressActivity(Context appContext) {
        return new Intent(appContext, ProgressActivity.class);
    }
}
