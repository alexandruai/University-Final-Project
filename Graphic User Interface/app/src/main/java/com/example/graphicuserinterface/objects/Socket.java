package com.example.graphicuserinterface.objects;

public class Socket {
    String id;
    boolean state[] = new boolean[4];

    public Socket(String id, boolean[] state) {
        this.id = id;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean[] getState() {
        return state;
    }

    public void setState(boolean[] state) {
        this.state = state;
    }

    public void getSocket(){}
    public void putSocket(){}
}
