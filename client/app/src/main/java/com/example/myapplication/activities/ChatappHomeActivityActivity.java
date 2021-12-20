package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.myapplication.R;
import com.example.myapplication.models.UserGroup;
import com.example.myapplication.view.NotificationAdapter;

import java.util.ArrayList;

public class ChatappHomeActivityActivity extends AppCompatActivity
{
    
    ArrayList<UserGroup> users = new ArrayList<UserGroup>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatapp_home_activity);
        
        for (int i = 0; i < 20; i++)
        {
            users.add( new UserGroup("SampleUsername # " + i) );
        }
        
        RecyclerView view = findViewById(R.id.home_lst_feed);
        Spinner cb = findViewById(R.id.home_cb_filter);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.home_filter_opts, android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        cb.setAdapter(adapter);
        
        
        NotificationAdapter listAdapter = new NotificationAdapter(users);
        view.setLayoutManager( new LinearLayoutManager(this) );
        view.setAdapter(
                listAdapter
        );
    }
    
}
