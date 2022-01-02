package com.example.myapplication.activities;

import static android.widget.AdapterView.OnItemSelectedListener;
import static android.widget.SearchView.OnQueryTextListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;
import com.example.myapplication.view.FriendshipAdapter;
import com.example.myapplication.view.GroupAdapter;
import com.example.myapplication.view.UserAdapter;

import java.util.List;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.FindFriendDto;
import domain.client.dto.FriendshipDto;
import domain.client.dto.GroupDto;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;

public class HomeActivity extends AppCompatActivity
        implements ActivityResultCallback<ActivityResult> {

    private SearchView searchView;
    private TextView searchViewText;
    private RecyclerView view;
    private Spinner cb;
    private UserAdapter usersAdapter;
    private ActivityResultLauncher<Intent> startForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatapp_home_activity);

        searchView = findViewById(R.id.home_search);
        searchViewText = findViewById(R.id.home_search_tv);
        view = findViewById(R.id.home_lst_feed);
        cb = findViewById(R.id.home_cb_filter);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.home_filter_opts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cb.setAdapter(adapter);
        cb.setOnItemSelectedListener(cbListener);

        searchView.setOnQueryTextListener(svListener);
        searchView.setOnSearchClickListener(view -> searchViewText.setVisibility(View.GONE));
        searchView.setOnCloseListener(this::closeSearchViewEvent);
        searchViewText.setOnClickListener((v) -> searchView.setIconified(false));

        startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetClient.register(this::onServerResponse);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetClient.unregister(this::onServerResponse);
    }

    private void onServerResponse(ServerResponse response) {
        runOnUiThread(() -> {
            if (OperationType.USER_GROUPS.equals(response.getOperationType())) {
                loadGroups((List<GroupDto>) response.getData());
            } else if (OperationType.FRIENDSHIP_LIST.equals(response.getOperationType())) {
                loadFriends((List<FriendshipDto>) response.getData());
            } else if (OperationType.UPDATE_FRIENDSHIP.equals(response.getOperationType())) {
                Toast.makeText(HomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                NetClient.sendRequest(new ServerRequest<>(OperationType.FRIENDSHIP_LIST));
            } else if (OperationType.FIND_FRIENDS.equals(response.getOperationType())) {
                if (StatusCode.FAILED.equals(response.getCode())) {
                    Toast.makeText(HomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                } else if (StatusCode.SUCCESSFUL.equals(response.getCode())) {
                    loadUsers((List<FindFriendDto>) response.getData());
                }
            } else if (OperationType.CREATE_FRIENDSHIP.equals(response.getOperationType())) {
                Toast.makeText(HomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                if (usersAdapter != null) {
                    usersAdapter.removeUserById((Long) response.getData());
                }
            }
        });
    }

    private void loadGroups(List<GroupDto> groups) {
        GroupAdapter listAdapter = new GroupAdapter(groups, this::showUserGroupMessages);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(listAdapter);
    }

    private void loadFriends(List<FriendshipDto> friendships) {
        FriendshipAdapter listAdapter = new FriendshipAdapter(friendships, this::showUserGroupMessages);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(listAdapter);
    }

    private void loadUsers(List<FindFriendDto> users) {
        usersAdapter = new UserAdapter(users);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(usersAdapter);
    }

    private void showUserGroupMessages(Long groupId) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.IN_CHAT_USER_ID, 0); // TODO: GetUserId?
        intent.putExtra(ChatActivity.IN_CHAT_GROUP_ID, groupId);
        
        startForResult.launch(intent);
        System.out.println(groupId);
    }

    @Override
    public void onActivityResult(ActivityResult result) {

    }

    private final OnItemSelectedListener cbListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            sendCbViewRequest(view);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    private final OnQueryTextListener svListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            searchView.clearFocus();
            cb.setVisibility(View.GONE);

            ServerRequest<String> request = new ServerRequest<>(OperationType.FIND_FRIENDS);
            request.setData(s);
            NetClient.sendRequest(request);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private void sendCbViewRequest(View view) {
        String text = ((TextView) view).getText().toString();
        if (text.equals("Groups")) {
            NetClient.sendRequest(new ServerRequest<>(OperationType.USER_GROUPS));
        } else if (text.equals("Friends")) {
            NetClient.sendRequest(new ServerRequest<>(OperationType.FRIENDSHIP_LIST));
        }
    }

    private boolean closeSearchViewEvent() {
        searchViewText.setVisibility(View.VISIBLE);
        sendCbViewRequest(cb.getSelectedView());
        cb.setVisibility(View.VISIBLE);
        return false;
    }
}
