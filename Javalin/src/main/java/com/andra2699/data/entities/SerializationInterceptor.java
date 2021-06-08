package com.andra2699.data.entities;

import com.andra2699.ApplicationContext;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;

import java.io.Serializable;

public class SerializationInterceptor extends EmptyInterceptor {
    private static final String ENTITIES_PACKAGE = "com.andra2699.data.entities.";

    public final ApplicationContext applicationContext;

    public SerializationInterceptor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
        if (!entityName.startsWith(ENTITIES_PACKAGE)) {
            return null;
        }
        String className = entityName.substring(ENTITIES_PACKAGE.length());
        return switch (className) {
            case "User" -> new User(applicationContext, (String)id);
            case "Device" -> new Device(applicationContext, (String)id);
            default -> null;
        };
    }
}
