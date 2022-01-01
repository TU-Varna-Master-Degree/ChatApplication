package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import domain.client.dto.GroupDto;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    final private List<GroupDto> data;
    final private Consumer<Long> showMessages;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView usernamesTv;
        private TextView lastDateTv;
        private DateTimeFormatter shortTimeFormatter;
        private DateTimeFormatter shortDateFormatter;

        public ViewHolder(@NonNull View itemView) {
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
            if (Duration.between(lastSendMessageDate, LocalDateTime.now()).toHours() < 24) {
                dateTimeFormat = shortTimeFormatter.format(lastSendMessageDate);
            } else {
                dateTimeFormat = shortDateFormatter.format(lastSendMessageDate);
            }

            lastDateTv.setText(dateTimeFormat);
        }
    }

    public GroupAdapter(List<GroupDto> data, Consumer<Long> showMessages) {
        this.data = data;
        this.showMessages = showMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_list_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupDto group = data.get(position);
        holder.setImageView(group.getGroupUsers().size());
        holder.setUsernamesTv(group.getGroupUsers());
        holder.setLastDateTv(group.getLastSendMessageDate());
        holder.itemView.setOnClickListener((v) -> showMessages.accept(group.getGroupId()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
