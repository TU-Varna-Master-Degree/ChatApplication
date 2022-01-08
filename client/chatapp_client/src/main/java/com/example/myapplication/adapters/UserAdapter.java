package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.models.FindFriend;
import com.example.myapplication.holders.UserViewHolder;
import com.example.myapplication.utils.NetClient;

import java.util.List;
import java.util.stream.IntStream;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    final private List<FindFriend> data;
    final private NetClient client;

    public UserAdapter(List<FindFriend> data, NetClient client) {
        this.data = data;
        this.client = client;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_element, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        FindFriend user = data.get(position);

        holder.setUsername(user.getUsername());
        holder.setOnAddFriend((view) -> {
            ServerRequest<Long> request = new ServerRequest<>(OperationType.CREATE_FRIENDSHIP);
            request.setData(user.getId());
            client.sendRequest(request);
        });
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
