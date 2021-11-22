package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        startActivity( new Intent(this, ServerConnectionTestActivity.class) );
        /*
        setContentView( R.layout.register_activity);
        
        findViewById( R.id.btn_reg_exit )
                .setOnClickListener( view -> MainActivity.this.finish() );*/
    }
}