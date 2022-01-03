package com.example.myapplication.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;

import java.time.LocalDateTime;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.MessageDto;
import domain.client.dto.SendMessageDto;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;

public class ChatItemViewHolder extends RecyclerView.ViewHolder
    implements IChatViewDataBinder
{
    ConstraintLayout layout;
    ImageView imgPFPAnchor;
    TextView tvDate, tvTime, tvUsername, tvMessage;
    EditText etMessage;
    
    MessageDto data = null;
    
    public ChatItemViewHolder(@NonNull View itemView)
    {
        super(itemView);
        bindViews(itemView);
        installEditModeBehaviour();
    }
    
    private void sendEditRequest(String newContent)
    {
        ServerRequest<SendMessageDto> req = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setMessageId( data.getNotificationId() );
            sendMessageDto.setMessageType(MessageType.TEXT);
            sendMessageDto.setContent(newContent);
        
            req.setData(sendMessageDto);
        }
        NetClient.sendRequest(req);
    }
    
    private void installEditModeBehaviour()
    {
        tvMessage.setOnLongClickListener(view ->
        {
            openEditor();
            return true;
        });
        
        etMessage.setOnFocusChangeListener((view, hasFocus) ->
        {
            InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
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
    
        if(hasTextChanges && data != null)
        {
            String newContent = etMessage.getText().toString();
            sendEditRequest(newContent);
        }
    
        tvMessage.setVisibility(View.VISIBLE);
        etMessage.setVisibility(View.GONE);
    }
    
    private void bindViews(View itemView)
    {
        imgPFPAnchor = itemView.findViewById(R.id.chat_item_img_pfp);
        tvDate = itemView.findViewById(R.id.chat_item_tv_date);
        tvTime = itemView.findViewById(R.id.chat_item_tv_time);
        tvUsername = itemView.findViewById(R.id.chat_item_tv_name);
        layout = itemView.findViewById(R.id.chat_item_constraint);
        tvMessage = itemView.findViewById(R.id.chat_item_tv_message);
        etMessage = itemView.findViewById(R.id.chat_item_et_message);
    }
    
    public void anchorLeft()
    {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        
        constraintSet.connect(R.id.chat_item_tv_name, ConstraintSet.LEFT, R.id.chat_item_left, ConstraintSet.LEFT);
        constraintSet.connect(R.id.chat_item_card, ConstraintSet.LEFT, R.id.chat_item_tv_name, ConstraintSet.LEFT);
        constraintSet.connect(R.id.chat_item_tv_time, ConstraintSet.LEFT, R.id.chat_item_card, ConstraintSet.RIGHT);
        constraintSet.applyTo(layout);
    
    }
    
    public void anchorRight()
    {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
    
        constraintSet.connect(R.id.chat_item_tv_name, ConstraintSet.RIGHT, R.id.chat_item_right, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chat_item_card, ConstraintSet.RIGHT, R.id.chat_item_tv_name, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chat_item_tv_time, ConstraintSet.RIGHT, R.id.chat_item_card, ConstraintSet.LEFT);
        
        constraintSet.applyTo(layout);
    }
    
    public void showTime()
    {
        tvTime.setVisibility(View.VISIBLE);
    }
    
    public void showDate()
    {
        tvDate.setVisibility(View.VISIBLE);
    }
    
    public void showUser()
    {
        tvUsername.setVisibility(View.VISIBLE);
    }
    
    @SuppressLint("DefaultLocale")
    @Override
    public void setMessageContent(MessageDto data)
    {
        this.data = data;
        LocalDateTime dt = this.data.getSendDate();
        tvMessage.setText( this.data.getContent() );
        tvTime.setText( String.format("%2d:%2d", dt.getHour(), dt.getMinute()) );
        tvDate.setText( String.format("%2d/%2d/%4d", dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()) );
        
        etMessage.setVisibility(View.GONE);
        tvTime.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void setUsername(String username)
    {
        tvUsername.setText(username);
    }
}
