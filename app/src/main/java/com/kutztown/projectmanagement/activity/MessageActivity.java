package com.kutztown.projectmanagement.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.MessageTableEntry;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.data.TableEntry;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MessageActivity extends AppCompatActivity {

    /**
     * Attribute containing array of messages
     */
    ArrayList<String> messageArray = null;

    /**
     * Listview that holds values from messageArray
     */
    ListView messageListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu12);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        Toast.makeText(getApplicationContext(), "Please click inside edit text for keyboard", Toast.LENGTH_LONG).show();
        checkTheme();

        Intent intent = getIntent();
        final String receiver = intent.getExtras().getString("receiver");

        final String sender = ApplicationData.currentUser.getEmail().substring(2, ApplicationData.currentUser.getEmail().length() - 1);

        this.messageArray = getMessagesBetweenUsers(sender, receiver);

        if (this.messageArray == null) {
            this.messageArray = new ArrayList<>();
        }

        messageListView = (ListView) findViewById(R.id.messageListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.left_message, messageArray);
        messageListView.setAdapter(adapter);

        // Retrieve messages of current user;
        //Log.d("debug", "MESSAGES: " + getMessagesbetweenUsers().get(0).toString());
        //this.messageArray = getMessagesBetweenUsers();

        /*if (this.messageArray == null) {
            this.messageArray = new ArrayList<>();
        }*/

        final EditText messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.requestFocus();

        final Button send = (Button) findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";

                //message = "You: " + messageEditText.getText().toString();
                message = messageEditText.getText().toString();
                messageArray.add(sender.substring(0, sender.indexOf("@")) + ": " + message);

                HTTPHandler handler = new HTTPHandler();
                MessageTableEntry entry = new MessageTableEntry(sender,receiver,message);

                try {
                    // Add the message
                    handler.insert(entry, "MessageTable");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("debug", "Error inserting new message into message table");
                }

                adapter.notifyDataSetChanged();
                messageEditText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, ApplicationData.amvMenu.getMenu());
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);
    }

    /**
     * Retrieve the projects from the logged in user and parse them into an arraylist
     * @return null if no projects, else arraylist of projects
     */
    protected ArrayList<String> getMessagesBetweenUsers(String sender, String receiver){

        ArrayList<String> messageArray = new ArrayList<String>();
        ArrayList<MessageTableEntry> totalMessageArray = new ArrayList<MessageTableEntry>();
        ArrayList<TableEntry> senderTableArray = new ArrayList<TableEntry>();
        ArrayList<TableEntry> receiverTableArray = new ArrayList<TableEntry>();

        // Get the message
        MessageTableEntry senderEntry = null;
        MessageTableEntry receiverEntry = null;
        try {
            senderTableArray = new HTTPHandler().selectAllTwo(sender, "Sender", new MessageTableEntry(), "MessageTable");
            receiverTableArray = new HTTPHandler().selectAllTwo(receiver, "Sender", new MessageTableEntry(), "MessageTable");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("debug", "ERROR: could not find messages");
        }

        for(TableEntry senderMessageTable : senderTableArray){
            senderEntry = (MessageTableEntry) senderMessageTable;
            Log.d("debug", senderEntry.getMessage());

            if(senderEntry.getReceiver().equals(receiver))
            {
                senderEntry.setMessage(sender.substring(0, sender.indexOf("@")) + ": " + senderEntry.getMessage());
                totalMessageArray.add(senderEntry);
            }
        }

        for(TableEntry receiverMessageTable : receiverTableArray){
            receiverEntry = (MessageTableEntry) receiverMessageTable;
            Log.d("debug", receiverEntry.getMessage());

            if(receiverEntry.getReceiver().equals(sender))
            {
                receiverEntry.setMessage(receiver.substring(0, receiver.indexOf("@")) + ": " + receiverEntry.getMessage());
                totalMessageArray.add(receiverEntry);
            }
        }

        Collections.sort(totalMessageArray);

        for(MessageTableEntry total : totalMessageArray){
            messageArray.add(total.getMessage().replace("_"," "));
        }

        return messageArray;
    }

    public void checkTheme()
    {
        //bigImg = (ImageView) findViewById(R.id.big_logo);
        ImageView miniLogo = (ImageView) findViewById(R.id.mini_logo);
        LinearLayout colorBar = (LinearLayout) findViewById(R.id.color_bar);

        if(!ApplicationData.theme.equals("default"))
        {
            switch(ApplicationData.theme)
            {
                case "Banana":
                        colorBar.setBackgroundResource(R.color.color_banana);
                        miniLogo.setImageResource(R.drawable.banner_logo_banana);
                        //bigImg.setImageResource(R.drawable.banner_logo_banana);
                        ApplicationData.theme = "Banana";
                        break;
                    case "Peach":
                        colorBar.setBackgroundResource(R.color.color_peach);
                        miniLogo.setImageResource(R.drawable.banner_logo_peach);
                        //bigImg.setImageResource(R.drawable.banner_logo_peach);
                        ApplicationData.theme = "Peach";
                        break;
                    case "Strawberry":
                        colorBar.setBackgroundResource(R.color.color_strawberry);
                        miniLogo.setImageResource(R.drawable.banner_logo_strawberry);
                        //bigImg.setImageResource(R.drawable.banner_logo_strawberry);
                        ApplicationData.theme = "Strawberry";
                        break;
                    case "Mellon":
                        colorBar.setBackgroundResource(R.color.color_mellon);
                        miniLogo.setImageResource(R.drawable.banner_logo_mellon);
                        //bigImg.setImageResource(R.drawable.banner_logo_mellon);
                        ApplicationData.theme = "Mellon";
                        break;
                    default:
                        colorBar.setBackgroundResource(R.color.color_default);
                        miniLogo.setImageResource(R.drawable.banner_logo);
                        //bigImg.setImageResource(R.drawable.banner_logo);
                        ApplicationData.theme = "Default";
                        break;
            }
        }
    }
}
