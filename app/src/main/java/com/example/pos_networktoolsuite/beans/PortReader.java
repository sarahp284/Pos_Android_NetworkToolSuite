package com.example.pos_networktoolsuite.beans;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.pos_networktoolsuite.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class PortReader {
    String filename="C:\\Users\\Sarah\\AndroidStudioProjects\\POS_NetworkToolSuite\\app\\src\\main\\java\\com\\example\\pos_networktoolsuite\\test\\test.csv";
    Map<String,String> ports=new TreeMap<>();

    public void read(InputStream is) throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String line="";
        while((line=br.readLine())!=null){
            String parts[]=line.split(";");
            ports.put(parts[0],parts[1]);
        }
    }


    public Map<String,String> getPorts(){
        return ports;
    }
}
