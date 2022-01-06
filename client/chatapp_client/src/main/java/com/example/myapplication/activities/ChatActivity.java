package com.example.myapplication.activities;

import static domain.client.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatApplication;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ChatAdapter;
import com.example.myapplication.fragments.DialogFriendsFragment;
import com.example.myapplication.utils.NetClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class ChatActivity extends AppCompatActivity
{
    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";

    private Long groupId;
    private EditText etMessage;
    private ChatAdapter adapter;
    private RecyclerView lstView;
    
    NetClient client;
    
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
    private final ActivityResultLauncher startBrowser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if(result.getResultCode() == RESULT_OK && result.getData() != null)
                {
                    new Thread(()-> requestSendFile( result.getData().getData())).start();
                }
                
            });
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    
        client = ((ChatApplication) getApplication()).getNetClient();
        
        setContentView(R.layout.chat_view);
        readIntentInput();
        initializeViews();
        requestMessageHistory();
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        client.register(this::onResponse);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        client.unregister(this::onResponse);
    }
    
    private void initializeViews()
    {
        lstView = findViewById(R.id.chat_view_list);
        lstView.setLayoutManager(new LinearLayoutManager(this));
        
        etMessage = findViewById(R.id.chat_view_et_input);
        
        etMessage.setOnLongClickListener(view ->
        {
            Intent intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);
    
            startBrowser.launch(Intent.createChooser(intent, "Select a file"));
            return true;
        }); // Start File Browser
        etMessage.setOnFocusChangeListener((view, hasFocus)->
        {
            if(!hasFocus)
            {
                InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(ChatActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }); // Close Keyboard
        
        findViewById(R.id.chat_layout_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_user).setOnClickListener(this::requestFriendsForGroup);
        findViewById(R.id.chat_view_btn_send).setOnClickListener((view) -> requestSendMessage(etMessage.getText().toString()));
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
        runOnUiThread(()->Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show());
        
        if (StatusCode.SUCCESSFUL.equals(response.getCode())) {
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
        try {
            dialogFriendsFragment.show(fragmentManager, "friends_fragment");
        } catch (IllegalStateException ex) {}
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
        runOnUiThread(()->{
            lstView.setAdapter( adapter );
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
    private void requestSendMessage(String message )
    {
        if(message.equals(""))
            return;
        
        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setGroupId( groupId );
            sendMessageDto.setContent(message);
            sendMessageDto.setMessageType(MessageType.TEXT);
            
            request.setData(sendMessageDto);
        }
        
        client.sendRequest(request);
    }
    
    private void requestSendFile(Uri filePath )
    {
        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            try
            {
                SendMessageDto sendMessageDto = new SendMessageDto();
                {
                    sendMessageDto.setGroupId(groupId);
                    InputStream iStream = getContentResolver().openInputStream(filePath);
                    sendMessageDto.setFileName(filePath.getPath());
                    sendMessageDto.setFile(readBytes(iStream));
                    sendMessageDto.setMessageType(MessageType.FILE);
                }
                request.setData(sendMessageDto);
            }
            catch (IOException ignored)
            {
        
            }
            
        }
        client.sendRequest(request);
    }
    
    private void onCreateNotificationResponse(ServerResponse<MessageDto> response)
    {
        MessageDto data = response.getData();
        if (data.getGroupId().equals(groupId)) runOnUiThread(()->
        {
            adapter.insert( data );
            lstView.scrollToPosition(adapter.getItemCount() - 1);
            etMessage.setText("");
        });
    }
    
    /// Edit notification
    private void onEditNotificationResponse(ServerResponse<MessageDto> response)
    {
        MessageDto data = response.getData();
        if (data.getGroupId().equals(groupId))
            runOnUiThread(()->adapter.update(data));
    }
    
    @SuppressWarnings("rawtypes")
    private void onResponse(ServerResponse response)
    {
        if (response.getCode() != StatusCode.SUCCESSFUL) {
            runOnUiThread(() -> Toast.makeText(ChatActivity.this, response.getMessage(), Toast.LENGTH_LONG).show());
            return;
        }
        
        // Dispatch
        switch (response.getOperationType())
        {
            case GROUP_NOTIFICATIONS: {
                onGroupNotificationResponse(response);
                break;
            }
            case CREATE_NOTIFICATION: {
                onCreateNotificationResponse(response);
                break;
            }
            case EDIT_NOTIFICATION: {
                onEditNotificationResponse(response);
                break;
            }
            case GROUP_FRIENDS_LIST: {
                showFriendsFragment(response);
                break;
            }
            case ADD_GROUP_FRIENDS: {
                addGroupFriendsHandler(response);
                break;
            }
        }
    }
    
    @NonNull
    private static byte[] readBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}