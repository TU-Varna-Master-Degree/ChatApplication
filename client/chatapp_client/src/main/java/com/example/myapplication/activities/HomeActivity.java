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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.FriendshipAdapter;
import com.example.myapplication.adapters.GroupAdapter;
import com.example.myapplication.adapters.UserAdapter;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.dialogue.ServerResponse;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.enums.StatusCode;
import com.example.myapplication.domain.models.FindFriend;
import com.example.myapplication.domain.models.Friendship;
import com.example.myapplication.domain.models.Group;
import com.example.myapplication.utils.NetClient;

import java.util.List;
import java.util.function.Consumer;

public class HomeActivity extends ChatAppBaseActivity
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
    protected void onResponse(ServerResponse response) {
        runOnUiThread(() -> {
            if (OperationType.USER_GROUPS.equals(response.getOperationType())) {
                loadGroups(client.getDataList(response, Group.class));
            } else if (OperationType.FRIENDSHIP_LIST.equals(response.getOperationType())) {
                loadFriends(client.getDataList(response, Friendship.class));
            } else if (OperationType.UPDATE_FRIENDSHIP.equals(response.getOperationType())) {
                Toast.makeText(HomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                client.sendRequest(new ServerRequest<>(OperationType.FRIENDSHIP_LIST));
            } else if (OperationType.FIND_FRIENDS.equals(response.getOperationType())) {
                if (StatusCode.FAILED.equals(response.getCode())) {
                    Toast.makeText(HomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                } else if (StatusCode.SUCCESSFUL.equals(response.getCode())) {
                    loadUsers(client.getDataList(response, FindFriend.class));
                }
            } else if (OperationType.CREATE_FRIENDSHIP.equals(response.getOperationType())) {
                Toast.makeText(HomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                if (usersAdapter != null) {
                    usersAdapter.removeUserById(client.getData(response, Long.class));
                }
            }
        });
    }

    private void loadGroups(List<Group> groups) {
        GroupAdapter listAdapter = new GroupAdapter(groups, this::showUserGroupMessages);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(listAdapter);
    }

    private void loadFriends(List<Friendship> friendships) {
        FriendshipAdapter listAdapter = new FriendshipAdapter(client, friendships, this::showUserGroupMessages);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(listAdapter);
    }

    private void loadUsers(List<FindFriend> users) {
        usersAdapter = new UserAdapter(users, client);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(usersAdapter);
    }

    private void showUserGroupMessages(Long groupId) {
        Intent intent = new Intent(this, ChatActivity.class);
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
            client.sendRequest(request);
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
            client.sendRequest(new ServerRequest<>(OperationType.USER_GROUPS));
        } else if (text.equals("Friends")) {
            client.sendRequest(new ServerRequest<>(OperationType.FRIENDSHIP_LIST));
        }
    }

    private boolean closeSearchViewEvent() {
        searchViewText.setVisibility(View.VISIBLE);
        sendCbViewRequest(cb.getSelectedView());
        cb.setVisibility(View.VISIBLE);
        return false;
    }
}
