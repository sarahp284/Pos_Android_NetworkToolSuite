package com.example.pos_networktoolsuite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PingScanner();
        TextView tv = (TextView) findViewById(R.id.output2);
        Intent intent = new Intent(this,PingScanner.class);
        startActivity(intent);
        for (String s: PingScanner.getWifis()
             ) {
            tv.append(s);
        }
    }
}
