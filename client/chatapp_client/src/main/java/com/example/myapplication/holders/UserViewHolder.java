package com.example.myapplication.holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private Long userId;
    private final TextView usernameTv;
    private final Button addFriend;

    public UserViewHolder(@NonNull View itemView ) {
        super(itemView);

        usernameTv = itemView.findViewById(R.id.home_user_tv);
        addFriend = itemView.findViewById(R.id.home_add_friend);
    }

    public void setOnAddFriend(View.OnClickListener listener ) {
        addFriend.setOnClickListener( listener );
    }

    public void setUsername(String username) {
        usernameTv.setText(username);
    }
}
