package com.kutztown.projectmanagement.data;

import java.sql.Blob;
import java.util.Date;

/**
 * @author Steven Gsntz
 * @date 3/26/2016.
 */
public class MessageTableEntry implements TableEntry {

    // Attribute list
    public int MessageId;
    public String Sender;
    public String Receiver;
    public String Subject;
    public String Message;
    public Blob document;
    public Date date;

    MessageTableEntry(int MessageId, String Sender, String Receiver,
                      String Subject, String Message) {
        this.MessageId = MessageId;
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Subject = Subject;
        this.Message = Message;
        this.date = new Date();
    }

    public void setBlob(Blob doc) {
        this.document = doc;
    }

    @Override
    public String writeAsGet() {
        return "MessageID=" + this.MessageId +
                "&Sender=" + this.Sender +
                "&Receiver=" + this.Receiver +
                "&Subject=" + this.Subject +
                "&Message=" + this.Message +
                "&Date=" + this.date;
    }

    @Override
    public String writeAsValueString() {
        return "\"" + this.MessageId + "\",\"" +
                this.Sender + "\",\"" +
                this.Receiver + "\",\"" +
                this.Subject + "\",\"" +
                this.Message + "\",\"" +
                this.date;
    }

    @Override
    public String getColumnString() {
        return "MessageID,Sender,Receiver,Subject,Message,Date";
    }
}
