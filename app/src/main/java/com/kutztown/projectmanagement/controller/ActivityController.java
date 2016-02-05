package com.kutztown.projectmanagement.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.kutztown.projectmanagement.activity.CreateAccountActivity;
import com.kutztown.projectmanagement.activity.LoginActivity;
import com.kutztown.projectmanagement.activity.ProgressActivity;
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
}
