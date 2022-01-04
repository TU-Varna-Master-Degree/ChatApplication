package com.example.myapplication.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.function.BiConsumer;

public class FriendsToGroupViewHolder extends RecyclerView.ViewHolder {

    private Long userId;
    private final TextView usernameTv;
    private final CheckBox userCb;
    private final BiConsumer<Boolean, Long> changeCheckedIds;

    public FriendsToGroupViewHolder(@NonNull View itemView, BiConsumer<Boolean, Long> changeCheckedIds) {
        super(itemView);
        this.changeCheckedIds = changeCheckedIds;
        usernameTv = itemView.findViewById(R.id.home_user_tv);
        userCb = itemView.findViewById(R.id.dialog_user_cb);
        userCb.setOnClickListener((v) -> addFriendId(false));
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        usernameTv.setText(username);
    }

    public void addFriendId(boolean changeCb) {
        if (changeCb) {
            userCb.setChecked(!userCb.isChecked());
        }
        changeCheckedIds.accept(userCb.isChecked(), userId);
    }
}