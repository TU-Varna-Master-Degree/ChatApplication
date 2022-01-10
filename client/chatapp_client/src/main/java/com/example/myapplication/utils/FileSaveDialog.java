package com.example.myapplication.utils;

import android.net.Uri;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.domain.models.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class FileSaveDialog implements ActivityResultCallback<Uri> {

    private final ActivityResultLauncher<String> launcher;
    private final Consumer<Uri> onFileSaveCallback;
    private final ComponentActivity activity;
    private Message data;

    public FileSaveDialog(ComponentActivity activity, Consumer<Uri> onFileSaveCallback) {
        launcher = activity.registerForActivityResult(new ActivityResultContracts.CreateDocument(), this);
        this.onFileSaveCallback = onFileSaveCallback;
        this.activity = activity;
    }

    public void openModalSaveAs(Message data) {
        this.data = data;
        launcher.launch(String.format("%s.%s", data.getFileName(), data.getFileType()));
    }

    @Override
    public void onActivityResult(Uri uri) {
        if (uri != null) {
            try (OutputStream outputStream = activity.getContentResolver().openOutputStream(uri)) {
                outputStream.write(data.getFile());
            } catch (IOException e) {
                activity.runOnUiThread(() -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG));
            }
            onFileSaveCallback.accept(uri);
        }
    }
}
