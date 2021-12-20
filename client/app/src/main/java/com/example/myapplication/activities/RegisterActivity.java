package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.myapplication.FormRuleTest;
import com.example.myapplication.R;
import com.example.myapplication.ServerHandler;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserRegisterModel;
import com.example.myapplication.models.client.ServerRequest;
import com.example.myapplication.models.enums.OperationType;

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
    
    private void DoRegister()
    {
        UserRegisterModel model = FetchModel();
        
        if(model == null)
        {
            // TODO: Display user message -> Failed validation
            return;
        }
        
        // TODO: Block user controls and animation
        
        // Launch in new thread
        // new Thread()
        // {
        //     @Override
        //     public void run()
        //     {
        //
        //     }
        // }.start();
    
        if( !RequestRegister(model) )
        {
            // runOnUiThread(() ->
            // {
            //     // TODO: Display user message
            // });
        }
        else
        {
            // FinishActivityJob( model );
        }
    }
    
    private boolean RequestRegister(UserRegisterModel model)
    {
        // TODO: Client register request
        User user = new User();
        user.setEmail( model.getEmail() );
        user.setPassword( model.getPassword() );
        user.setUsername( model.getUsername() );
    
        ServerRequest<User> request = new ServerRequest<>(OperationType.USER_REGISTER);
        request.setData( user );
        
        ServerHandler.sendRequest(request);
        return false;
    }
    
    private void FinishActivityJob(UserRegisterModel model)
    {
        Intent intent = getIntent();
        intent.putExtra("Username", model.getUsername());
        intent.putExtra("Password", model.getPassword());
        intent.putExtra("Email", model.getEmail());
    
        setResult(RESULT_OK);
        
        RegisterActivity.this.finish();
    }
    
    
    private UserRegisterModel FetchModel()
    {
        String password = etPassword.getText().toString(),
                confirm_password = etConfirmPassword.getText().toString(),
        user = etUsername.getText().toString(),
        mail =  etEmail.getText().toString();
        
        final FormRuleTest[] rules = {
            new FormRuleTest( user, FormRuleTest.USER_REG_EX),
            new FormRuleTest( password, FormRuleTest.PW_REG_EX),
            new FormRuleTest( mail, FormRuleTest.EMAIL_REG_EX),
        };
        
        if(!password.equals(confirm_password))
            return null;
        
        for(FormRuleTest rule : rules)
        {
            if( !rule.check() )
                return null;
        }
        

        return new UserRegisterModel(user, mail, password);
    }
}