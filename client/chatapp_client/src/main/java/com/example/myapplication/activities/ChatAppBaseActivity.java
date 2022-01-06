package com.example.myapplication.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ChatApplication;
import com.example.myapplication.utils.NetClient;

import domain.client.dialogue.ServerResponse;

public class ChatAppBaseActivity extends AppCompatActivity
{
    protected NetClient client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        client = ((ChatApplication) getApplication()).getNetClient();
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        client.register(this::onResponse);
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        client.unregister(this::onResponse);
    }
    
    protected void onResponse(ServerResponse response)
    {
    
    }
    
    protected NetClient getNetClient()
    {
        return client;
    }
}
