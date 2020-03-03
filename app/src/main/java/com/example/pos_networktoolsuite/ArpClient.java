package com.example.pos_networktoolsuite;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;

public class ArpClient extends Application {

    LinkedList<String> devices = new LinkedList<>();
    WifiManager mWifiManager;
private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //  instance = this;
        mContext = getApplicationContext();
    }

    public void pingservice(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            String subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);


            for (int i = 1; i < 255; i++) {

                String host = subnet + "." + i;
                System.out.println(host);

                try {
                    if (InetAddress.getByName(host).isReachable(10000)) {

                        String strMacAddress = getMacFromIP(host);

                        Log.w("DeviceDiscovery", "Reachable Host: " + String.valueOf(host) + " and Mac : " + strMacAddress + " is reachable!");
                        log("DeviceDiscovery Reachable Host: " + String.valueOf(host) + " and Mac : " + strMacAddress + " is reachable!");

                    } else {
                        Log.e("DeviceDiscovery", "❌ Not Reachable Host: " + String.valueOf(host));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }
    public void log(String message)
    {
        devices.add(message);
    }

    public LinkedList<String> getDevices()
    {
        return devices;
    }

    public String getMacFromIP(String ipaddress) {
        String mac = "";
        Log.i("IPScanning", "Scan was started");
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" +");
                if (parts != null && parts.length >= 4) {
                    String ip = parts[0];
                    String macadr = parts[3];
                    if (mac.matches("..:..:..:..:..:..")) {

                        if (ip.equalsIgnoreCase(ipaddress)) {
                            System.out.println(mac);
                            return mac;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

return "";
    }
    private String getSubnetAddress(int address)
    {
       String ipString="";
        try {
             ipString = String.format(
                    "%d.%d.%d",
                    (address & 0xff),
                    (address >> 8 & 0xff),
                    (address >> 16 & 0xff));
        }catch(Exception e){

        }
            return ipString;

    }

    public static void main(String[] args) {
        ArpClient ac=new ArpClient();
        ac.pingservice();
    }
}
