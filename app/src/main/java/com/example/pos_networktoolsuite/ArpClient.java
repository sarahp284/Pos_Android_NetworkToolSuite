package com.example.pos_networktoolsuite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;

public class ArpClient  {

    LinkedList<String> devices = new LinkedList<>();
    WifiManager mWifiManager;
private static Context mContext;
    Handler mHandler = new Handler();
    int hostint=0;


    public ArpClient(Context c){
mContext=c;
    }


    protected String doInBackground(int hostint) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        String subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);
            String host = subnet + "." + hostint;
        String strMacAddress="";
            try {
                if (InetAddress.getByName(host).isReachable(50)) {

                    strMacAddress = getMacFromIP(host);
                    Log.w("IP",host);
                    return  "IP Adr.: "+host +"\n  MAC Adr.: "+strMacAddress;
                } else {
                  // return "no device";
                }
            } catch (IOException e) {
                e.printStackTrace();

        }
        return "a";
    }

    public void log(String message)
    {
        devices.add(message);
    }


    public LinkedList<String> getDevices()
    {
        Log.w("Devicetest",devices.size()+"");
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
                    if (macadr.matches("..:..:..:..:..:..")) {

                        if (ip.equalsIgnoreCase(ipaddress)) {
                            return macadr;
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


}
