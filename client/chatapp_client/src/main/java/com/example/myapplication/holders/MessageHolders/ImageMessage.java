package com.example.myapplication.holders.MessageHolders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.utils.FileSaveDialog;

import java.util.function.Consumer;

public class ImageMessage extends BaseMessage {

    private final CardView cardView;
    private final TextView fileName;
    private final ImageView preview;
    private final Consumer<Long> requestImage;
    private final FileSaveDialog saveDialog;

    public ImageMessage(int boxColor, View view, Consumer<Long> requestImage, FileSaveDialog saveDialog) {
        super(view);
        this.requestImage = requestImage;
        this.saveDialog = saveDialog;

        this.cardView = view.findViewById(R.id.chat_item_file_link);
        this.cardView.setBackgroundColor(boxColor);
        this.fileName = view.findViewById(R.id.chat_item_file_text);
        LinearLayout layout = view.findViewById(R.id.chat_item_file_container);
        preview = new ImageView(layout.getContext());
    }

    @Override
    public void setMessageContent(Message message) {
        super.setMessageContent(message);
        cardView.removeView(preview);

        if (message.getFile() == null) {
            requestImage.accept(message.getNotificationId());
            String name = String.format("%s.%s", message.getFileName(), message.getFileType());
            fileName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            fileName.setText(String.format("Loading file \"%s\" ...", name));
        } else {
            try {
                Bitmap bmp = BitmapFactory.decodeByteArray(message.getFile(), 0, message.getFile().length);
                preview.setImageBitmap(Bitmap.createScaledBitmap(bmp, 480, 270, false));
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileName.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            cardView.getLayoutParams().height = 270;
            cardView.getLayoutParams().width = 480;
            cardView.addView(preview);

            preview.setOnLongClickListener((v) -> {
                saveDialog.openModalSaveAs(message);
                return true;
            });
        }
    }
}
