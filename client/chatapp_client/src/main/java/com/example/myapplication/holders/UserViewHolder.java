package com.example.myapplication.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.utils.NetClient;
import com.example.myapplication.R;

import domain.client.dialogue.ServerRequest;
import domain.client.enums.OperationType;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private Long userId;
    private final TextView usernameTv;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        usernameTv = itemView.findViewById(R.id.home_user_tv);
        itemView.findViewById(R.id.home_add_friend)
                .setOnClickListener(this::createFriendship);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        usernameTv.setText(username);
    }

    private void createFriendship(View view) {
        ServerRequest<Long> request = new ServerRequest<>(OperationType.CREATE_FRIENDSHIP);
        request.setData(userId);
        NetClient.sendRequest(request);
    }
}
