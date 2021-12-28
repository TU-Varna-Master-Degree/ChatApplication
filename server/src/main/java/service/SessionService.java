package service;

import domain.client.dialogue.ServerResponse;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public interface SessionService {

    public void createSession(ServerResponse serverResponse, SelectionKey key);

    public void destroySession(SelectionKey key);

    public boolean isAuthorized(SelectionKey key);

    public Long getCurrentUserId(SelectionKey key);

    public SocketChannel getChannelById(Long userId);
}
