package com.example.myapplication.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.utils.NetClient;
import com.example.myapplication.R;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.enums.FriendshipState;
import domain.client.enums.OperationType;

public class FriendshipViewHolder extends RecyclerView.ViewHolder {

    private Long receiverId;
    private final TextView usernameTv;
    private final ImageButton acceptButton;
    private final ImageButton rejectButton;

    public FriendshipViewHolder(@NonNull View itemView) {
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
