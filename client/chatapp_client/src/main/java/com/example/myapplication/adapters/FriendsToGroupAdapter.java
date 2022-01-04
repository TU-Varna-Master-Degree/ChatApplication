package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.holders.FriendsToGroupViewHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.client.dto.GroupFriendDto;

public class FriendsToGroupAdapter extends RecyclerView.Adapter<FriendsToGroupViewHolder> {

    final private List<GroupFriendDto> data;
    final private Set<Long> checkedIds;

    public FriendsToGroupAdapter(List<GroupFriendDto> data) {
        this.data = data;
        this.checkedIds = new HashSet<>();
    }

    @NonNull
    @Override
    public FriendsToGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_user_element, parent, false);

        return new FriendsToGroupViewHolder(view, this::changeCheckedIds);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsToGroupViewHolder holder, int position) {
        GroupFriendDto friend = data.get(position);
        holder.setUserId(friend.getId());
        holder.setUsername(friend.getUsername());
        holder.itemView.setOnClickListener((v) -> holder.addFriendId(true));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Set<Long> getCheckedIds() {
        return checkedIds;
    }

    private void changeCheckedIds(Boolean isChecked, Long userId) {
        if (isChecked) {
            checkedIds.add(userId);
        } else {
            checkedIds.remove(userId);
        }
    }
}
