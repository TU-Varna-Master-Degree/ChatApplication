package com.example.myapplication.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class FormRules implements TextWatcher {

    public static final Pattern USER_REG_EX = Pattern.compile("^[a-zA-Z0-9._\\-]{5,}$");
    public static final Pattern PW_REG_EX = Pattern.compile("^[a-zA-Z0-9@#$%^&+=]{3,}$");
    public static final Pattern EMAIL_REG_EX =
            Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    private final EditText input;
    private final Pattern pattern;
    private final TextView textView;

    public FormRules(EditText input, Pattern pattern, TextView errorTv) {
        this.input = input;
        this.pattern = pattern;
        this.textView = errorTv;

        input.addTextChangedListener(this);
    }

    public boolean check() {
        if (pattern.matcher(input.getText().toString()).matches()) {
            textView.setVisibility(View.INVISIBLE);
            return true;
        } else {
            textView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        check();
    }
}
