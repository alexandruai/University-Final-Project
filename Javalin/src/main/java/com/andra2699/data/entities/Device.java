package com.andra2699.data.entities;

import com.andra2699.ApplicationContext;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Accessors(chain = true)
public class Device {
    public static final int SOCKET_COUNT = 4;

    @Transient
    private final ApplicationContext applicationContext;

    public Device(ApplicationContext applicationContext, String id) {
        this.applicationContext = applicationContext;
        this.id = id;
    }

    @Getter @Id
    private String id;

    private String states;

    private boolean getState(int index) {
        if (index < 0 || index >= SOCKET_COUNT) {
            throw new IllegalArgumentException();
        }
        return Boolean.parseBoolean(states.split(" ")[index]);
    }

    private void setState(int index, boolean value) {
        if (index < 0 || index >= SOCKET_COUNT) {
            throw new IllegalArgumentException();
        }
        Boolean[] s = Arrays.stream(states.split(" ")).map(Boolean::parseBoolean).toArray(Boolean[]::new);
        s[index] = value;
        states = Arrays.stream(s).map(Object::toString).collect(Collectors.joining(" "));
    }

    @Getter
    @ManyToMany(mappedBy = "devices", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

}
