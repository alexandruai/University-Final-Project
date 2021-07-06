package com.andra2699.data.entities;

import com.andra2699.ApplicationContext;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Accessors(chain = true)
@NamedQueries({
        @NamedQuery(name = "User_FindByUsername", query = "SELECT u from User u where u.username = ?1"),
        @NamedQuery(name = "User_FindByToken", query = "SELECT u from User u where u.token = ?1")
})
public class User {

    @Transient
    private final ApplicationContext applicationContext;

    @Getter @Id
    private String id;

    @Getter
    private String username;

    @Getter @Setter
    private String password;

    @Getter
    private String token;

    public User(ApplicationContext applicationContext, String username, String password) {
        this.id = UUID.randomUUID().toString();
        this.applicationContext = applicationContext;
        this.username = username;
        this.password = password;
    }

    public User(ApplicationContext applicationContext, String id) {
        this.applicationContext = applicationContext;
        this.id = id;
        regenerateToken();
    }

    @Getter
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_device",
            joinColumns = @JoinColumn(name = "device"),
            inverseJoinColumns = @JoinColumn(name = "user"))
    Set<Device> devices = new HashSet<>();

    void addDevice(Device device) {
        devices.add(device);
        device.getUsers().add(this);
    }

    void removeDevice(Device device) {
        devices.remove(device);
        device.getUsers().remove(this);
    }

    String regenerateToken() {
        token = "TK" + UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
}
