package com.kutztown.projectmanagement.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.kutztown.projectmanagement.graphing.GraphingTest;

/**
 * Created by Steven Gantz on 1/26/2016.
 *
 * This class controls which activity is being brought to
 * the forefront next.
 */
public class ActivityController extends Application{

    /**
     * Debug class that opens the graphing test activity
     * @return graphing test activity intent
     */
    public static Intent openGraphingTest(){
        return new Intent(mContext, GraphingTest.class);
    }

    /**
     * Application context resources to allow
     * this class to function as a static access class.
     */
    private static Context mContext;
    public void onCreate(){
        super.onCreate();
        mContext = this.getApplicationContext();
    }
    public static Context getAppContext(){
        return mContext;
    }

}
