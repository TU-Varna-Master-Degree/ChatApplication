package dao;

import domain.entities.User;

public interface UserDao {

    public boolean checkUsernameExist(String username);

    public boolean checkUserEmailExist(String email);

    public void save(User user);

    public Long login(String username, String password);

    public User getById(Long userId);
}
