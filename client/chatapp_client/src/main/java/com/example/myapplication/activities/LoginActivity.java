package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.dialogue.ServerResponse;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.enums.StatusCode;
import com.example.myapplication.domain.models.User;
import com.example.myapplication.utils.FormRules;

public class LoginActivity extends BaseActivity
        implements ActivityResultCallback<ActivityResult>
{

    static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";
    static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    static final String LOGIN_REMEMBER = "LOGIN_REMEMBER";

    private EditText etUserLoginInput;
    private EditText etPassword;
    private CheckBox chbRememberMe;

    private FormRules[] rules;

    private ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserLoginInput = findViewById(R.id.login_tv_input);
        etPassword = findViewById(R.id.login_tv_password);
        chbRememberMe = findViewById(R.id.login_chb_remember_me);

        findViewById(R.id.login_tv_register).setOnClickListener(view -> LaunchRegisterForm());
        findViewById(R.id.login_btn_login).setOnClickListener(view -> DoLogin());

        if (Debug.isDebuggerConnected()) {
            SetDebugInfo();
        }

        TextView tvUsernameError = findViewById(R.id.login_error_username);
        TextView tvPasswordError = findViewById(R.id.login_error_password);

        rules = new FormRules[]
        {
            new FormRules(etUserLoginInput, FormRules.USER_REG_EX, tvUsernameError),
            new FormRules(etPassword, FormRules.PW_REG_EX, tvPasswordError)
        };
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LOGIN_USER_INFO, etUserLoginInput.getText().toString());
        outState.putString(LOGIN_PASSWORD, etPassword.getText().toString());
        outState.putBoolean(LOGIN_REMEMBER, chbRememberMe.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        etUserLoginInput.setText(outState.getString(LOGIN_USER_INFO));
        etPassword.setText(outState.getString(LOGIN_PASSWORD));
        chbRememberMe.setChecked(outState.getBoolean(LOGIN_REMEMBER));
    }

    @Override
    protected void onStart() {
        super.onStart();
        TryRememberLogin();
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if(result.getResultCode() == RegisterActivity.RESULT_OK) {
            runOnUiThread(()->Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show());
        }
    }

    private void TryRememberLogin() {
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        if (!pref.getBoolean(LOGIN_REMEMBER, false)) {
            return;
        }

        User model = new User();
        model.setUsername(pref.getString(LOGIN_USER_INFO, null));
        model.setPassword(pref.getString(LOGIN_PASSWORD, null));

        RequestLogin(model);
    }

    protected void onResponse(ServerResponse response) {
        if (response.getOperationType() == OperationType.USER_LOGIN) {
            CompleteActivity(response);
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetDebugInfo() {
        etUserLoginInput.setText("DimitarGo");
        etPassword.setText("1111");
    }

    private void LaunchRegisterForm() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startForResult.launch(intent);
    }

    private void DoLogin() {
        User model;
        if( (model = FetchModel()) != null) {
            RequestLogin(model);
        }
    }

    private User FetchModel() {
        String userInput = etUserLoginInput.getText().toString(),
                userPassword = etPassword.getText().toString();

        boolean isCorrect = true;

        for (FormRules test : rules) {
            if (!test.check()) {
                isCorrect = false;
            }
        }

        if (isCorrect) {
            User user = new User();
            user.setUsername(userInput);
            user.setPassword(userPassword);
            return user;
        } else {
            return null;
        }
    }

    private void RequestLogin(User model) {
        ServerRequest<User> request = new ServerRequest<>(OperationType.USER_LOGIN);
        request.setData(model);
        getNetClient().sendRequest(request);
    }

    private void CompleteActivity(ServerResponse response)
    {
        if(response.getOperationType() != OperationType.USER_LOGIN)
            return;

        if ( response.getCode() == StatusCode.SUCCESSFUL)
        {
            SaveConfig();
            setResult(LoginActivity.RESULT_OK);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity( intent );

            finish();
        }
        else
        {
            runOnUiThread(() ->
            {
                Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    private void SaveConfig() {
        if (!chbRememberMe.isChecked())
            return;

        SharedPreferences.Editor pref = getPreferences(Context.MODE_PRIVATE).edit();
        pref.putString(LOGIN_USER_INFO, etUserLoginInput.getText().toString());
        pref.putString(LOGIN_PASSWORD, etPassword.getText().toString());
        pref.putBoolean(LOGIN_REMEMBER, true);
        pref.apply();
    }
}