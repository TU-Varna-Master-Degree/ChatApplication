package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.holders.FriendshipViewHolder;
import com.example.myapplication.utils.NetClient;

import java.util.List;
import java.util.function.Consumer;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.FriendshipDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.enums.FriendshipState;
import domain.client.enums.OperationType;

public class FriendshipAdapter extends RecyclerView.Adapter<FriendshipViewHolder> {

    final private List<FriendshipDto> data;
    final private Consumer<Long> showMessages;
    final private NetClient client;
    
    public FriendshipAdapter(NetClient client ,List<FriendshipDto> data, Consumer<Long> showMessages) {
        this.data = data;
        this.showMessages = showMessages;
        this.client = client;
    }

    @NonNull
    @Override
    public FriendshipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendship_list_element, parent, false);

        return new FriendshipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendshipViewHolder holder, int position) {
        FriendshipDto friendship = data.get(position);
        holder.setUsernameTv(friendship.getSenderUsername());

        if (FriendshipState.ACCEPTED.equals(friendship.getState())) {
            holder.itemView.setOnClickListener((v) -> showMessages.accept(friendship.getGroupId()));
        } else {
            holder.showButtons((newState)-> {
                ServerRequest<UpdateFriendshipDto> request = new ServerRequest<>(OperationType.UPDATE_FRIENDSHIP);
                {
                    UpdateFriendshipDto updateFriendshipDto = new UpdateFriendshipDto();
                    updateFriendshipDto.setReceiverId(friendship.getSenderId());
                    updateFriendshipDto.setFriendshipState(newState);
                    request.setData(updateFriendshipDto);
                }
                client.sendRequest(request);
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
