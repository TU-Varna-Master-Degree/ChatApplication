package service.impl;

import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;
import domain.client.enums.StatusCode;
import service.SessionService;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class SessionServiceImpl implements SessionService {

    private Map<Long, SocketChannel> sessions;

    public SessionServiceImpl() {
        sessions = new HashMap<>();
    }

    public void createSession(ServerResponse serverResponse, SocketChannel socketChannel, SelectionKey key) {
        if (serverResponse.getCode().equals(StatusCode.SUCCESSFUL)) {
            Long userId = (Long) serverResponse.getData();
            sessions.put(userId, socketChannel);
            key.attach(userId);
        }
    }

    public void destroySession(SelectionKey key) {
        sessions.remove(key.attachment());
    }

    public boolean isAuthorized(SelectionKey key) {
        return key.attachment() != null;
    }

    public Long getCurrentUserId(SelectionKey key) {
        return (Long) key.attachment();
    }

    public SocketChannel getChannelById(Long userId) {
        return sessions.get(userId);
    }
}
