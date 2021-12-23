package service;

import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;

public interface UserService {

    public ServerResponse register(UserDto userDto);

    public ServerResponse login(UserDto userDto);
}
