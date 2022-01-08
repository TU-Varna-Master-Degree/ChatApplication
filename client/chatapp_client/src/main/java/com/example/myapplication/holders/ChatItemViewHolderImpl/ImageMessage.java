package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.R;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import domain.client.dto.MessageDto;

public class ImageMessage extends FileMessage
{
    ImageView preview;
    LinearLayout layout;
    BiConsumer<Long, ImageView>  imageViewData;
    
    public ImageMessage(View view, Consumer<Long> onDownloadRequestCallback, BiConsumer<Long, ImageView> requestImageView)
    {
        super(view, onDownloadRequestCallback);
        this.imageViewData = requestImageView;
        
        layout  = view.findViewById(R.id.chat_item_file_container);
        preview = new ImageView(layout.getContext());
        preview.setMaxWidth( 300 );
        preview.setMinimumWidth(300);
        preview.setMaxHeight( 300 );
        preview.setMinimumHeight(300);
       
    }
    
    @Override
    public void setMessageContent(MessageDto data)
    {
        super.setMessageContent(data);
        try{
            Bitmap bmp = BitmapFactory.decodeByteArray(data.getFile(), 0, data.getFile().length);
            preview.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300, 300, false));
        }
        catch (Exception e)
        {
            String strnig = e.getMessage();
        }
        layout.addView(preview);
        // imageViewData.accept(data.getNotificationId(), preview);
    }
}
