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
private static String output;
private String uname;
private static Session session;
private String pw;
private String ip;
    static JSch jsch;
public void setValues(String username, String ip, String pw){
    uname=username;
    this.ip=ip;
    this.pw=pw;
     jsch = new JSch();
}

        @SuppressLint("StaticFieldLeak")
        public void onSSHconnect(final String command){

            Log.i("inonfuckingcreate","inonfuckingcreate");
            new AsyncTask<Integer, Void, Void>(){
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        publishProgress(new Void[0]);
                        executeRemoteCommand(uname, pw,ip, 22, command);

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
            session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            // SSH Channel
            ChannelExec channelssh = (ChannelExec)
                    session.openChannel("exec");
            Log.w("test","connected");
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
                    Thread.sleep(100);
                } catch (Exception ee) {
                    Log.w("test",ee.getMessage());
                }
            }

            channelssh.disconnect();
            Log.w("out",o.toString());
            output=username+": "+o.toString();
            return baos.toString();
        }
        public void closeSession()
        {
            session.disconnect();
        }
        public String getOutput(){
            return output;
    }
}
