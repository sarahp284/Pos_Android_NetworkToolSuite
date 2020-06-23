package com.example.pos_networktoolsuite.beans;

/*Beans Class for Ports which are used in Portscan Class*/

public class OpenPort {
    private int port;
    private boolean isOpen;
    private String app;

    public OpenPort(int port, boolean isOpen, String app) {
        this.port = port;
        this.isOpen = isOpen;
        this.app = app;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public int getPort() {
        return port;
    }

    public String getApp() {
        return app;
    }
}
