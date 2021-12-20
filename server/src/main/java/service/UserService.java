package service;

import domain.client.ServerResponse;
import domain.entities.User;

public interface UserService {

    public ServerResponse register(User user);

    public ServerResponse login(User user);
}
