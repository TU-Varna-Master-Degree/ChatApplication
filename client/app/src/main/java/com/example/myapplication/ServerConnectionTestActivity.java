package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerConnectionTestActivity extends AppCompatActivity
{
    private static final int SERVER_PORT = 1337;
    private static final String SERVER_IP = "95.42.42.125";
    private final List<String> messages = new ArrayList<String>();
    
    private final Client client = new Client(SERVER_IP, SERVER_PORT);
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.server_connection_test );
        
        ((Button)findViewById(R.id.btn_send)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doSend( popEditTextMessage() );
            }
        });
    
        ((ListView)findViewById(R.id.lv_messages)).setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages));
        
    }
    
    private String popEditTextMessage()
    {
        EditText et = (EditText) findViewById(R.id.et_message);
        String message = et.getText().toString();
        
        et.setText("");
        
        return message;
    }
    
    private void doSend(String msg)
    {
        if ( msg.equals("") )
            return;
        
        messages.add( "CLIENT: " + msg );
    
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    client.open();
                    client.sendMessage( msg );
                    String received = client.receiveMessage();
                    client.close();
                    
                    // update view
                    messages.add( "SERVER: " + received );
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        
        }.start();
    }
   
}