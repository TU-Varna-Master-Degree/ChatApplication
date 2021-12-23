package dao;

import domain.entities.User;

public interface UserDao {

    public boolean checkUserEmailExist(String email);

    public void save(User user);

    public Long login(String email, String password);
}
