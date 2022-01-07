package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

import domain.client.dto.MessageDto;

public class FileMessage extends ImplBase
{
    CardView fileLink;
    TextView fileName;
    
    public FileMessage(View view)
    {
        super(view);
        fileLink = view.findViewById(R.id.chat_item_file_link);
        fileName = view.findViewById(R.id.chat_item_file_text);
    
        installLinkBehaviour();
        
    }
    
    private void installLinkBehaviour()
    {
    
    }
    
    @Override
    public void setMessageContent(MessageDto data)
    {
        super.setMessageContent(data);
        
        String name = data.getFileName() + data.getFileType();
        fileName.setText(name);
    }
}
