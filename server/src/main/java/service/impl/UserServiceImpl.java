package service.impl;

import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;
import domain.enums.StatusCode;
import domain.entities.User;
import org.apache.commons.codec.digest.DigestUtils;
import service.UserService;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public ServerResponse register(UserDto userDto) {
        if (userDao.checkUsernameExist(userDto.getUsername())) {
            return new ServerResponse(StatusCode.FAILED, "Username already exists!");
        }

        if (userDao.checkUserEmailExist(userDto.getEmail())) {
            return new ServerResponse(StatusCode.FAILED, "User email already exists!");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(DigestUtils.sha256Hex(userDto.getPassword()));

        userDao.save(user);
        return new ServerResponse(StatusCode.SUCCESSFUL);
    }

    public ServerResponse login(UserDto userDto) {
        String sha256Password = DigestUtils.sha256Hex(userDto.getPassword());
        Long userId = userDao.login(userDto.getUsername(), sha256Password);

        if (userId != null) {
            ServerResponse<Long> response = new ServerResponse<>(StatusCode.SUCCESSFUL);
            response.setData(userId);
            return response;
        } else {
            return new ServerResponse(StatusCode.FAILED, "Please, try again!");
        }
    }
}
