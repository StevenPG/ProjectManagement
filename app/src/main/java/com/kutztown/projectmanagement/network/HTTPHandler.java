package com.kutztown.projectmanagement.network;

import android.util.Log;

import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.CommaListParser;
import com.kutztown.projectmanagement.data.MessageTableEntry;
import com.kutztown.projectmanagement.data.ProgressTableEntry;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.data.TableEntry;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.exception.ServerNotRunningException;
import com.kutztown.projectmanagement.exception.UserNotFoundException;
import com.kutztown.projectmanagement.exception.ValueAlreadyExistsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * @author Steven Gantz on 2/4/2016.
 * @date 2/4/2016
 * This class abstracts away the annoying
 * aspects of making the HTTP calls.
 */
public class HTTPHandler {

    /**
     * General empty constructor
     */
    public HTTPHandler(){

    }

    /**
     * Spaces in the updatestatement must be listed as %20 for the http header
     *
     * This is represented as a " " in python.
     *
     * usage: handler.update("firstname=\"John\"%20where%20email=\"emailaddress@.kutztown.edu\"", "usertable");
     *
     * @param updateStatement - The SQL statemene to be executed
     * @param table - which table to execute the statement in
     * @return - status
     * @throws Exception
     */
    public int update(String updateStatement, String table) throws Exception {

        String approute = "update";

        if(!this.pingServer(ApplicationData.SERVER_IP)){
            throw new ServerNotRunningException();
        }

        String parameterString = "table=" + table +
                "&updatestring=" + updateStatement;

        Log.d("debug", parameterString);

        URL url = buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT,
                approute, false, parameterString);

        WebTask task = new WebTask(url);
        task.execute((Void) null);

        while(!task.grabString){
            // Busy wait until the connection is done
            //Log.d("debug", "Busy waiting until connection is done");
        }
        String stringEntry = task.dataString;

        // If there was an issue, a 2 will be returned
        if(stringEntry.charAt(0) == '2'){
            throw new InvalidParameterException("Search query wasn't filled out right");
        }

        // If the server returned a 0, the value already exists
        if(stringEntry.charAt(0) == '0'){
            throw new Exception();
        }

