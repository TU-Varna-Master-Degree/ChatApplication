package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.enums.FriendshipState;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.models.Friendship;
import com.example.myapplication.domain.models.UpdateFriendship;
import com.example.myapplication.holders.FriendshipViewHolder;
import com.example.myapplication.utils.NetClient;

import java.util.List;
import java.util.function.Consumer;

public class FriendshipAdapter extends RecyclerView.Adapter<FriendshipViewHolder> {

    final private List<Friendship> data;
    final private Consumer<Long> showMessages;
    final private NetClient client;

    public FriendshipAdapter(NetClient client ,List<Friendship> data, Consumer<Long> showMessages) {
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
        Friendship friendship = data.get(position);
        holder.setUsernameTv(friendship.getSenderUsername());

        if (FriendshipState.ACCEPTED.equals(friendship.getState())) {
            holder.itemView.setOnClickListener((v) -> showMessages.accept(friendship.getGroupId()));
        } else {
            holder.showButtons((newState)-> {
                ServerRequest<UpdateFriendship> request = new ServerRequest<>(OperationType.UPDATE_FRIENDSHIP);
                {
                    UpdateFriendship updateFriendship = new UpdateFriendship();
                    updateFriendship.setReceiverId(friendship.getSenderId());
                    updateFriendship.setFriendshipState(newState);
                    request.setData(updateFriendship);
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
