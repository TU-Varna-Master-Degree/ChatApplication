package com.example.myapplication.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView usernamesTv;
    private TextView lastDateTv;
    private DateTimeFormatter shortTimeFormatter;
    private DateTimeFormatter shortDateFormatter;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_img);
        usernamesTv = itemView.findViewById(R.id.home_user_tv);
        lastDateTv = itemView.findViewById(R.id.item_tv_date);
        shortTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        shortDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public void setImageView(int userCount) {
        if (userCount > 1) {
            imageView.setImageResource(R.drawable.ic_home_people);
        } else {
            imageView.setImageResource(R.drawable.ic_home_person);
        }
    }

    public void setUsernamesTv(List<String> usernames) {
        StringBuilder sb = new StringBuilder();

        for (String username : usernames) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            if (sb.length() + username.length() > 27) {
                sb.append("...");
                break;
            } else {
                sb.append(username);
            }
        }

        usernamesTv.setText(sb.toString());
    }

    public void setLastDateTv(LocalDateTime lastSendMessageDate) {
        String dateTimeFormat;

        if (lastSendMessageDate == null) {
            dateTimeFormat = "";
        } else if (Duration.between(lastSendMessageDate, LocalDateTime.now()).toHours() < 24) {
            dateTimeFormat = shortTimeFormatter.format(lastSendMessageDate);
        } else {
            dateTimeFormat = shortDateFormatter.format(lastSendMessageDate);
        }

        lastDateTv.setText(dateTimeFormat);
    }
}
