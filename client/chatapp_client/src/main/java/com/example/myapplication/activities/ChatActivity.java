package com.example.myapplication.activities;

import static domain.client.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.NotificationDto;
import domain.client.dto.SendMessageDto;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;

public class ChatActivity extends AppCompatActivity
{
    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";
    public static final String IN_CHAT_USER_ID = "IN_CHAT_USER_ID";
    private Long groupId;
    private Long povUserId;
    
    private RecyclerView lstView;
    private EditText etMessage;
    private Button btnSend;
    private Toolbar toolbar;
    
    private NotificationDto notifications;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        
        // Initialize View
        etMessage = findViewById(R.id.chat_view_et_input);
        etMessage.setOnLongClickListener(view ->
        {
            // TODO: Browse file
            return false;
        });
        
        btnSend = findViewById(R.id.chat_view_btn_send);
        btnSend.setOnClickListener((view)-> ChatActivity.this.sendMessage() );
        
        lstView = findViewById(R.id.chat_view_list);
        lstView.setLayoutManager(new LinearLayoutManager(this));
    
        toolbar = findViewById(R.id.chat_view_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item ->
        {
            if (item.getItemId() == R.id.action_add_friend_to_chat)
            {
                // TODO: Add Friend to Chat Dialog;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    
    );
        setSupportActionBar( toolbar );
        
        // TODO: Add friend to group menu
        
        // Read input
        Intent intent = getIntent();
        povUserId = intent.getLongExtra( IN_CHAT_USER_ID, 0);
        groupId = intent.getLongExtra( IN_CHAT_GROUP_ID, 0);
    
        
        
        if(povUserId == 0 || groupId == 0 )
        {
            setResult(RESULT_CANCELED);
            finish();
        }
        
        // Fetch view content
        getMessageHistory();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        NetClient.register(this::onResponse);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        NetClient.unregister(this::onResponse);
    }
    
    void getMessageHistory()
    {
        ServerRequest<Long> request = new ServerRequest<>(GROUP_NOTIFICATIONS);
        {
            request.setData( groupId );
        }
        
        NetClient.sendRequest(request);
    }
    
    void sendMessage()
    {
        String message = etMessage.getText().toString();
        
        if(message.equals(""))
            return;
    
        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setGroupId( groupId );
            sendMessageDto.setMessageType(MessageType.TEXT);
            sendMessageDto.setContent(message);
    
            request.setData(sendMessageDto);
        }
        
        NetClient.sendRequest( request );
    }
    
    void onResponse(ServerResponse response)
    {
        // Dispatch
        switch(response.getOperationType())
        {
            case GROUP_NOTIFICATIONS:
            {
                onGroupNotificationResponse(response);
                break;
            }
            case CREATE_NOTIFICATION:
            {
                onCreateNotificationResponse(response);
                break;
            }
            case EDIT_NOTIFICATION:
            {
                onEditNotificationResponse(response);
                break;
            }
        }

    }
    
    void onGroupNotificationResponse(ServerResponse<NotificationDto> response)
    {
        if( !CheckResponseStatus(response.getCode(), "Failed to fetch group message history") )
            return;
        
        notifications = response.getData();
        
        // TODO: MessageDto -> GroupMessageDto: Reason need SenderId to determine message anchoring
        // lstView.setAdapter( new ChatAdapter( povUserId, null, null) );
    }
    
    void onCreateNotificationResponse(ServerResponse response)
    {
        if( !CheckResponseStatus(response.getCode(), "Failed to send message.") )
            return;
        
        // notifications.getMessages().add( respon... )
        // TODO: Update: Push new notification
    }
    
    void onEditNotificationResponse(ServerResponse response)
    {
        if( !CheckResponseStatus(response.getCode(),  "Failed to edit message") )
            return;
        
        // TODO: Update: Notification
    }
    
    private boolean CheckResponseStatus(StatusCode code, String errorMessage)
    {
        if(code != StatusCode.SUCCESSFUL)
        {
            runOnUiThread( ()-> Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show() );
            return false;
        }
        
        return true;
    }
}