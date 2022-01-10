package com.example.myapplication.holders.MessageHolders;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.example.myapplication.domain.models.Message;

import java.util.function.Consumer;

public class FileMessage extends BaseMessage {

    CardView fileLink;
    TextView fileName;
    Consumer<Long> onDownloadRequest;

    public FileMessage(int boxColor, View view, Consumer<Long> onDownloadRequestCallback) {
        super(view);
        this.fileLink = view.findViewById(R.id.chat_item_file_link);
        this.fileName = view.findViewById(R.id.chat_item_file_text);
        this.fileLink.setBackgroundColor(boxColor);
        this.fileName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.onDownloadRequest = onDownloadRequestCallback;

        installLinkBehaviour();
    }

    private void installLinkBehaviour() {
        fileName.setOnLongClickListener(view -> {
            Long id = FileMessage.this.getData().getNotificationId();
            onDownloadRequest.accept(id);
            return true;
        });
    }

    @Override
    public void setMessageContent(Message data) {
        super.setMessageContent(data);
        String name = String.format("%s.%s", data.getFileName(), data.getFileType());
        fileName.setText(name);
    }
}
