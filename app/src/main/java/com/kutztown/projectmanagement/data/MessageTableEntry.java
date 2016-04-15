package com.kutztown.projectmanagement.data;

import android.util.Log;

import java.sql.Blob;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Steven Gsntz
 * @date 3/26/2016.
 * Modified by Tyler Mariano 4/13/2016
 */
public class MessageTableEntry implements TableEntry, Comparable<MessageTableEntry> {

    // Attribute list
    public int MessageId;
    public String Sender;
    public String Receiver;
    public String Subject;
    public String Message;
    //public Blob document;
    public String Document;
    public Date Date;

    /**
     * Empty constructor for construction purposes
     */
    public MessageTableEntry(){

    }

    public MessageTableEntry(String Sender, String Receiver, String Message) {
        this.MessageId = -1;
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Subject = "";
        this.Message = Message;
        this.Document = "";
        //SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM d HH:mm:ss zz yyyy");
        //Date d = new Date();
        this.Date = new Date();
    }

    /**
     * Secondary constructor, takes an arraylist of each element in an entry and builds the object.
     *
     * @param messageTableEntry - the arraylist of entries
     **/
    public MessageTableEntry(ArrayList<String> messageTableEntry) {

        // The arraylist should have all of the entries in the order that they are in the table.
        //this.MessageId = Integer.parseInt(messageTableEntry.get(0));
        this.Sender = messageTableEntry.get(1);
        this.Receiver = messageTableEntry.get(2);
        this.Subject = messageTableEntry.get(3);
        this.Message = messageTableEntry.get(4);
        this.Document = messageTableEntry.get(5);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zz yyyy");
        String dateStr = messageTableEntry.get(6);
        //dateStr = dateStr.substring(2, dateStr.length()-1);
        ParsePosition pos = new ParsePosition(0);

        this.Date = sdf.parse(dateStr.replace("_", " "),pos);

        Log.d("debug", "DATE: " + dateStr);
    }

    public MessageTableEntry(int MessageId, String Sender, String Receiver, String Subject, String Message) {
        this.MessageId = MessageId;
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Subject = Subject;
        this.Message = Message;
        this.Date = new Date();
    }

    public MessageTableEntry(String Sender, String Receiver,
                             String Subject, String Message) {
        //this.MessageId = MessageId;
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Subject = Subject;
        this.Message = Message;
        this.Date = new Date();
    }

    /*public void setBlob(Blob doc) {
        this.document = doc;
    }*/

    @Override
    public String writeAsGet() {
        return "&Sender=" + this.Sender +
                "&Receiver=" + this.Receiver +
                "&Subject=" + this.Subject +
                "&Message=" + this.Message +
                "&Document=" + this.Document +
                "&Date=" + this.Date;
    }

    @Override
    public String writeAsValueString() {
        return "\"" + this.Sender + "\",\"" +
                this.Receiver + "\",\"" +
                this.Subject + "\",\"" +
                this.Message.replace(" ", "_") + "\",\"" +
                this.Document + "\",\"" +
                this.Date.toString().replace(" ", "_") + "\"";
    }

    public void setMessage(String message)
    {
        this.Message = message;
    }

    public String getSender()
    {
        return Sender;
    }

    public String getReceiver()
    {
        return Receiver;
    }

    public String getMessage()
    {
        return Message;
    }

    public Date getDateTime()
    {
        return Date;
    }

    public ArrayList<String> getConversation(String senderEmail, String receiverEmail)
    {
        ArrayList<String> conversation = new ArrayList<String>();

        for(String convo : conversation){

        }

        return conversation;
    }

    @Override
    public int compareTo(MessageTableEntry o) {
        if (getDateTime() == null || o.getDateTime() == null)
        {
            Log.d("debug", "ERROR: Null Message Date");
            return 0;
        }
        return getDateTime().compareTo(o.getDateTime());
    }

    @Override
    public String getColumnString() {
        return "Sender,Receiver,Subject,Message,Document,Date";
    }
}
