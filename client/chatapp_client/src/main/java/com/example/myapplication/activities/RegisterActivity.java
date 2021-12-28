package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.FormRuleTest;
import com.example.myapplication.NetClient;
import com.example.myapplication.R;
import com.example.myapplication.models.UserRegisterModel;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;

public class RegisterActivity extends AppCompatActivity
{
    static final String REGISTER_USER_NAME = "REGISTER_USER_NAME";
    static final String REGISTER_EMAIL = "REGISTER_EMAIL";
    static final String REGISTER_PASSWORD = "REGISTER_PASSWORD";
    static final String REGISTER_CONFIRM_PASSWORD = "REGISTER_CONFIRM_PASSWORD";
    
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    
    FormRuleTest[] rules;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
    
        etUsername = findViewById(R.id.reg_et_username);
        etEmail = findViewById(R.id.reg_et_email);
        etPassword = findViewById(R.id.reg_et_pw);
        etConfirmPassword = findViewById(R.id.reg_et_pw_confirm);
        
        findViewById( R.id.btn_reg_exit )
                .setOnClickListener( view ->
                        {
                            setResult(RESULT_CANCELED);
                            RegisterActivity.this.finish();
                        });
        
        findViewById(R.id.btn_reg_confirm)
                .setOnClickListener( view ->  DoRegister());
        
        if(savedInstanceState != null)
        {
            etUsername.setText( savedInstanceState.getString(REGISTER_USER_NAME) );
            etEmail.setText( savedInstanceState.getString(REGISTER_EMAIL) );
            etPassword.setText( savedInstanceState.getString(REGISTER_PASSWORD) );
            etConfirmPassword.setText( savedInstanceState.getString(REGISTER_CONFIRM_PASSWORD) );
        }
        
        if( Debug.isDebuggerConnected())
        {
            SetDebugInfo();
        }
        
        rules = new FormRuleTest[] {
                new FormRuleTest( etUsername, FormRuleTest.USER_REG_EX),
                new FormRuleTest( etPassword, FormRuleTest.PW_REG_EX),
                new FormRuleTest( etEmail, FormRuleTest.EMAIL_REG_EX),
        };
    }
    
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(REGISTER_USER_NAME, etUsername.getText().toString());
        outState.putString(REGISTER_EMAIL, etEmail.getText().toString());
        outState.putString(REGISTER_PASSWORD, etPassword.getText().toString());
        outState.putString(REGISTER_CONFIRM_PASSWORD, etConfirmPassword.getText().toString());
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        NetClient.register(this::onServerResponse);
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        NetClient.unregister(this::onServerResponse);
    }
    
    @SuppressLint("SetTextI18n")
    private void SetDebugInfo()
    {
        etUsername.setText("nvmcomrade");
        etEmail.setText("vonamedog.d@gmail.com");
        etPassword.setText("1q2w#E4r5t");
        etConfirmPassword.setText("1q2w#E4r5t");
    }
    
    private void DoRegister()
    {
        UserRegisterModel model;
        if((model = FetchFormModel()) != null)
        {
            RequestRegister(model);
        }
    }
    
    private void RequestRegister(UserRegisterModel model)
    {
        UserDto user = new UserDto();
        user.setEmail( model.getEmail() );
        user.setPassword( model.getPassword() );
        user.setUsername( model.getUsername() );
    
        ServerRequest<UserDto> request =
                new domain.client.dialogue.ServerRequest<>(OperationType.USER_REGISTER);
        request.setData( user );
        
        NetClient.sendRequest(request);
    
        // TODO: Block user controls and animation
    }
    
    private void CompleteRegister(ServerResponse model)
    {
        if(model.getCode() == StatusCode.SUCCESSFUL)
        {
            Intent intent = getIntent();
            
            // TODO: write response data to intent
            
            setResult(RESULT_OK);
            RegisterActivity.this.finish();
        }
        else
        {
            runOnUiThread(()->
            {
                Toast.makeText(this, "Failed to register", Toast.LENGTH_LONG).show();
            });
        }
    }
    
    
    private UserRegisterModel FetchFormModel()
    {
        String password = etPassword.getText().toString(),
                confirm_password = etConfirmPassword.getText().toString(),
        user = etUsername.getText().toString(),
        mail =  etEmail.getText().toString();
        
        if(!password.equals(confirm_password))
            return null;
        
        for(FormRuleTest rule : rules)
        {
            if( !rule.check() )
            {
                return null;
            }
        }
        
        return new UserRegisterModel(user, mail, password);
    }
    
    public void onServerResponse(ServerResponse response)
    {
        if (response.getOperationType() == OperationType.USER_REGISTER)
        {
            CompleteRegister(response);
        }
    }
}