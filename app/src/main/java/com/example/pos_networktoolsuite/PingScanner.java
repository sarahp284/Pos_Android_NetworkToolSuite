package com.example.pos_networktoolsuite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PingScanner {


    public String ping(String host) throws IOException {
        Runtime r=Runtime.getRuntime();
        String output="";


        Process p=r.exec(new String[] {"ping", "-c 1", host});
        BufferedReader in=new BufferedReader(new InputStreamReader(p.getInputStream()));
        String inputLine;
        while((inputLine=in.readLine())!=null)
        {
            if(inputLine.contains("time")){
               String test=inputLine.substring(inputLine.indexOf("time")+5,inputLine.indexOf("ms")+2);
                output += test;
                break;

            }
        }

        return output;
    }
}



