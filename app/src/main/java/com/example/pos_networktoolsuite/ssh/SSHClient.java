package com.example.pos_networktoolsuite.ssh;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.example.pos_networktoolsuite.MainActivity;
import com.example.pos_networktoolsuite.R;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class SSHClient extends Activity {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            Log.i("inonfuckingcreate","inonfuckingcreate");
            setContentView(R.layout.activity_main);
            new AsyncTask<Integer, Void, Void>(){
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        publishProgress(new Void[0]);
                        executeRemoteCommand("root", "myPW","192.168.0.26", 22, "ls");

                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);
                    Log.w("f","f");
                }
            }.execute(1);
        }

        public static String executeRemoteCommand(String username,String password,String hostname,int port, String command)
                throws Exception {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            session.connect();

            // SSH Channel
            ChannelExec channelssh = (ChannelExec)
                    session.openChannel("exec");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channelssh.setOutputStream(baos);

            // Execute command
            channelssh.setCommand(command);
            channelssh.connect();
            channelssh.disconnect();

            return baos.toString();
        }
}
