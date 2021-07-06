package com.andra2699.data;

import com.andra2699.ApplicationContext;
import com.andra2699.data.entities.Device;
import com.andra2699.data.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Optional;

public class DataRepository {
    private static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

    private final ApplicationContext applicationContext;
    private final Session session;

    private Transaction tx = null;

    DataRepository(ApplicationContext applicationContext, Session session) {
        this.applicationContext = applicationContext;
        this.session = session;
    }

    public Optional<Device> getDeviceByID(String id) {
        return Optional.ofNullable(session.find(Device.class, id));
    }

    public Optional<User> getUserByID(String id) {
        return Optional.ofNullable(session.find(User.class, id));
    }

    public Optional<User> getUserByUsername(String username) {
        Query<User> query = session.createNamedQuery("User_FindByUsername");
        query.setParameter(1, username);
        query.setMaxResults(1);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException _u) {
            return Optional.empty();
        } catch (IllegalStateException | PersistenceException e) {
            logger.error("Failed to fetch user", e);
            return Optional.empty();
        }
    }

    public Optional<User> getUserByToken(String token) {
        Query<User> query = session.createNamedQuery("User_FindByToken");
        query.setParameter(1, token);
        query.setMaxResults(1);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException _u) {
            return Optional.empty();
        } catch (IllegalStateException | PersistenceException e) {
            logger.error("Failed to fetch user", e);
            return Optional.empty();
        }
    }

    public void save(Device device) {
        session.save(device);
    }

    public void save(User user) {
        session.save(user);
    }

    public void beginTransaction() {
        if (tx != null) {
            logger.warn("Reopening transaction");
            return;
        }
        tx = session.beginTransaction();
    }

    public void commitTransaction() {
        if (tx == null) {
            logger.warn("Comining nonexistent transaction");
            return;
        }
        tx.commit();
        tx = null;
    }

    public void close() {
        if (tx != null) tx.commit();
        session.close();
    }
}
