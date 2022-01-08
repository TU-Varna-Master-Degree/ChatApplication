package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.dialogue.ServerResponse;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.enums.StatusCode;
import com.example.myapplication.domain.models.User;
import com.example.myapplication.utils.FormRules;

public class RegisterActivity extends ChatAppBaseActivity {

    static final String REGISTER_USER_NAME = "REGISTER_USER_NAME";
    static final String REGISTER_EMAIL = "REGISTER_EMAIL";
    static final String REGISTER_PASSWORD = "REGISTER_PASSWORD";
    static final String REGISTER_CONFIRM_PASSWORD = "REGISTER_CONFIRM_PASSWORD";

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private TextView tvConfPasswordError;

    private FormRules[] rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        etUsername = findViewById(R.id.reg_et_username);
        etEmail = findViewById(R.id.reg_et_email);
        etPassword = findViewById(R.id.reg_et_pw);
        etConfirmPassword = findViewById(R.id.reg_et_pw_confirm);

        findViewById(R.id.btn_reg_exit)
                .setOnClickListener(view ->
                {
                    setResult(RESULT_CANCELED);
                    RegisterActivity.this.finish();
                });

        findViewById(R.id.btn_reg_confirm)
                .setOnClickListener(view -> DoRegister());
        
        if (Debug.isDebuggerConnected()) {
            SetDebugInfo();
        }

        TextView tvUsernameError = findViewById(R.id.reg_error_username);
        TextView tvPasswordError = findViewById(R.id.reg_error_password);
        TextView tvEmailError = findViewById(R.id.reg_error_email);
        tvConfPasswordError = findViewById(R.id.reg_error_conf_password);

        rules = new FormRules[]{
                new FormRules(etUsername, FormRules.USER_REG_EX, tvUsernameError),
                new FormRules(etPassword, FormRules.PW_REG_EX, tvPasswordError),
                new FormRules(etEmail, FormRules.EMAIL_REG_EX, tvEmailError),
        };
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(REGISTER_USER_NAME, etUsername.getText().toString());
        outState.putString(REGISTER_EMAIL, etEmail.getText().toString());
        outState.putString(REGISTER_PASSWORD, etPassword.getText().toString());
        outState.putString(REGISTER_CONFIRM_PASSWORD, etConfirmPassword.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        etUsername.setText(outState.getString(REGISTER_USER_NAME));
        etEmail.setText(outState.getString(REGISTER_EMAIL));
        etPassword.setText(outState.getString(REGISTER_PASSWORD));
        etConfirmPassword.setText(outState.getString(REGISTER_CONFIRM_PASSWORD));
    }

    @SuppressLint("SetTextI18n")
    private void SetDebugInfo() {
        etUsername.setText("Gosho");
        etEmail.setText("gosho@abv.bg");
        etPassword.setText("1111");
        etConfirmPassword.setText("1111");
    }

    private void DoRegister() {
        User model;
        if ((model = FetchFormModel()) != null) {
            RequestRegister(model);
        }
    }

    @SuppressLint("SetTextI18n")
    private User FetchFormModel() {
        String password = etPassword.getText().toString(),
                confirm_password = etConfirmPassword.getText().toString(),
                user = etUsername.getText().toString(),
                mail = etEmail.getText().toString();

        boolean isCorrect = true;

        if (!password.equals(confirm_password)) {
            tvConfPasswordError.setVisibility(View.VISIBLE);
            isCorrect = false;
        } else {
            tvConfPasswordError.setVisibility(View.INVISIBLE);
        }

        for (FormRules rule : rules) {
            if (!rule.check()) {
                isCorrect = false;
            }
        }

        if (isCorrect) {
            User model = new User();
            model.setUsername(user);
            model.setPassword(password);
            model.setEmail(mail);
            return model;
        } else {
            return null;
        }
    }

    private void RequestRegister(User model) {
        ServerRequest<User> request = new ServerRequest<>(OperationType.USER_REGISTER);
        request.setData(model);
        client.sendRequest(request);
    }

    @Override
    protected void onResponse(ServerResponse response) {
        if (response.getOperationType() == OperationType.USER_REGISTER) {
            CompleteRegister(response);
        }
    }

    private void CompleteRegister(ServerResponse model) {
        if (model.getCode() == StatusCode.SUCCESSFUL) {
            setResult(RESULT_OK);
            RegisterActivity.this.finish();
        } else {
            runOnUiThread(() ->
                    Toast.makeText(this, model.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}