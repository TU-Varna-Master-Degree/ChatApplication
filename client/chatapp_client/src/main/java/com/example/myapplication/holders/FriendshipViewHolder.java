package com.example.myapplication.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.function.Consumer;

import domain.client.enums.FriendshipState;

public class FriendshipViewHolder extends RecyclerView.ViewHolder {
    
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
    

    public void showButtons(Consumer<FriendshipState> changeFriendshipState) {
        acceptButton.setVisibility(View.VISIBLE);
        rejectButton.setVisibility(View.VISIBLE);
        acceptButton.setOnClickListener((v) -> changeFriendshipState.accept(FriendshipState.ACCEPTED));
        rejectButton.setOnClickListener((v) -> changeFriendshipState.accept(FriendshipState.REJECTED));
    }
}


