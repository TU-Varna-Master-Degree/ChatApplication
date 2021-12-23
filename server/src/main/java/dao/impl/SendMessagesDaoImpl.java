package dao.impl;

import dao.SendMessageDao;
import domain.client.dto.SendMessageDto;

import javax.persistence.EntityManager;

public class SendMessagesDaoImpl implements SendMessageDao {

    // TODO: Implement
    private static final String QUERY_STRING =
            "INSERT";
    private final EntityManager entityManager;

    public SendMessagesDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void send(SendMessageDto data) {
        entityManager.getTransaction().begin();
        entityManager.createQuery( QUERY_STRING )
                .executeUpdate();
        entityManager.getTransaction().commit();
    }
}
