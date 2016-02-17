package com.kutztown.projectmanagement.com.kutztown.projectmanagement.networking;

import android.os.AsyncTask;
import android.util.Log;

import com.kutztown.projectmanagement.data.UserTableEntry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Steven Gantz
 * @date 2/16/2016
 *
 * This class allows for retrieval of a user from the database asynchronously.
 */
public class SelectUserTask extends AsyncTask<Void, Void, String> {

    /**
     * Store the URL for use within the background thread
     * Have a flag for when it is alright to grab the string.
     */
    private URL url;
    public boolean grabString;

    public String userString;

    /**
     * General constructor builds the url and
     * accesses it within the background thread.
     * @param url
     */
    SelectUserTask(URL url){
        this.grabString = false;
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            this.userString = HTTPHandler.readFromURLConnection(httpURLConnection);
            this.grabString = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("debug", "IOException occurred in background thread");
            this.userString = "Error";
            this.grabString = true;
        }

        return this.userString;
    }

    @Override
    protected void onPostExecute(final String userEntry){
        this.grabString = true;
        this.userString = userEntry;
    }
}