        // If everything went fine, return a 1
        return 1;
    }

    /**
     * This method inserts a row into a table
     * @param entry - table entry to parse into an insert string
     * @param table - the table to insert the values into
     * @return - Status on the success or not of the operation
     */
    public int insert(TableEntry entry, String table) throws Exception {

        String approute = "insert";

        if(!this.pingServer(ApplicationData.SERVER_IP)){
            throw new ServerNotRunningException();
        }

        String parameterString = "table=" + table +
                "&valuestring=" + entry.writeAsValueString() +
                "&columnstring=" + entry.getColumnString();
        Log.d("debug", parameterString);

        URL url = buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT,
                approute, false, parameterString);

        WebTask task = new WebTask(url);
        task.execute((Void) null);

        while(!task.grabString){
            // Busy wait until the connection is done
            //Log.d("debug", "Busy waiting until connection is done");
        }
        String stringEntry = task.dataString;

        // If the server returned a 0, the value already exists
        if(stringEntry.charAt(0) == '0'){
            throw new ValueAlreadyExistsException("Value already exists");
        }

        // If there was an issue, a 2 will be returned
        if(stringEntry.charAt(0) == '2'){
            throw new InvalidParameterException("Search query wasn't filled out right");
        }

        // If everything went fine, return a 1
        return 1;
    }

    /**
     * This method uses polymorphism to allow the caller to pass in its own
     * tableEntry object. This object will be referenced based on the interface,
     * and so can be used with any type of table. Eventually returning a filled
     * table entry of the correct type.
     *
     * @param searchValue - value to search for
     * @param searchRecord - what field in usertable to check in
     * @param entry - an object of the correct entry type to use for record and search
     *
     * @return - the table entry of the user requested
     * @throws ServerNotRunningException - if the server is not running
     * @throws UserNotFoundException - if the user was not found
     */
    public TableEntry select(String searchValue, String searchRecord, TableEntry entry, String table)
            throws Exception {

        // Approute for select is always select
        String approute = "select";

        if(!this.pingServer(ApplicationData.SERVER_IP)){
            throw new ServerNotRunningException();
        }

        try {
            String parameterString = "search=" + searchValue
                    + "&record=" + searchRecord + "&table=" + table;

            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,
                    approute, false, parameterString);

            Log.d("debug", parameterString);

            WebTask task = new WebTask(url);
            task.execute((Void) null);

            while(!task.grabString){
                // Busy wait until the connection is done
                //Log.d("debug", "Busy waiting until connection is done");
            }
            String stringEntry = task.dataString;

            // If the server returned a 0, nothing was found
            if(stringEntry.charAt(0) == '0'){
                throw new Exception();
            }

            // If there was an issue, a 2 will be returned
            if(stringEntry.charAt(0) == '-' && stringEntry.charAt(1) == '1'){
                throw new Exception("Search query wasn't filled out right");
            }

            Log.d("debug", "DEBUG:" + stringEntry);

            // Parse the string into an arraylist
            ArrayList<String> parsedList = CommaListParser.parseString(stringEntry);

            // build the object using the arraylist as a parameter
            if(entry instanceof UserTableEntry)
                entry = new UserTableEntry(parsedList);
            else if(entry instanceof ProgressTableEntry)
                entry = new ProgressTableEntry(parsedList);
            else if(entry instanceof TaskTableEntry) {
                entry = new TaskTableEntry(parsedList);
            }
            else if(entry instanceof ProjectTableEntry)
                entry = new ProjectTableEntry(parsedList);

            return entry;

        } catch (MalformedURLException e) {
            throw new ServerNotRunningException("Some error occurred building URL");
        } catch (IOException e) {
            throw new ServerNotRunningException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    /* Made A Second One Just So I Don't Break Things */
    public ArrayList<TableEntry> selectAllTwo(String searchValue,
                                           String searchRecord,
                                           TableEntry entry,
                                           String table) throws ServerNotRunningException{
        // Approute for select is always select
        String approute = "select";

        if (!this.pingServer(ApplicationData.SERVER_IP)) {
            throw new ServerNotRunningException();
        }

        ArrayList<TableEntry> messageList = new ArrayList<>();

        try {
            String parameterString = "search=" + searchValue
                    + "&record=" + searchRecord + "&table=" + table;

            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,
                    approute, false, parameterString);

            Log.d("debug", parameterString);

            WebTask task = new WebTask(url);
            task.execute((Void) null);

            while (!task.grabString) {
                // Busy wait until the connection is done
                //Log.d("debug", "Busy waiting until connection is done");
            }
            String stringEntry = task.dataString;

            // If the server returned a 0, nothing was found
            if(stringEntry.charAt(0) == '0'){
                throw new Exception("Nothing was found");
            }

            // If there was an issue, a 2 will be returned
            if(stringEntry.charAt(0) == '-' && stringEntry.charAt(1) == '1'){
                throw new Exception("Search query wasn't filled out right");
            }

            // Break into pieces
            ArrayList<String> messageArray = new ArrayList<>();
            Log.d("debug", "TEST: " + stringEntry);
            //stringEntry = stringEntry.replace(")","");
            //stringEntry = stringEntry.replace("(","");
            String[] messageDB = stringEntry.split("\\(");



            for(int i = 0; i < messageDB.length; i++){
                // Remove last piece of python list
                messageDB[i] = messageDB[i].replace(")","");
                messageDB[i] = messageDB[i].replace("(", "");
                String[] messageParts = messageDB[i].split(",");

                messageArray = new ArrayList<>();

                for(int j = 0; j < messageParts.length; j++){
                    // Remove last piece of python list
                    //String[] message = messages[i].split(",");
                    //message = message[.replace("),","");

                    //remove uniqueId section
                    if(messageParts[j].length() < 3){
                        messageParts[j] = "";
                    } else {
                        messageParts[j] = messageParts[j].substring(3, messageParts[j].length()-1);
                    }

                    // Remove final bracket
                    //member.replace("\\)]", "");

                    // Print each member for debugging
                    Log.d("debug", messageParts[j]);
                    messageArray.add(messageParts[j]);
                }

                messageList.add(new MessageTableEntry(messageArray));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messageList;
    }

    public ArrayList<TableEntry> selectAll(String searchValue,
                                           String searchRecord,
                                           TableEntry entry,
                                           String table) throws ServerNotRunningException{
        // Approute for select is always select
        String approute = "select";

        if (!this.pingServer(ApplicationData.SERVER_IP)) {
            throw new ServerNotRunningException();
        }

        ArrayList<TableEntry> memberList = new ArrayList<>();

        try {
            String parameterString = "search=" + searchValue
                    + "&record=" + searchRecord + "&table=" + table;

            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,
                    approute, false, parameterString);

            Log.d("debug", parameterString);

            WebTask task = new WebTask(url);
            task.execute((Void) null);

            while (!task.grabString) {
                // Busy wait until the connection is done
                //Log.d("debug", "Busy waiting until connection is done");
            }
            String stringEntry = task.dataString;

            // If the server returned a 0, nothing was found
            if(stringEntry.charAt(0) == '0'){
                throw new Exception("Nothing was found");
            }

            // If there was an issue, a 2 will be returned
            if(stringEntry.charAt(0) == '-' && stringEntry.charAt(1) == '1'){
                throw new Exception("Search query wasn't filled out right");
            }

            // Break into pieces
            ArrayList<String> memberArray = new ArrayList<>();
            String[] members = stringEntry.split("\\(");

            for(int i = 0; i < members.length; i++){
                // Remove last piece of python list
                String member = members[i];
                member = member.replace("),", "");

                //remove uniqueId section
                if(member.length() < 3){
                    member = "";
                } else {
                    member = member.substring(3, member.length());
                }

                // Remove final bracket
                member.replace("\\)]", "");

                // Print each member for debugging
                Log.d("debug", member);
                memberArray.add(member);
            }

            // Break each item in memberArray into a usertableentry for memberlist
            ArrayList<String> builder = new ArrayList<>();
            for(String member : memberArray){
                // handle empty entry
                if(member.length() < 3){
                    continue;
                } else {
                    builder.add(member);
                }
            }

            if(entry instanceof UserTableEntry)
                memberList.add(new UserTableEntry(builder));
            else if(entry instanceof ProgressTableEntry)
                memberList.add(new ProgressTableEntry(builder));
            else if(entry instanceof TaskTableEntry)
                memberList.add(new TaskTableEntry(builder));
            else if(entry instanceof ProjectTableEntry)
                memberList.add(new ProjectTableEntry(builder));
            else if(entry instanceof MessageTableEntry)
                memberList.add(new MessageTableEntry(builder));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberList;
    }

    public ArrayList<String> getAll(String paramString) throws ServerNotRunningException {
        // Approute for select is always select
        String approute = "getall";

        if (!this.pingServer(ApplicationData.SERVER_IP)) {
            throw new ServerNotRunningException();
        }

        ArrayList<String> memberList = new ArrayList<>();

        try {
            String parameterString = paramString;

            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,
                    approute, false, parameterString);

            Log.d("debug", "ParamString" + parameterString);

            WebTask task = new WebTask(url);
            task.execute((Void) null);

            while (!task.grabString) {
                // Busy wait until the connection is done
                //Log.d("debug", "Busy waiting until connection is done");
            }
            String stringEntry = task.dataString;

            Log.d("debug", "From DB" + stringEntry);

            // Break into pieces
            ArrayList<String> memberArray = new ArrayList<>();
            String[] members = stringEntry.split("\\(");

            for(int i = 0; i < members.length; i++){
                // Remove last piece of python list
                String member = members[i];
                member = member.replace("),", "");

                //remove uniqueId section
                if(member.length() < 3){
                    member = "";
                } else {
                    if("".equals(paramString)) {
                        member = member.substring(3, member.length());
                    } else {
                        //do nothing, keep the whole string
                    }
                }

                // Remove final bracket
                member.replace("\\)]", "");

                // Print each member for debugging
                Log.d("debug", member);
                memberArray.add(member);
            }

            // Break each item in memberArray into a usertableentry for memberlist
            for(String member : memberArray){
                // handle empty entry
                if(member.length() < 3){
                    continue;
                } else {
                    String[] memberAttribs = member.split(",");
                    // 0 is first name, 1 is last name, 2 is email
                    memberList.add(memberAttribs[2]);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return memberList;
    }

        /**
     * This method reaches out to the webservice at
     * the /version route and requests the webservice version.
     * @return - The web service version as a string.
     */
    public String getWSVersion(){
        String version = null;
        if(!this.pingServer(ApplicationData.SERVER_IP)){
            return "X";
        }

        try {
            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,"version",
                    false, "");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            version = readFromURLConnection(httpURLConnection);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            return "URL Error";
        } catch (IOException e) {
            //e.printStackTrace();
            return "Can't reach page";
        }

        return version;
    }

    /**
     * Generic database call
     * @param parameterString
     * @throws Exception
     */
    public String genericCall(String parameterString, String appRoute)
            throws Exception {

        if(!this.pingServer(ApplicationData.SERVER_IP)){
            Log.d("debug","Pinging: " + ApplicationData.SERVER_IP);
            throw new ServerNotRunningException();
        }

        String returnedFromDB = "";

        try {

            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,
                    appRoute, false, parameterString);

            Log.d("debug", "URL: " + url.toString());

            WebTask task = new WebTask(url);
            task.execute((Void) null);

            while(!task.grabString){
                // Busy wait until the connection is done
                //Log.d("debug", "Busy waiting until connection is done");
            }
            String stringEntry = task.dataString;

            returnedFromDB = task.dataString;

            // If the server returned a 0, nothing was found
            if(stringEntry.charAt(0) == '0'){
                throw new Exception();
            }

            // If there was an issue, a 2 will be returned
            if(stringEntry.charAt(0) == '-' && stringEntry.charAt(1) == '1'){
                throw new Exception("Search query wasn't filled out right");
            }

            Log.d("debug", "Generic DEBUG:" + stringEntry);

            if(stringEntry.charAt(0) == '2'){
                Log.d("debug", "Something wasn't filled out correctly");
            }

            if(stringEntry.charAt(0) == '1'){
                Log.d("debug", "Successfully made Generic call");
            }

        } catch (MalformedURLException e) {
            throw new ServerNotRunningException("Some error occurred building URL");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }

        return returnedFromDB;
    }

    /**
     *
     * @param parameterString - formatted parameter string
     * @return 0 - if the user is not found
     * @return 1 - if the user is found
     * @return 2 - if the server is down
     * @return 3 - if another error occurred
     */
    public String login(String parameterString){

        String data;
        if(!this.pingServer(ApplicationData.SERVER_IP)){
            return "2";
        }

        try {
            URL url = buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT, "login", false, parameterString);

            // Generate the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            data = readFromURLConnection(urlConnection);

        } catch (MalformedURLException e) {
            Log.v("error", "Error forming URL");
            return "3";
        } catch (IOException e) {
            Log.v("error", "Error accessing url");
            return "3";
        }

        return String.valueOf(data.charAt(0));
    }

    /**
     * Attempt to create an account on the server
     *
     * @param parameterString - formatted parameter string
     * @return 0 - if create account was successful
     * @return 1 - if account already exists
     * @return 2 - if an error occurred
     * @return 3 - if the server is down
     */
    public String createAccount(String parameterString){

        String data;
        if(!this.pingServer(ApplicationData.SERVER_IP)){
            return "3";
        }

        try {
            URL url = buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT, "createaccount", false, parameterString);

            // Generate the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            data = readFromURLConnection(urlConnection);

        } catch (MalformedURLException e) {
            Log.v("error", "Error forming URL");
            return "2";
        } catch (IOException e) {
            Log.v("error", "Error accessing url");
            return "2";
        }

        return String.valueOf(data.charAt(0));
    }

    /**
     *
     * @param url - url to read from
     * @return - String returned from a web service
     */
    public static String readFromURLConnection(HttpURLConnection url) throws IOException {
        Log.d("debug", "Url: " + url);

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            reader =
                    new BufferedReader(new InputStreamReader(
                            url.getInputStream()
                    ));
            // Build String
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
        } catch (IOException io){
            throw io;
        } finally {
            // Close reader and return built string
            if(reader != null) {
                reader.close();
            }
            return builder.toString();
        }

    }

    /**
     * The method allows the caller to quickly check whether
     * a server is available for access or not.
     * @param serverIp - the ip address to attempt to read
     * @return boolean value representing the server being up or not
     */
    public boolean pingServer(String serverIp) {

        // Flag variable for ping success
        boolean success = false;

        try {
            PingHttpThread pinger = new PingHttpThread();
            Thread httpThread = new Thread(pinger);

            // Send the http ping request
            httpThread.start();

            // Wait for thread to finish
            httpThread.join();

            // Get whether the value was a success or not
            success = pinger.getSuccess();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return success;
    }

    /**
     *
     * @param url - The IP address of the URL
     * @param port - The port of the web service
     * @param path - The app route for an application
     * @param useHttps - Whether or not to use Https in the URL
     * @param getParameters - null string if no params, formatted parameter string if valid
     * @return a URL object that is created using the parameters
     */
    public static URL buildURL(String url, String port, String path, boolean useHttps, String getParameters) throws MalformedURLException {
        StringBuilder builder = new StringBuilder();

        // Add the protocol
        if(useHttps)
            builder.append("https://");
        else
            builder.append("http://");

        // Add the URL
        builder.append(url);

        // Add the port
        builder.append(":");
        builder.append(port);

        // Add the app route
        builder.append("/");
        builder.append(path);

        // Add the parameter list if getParameters is not null
        if(getParameters != null){
            builder.append("?");
            builder.append(getParameters);
        }

        return new URL(builder.toString());
    }
}
