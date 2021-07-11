package com.example.graphicuserinterface.objects;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Arrays;

public class Socket implements Serializable {
    String id;
    boolean[] state;

    public Socket(String id) {
        this.id = id;
        this.state = new boolean[]{false, false, false, false};
    }

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
    public boolean getStateIndex(int index){ return this.state[index]; }

    public void setState(boolean[] state) {
        this.state = state;
    }

    public void getSocket(){}
    public void putSocket(){}

    @Override
    public String toString() {
        return "Socket{" +
                "id='" + id + '\'' +
                ", state=" + Arrays.toString(state) +
                '}';
    }
}
