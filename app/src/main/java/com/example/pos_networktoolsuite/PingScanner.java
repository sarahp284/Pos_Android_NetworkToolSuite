package com.example.pos_networktoolsuite;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class PingScanner {


    public String ping(String host) throws IOException {
        Runtime r=Runtime.getRuntime();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String output="";
        if(InetAddress.getByName(host).isReachable(200)) {
            Process p = r.exec(new String[]{"ping", "-c 1 -w 1", host});
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Log.w("output", inputLine);
                if (inputLine.contains("time 0ms")) {
                    output = "Ping not successful";
                    break;
                }
                if (inputLine.contains("time=")) {
                    String test = inputLine.substring(inputLine.indexOf("time") + 5, inputLine.indexOf("ms") + 2);
                    output += test;
                    break;

                }
            }
        }else{
            output="Host not reachable";
        }

        return output;
    }
}



