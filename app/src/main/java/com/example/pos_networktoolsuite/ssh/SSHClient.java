package com.example.pos_networktoolsuite.ssh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class SSHClient extends Activity {


        @SuppressLint("StaticFieldLeak")
        public void onSSHconnect(){

            Log.i("inonfuckingcreate","inonfuckingcreate");
            new AsyncTask<Integer, Void, Void>(){
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        publishProgress(new Void[0]);
                        executeRemoteCommand("root", "toor","192.168.1.238", 22, "ls");

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
StringBuilder o=new StringBuilder();
            // Execute command
            channelssh.setCommand(command);
            InputStream in = channelssh.getInputStream();
            InputStream err = channelssh.getExtInputStream();
            channelssh.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    o.append(new String(tmp, 0, i));
                }
                if (channelssh.isClosed()) {
                    if ((in.available() > 0) || (err.available() > 0)) continue;
                    System.out.println("exit-status: " + channelssh.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

            channelssh.disconnect();
            Log.w("tst","test");
            Log.w("output",o.toString());
            return baos.toString();
        }
}
