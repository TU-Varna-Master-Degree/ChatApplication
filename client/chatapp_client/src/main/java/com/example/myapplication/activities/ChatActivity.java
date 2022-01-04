package com.example.myapplication.activities;

import static domain.client.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.utils.NetClient;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ChatAdapter;
import com.example.myapplication.fragments.DialogFriendsFragment;

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

public class ChatActivity extends AppCompatActivity {
    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";

    private Long groupId;
    private EditText etMessage;
    private ChatAdapter adapter;
    RecyclerView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        btnSend.setOnClickListener((view) -> ChatActivity.this.sendMessage());

        lstView = findViewById(R.id.chat_view_list);
        lstView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.chat_layout_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_user).setOnClickListener(this::getFriendsForGroup);

        // Read input
        Intent intent = getIntent();
        groupId = intent.getLongExtra(IN_CHAT_GROUP_ID, 0);
        if (groupId == 0) {
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

    void getMessageHistory() {
        ServerRequest<Long> request = new ServerRequest<>(GROUP_NOTIFICATIONS);
        {
            request.setData(groupId);
        }

        NetClient.sendRequest(request);
    }

    private void getFriendsForGroup(View view) {
        ServerRequest<Long> request = new ServerRequest<>(OperationType.GROUP_FRIENDS_LIST);
        request.setData(groupId);
        NetClient.sendRequest(request);
    }

    void sendMessage() {
        String message = etMessage.getText().toString();

        if (!impl_detail_has_valid_message())
            return;

        ServerRequest<SendMessageDto> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        {
            SendMessageDto sendMessageDto = impl_detail_content_text();
            request.setData(sendMessageDto);
        }

        NetClient.sendRequest(request);
    }

    @SuppressWarnings("rawtypes")
    void onResponse(ServerResponse response) {
        // Dispatch
        switch (response.getOperationType()) {
            case GROUP_NOTIFICATIONS: {
                assert response.getData().getClass() == NotificationDto.class;
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

    private void addGroupFriendsHandler(ServerResponse response) {
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();

        if (StatusCode.SUCCESSFUL.equals(response.getCode())) {
            groupId = (Long) response.getData();
            //TODO: could be simplify to update only participants
            getMessageHistory();
        }
    }

    private void showFriendsFragment(ServerResponse<ArrayList<GroupFriendDto>> response) {
        ArrayList<GroupFriendDto> friends = response.getData();
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFriendsFragment dialogFriendsFragment = DialogFriendsFragment.newInstance(groupId, friends);
        try {
            dialogFriendsFragment.show(fragmentManager, "friends_fragment");
        } catch (IllegalStateException ex) {}
    }

    void onGroupNotificationResponse(ServerResponse<NotificationDto> response) {
        if (!checkResponseStatus(response.getCode(), "Failed to fetch group message history"))
            return;

        NotificationDto notifications = response.getData();
        adapter = new ChatAdapter(notifications);
        runOnUiThread(()->{

                lstView.setAdapter( adapter );
                lstView.scrollToPosition(notifications.getMessages().size() - 1);

        });
    }

    void onCreateNotificationResponse(ServerResponse<MessageDto> response) {
        MessageDto data = response.getData();
        if (data != null && !data.getGroupId().equals(groupId)) {
            return;
        }

        if (!checkResponseStatus(response.getCode(), response.getMessage()))
            return;

        runOnUiThread(()->
        {
            adapter.insert( data );
            lstView.scrollToPosition(adapter.getItemCount() - 1);
            etMessage.setText("");
        });
    }

    void onEditNotificationResponse(ServerResponse<MessageDto> response) {
        MessageDto data = response.getData();
        if (data != null && !data.getGroupId().equals(groupId)) {
            return;
        }

        if (!checkResponseStatus(response.getCode(), response.getMessage()))
            return;

        runOnUiThread(()->adapter.update(data));
    }

    private boolean checkResponseStatus(StatusCode code, String errorMessage) {
        if (code != StatusCode.SUCCESSFUL) {
            runOnUiThread(() -> Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            return false;
        }

        return true;
    }

    private void impl_detail_browse_file() {
        // TODO: Dimitar Implement
        assert false;
    }
    
    private boolean impl_detail_has_valid_message()
    {
        return !etMessage.getText().toString().equals("");
    }

    private SendMessageDto impl_detail_content_text()
    {
        // TODO: Dimitar Implement
        String message = etMessage.getText().toString();

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setGroupId(groupId);
        sendMessageDto.setMessageType(MessageType.TEXT);
        sendMessageDto.setContent(message);

        return sendMessageDto;
    }
}