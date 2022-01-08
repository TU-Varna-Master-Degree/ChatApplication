package com.example.myapplication.activities;

import static com.example.myapplication.domain.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.net.Uri;
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
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.dialogue.ServerResponse;
import com.example.myapplication.domain.enums.MessageType;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.enums.StatusCode;
import com.example.myapplication.domain.models.GroupFriend;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.domain.models.Notification;
import com.example.myapplication.domain.models.SendMessage;
import com.example.myapplication.fragments.DialogFriendsFragment;
import com.example.myapplication.utils.AndroidFileLoaderDialog;
import com.example.myapplication.utils.AndroidFileSaveDialog;
import com.example.myapplication.utils.AndroidLocalFileData;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends ChatAppBaseActivity
{
    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";

    private Long groupId;
    private EditText etMessage;
    private ChatAdapter adapter;
    private RecyclerView lstView;
    private LinearLayout mbLayout;
    private View attachmentView;
    
    AndroidFileLoaderDialog browser = new AndroidFileLoaderDialog(this, this::addFileAttachment);
    AndroidFileSaveDialog save = new AndroidFileSaveDialog(this, this::openContent);
    AndroidLocalFileData attachment = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        
        readIntentInput();
        initializeViews();
        requestMessageHistory();
    }

    private void addFileAttachment(AndroidLocalFileData data)
    {
        this.attachment = data;

        String label = data.getFileName();

        mbLayout.addView( attachmentView );
        ((TextView)attachmentView.findViewById(R.id.chat_item_file_text)).setText( label );
    }

    private void removeFileAttachment()
    {
        this.attachment = null;
        mbLayout.removeView(attachmentView);
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

        mbLayout = ((LinearLayout)findViewById(R.id.chat_messagebox_layout));
        attachmentView = getLayoutInflater().inflate(R.layout.chat_item_file_container, null);

        findViewById(R.id.chat_layout_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_user).setOnClickListener(this::requestFriendsForGroup);
        findViewById(R.id.chat_view_btn_send).setOnClickListener((view) ->
        {
            String message = etMessage.getText().toString();

            if(!message.equals(""))
                requestSendMessage(etMessage.getText().toString());

            if(attachment != null)
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
            groupId = client.getData(response, Long.class);
            //TODO: could be simplified to update only participants
            requestMessageHistory();
        }
    }

    private void showFriendsFragment(ServerResponse<ArrayList<GroupFriend>> response)
    {
        List<GroupFriend> friends = client.getDataList(response, GroupFriend.class);
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

    private void onGroupNotificationResponse(ServerResponse<Notification> response)
    {
        Notification notifications = client.getData(response, Notification.class);

        adapter = new ChatAdapter(notifications, client, this::onFileContentReceived);
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
        ServerRequest<SendMessage> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessage sendMessageDto = new SendMessage();
            sendMessageDto.setGroupId(groupId);
            sendMessageDto.setContent(message);
            sendMessageDto.setMessageType(MessageType.TEXT);

            request.setData(sendMessageDto);
        }

        client.sendRequest(request);
    }

    private void requestSendFile(AndroidLocalFileData data)
    {
        ServerRequest<SendMessage> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessage sendMessageDto = new SendMessage();
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
    
    private void onCreateNotificationResponse(ServerResponse<Message> response)
    {
        Message data = client.getData(response, Message.class);
        
        if (data.getGroupId().equals(groupId)) runOnUiThread(() ->
        {
            adapter.insert(data);
            lstView.scrollToPosition(adapter.getItemCount() - 1);
            etMessage.setText("");
            
            if(data.getMessageType() == MessageType.FILE)
            {
                removeFileAttachment();
            }
            
        });
    }
    
    /// Edit notification
    private void onEditNotificationResponse(ServerResponse<Message> response)
    {
        Message data = client.getData(response, Message.class);
        if (data.getGroupId().equals(groupId))
            runOnUiThread(() -> adapter.update(data));
    }
    
    private void onFileContentReceived(Message response)
    {
        // TODO: Replace with FileContentDto
        if( response.getGroupId().equals(groupId) )runOnUiThread(()->
        {
            save.openModalSaveAs( response );
        });
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
        switch (response.getOperationType()) {
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
            default: // TODO: Download Content
                // onFileContentReceived(response.getData());
        }
    }
    
    private void openContent(Uri uri)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, getContentResolver().getType(uri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}