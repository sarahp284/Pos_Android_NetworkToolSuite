package com.example.pos_networktoolsuite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private Button btnClick;
    private ListView liste;

int action=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        final Context c = getApplicationContext();
        ListView lv = (ListView) findViewById(R.id.action);
        final ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems);
        lv.setAdapter(adapter);
        listItems.add("ARP \n Scan");
        listItems.add("Ping \n Client");
        listItems.add("SSH \n Client");
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                action = position;
                if(action==0){
                    callARPClient();
                }
            }


        });

    }
    public void callARPClient(){
        setContentView(R.layout.activity_main);
        btnClick = (Button) findViewById(R.id.button);
        liste = (ListView) findViewById(R.id.liste);
        ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        liste.setAdapter(adapter);
        final ArrayAdapter<String> finalAdapter = adapter;
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                final ArpClient ar = new ArpClient(getApplicationContext());
                handler.post(new Runnable() {
                    private int i = 0;

                    public void run() {

                        if (i <= 255) {
                            // Here `this` refers to the anonymous `Runnable`
                            String set = ar.doInBackground(i);
                            if (!set.equals("a")) {
                                listItems.add(set);
                                finalAdapter.notifyDataSetChanged();
                                Log.w("result", set);
                                //  output.append(set + "\n");
                            }
                            i++;
                            handler.postDelayed(this, 1);
                        } else {
                            Log.w("done", "done");
                        }
                    }
                });
            }
        });
    }
}

