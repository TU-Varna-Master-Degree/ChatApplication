package com.example.myapplication.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidLocalFileData
{
    String fileExt;
    String fileName;
    byte[] data;
    
    public AndroidLocalFileData(Context context, Uri uri) throws IOException
    {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        
        this.fileName = queryForFileName( context, uri );
        this.fileExt = fileName.substring(fileName.lastIndexOf("."));
        this.data = readBytes(iStream);
    }
    
    public String getFileExt()
    {
        return fileExt;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public byte[] getData()
    {
        return data;
    }
    
    private String queryForFileName(Context context, Uri uri)
    {
        /// From:  https://developer.android.com/training/secure-file-sharing/retrieve-info
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        
        return returnCursor.getString(nameIndex);
    }
    
    @NonNull
    private static byte[] readBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
