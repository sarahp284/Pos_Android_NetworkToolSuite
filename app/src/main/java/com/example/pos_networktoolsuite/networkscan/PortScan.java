package com.example.pos_networktoolsuite.networkscan;

import android.os.StrictMode;
import android.util.Log;

import com.example.pos_networktoolsuite.beans.OpenPort;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

public class PortScan {

    public OpenPort startPortscan(String ip, int port, int timeout) {
        OpenPort op;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
      try{
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
              op=  new OpenPort(port, true);
            } catch (Exception ex) {
         // Log.w("test",ex);
             op=  new OpenPort(port, false);
            }
    return op;
    }
}
