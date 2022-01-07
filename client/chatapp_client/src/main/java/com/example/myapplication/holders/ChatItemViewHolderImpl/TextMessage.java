package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.function.BiConsumer;

import domain.client.dto.MessageDto;

public class TextMessage extends ImplBase
{
    TextView tvMessage;
    EditText etMessage;
    BiConsumer<Long, String> onEditCommit;
    
    public TextMessage(View view, BiConsumer<Long, String> onEditCommitCallback)
    {
        super(view);
        tvMessage = view.findViewById(R.id.chat_item_tv_message);
        etMessage = view.findViewById(R.id.chat_item_et_message);
        
        this.onEditCommit = onEditCommitCallback;
        installEditModeBehaviour();
    }
    
    public void installEditModeBehaviour()
    {
        tvMessage.setOnLongClickListener(view ->
        {
            openEditor();
            return true;
        });
        
        etMessage.setOnFocusChangeListener((view, hasFocus) ->
        {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(hasFocus)
            {
                imm.showSoftInput(view, 0);
            }
            else
            {
                closeEditor();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }
    
    private void openEditor()
    {
        etMessage.setVisibility(View.VISIBLE);
        etMessage.setText(tvMessage.getText());
        etMessage.requestFocus();
        
        tvMessage.setVisibility(View.GONE);
    }
    
    
    private void closeEditor()
    {
        boolean hasTextChanges = tvMessage.getText().toString().equals( tvMessage.getText().toString());
        
        if(hasTextChanges)
        {
            onEditCommit.accept(
                    data.getNotificationId(),
                    etMessage.getText().toString()
            );
        }
        
        tvMessage.setVisibility(View.VISIBLE);
        etMessage.setVisibility(View.GONE);
    }
    
    @Override
    public void setMessageContent(MessageDto data)
    {
        super.setMessageContent(data);
        
        tvMessage.setText(data.getContent());
        etMessage.setVisibility(View.GONE);
    }
}
