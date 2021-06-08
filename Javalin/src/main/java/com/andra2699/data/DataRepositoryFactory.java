package com.andra2699.data;

import com.andra2699.ApplicationContext;
import com.andra2699.data.entities.Device;
import com.andra2699.data.entities.SerializationInterceptor;
import com.andra2699.data.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class DataRepositoryFactory {

    public final ApplicationContext applicationContext;
    public final SessionFactory sessionFactory;

    public DataRepositoryFactory(ApplicationContext applicationContext, String databasePath) {
        if (databasePath == null || databasePath.isEmpty())  databasePath = "mem:test";
        this.applicationContext = applicationContext;
        Configuration cfg = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Device.class)
                .setInterceptor(new SerializationInterceptor(applicationContext));
        ServiceRegistry sevReg = new StandardServiceRegistryBuilder()
                .applySettings(hibernatePropsH2("jdbc:h2:" + databasePath))
                .build();
        sessionFactory = cfg.buildSessionFactory(sevReg);
    }

    public DataRepository create() {
        return new DataRepository(applicationContext, sessionFactory.openSession());
    }

    private static Properties hibernatePropsH2(String h2url) {
        Properties hProps = new Properties();
        hProps.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hProps.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        hProps.setProperty("hibernate.connection.url", h2url);
        hProps.setProperty("hibernate.connection.username", "sa");
        hProps.setProperty("hibernate.connection.password", "");
        hProps.setProperty("hibernate.hbm2ddl.auto", "update");
        hProps.setProperty("hibernate.show_sql", "false");
        return hProps;
    }
}
