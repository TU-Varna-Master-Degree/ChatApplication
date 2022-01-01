package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NetClient;
import com.example.myapplication.R;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.FindFriendDto;
import domain.client.dto.FriendshipDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.enums.FriendshipState;
import domain.client.enums.OperationType;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    final private List<FindFriendDto> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Long userId;
        private final TextView usernameTv;

        public ViewHolder(@NonNull View itemView) {
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

    public UserAdapter(List<FindFriendDto> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
