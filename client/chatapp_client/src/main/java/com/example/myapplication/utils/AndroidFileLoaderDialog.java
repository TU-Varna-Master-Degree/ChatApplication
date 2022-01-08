package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.IOException;
import java.util.function.Consumer;

public class AndroidFileLoaderDialog
{
    private final ActivityResultLauncher launcher;
    private final Consumer<AndroidLocalFileData> onFileLoadedCallback;
    private final ComponentActivity activity;
    
    public AndroidFileLoaderDialog(ComponentActivity activity, Consumer<AndroidLocalFileData> onFileLoadedCallback)
    {
        launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);
        this.onFileLoadedCallback = onFileLoadedCallback;
        this.activity = activity;
    }
    
    private void onActivityResult(ActivityResult result)
    {
        if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
        {
            new Thread( () -> this.loadFileAndNotify( result.getData().getData()) ) .start();
        }
    }
    
    private void loadFileAndNotify(Uri uri)
    {
        try
        {
            AndroidLocalFileData file = new AndroidLocalFileData(activity, uri );
            activity.runOnUiThread(()-> onFileLoadedCallback.accept( file ));
        }
        catch (IOException e)
        {
            activity.runOnUiThread( () -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG ));
        }
    }
    
    public void startModalDialog()
    {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        
        launcher.launch(Intent.createChooser(intent, "Select a file"));
    }
    
}