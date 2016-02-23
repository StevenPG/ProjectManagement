package com.kutztown.projectmanagement.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Steven Gantz
 * @date 2/16/2016
 *
 * This class allows for asynchronous database access.
 */
public class WebTask extends AsyncTask<Void, Void, String> {

    /**
     * Store the URL for use within the background thread
     * Have a flag for when it is alright to grab the string.
     */
    private URL url;
    public boolean grabString;

    public String dataString;

    /**
     * General constructor builds the url and
     * accesses it within the background thread.
     * @param url
     */
    WebTask(URL url){
        this.grabString = false;
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            this.dataString = HTTPHandler.readFromURLConnection(httpURLConnection);
            this.grabString = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("debug", "IOException occurred in background thread");
            this.dataString = "Error";
            this.grabString = true;
        }

        return this.dataString;
    }

    @Override
    protected void onPostExecute(final String userEntry){
        this.grabString = true;
        this.dataString = userEntry;
    }
}
