package com.example.myapplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private final String ip;
    private final int port;
    
    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    
    public Client(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }
    
    public void open() throws IOException
    {
        if(socket != null)
            return;
        
        this.socket = new Socket(
                InetAddress.getByName( ip ),
                port
        );
        this.out = new PrintWriter( socket.getOutputStream(), true );
        this.in = new Scanner( socket.getInputStream() );
    }
    
    public void close() throws IOException
    {
        socket.close();
        socket = null;
    }
    
    public void sendMessage(String msg)
    {
        this.out.println( msg );
    }
    
    public String receiveMessage()
    {
        return in.nextLine();
    }
    
}