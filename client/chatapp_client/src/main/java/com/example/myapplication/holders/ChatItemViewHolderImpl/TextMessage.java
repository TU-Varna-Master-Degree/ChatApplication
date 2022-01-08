package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.domain.models.Message;

import java.util.function.BiConsumer;

public class TextMessage extends ImplBase
{
    TextView tvMessage;
    EditText etMessage;
    LinearLayout layout;
    
    BiConsumer<Long, String> onEditCommit;
    
    public TextMessage(View view, BiConsumer<Long, String> onEditCommitCallback)
    {
        super(view);
        layout = view.findViewById(R.id.chat_item_text);
        
        tvMessage = layout.findViewById(R.id.chat_item_tv_message);
        etMessage = layout.findViewById(R.id.chat_item_et_message);
        
        layout.removeView(etMessage);

        this.onEditCommit = onEditCommitCallback;
        installEditModeBehaviour();
    }
    public TextMessage(View view)
    {
        super(view);
    
        layout = view.findViewById(R.id.chat_item_text);
    
        tvMessage = layout.findViewById(R.id.chat_item_tv_message);
        etMessage = layout.findViewById(R.id.chat_item_et_message);
        
        layout.removeView(etMessage);
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
        layout.addView(etMessage);
        etMessage.setText(tvMessage.getText());
        etMessage.requestFocus();
        
      
        layout.removeView(tvMessage);
    }
    
    
    private void closeEditor()
    {
        boolean hasTextChanges = !etMessage.getText().toString().equals( tvMessage.getText().toString());
        
        if(hasTextChanges)
        {
            onEditCommit.accept(
                    data.getNotificationId(),
                    etMessage.getText().toString()
            );
        }
        
        etMessage.clearFocus();
        
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
        
        
        
        layout.addView(tvMessage);
        layout.removeView(etMessage);
    }
    
    @Override
    public void setMessageContent(Message data)
    {
        super.setMessageContent(data);
        
        tvMessage.setText(data.getContent());
    }
}
