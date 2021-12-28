package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.FormRuleTest;
import com.example.myapplication.NetClient;
import com.example.myapplication.R;
import com.example.myapplication.models.UserLoginModel;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;


public class LoginActivity extends AppCompatActivity
        implements ActivityResultCallback<ActivityResult>
{
    static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";
    static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    static final String LOGIN_REMEMBER = "LOGIN_REMEMBER";
    
    private EditText etUserLoginInput;
    private EditText etPassword;
    private CheckBox chbRememberMe;
    
    FormRuleTest[] rules;
    
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
        
        if(Debug.isDebuggerConnected())
        {
            SetDebugInfo();
        }
        
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                LoginActivity.this);
        
        rules = new FormRuleTest[]
        {
                new FormRuleTest(etUserLoginInput, FormRuleTest.EMAIL_OR_PW_REG_EX),
                new FormRuleTest(etPassword, FormRuleTest.PW_REG_EX)
        };
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
        NetClient.register(this::onServerResponse);
        
        TryRememberLogin();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        NetClient.unregister(this::onServerResponse);
    }
    
    @Override
    public void onActivityResult(ActivityResult result)
    {
        if (result.getResultCode() == RegisterActivity.RESULT_OK)
        {
            Intent data = result.getData();
            // TODO: Fill login with register data
        }
    }

    
    public void onServerResponse(ServerResponse response)
    {
        if(response.getOperationType() == OperationType.USER_LOGIN)
        {
            CompleteActivity(response);
        }
    }
    
    @SuppressLint("SetTextI18n")
    private void SetDebugInfo()
    {
        etUserLoginInput.setText("nvmcomrade");
        etPassword.setText("1q2w#E4r5t");
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
    
        RequestLogin(model);
    }
    
    private UserLoginModel FetchModel()
    {
        String userInput = etUserLoginInput.getText().toString(),
                userPassword = etPassword.getText().toString();
        
        for(FormRuleTest test : rules)
        {
            if(!test.check())
                
                return null;
        }
        
        return new UserLoginModel(userInput, userPassword);
    }
    
    
    private void DoLogin()
    {
        UserLoginModel model;
        if( (model = FetchModel()) != null)
        {
            RequestLogin(model);
        }
    }
    
    private void RequestLogin(UserLoginModel model)
    {
        // TODO: Client sends login request to server
        Object data = null;
        
        ServerRequest request = new ServerRequest(OperationType.USER_LOGIN);
        request.setData(data);
        
        NetClient.sendRequest( request );
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
    
    private void CompleteActivity(ServerResponse response)
    {
        if(response.getCode() == StatusCode.SUCCESSFUL)
        {
            SaveConfig();
            Intent intent = getIntent();
            // TODO: Save state
    
            setResult( LoginActivity.RESULT_OK );
            this.finish();
        }
        else
        {
            runOnUiThread(() ->
            {
                Toast.makeText(LoginActivity.this, "Failed to login.", Toast.LENGTH_LONG).show();
            });
        }
    }
    
    
}