package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.models.Group;
import com.example.myapplication.holders.GroupViewHolder;

import java.util.List;
import java.util.function.Consumer;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    final private List<Group> data;
    final private Consumer<Long> showMessages;

    public GroupAdapter(List<Group> data, Consumer<Long> showMessages) {
        this.data = data;
        this.showMessages = showMessages;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_list_element, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = data.get(position);
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
