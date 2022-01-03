package com.example.myapplication.activities;

import static domain.client.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;
import com.example.myapplication.view.ChatAdapter;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.GroupMessageDto;
import domain.client.dto.NotificationDto;
import domain.client.dto.SendMessageDto;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;

public class ChatActivity extends AppCompatActivity
{
    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";

    private Long groupId;
    private EditText etMessage;
    private ChatAdapter adapter;
    RecyclerView lstView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        
        // Initialize View
        etMessage = findViewById(R.id.chat_view_et_input);
        etMessage.setOnLongClickListener(view ->
        {
            impl_detail_browse_file();
            return true;
        });
        etMessage.setOnFocusChangeListener((view, hasFocus)->
        {
            if(!hasFocus)
            {
                InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(ChatActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        Button btnSend = findViewById(R.id.chat_view_btn_send);
        btnSend.setOnClickListener((view)-> ChatActivity.this.sendMessage() );
    
        lstView = findViewById(R.id.chat_view_list);
        lstView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize Navigation
        Toolbar toolbar = findViewById(R.id.chat_view_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item ->
        {
            if (item.getItemId() == R.id.action_add_friend_to_chat)
            {
                // TODO: Add Friend to Chat Dialog;
                // TODO: DON'T ADD USERS TO GROUP
                return true;
            }
            return super.onOptionsItemSelected(item);
        });
        setSupportActionBar(toolbar);
        
        
        // Read input
        Intent intent = getIntent();
        groupId = intent.getLongExtra( IN_CHAT_GROUP_ID, 0);
        if(groupId == 0 )
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
        
        if( !impl_detail_has_valid_message() )
            return;
    
        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = impl_detail_content_text();
            request.setData(sendMessageDto);
        }
        
        NetClient.sendRequest( request );
    }
    
    @SuppressWarnings("rawtypes")
    void onResponse(ServerResponse response)
    {
        // Dispatch
        switch(response.getOperationType())
        {
            case GROUP_NOTIFICATIONS:
            {
                assert response.getData().getClass() == NotificationDto.class;
                onGroupNotificationResponse(response);
                break;
            }
            case CREATE_NOTIFICATION:
            {
                assert response.getData().getClass() == GroupMessageDto.class;
                onCreateNotificationResponse(response);
                break;
            }
            case EDIT_NOTIFICATION:
            {
                assert response.getData().getClass() == GroupMessageDto.class;
                onEditNotificationResponse(response);
                break;
            }
        }

    }
    
    void onGroupNotificationResponse(ServerResponse<NotificationDto> response)
    {
        if( !checkResponseStatus(response.getCode(), "Failed to fetch group message history") )
            return;
        
        NotificationDto notifications = response.getData();
        adapter = new ChatAdapter(notifications);
        runOnUiThread(()->{
                
                lstView.setAdapter( adapter );
                lstView.scrollToPosition(notifications.getMessages().size() - 1);
        
        });
    }
    
    void onCreateNotificationResponse(ServerResponse<GroupMessageDto> response)
    {
        if( !checkResponseStatus(response.getCode(), "Failed to send message.") )
            return;
    
        GroupMessageDto data = response.getData();
        runOnUiThread(()->
        {
            adapter.insert( data );
            lstView.scrollToPosition(adapter.getItemCount() - 1);
            etMessage.setText("");
        });
    }
    
    void onEditNotificationResponse(ServerResponse<GroupMessageDto> response)
    {
        if( !checkResponseStatus(response.getCode(),  "Failed to edit message") )
            return;
        
        GroupMessageDto data = response.getData();
        runOnUiThread(()->adapter.update(data));
    }
    
    private boolean checkResponseStatus(StatusCode code, String errorMessage)
    {
        if(code != StatusCode.SUCCESSFUL)
        {
            runOnUiThread( ()-> Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show() );
            return false;
        }
        
        return true;
    }
    
    private void impl_detail_browse_file()
    {
        // TODO: Dimitar Implement
        assert false;
    }
    
    private boolean impl_detail_has_valid_message()
    {
        return !etMessage.getText().toString().equals("");
    }
    
    private SendMessageDto impl_detail_content_text()
    {
        String message = etMessage.getText().toString();
        
        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setGroupId( groupId );
        sendMessageDto.setMessageType(MessageType.TEXT);
        sendMessageDto.setContent(message);
        
        return sendMessageDto;
    }
}