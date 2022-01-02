package com.example.myapplication.view;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.GroupUserDto;
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
    GroupUserDto user = null;
    
    public ChatItemViewHolder(@NonNull View itemView)
    {
        super(itemView);
        bindViews(itemView);
        installEditModeBehaviour();
    }
    
    private Long impl_detail_todo_refactor(MessageDto msg)
    {
        return null; // TODO: Implement
    }
    
    private void sendEditRequest(String newContent)
    {
        ServerRequest<SendMessageDto> req = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setMessageId( impl_detail_todo_refactor(data) );
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
            etMessage.setVisibility(View.VISIBLE);
            etMessage.setText(tvMessage.getText());
            tvMessage.setVisibility(View.GONE);
            etMessage.requestFocus();
        
            return true;
        });
    
        etMessage.setOnFocusChangeListener((view, hasFocus) ->
        {
            if(hasFocus)
                return;
        
            boolean hasTextChanges = tvMessage.getText().toString().equals( tvMessage.getText().toString());
            
            if(hasTextChanges && data != null)
            {
                String newContent = etMessage.getText().toString();
                tvMessage.setText( newContent );
                sendEditRequest(newContent);
            }
        
            tvMessage.setVisibility(View.VISIBLE);
            etMessage.setVisibility(View.GONE);
        });
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
        
        constraintSet.connect(R.id.chat_item_tv_name, ConstraintSet.LEFT, R.id.chat_item_left, ConstraintSet.END);
        constraintSet.connect(R.id.chat_item_card, ConstraintSet.LEFT, R.id.chat_item_tv_name, ConstraintSet.START);
        constraintSet.connect(R.id.chat_item_tv_time, ConstraintSet.LEFT, R.id.chat_item_card, ConstraintSet.END);
        constraintSet.applyTo(layout);
    
    }
    
    public void anchorRight()
    {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
    
        constraintSet.connect(R.id.chat_item_tv_name, ConstraintSet.RIGHT, R.id.chat_item_right, ConstraintSet.START);
        constraintSet.connect(R.id.chat_item_card, ConstraintSet.RIGHT, R.id.chat_item_tv_name, ConstraintSet.END);
        constraintSet.connect(R.id.chat_item_tv_time, ConstraintSet.RIGHT, R.id.chat_item_card, ConstraintSet.START);
        
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
    
    @Override
    public void setMessageContent(MessageDto data)
    {
        this.data = data;
    }
    
    @Override
    public void setMessageUserData(GroupUserDto user)
    {
        this.user = user;
    }
}
