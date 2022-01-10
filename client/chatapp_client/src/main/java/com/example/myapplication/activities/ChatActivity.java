package com.example.myapplication.activities;

import static com.example.myapplication.domain.enums.OperationType.GROUP_NOTIFICATIONS;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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
import com.example.myapplication.domain.models.GroupUser;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.domain.models.NewUsersToGroup;
import com.example.myapplication.domain.models.Notification;
import com.example.myapplication.domain.models.SendMessage;
import com.example.myapplication.fragments.DialogFriendsFragment;
import com.example.myapplication.utils.FileLoaderDialog;
import com.example.myapplication.utils.FileSaveDialog;
import com.example.myapplication.utils.LocalFileData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    public static final String IN_CHAT_GROUP_ID = "IN_CHAT_GROUP_ID";

    private Long groupId;
    private EditText etMessage;
    private ImageButton btnRemoveFile;
    private ChatAdapter adapter;
    private RecyclerView lstView;
    private TextView tvUsernames;
    private ColorStateList defaultTextColor;

    FileLoaderDialog browser;
    FileSaveDialog save;
    LocalFileData attachment = null;

    private Long userId;
    private LinkedList<GroupUser> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        browser = new FileLoaderDialog(this, this::addFileAttachment, getNetClient());
        save = new FileSaveDialog(this, this::openContent);
        setContentView(R.layout.chat_view);

        readIntentInput();
        initializeViews();
        requestMessageHistory();
    }

    private void readIntentInput() {
        Intent intent = getIntent();
        groupId = intent.getLongExtra(IN_CHAT_GROUP_ID, 0);

        if (groupId == 0) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void initializeViews() {
        lstView = findViewById(R.id.chat_view_list);
        lstView.setLayoutManager(new LinearLayoutManager(this));

        etMessage = findViewById(R.id.chat_view_et_input);
        etMessage.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etMessage.setOnLongClickListener(view ->
        {
            browser.startModalDialog();
            return true;
        });
        etMessage.setOnFocusChangeListener((view, hasFocus) ->
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(ChatActivity.INPUT_METHOD_SERVICE);
            if (!hasFocus) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {
                imm.showSoftInput(view, 0);
            }
        });

        defaultTextColor = etMessage.getTextColors();

        btnRemoveFile = findViewById(R.id.chat_view_delete_file);
        btnRemoveFile.setVisibility(View.GONE);
        btnRemoveFile.setOnClickListener((v) -> removeFileAttachment());

        tvUsernames = findViewById(R.id.chat_layout_usernames);

        findViewById(R.id.chat_layout_back).setOnClickListener(v -> {
            setResult(ChatActivity.RESULT_CANCELED);
            finish();
        });
        findViewById(R.id.btn_add_user).setOnClickListener(this::requestFriendsForGroup);
        findViewById(R.id.chat_view_btn_send).setOnClickListener((view) -> {
            if (attachment != null) {
                requestSendFile(attachment);
            } else {
                requestSendMessage(etMessage.getText().toString());
            }
        });
    }

    private void addFileAttachment(LocalFileData data) {
        this.attachment = data;
        String label = String.format("\"%s.%s\" attached.", data.getFileName(), data.getFileExt());
        etMessage.setText(label);
        etMessage.setTextColor(ContextCompat.getColor(this, R.color.blue));
        etMessage.setEnabled(false);
        btnRemoveFile.setVisibility(View.VISIBLE);
    }

    private void removeFileAttachment() {
        this.attachment = null;
        etMessage.setText("");
        etMessage.setTextColor(defaultTextColor);
        etMessage.setEnabled(true);
        btnRemoveFile.setVisibility(View.GONE);
    }

    private void requestMessageHistory() {
        ServerRequest<Long> request = new ServerRequest<>(GROUP_NOTIFICATIONS);
        request.setData(groupId);
        getNetClient().sendRequest(request);
    }

    private void requestFriendsForGroup(View view) {
        ServerRequest<Long> request = new ServerRequest<>(OperationType.GROUP_FRIENDS_LIST);
        request.setData(groupId);
        getNetClient().sendRequest(request);
    }

    private void requestSendMessage(String message) {
        SendMessage sendMessageDto = new SendMessage();
        sendMessageDto.setGroupId(groupId);
        sendMessageDto.setContent(message);
        sendMessageDto.setMessageType(MessageType.TEXT);

        ServerRequest<SendMessage> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        request.setData(sendMessageDto);
        getNetClient().sendRequest(request);
    }

    private void requestSendFile(LocalFileData data) {
        SendMessage sendMessageDto = new SendMessage();
        sendMessageDto.setGroupId(groupId);
        sendMessageDto.setFileName(data.getFileName());
        sendMessageDto.setFileType(data.getFileExt());
        sendMessageDto.setFile(data.getData());
        sendMessageDto.setMessageType(MessageType.FILE);

        ServerRequest<SendMessage> request = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
        request.setData(sendMessageDto);
        getNetClient().sendRequest(request);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void onResponse(ServerResponse response) {
        if (response.getCode() != StatusCode.SUCCESSFUL) {
            runOnUiThread(() -> Toast.makeText(ChatActivity.this, response.getMessage(), Toast.LENGTH_LONG).show());
            return;
        }

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
            case GET_NOTIFICATION: {
                onFileContentReceived(response);
                break;
            }
        }
    }

    private void onGroupNotificationResponse(ServerResponse<Notification> response) {
        Notification notification = getNetClient().getData(response, Notification.class);
        users = new LinkedList<>(notification.getUsers());
        userId = notification.getUserId();
        adapter = new ChatAdapter(notification, getNetClient(), save);

        runOnUiThread(() -> {
            updateUsernames();
            lstView.setAdapter(adapter);
            lstView.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    private void onCreateNotificationResponse(ServerResponse<Message> response) {
        Message data = getNetClient().getData(response, Message.class);

        if (data.getGroupId().equals(groupId)) runOnUiThread(() -> {
            adapter.insert(data);
            lstView.scrollToPosition(adapter.getItemCount() - 1);
            etMessage.setText("");

            MessageType messageType = data.getMessageType();
            if (messageType == MessageType.FILE || messageType == MessageType.IMAGE) {
                runOnUiThread(this::removeFileAttachment);
            }
        });
    }

    private void onEditNotificationResponse(ServerResponse<Message> response) {
        Message data = getNetClient().getData(response, Message.class);
        if (data.getGroupId().equals(groupId))
            runOnUiThread(() -> adapter.update(data));
    }

    private void showFriendsFragment(ServerResponse<ArrayList<GroupFriend>> response) {
        List<GroupFriend> friends = getNetClient().getDataList(response, GroupFriend.class);
        runOnUiThread(() -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFriendsFragment dialogFriendsFragment = DialogFriendsFragment.newInstance(getNetClient(), groupId, friends);
            try {
                dialogFriendsFragment.show(fragmentManager, "friends_fragment");
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addGroupFriendsHandler(ServerResponse response) {
        if (StatusCode.SUCCESSFUL.equals(response.getCode())) {
            NewUsersToGroup newUsersToGroup = getNetClient().getData(response, NewUsersToGroup.class);
            if (!newUsersToGroup.getOldGroupId().equals(groupId)) {
                return;
            }

            groupId = newUsersToGroup.getNewGroupId();
            runOnUiThread(() -> {
                newUsersToGroup.getNewUsers().forEach(u -> {
                    adapter.notifyForNewUserMessage(u);
                    users.push(u);
                });
                updateUsernames();
                lstView.scrollToPosition(adapter.getItemCount() - 1);
            });
        }
    }

    private void onFileContentReceived(ServerResponse<Message> serverResponse) {
        Message message = getNetClient().getData(serverResponse, Message.class);

        runOnUiThread(() -> {
            if (MessageType.IMAGE.equals(message.getMessageType())) {
                adapter.update(message);
                lstView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (MessageType.FILE.equals(message.getMessageType())) {
                save.openModalSaveAs(message);
            }
        });
    }

    private void openContent(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, getContentResolver().getType(uri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void updateUsernames() {
        StringBuilder sb = new StringBuilder();

        for (GroupUser user : users) {
            if (!user.getId().equals(userId)) {
                String username = user.getUsername();
                if (sb.length() > 0) {
                    sb.append(", ");
                }

                if (sb.length() + username.length() > 24) {
                    sb.append("...");
                    break;
                } else {
                    sb.append(username);
                }
            }
        }

        tvUsernames.setText(sb.toString());
    }
}