package com.example.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.UserGroup;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{
    final private ArrayList<UserGroup> data;
    
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        ImageView img;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            
            tv = itemView.findViewById(R.id.item_tv_name);
        }
        
        public void setName(String name)
        {
            tv.setText(name);
        }
        
    }
    
    public NotificationAdapter(ArrayList<UserGroup> data)
    {
        this.data = data;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_element, parent, false);
        
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        UserGroup item = data.get(position);
        holder.setName( item.getName() );
    }
    
    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
