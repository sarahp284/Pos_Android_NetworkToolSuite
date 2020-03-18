package com.example.pos_networktoolsuite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pos_networktoolsuite.ssh.SSHClient;


public class MainActivity extends AppCompatActivity {
    private Button btnClick;
    ArpClient arp;
    SSHClient ssh;
    private TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arp=new ArpClient();
        ssh = new SSHClient();
        ssh.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClick = (Button) findViewById(R.id.button);
        output =(TextView) findViewById(R.id.textView);
        btnClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                arp.pingservice();
                for (String s: arp.getDevices()
                     ) {
                    output.append(s);
                }
            }
        });


    }

}
