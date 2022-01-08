package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

import java.util.function.Consumer;

import domain.client.dto.MessageDto;

public class FileMessage extends ImplBase
{
    CardView fileLink;
    TextView fileName;
    Consumer<Long> onDownloadRequest;
    
    public FileMessage(View view, Consumer<Long> onDownloadRequestCallback)
    {
        super(view);
        this.fileLink = view.findViewById(R.id.chat_item_file_link);
        this.fileName = view.findViewById(R.id.chat_item_file_text);
        this.fileName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.onDownloadRequest = onDownloadRequestCallback;
        
        installLinkBehaviour();
        
    }
    
    private void installLinkBehaviour()
    {
        fileName.setOnLongClickListener(view ->
        {
            Long id = FileMessage.this.getData().getNotificationId();
            // onDownloadRequest.accept(id);
            
            return true;
        });
    }
    
    @Override
    public void setMessageContent(MessageDto data)
    {
        super.setMessageContent(data);
        
        String name = data.getFileName() + data.getFileType();
        fileName.setText(name);
    }
}
