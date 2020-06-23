package com.example.pos_networktoolsuite.networkscan;

import android.os.StrictMode;
import android.util.Log;

import com.example.pos_networktoolsuite.beans.OpenPort;
import com.example.pos_networktoolsuite.beans.PortReader;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/*Class which performs port scans on specific host ips*/
public class PortScan {
    PortReader pr;
    Map<String, String> ports = new TreeMap<>();

    public PortScan(Map<String, String> ports) {
        this.ports = ports;
    }

//Method returns list of all ports with boolean if open or not

    public OpenPort startPortscan(String ip, int port, int timeout) {
        OpenPort op;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
            request.setHost(ip);
            IcmpPingResponse help = IcmpPingUtil.executePingRequest(request);
            String returnm = help + "";
            if (returnm.contains("successFlag: true")) {


                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                String app = "";
                op = new OpenPort(port, true, app);
            } else {
                op = new OpenPort(0, false, "");
            }
        } catch (Exception ex) {
            // Log.w("test",ex);
            op = new OpenPort(port, false, "");
        }
        return op;
    }
}
