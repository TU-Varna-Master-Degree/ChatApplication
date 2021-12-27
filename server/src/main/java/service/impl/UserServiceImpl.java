package service.impl;

import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;
import domain.client.enums.StatusCode;
import domain.entities.User;
import org.apache.commons.codec.digest.DigestUtils;
import service.UserService;
import org.modelmapper.ModelMapper;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
    }

    public ServerResponse register(UserDto userDto) {
        if (userDao.checkUsernameExist(userDto.getUsername())) {
            return new ServerResponse(StatusCode.FAILED, "Username already exists!");
        }

        if (userDao.checkUserEmailExist(userDto.getEmail())) {
            return new ServerResponse(StatusCode.FAILED, "User email already exists!");
        }

        User user = this.modelMapper.map(userDto, User.class);
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
