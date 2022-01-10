package com.example.myapplication.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalFileData {

    private final String fileName;
    private final String fileExt;
    private final byte[] data;

    public LocalFileData(Context context, Uri uri) throws IOException {
        String fullName = getFullName(context, uri);
        this.fileName = fullName.substring(0, fullName.lastIndexOf("."));
        this.fileExt = fullName.substring(fullName.lastIndexOf(".") + 1);
        InputStream stream = context.getContentResolver().openInputStream(uri);
        this.data = readBytes(stream);
    }

    private String getFullName(Context context, Uri uri) {
        /// From:  https://developer.android.com/training/secure-file-sharing/retrieve-info
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

    @NonNull
    private static byte[] readBytes(InputStream inputStream) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            return bos.toByteArray();
        }
    }

    public String getFileExt() {
        return fileExt;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}
