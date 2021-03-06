package com.andra2699.data.entities;

import com.andra2699.ApplicationContext;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.Iterator;

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

    @Override
    public void postFlush(Iterator entities) throws CallbackException {
        while (entities.hasNext()) {
            Object obj = entities.next();
            if (obj instanceof Device) {
                JSONArray arr = new JSONArray();
                for (int i = 0; i < 4; i++) {
                    arr.put(((Device) obj).getState(i));
                }
                applicationContext.getRestEndpoint().sendState(((Device) obj).getId(), arr.toString());
            }
        }
    }
}
