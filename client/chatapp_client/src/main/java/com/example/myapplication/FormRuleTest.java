package com.example.myapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

public class FormRuleTest implements TextWatcher
{
    /*
             - Password must contain at least one digit [0-9].
             - Password must contain at least one lowercase Latin character [a-z].
             - Password must contain at least one uppercase Latin character [A-Z].
             - Password must contain at least one special character like ! @ # & ( ).
             - Password must contain a length of at least 8 characters and a maximum of 20 characters.
             */
    public static final Pattern PW_REG_EX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    /*
                    - Username must be 5 characters or longer
                * */
    
    @SuppressWarnings("RegExpRedundantEscape")
    public static final Pattern USER_REG_EX =
            Pattern.compile("^[a-zA-Z0-9\\._\\-]{5,}$");
    public static final Pattern EMAIL_REG_EX =
            Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    @SuppressWarnings("RegExpRedundantEscape")
    public static final Pattern EMAIL_OR_PW_REG_EX =
            Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$|^[a-zA-Z0-9\\._\\-]{5,}$" );
    
    private final EditText input;
    private final Pattern pattern;
    
    public FormRuleTest(EditText input, Pattern pattern)
    {
        this.input = input;
        this.pattern = pattern;
    
        input.addTextChangedListener(this);
    }
    
    public boolean check()
    {
        return pattern.matcher( input.getText().toString() ).matches();
    }
    
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
    
    }
    
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        
    }
    
    @Override
    public void afterTextChanged(Editable editable)
    {
    
    }
}
