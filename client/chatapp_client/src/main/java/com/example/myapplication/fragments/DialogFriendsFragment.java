package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.utils.NetClient;
import com.example.myapplication.R;
import com.example.myapplication.adapters.FriendsToGroupAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.AddGroupFriendsDto;
import domain.client.dto.GroupFriendDto;
import domain.client.enums.OperationType;

public class DialogFriendsFragment extends DialogFragment {

    private static final String GROUP_ID = "GROUP_ID";
    private static final String FRIENDS_ID = "FRIENDS_ID";

    private Long groupId;

    public DialogFriendsFragment() {
    }

    public static DialogFriendsFragment newInstance(Long groupId, ArrayList<GroupFriendDto> friends) {
        DialogFriendsFragment fragment = new DialogFriendsFragment();
        Bundle args = new Bundle();
        args.putLong(GROUP_ID, groupId);
        args.putSerializable(FRIENDS_ID, friends);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView friendsRv = view.findViewById(R.id.dialog_group_rv);
        Button btnCancel = view.findViewById(R.id.dialog_cancel_tv);
        Button btnOk = view.findViewById(R.id.dialog_ok_tv);

        groupId = getArguments().getLong(GROUP_ID);
        List<GroupFriendDto> friends = (List<GroupFriendDto>) getArguments().getSerializable(FRIENDS_ID);

        FriendsToGroupAdapter adapter = new FriendsToGroupAdapter(friends);
        friendsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendsRv.setAdapter(adapter);

        btnCancel.setOnClickListener((v) -> dismiss());
        btnOk.setOnClickListener((v) -> addFriendsToGroup(adapter::getCheckedIds));
    }

    private void addFriendsToGroup(Supplier<Set<Long>> friendIdsSupplier) {
        List<Long> checkedIds = new ArrayList<>(friendIdsSupplier.get());
        if (checkedIds.size() == 0) {
            Toast.makeText(getActivity(), "Please, select at least one friend!", Toast.LENGTH_LONG).show();
        } else {
            ServerRequest<AddGroupFriendsDto> request = new ServerRequest<>(OperationType.ADD_GROUP_FRIENDS);
            AddGroupFriendsDto addGroupFriendsDto = new AddGroupFriendsDto();
            addGroupFriendsDto.setGroupId(groupId);
            addGroupFriendsDto.setUserIds(checkedIds);
            request.setData(addGroupFriendsDto);
            NetClient.sendRequest(request);
            dismiss();
        }
    }
}