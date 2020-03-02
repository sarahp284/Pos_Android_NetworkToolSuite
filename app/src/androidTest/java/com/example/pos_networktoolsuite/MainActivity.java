package com.example.pos_networktoolsuite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pos_networktoolsuite.R;

public class MainActivity extends AppCompatActivity {
    private Button btnClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClick = (Button) findViewById(R.id.button) ;
        btnClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ArpClient arp=new ArpClient();
                arp.pingservice();

            }
        });


    }

}
