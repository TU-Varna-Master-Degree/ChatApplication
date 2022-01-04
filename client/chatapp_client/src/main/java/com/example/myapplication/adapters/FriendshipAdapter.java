package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.holders.FriendshipViewHolder;

import java.util.List;
import java.util.function.Consumer;

import domain.client.dto.FriendshipDto;
import domain.client.enums.FriendshipState;

public class FriendshipAdapter extends RecyclerView.Adapter<FriendshipViewHolder> {

    final private List<FriendshipDto> data;
    final private Consumer<Long> showMessages;

    public FriendshipAdapter(List<FriendshipDto> data, Consumer<Long> showMessages) {
        this.data = data;
        this.showMessages = showMessages;
    }

    @NonNull
    @Override
    public FriendshipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendship_list_element, parent, false);

        return new FriendshipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendshipViewHolder holder, int position) {
        FriendshipDto friendship = data.get(position);
        holder.setUsernameTv(friendship.getSenderUsername());

        if (FriendshipState.ACCEPTED.equals(friendship.getState())) {
            holder.itemView.setOnClickListener((v) -> showMessages.accept(friendship.getGroupId()));
        } else {
            holder.setReceiverId(friendship.getSenderId());
            holder.showButtons();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
