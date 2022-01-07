package com.example.myapplication.activities;

import static domain.client.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ChatAdapter;
import com.example.myapplication.fragments.DialogFriendsFragment;
import com.example.myapplication.utils.AndroidFileLoaderDialog;
import com.example.myapplication.utils.AndroidLocalFileData;

import java.util.ArrayList;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.GroupFriendDto;
import domain.client.dto.MessageDto;
import domain.client.dto.NotificationDto;
import domain.client.dto.SendMessageDto;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;

public class ChatActivity extends ChatAppBaseActivity
{
    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";
    
    private Long groupId;
    private EditText etMessage;
    private ChatAdapter adapter;
    private RecyclerView lstView;
    
    AndroidFileLoaderDialog browser = new AndroidFileLoaderDialog(this, this::addAttachment);
    AndroidLocalFileData attachment = null;
    
    /*
    * Потребителски имена - (header)
      TODO: File view fix
    * public void setUsernamesTv(List<String> usernames) {
        StringBuilder sb = new StringBuilder();

        for (String username : usernames) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            if (sb.length() + username.length() > 27) {
                sb.append("...");
                break;
            } else {
                sb.append(username);
            }
        }

        usernamesTv.setText(sb.toString());
    }
    * */
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        
        readIntentInput();
        initializeViews();
        requestMessageHistory();
    }
    
    private void addAttachment(AndroidLocalFileData data)
    {
        this.attachment = data;
        runOnUiThread(() -> {
            String label = data.getFileName();
            
            LinearLayout messagebox_layout = ((LinearLayout)findViewById(R.id.chat_messagebox_layout));
            
            View view = getLayoutInflater().inflate(R.layout.chat_item_file_container, null);
            ((TextView)view.findViewById(R.id.chat_item_file_text)).setText( label );
            
            messagebox_layout.addView(view);
            
        });
        
    }
    
    private void initializeViews()
    {
        lstView = findViewById(R.id.chat_view_list);
        lstView.setLayoutManager(new LinearLayoutManager(this));
        
        etMessage = findViewById(R.id.chat_view_et_input);
        etMessage.setOnLongClickListener(view ->
        {
            browser.startModalDialog();
            return true;
        });
        etMessage.setOnFocusChangeListener((view, hasFocus) ->
        {
            if (!hasFocus)
            {
                InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(ChatActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }); // Close Keyboard
        
        findViewById(R.id.chat_layout_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_user).setOnClickListener(this::requestFriendsForGroup);
        findViewById(R.id.chat_view_btn_send).setOnClickListener((view) ->
        {
            requestSendMessage(etMessage.getText().toString());
            requestSendFile(attachment);
        });
    }
    
    private void readIntentInput()
    {
        Intent intent = getIntent();
        groupId = intent.getLongExtra(IN_CHAT_GROUP_ID, 0);
        
        if (groupId == 0)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    
    /// ASYNC EVENTS
    /// --------------------
    private void addGroupFriendsHandler(ServerResponse response)
    {
        runOnUiThread(() -> Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show());
        
        if (StatusCode.SUCCESSFUL.equals(response.getCode()))
        {
            groupId = (Long) response.getData();
            //TODO: could be simplified to update only participants
            requestMessageHistory();
        }
    }
    
    private void showFriendsFragment(ServerResponse<ArrayList<GroupFriendDto>> response)
    {
        ArrayList<GroupFriendDto> friends = response.getData();
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFriendsFragment dialogFriendsFragment = DialogFriendsFragment.newInstance(client, groupId, friends);
        try
        {
            dialogFriendsFragment.show(fragmentManager, "friends_fragment");
        }
        catch (IllegalStateException ex) {}
    }
    
    /// Get Notification List
    private void requestMessageHistory()
    {
        ServerRequest<Long> request = new ServerRequest<>(GROUP_NOTIFICATIONS);
        {
            request.setData(groupId);
        }
        client.sendRequest(request);
    }
    
    private void onGroupNotificationResponse(ServerResponse<NotificationDto> response)
    {
        NotificationDto notifications = response.getData();
        
        adapter = new ChatAdapter(notifications, client);
        runOnUiThread(() ->
        {
            lstView.setAdapter(adapter);
            lstView.scrollToPosition(notifications.getMessages().size() - 1);
        });
    }
    
    private void requestFriendsForGroup(View view)
    {
        ServerRequest<Long> request = new ServerRequest<>(OperationType.GROUP_FRIENDS_LIST);
        request.setData(groupId);
        client.sendRequest(request);
    }
    
    /// Create Notification
    private void requestSendMessage(String message)
    {
        if (message.equals(""))
            return;
        
        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setGroupId(groupId);
            sendMessageDto.setContent(message);
            sendMessageDto.setMessageType(MessageType.TEXT);
            
            request.setData(sendMessageDto);
        }
        
        client.sendRequest(request);
    }
    
    private void requestSendFile(AndroidLocalFileData data)
    {
        if (data == null)
            return;
        
        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = new SendMessageDto();
            {
                sendMessageDto.setGroupId(groupId);
                sendMessageDto.setFileName(data.getFileName());
                sendMessageDto.setFileType(data.getFileExt());
                sendMessageDto.setFile(data.getData());
                sendMessageDto.setMessageType(MessageType.FILE);
            }
            request.setData(sendMessageDto);
        }
        client.sendRequest(request);
    }
    
    private void onCreateNotificationResponse(ServerResponse<MessageDto> response)
    {
        MessageDto data = response.getData();
        
        if (data.getGroupId().equals(groupId)) runOnUiThread(() ->
        {
            adapter.insert(data);
            lstView.scrollToPosition(adapter.getItemCount() - 1);
            etMessage.setText("");
            
            if(data.getFile() != null)
            {
                this.attachment = null;
                LinearLayout layout = (LinearLayout)findViewById(R.id.chat_messagebox_layout);
                layout.removeView( layout.findViewById(R.id.chat_item_file_link) );
            }
            
        });
    }
    
    /// Edit notification
    private void onEditNotificationResponse(ServerResponse<MessageDto> response)
    {
        MessageDto data = response.getData();
        if (data.getGroupId().equals(groupId))
            runOnUiThread(() -> adapter.update(data));
    }
    
    
    @SuppressWarnings("rawtypes")
    @Override
    protected void onResponse(ServerResponse response)
    {
        if (response.getCode() != StatusCode.SUCCESSFUL)
        {
            runOnUiThread(() -> Toast.makeText(ChatActivity.this, response.getMessage(), Toast.LENGTH_LONG).show());
            return;
        }
        
        // Dispatch
        switch (response.getOperationType())
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
            case GROUP_FRIENDS_LIST:
            {
                showFriendsFragment(response);
                break;
            }
            case ADD_GROUP_FRIENDS:
            {
                addGroupFriendsHandler(response);
                break;
            }
        }
    }
}