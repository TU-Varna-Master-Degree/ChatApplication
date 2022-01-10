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

public class FileLoaderDialog {

    private final ActivityResultLauncher launcher;
    private final Consumer<LocalFileData> onFileLoadedCallback;
    private final ComponentActivity activity;
    private final NetClient client;

    public FileLoaderDialog(ComponentActivity activity, Consumer<LocalFileData> onFileLoadedCallback, NetClient client) {
        launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);
        this.onFileLoadedCallback = onFileLoadedCallback;
        this.activity = activity;
        this.client = client;
    }

    public void startModalDialog() {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        launcher.launch(Intent.createChooser(intent, "Select a file"));
    }

    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            client.executeTask(() -> this.loadFileAndNotify(result.getData().getData()));
        }
    }

    private void loadFileAndNotify(Uri uri) {
        try {
            LocalFileData file = new LocalFileData(activity, uri);
            activity.runOnUiThread(() -> onFileLoadedCallback.accept(file));
        } catch (IOException e) {
            activity.runOnUiThread(() -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG));
        }
    }
}