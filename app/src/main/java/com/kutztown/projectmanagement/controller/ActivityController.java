package com.kutztown.projectmanagement.controller;

import android.app.Activity;
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
    public static Intent openGraphingTest(Context appContext){
        return new Intent(appContext, GraphingTest.class);
    }
}
