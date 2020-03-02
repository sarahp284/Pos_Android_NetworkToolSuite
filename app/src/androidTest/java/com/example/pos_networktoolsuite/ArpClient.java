package com.example.pos_networktoolsuite;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

public class ArpClient extends Activity {
    public void pingservice(){
        try {
            WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
           // WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            String subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);


            for (int i = 1; i < 255; i++) {

                String host = subnet + "." + i;
                System.out.println(host);

                try {
                    if (InetAddress.getByName(host).isReachable(10000)) {

                        String strMacAddress = getMacFromIP(host);

                        Log.w("DeviceDiscovery", "Reachable Host: " + String.valueOf(host) + " and Mac : " + strMacAddress + " is reachable!");
                    } else {
                        Log.e("DeviceDiscovery", "❌ Not Reachable Host: " + String.valueOf(host));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){

        }
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
