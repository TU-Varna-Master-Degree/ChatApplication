package com.example.myapplication;

import android.app.Application;
import android.widget.Toast;

import com.example.myapplication.utils.NetClient;

import java.io.IOException;

public class ChatApplication extends Application {

    private static final String SERVER_ADDRESS = "95.42.42.125";
    private static final int SERVER_PORT = 1300;

    private final NetClient client = new NetClient(SERVER_ADDRESS, SERVER_PORT);

    @Override
    public void onCreate() {
        super.onCreate();
        client.start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            client.stop();
        } catch (IOException e) {
            Toast.makeText(ChatApplication.this, "Failed to gracefully close server connection.", Toast.LENGTH_LONG).show();
        }
    }

    public NetClient getNetClient() {
        return client;
    }
}