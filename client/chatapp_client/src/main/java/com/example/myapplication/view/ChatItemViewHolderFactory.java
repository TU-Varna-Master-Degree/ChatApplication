package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ChatItemViewHolderFactory
{
    RecyclerView.ViewHolder create(ViewGroup parent, ChatItemViewType type)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_view_item, parent, false);
        
        
        ChatItemViewHolder viewHolder = new ChatItemViewHolder(view);
        
        switch( type )
        {
            case VIEW_TYPE_POV_TEXT:
            case VIEW_TYPE_POV_IMAGE:
            case VIEW_TYPE_POV_FILE:
                viewHolder.anchorRight();
            case VIEW_TYPE_OTHER_FILE:
            case VIEW_TYPE_OTHER_IMAGE:
            case VIEW_TYPE_OTHER_TEXT:
                viewHolder.anchorLeft();
        }

        return viewHolder;
    }
}
