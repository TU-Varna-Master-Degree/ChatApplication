package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;

import java.util.List;
import java.util.function.Consumer;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.FriendshipDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.enums.FriendshipState;
import domain.client.enums.OperationType;

public class FriendshipAdapter extends RecyclerView.Adapter<FriendshipAdapter.ViewHolder> {

    final private List<FriendshipDto> data;
    final private Consumer<Long> showMessages;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Long receiverId;
        private final TextView usernameTv;
        private final ImageButton acceptButton;
        private final ImageButton rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTv = itemView.findViewById(R.id.home_user_tv);
            acceptButton = itemView.findViewById(R.id.item_button_accept);
            rejectButton = itemView.findViewById(R.id.item_button_reject);
        }

        public void setUsernameTv(String username) {
            usernameTv.setText(username);
        }

        public void setReceiverId(Long receiverId) {
            this.receiverId = receiverId;
        }

        public void showButtons() {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            acceptButton.setOnClickListener((v) -> changeFriendshipState(FriendshipState.ACCEPTED));
            rejectButton.setOnClickListener((v) -> changeFriendshipState(FriendshipState.REJECTED));
        }

        private void changeFriendshipState(FriendshipState newState) {
            ServerRequest<UpdateFriendshipDto> request = new ServerRequest<>(OperationType.UPDATE_FRIENDSHIP);
            UpdateFriendshipDto updateFriendshipDto = new UpdateFriendshipDto();
            updateFriendshipDto.setReceiverId(receiverId);
            updateFriendshipDto.setFriendshipState(newState);
            request.setData(updateFriendshipDto);
            NetClient.sendRequest(request);
        }
    }

    public FriendshipAdapter(List<FriendshipDto> data, Consumer<Long> showMessages) {
        this.data = data;
        this.showMessages = showMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendship_list_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
