package com.example.pos_networktoolsuite.networkscan;

import com.example.pos_networktoolsuite.beans.OpenPort;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

public class PortScan {

    public LinkedList<OpenPort> startPortscan(String ip, int[] ports, int timeout) {
    LinkedList openports = new LinkedList();
        for (int port : ports
        ) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                openports.add(new OpenPort(port, true));
            } catch (Exception ex) {
                openports.add(new OpenPort(port, false));
            }
        }

    return openports;
    }
}
