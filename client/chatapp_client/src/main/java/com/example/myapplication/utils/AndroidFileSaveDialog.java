package com.example.myapplication.utils;

import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.domain.models.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class AndroidFileSaveDialog implements ActivityResultCallback<Uri>
{
    private final ActivityResultLauncher<String> launcher;
    private final Consumer<Uri> onFileSaveCallback;
    private final ComponentActivity activity;
    private Message data;
    
    public AndroidFileSaveDialog(ComponentActivity activity, Consumer<Uri> onFileSaveCallback)
    {
        launcher = activity.registerForActivityResult(new ActivityResultContracts.CreateDocument(), this);
        this.onFileSaveCallback = onFileSaveCallback;
        this.activity = activity;
    }
    
    public void openModalSaveAs( Message data )
    {
        // TODO: Downloaded source MsgDto???
        this.data = data;
        
        // Start activity to prompt user for save location
        launcher.launch( data.getFileName() );
    }
    
    @Override
    public void onActivityResult(Uri result)
    {
        String path = result.getPath();
        path = result.getEncodedPath();
        result.getPathSegments();
        // Write content
        
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        FileOutputStream outputStreamWriter;
        try
        {
           
            outputStreamWriter =  new FileOutputStream(result.getPath(), false);
                    // activity.openFileOutput(result.getPath(), Context.MODE_APPEND);
            outputStreamWriter.write(data.getFile());
            outputStreamWriter.flush();
            onFileSaveCallback.accept(result);
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            activity.runOnUiThread( ()-> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG));
        }
    
        
    }
   
}
