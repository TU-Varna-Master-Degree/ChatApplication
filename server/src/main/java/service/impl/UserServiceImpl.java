package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.client.ServerResponse;
import domain.entities.User;
import domain.enums.StatusCode;
import org.apache.commons.codec.digest.DigestUtils;
import service.UserService;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public ServerResponse register(User user) {
        if (userDao.checkUserEmailExist(user.getEmail())) {
            return new ServerResponse(StatusCode.FAILED, "User email already exists!");
        }

        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        userDao.save(user);
        return new ServerResponse(StatusCode.SUCCESSFUL);
    }

    public ServerResponse login(User user) {
        String sha256Password = DigestUtils.sha256Hex(user.getPassword());

        if (userDao.login(user.getEmail(), sha256Password)) {
            return new ServerResponse(StatusCode.SUCCESSFUL);
        } else {
            return new ServerResponse(StatusCode.FAILED, "Please, try again!");
        }
    }
}
