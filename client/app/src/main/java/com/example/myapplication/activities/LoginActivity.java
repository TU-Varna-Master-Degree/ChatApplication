package com.example.myapplication.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.myapplication.FormRuleTest;
import com.example.myapplication.R;
import com.example.myapplication.models.UserLoginModel;

public class LoginActivity extends AppCompatActivity
        implements ActivityResultCallback<ActivityResult>
{
    static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";
    static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    static final String LOGIN_REMEMBER = "LOGIN_REMEMBER";
    
    private EditText etUserLoginInput;
    private EditText etPassword;
    private CheckBox chbRememberMe;
    ActivityResultLauncher<Intent> startForResult;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        etUserLoginInput = findViewById(R.id.login_tv_input);
        etPassword = findViewById(R.id.login_tv_password);
        chbRememberMe = findViewById(R.id.login_chb_remember_me);
        
        findViewById(R.id.login_tv_register).setOnClickListener(view -> LaunchRegisterForm() );
        findViewById(R.id.login_btn_login).setOnClickListener( view -> DoLogin() );
        
        if( savedInstanceState != null )
        {
            etUserLoginInput.setText( savedInstanceState.getString(LOGIN_USER_INFO) );
            etPassword.setText( savedInstanceState.getString(LOGIN_PASSWORD));
            chbRememberMe.setChecked( savedInstanceState.getBoolean(LOGIN_REMEMBER) );
        }
        
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                LoginActivity.this);
    }
    
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(LOGIN_USER_INFO, etUserLoginInput.getText().toString());
        outState.putString(LOGIN_PASSWORD, etPassword.getText().toString());
        outState.putBoolean(LOGIN_REMEMBER, chbRememberMe.isChecked());
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        TryRememberLogin();
    }
    
    @Override
    public void onActivityResult(ActivityResult result)
    {
        if (result.getResultCode() == RegisterActivity.RESULT_OK)
        {
            Intent data = result.getData();
            assert data != null;
            Bundle bundle = data.getExtras();
            
            // TODO: Refactor
            etUserLoginInput.setText( bundle.getString("Email") );
            etPassword.setText( bundle.getString("Password") );
        }
    }
    
    private void LaunchRegisterForm()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startForResult.launch(intent);
    }
    
    private void TryRememberLogin()
    {
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        if( !pref.getBoolean( LOGIN_REMEMBER, false ) )
        {
            return;
        }
        
        UserLoginModel model = new UserLoginModel(
                pref.getString(LOGIN_USER_INFO, null),
                pref.getString(LOGIN_PASSWORD, null)
        );
    
        
        new Thread()
        {
            @Override
            public void run()
            {
                if( RequestLogin(model) )
                {
                    FinishActivityJob();
                }
            }
        }.start();
        
    }
    
    private UserLoginModel FetchModel()
    {
        String userInput = etUserLoginInput.getText().toString(),
                userPassword = etPassword.getText().toString();
    
        FormRuleTest[] rules = {
            new FormRuleTest(userInput, FormRuleTest.EMAIL_OR_PW_REG_EX),
            new FormRuleTest(userPassword, FormRuleTest.PW_REG_EX)
        };
    
        for(FormRuleTest test : rules)
        {
            if(!test.check())
                return null;
        }
        
        return new UserLoginModel(userInput, userPassword);
    }
    
    
    private void DoLogin()
    {
        UserLoginModel model = FetchModel();
        if( model == null)
        {
            // TODO: Display User Message
            return;
        }
        
        /// Attempt login in another thread
        new Thread()
        {
            @Override
            public void run()
            {
                if(RequestLogin(model))
                {
                    SaveConfig();
                    FinishActivityJob();
                }
                else
                {
                    runOnUiThread(() ->
                    {
                        // TODO: Display User Message
                    });
                }
            }
        }.start();
    }
    
    private boolean RequestLogin(UserLoginModel model)
    {
        // TODO: Client sends login request to server
        return false;
    }
    
    private void SaveConfig()
    {
        if(!chbRememberMe.isChecked() )
            return;

        SharedPreferences.Editor pref = getPreferences(Context.MODE_PRIVATE).edit();
        pref.putString(LOGIN_USER_INFO, etUserLoginInput.getText().toString());
        pref.putString(LOGIN_PASSWORD, etPassword.getText().toString());
        pref.putBoolean(LOGIN_REMEMBER, true);
        pref.apply();
    }
    
    private void FinishActivityJob()
    {
        Intent intent = getIntent();
        // TODO: Save state
        
        setResult( LoginActivity.RESULT_OK );
        this.finish();
    }
    
}