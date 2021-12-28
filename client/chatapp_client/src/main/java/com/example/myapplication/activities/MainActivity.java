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
        implements ActivityResultCallback<ActivityResult>
   
{
    private static final String SERVER_ADDRESS = "95.42.42.125";
    private static final int SERVER_PORT = 1300;
    
    public static final String SERVER_HANDLER = "SERIALIZABLE_SERVER_HANDLER";
    
    ActivityResultLauncher<Intent> startForResult;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                MainActivity.this);
        
        new Thread( () ->
        {
            try
            {
                // Initialize Connection
                NetClient.start(SERVER_ADDRESS, SERVER_PORT);
            }
            catch(IOException e)
            {
                runOnUiThread( () ->
                {
                    Toast.makeText(MainActivity.this, "Failed to connect server.", Toast.LENGTH_LONG).show();
                });
            }
            
        }).start();
    
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startForResult.launch( intent );
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // new Thread(()-> {
        //     try
        //     {
        //         NetClient.stop();
        //     }
        //     catch(IOException e)
        //     {
        //         runOnUiThread( () ->
        //         {
        //             Toast.makeText(MainActivity.this, "Failed to close server connection.", Toast.LENGTH_LONG).show();
        //         });
        //     }
        //
        // }).start();
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