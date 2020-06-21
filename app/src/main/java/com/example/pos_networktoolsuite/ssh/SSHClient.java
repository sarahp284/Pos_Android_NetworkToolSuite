package com.example.pos_networktoolsuite.ssh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class SSHClient extends Activity {
private static String output;
private String commanduse = "ls";
private String uname;
private Session session;
    ByteArrayOutputStream baos;
    InputStream in;
    InputStream err;
    private ChannelExec channelssh;
    StringBuilder o;
    String previousreturn = "azter12aw1";
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
                            Log.w("test",e.toString());
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

        public String executeRemoteCommand(String username,String password,String hostname,int port, String command)
                throws Exception {
        Log.w("command", command);
            if(session == null)
            {
                session = jsch.getSession(username, hostname, port);
                session.setPassword(password);
                // Avoid asking for key confirmation
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
            }
            baos = new ByteArrayOutputStream();
                channelssh = (ChannelExec)
                        session.openChannel("exec");
                Log.w("test","connected");
                o= new StringBuilder();
                // Execute command
            commanduse+=" ; "+command;
            Log.w("commanduse",commanduse);
            channelssh.setCommand(commanduse);
            channelssh.setOutputStream(baos);

            channelssh.connect();
            in = channelssh.getInputStream();
            err = channelssh.getExtInputStream();
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
            Log.w("out",o.toString());
            output=StringUtils.remove(o.toString(), previousreturn);
            previousreturn = o.toString();

            Log.w("returnvalue",o.toString());
            Log.w("baosstring", baos.toString());
            return baos.toString();
        }
        public void closeSession()
        {
            channelssh.disconnect();
            channelssh = null;
            session.disconnect();
            session = null;
        }
        public String getOutput(){
            return output;
    }
}
