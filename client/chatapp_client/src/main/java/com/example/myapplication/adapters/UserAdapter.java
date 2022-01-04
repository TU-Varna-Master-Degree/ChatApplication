package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.holders.UserViewHolder;

import java.util.List;
import java.util.stream.IntStream;

import domain.client.dto.FindFriendDto;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    final private List<FindFriendDto> data;

    public UserAdapter(List<FindFriendDto> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_element, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        FindFriendDto user = data.get(position);
        holder.setUserId(user.getId());
        holder.setUsername(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeUserById(Long userId) {
        int position = IntStream.range(0, data.size())
            .filter(i -> data.get(i).getId().equals(userId))
            .findFirst()
            .orElse(-1);

        if (position != -1) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
}
