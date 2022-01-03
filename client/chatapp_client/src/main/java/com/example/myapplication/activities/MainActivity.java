package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.NetClient;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements ActivityResultCallback<ActivityResult> {

    ActivityResultLauncher<Intent> startForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                MainActivity.this);

        NetClient.start(super::runOnUiThread, this);

        Intent intent = new Intent(this, LoginActivity.class);
        startForResult.launch(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        new Thread(()-> {
            try
            {
                NetClient.stop();
            }
            catch(IOException e)
            {
                runOnUiThread( () ->
                {
                    Toast.makeText(MainActivity.this, "Failed to close server connection.", Toast.LENGTH_LONG).show();
                });
            }
        
        }).start();
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if(result.getResultCode() == LoginActivity.RESULT_OK) {
            Intent intent = new Intent(this, HomeActivity.class);
            startForResult.launch(intent);
        }
        else
        {
            finish();
        }
    }
}