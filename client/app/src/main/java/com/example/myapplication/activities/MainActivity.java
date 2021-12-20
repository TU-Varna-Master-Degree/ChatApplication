package com.example.myapplication.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.ServerHandler;

public class MainActivity extends AppCompatActivity
        implements ActivityResultCallback<ActivityResult>
   
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    
        new Thread()
        {
            @Override
            public void run()
            {
                ServerHandler.start();
            }
            
        }.start();
        
        
        // Login
        Intent intent = new Intent(this, LoginActivity.class);
        
        ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                MainActivity.this);
        startForResult.launch( intent );

    }
    
    
    @Override
    public void onActivityResult(ActivityResult result)
    {
        if(result.getResultCode() == LoginActivity.RESULT_OK)
        {
            // GO to home screen
            Intent intent = new Intent(this, ChatappHomeActivityActivity.class);
            startActivity( intent );
        }
    }
}