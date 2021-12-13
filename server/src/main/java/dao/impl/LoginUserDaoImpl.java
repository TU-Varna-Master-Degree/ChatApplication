package dao.impl;
import dao.LoginUserDao;
import javax.persistence.EntityManager;


public class LoginUserDaoImpl implements LoginUserDao {
    private final EntityManager entityManager;

    public LoginUserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Validity check
    // Връща true/false ако съществува потребител с такова име и парола
    public boolean Login(String eMail, String passWord) {

        String query = "select count (email)  from User u where"+
                " u.email = :e_mail and u.password = :pass";

        Long count = (Long) entityManager.createQuery( query )
                        .setParameter("e_mail",eMail).
                setParameter("pass",passWord)
                .getSingleResult();

        return !count.equals(0L);

}}
