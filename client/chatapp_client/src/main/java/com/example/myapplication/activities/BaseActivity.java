package com.example.myapplication.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ChatApplication;
import com.example.myapplication.domain.dialogue.ServerResponse;
import com.example.myapplication.utils.NetClient;

import java.util.function.Consumer;

public abstract class BaseActivity extends AppCompatActivity {

    private NetClient client;
    private Consumer<ServerResponse> handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = ((ChatApplication) getApplication()).getNetClient();
        handler = this::onResponse;
    }

    @Override
    protected void onStart() {
        super.onStart();
        client.register(handler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.unregister(handler);
    }

    protected abstract void onResponse(ServerResponse response);

    protected NetClient getNetClient() {
        return client;
    }
}
