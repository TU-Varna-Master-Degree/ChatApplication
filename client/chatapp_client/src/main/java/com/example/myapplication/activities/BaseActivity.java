package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
        
        setupUI( findViewById( android.R.id.content ).getRootView() );
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
    
    private  void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
    
    private void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) ->
            {
                hideSoftKeyboard();
                return false;
            });
        }
        
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
