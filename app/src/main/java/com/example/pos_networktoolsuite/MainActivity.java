package com.example.pos_networktoolsuite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private Button btnClick;
    private ListView liste;
    int menu = 0;
    int action = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        final Context c = getApplicationContext();
        final ListView lv = (ListView) findViewById(R.id.action);
        ImageView i = findViewById(R.id.menuBar);
        final ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems);
        lv.setAdapter(adapter);
        listItems.add("ARP\nScan");
        listItems.add("Ping\nClient");
        listItems.add("SSH\nClient");
        adapter.notifyDataSetChanged();
        lv.setVisibility(View.INVISIBLE);
        lv.setEnabled(false);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                action = position;
                if (action == 0) {
                    callARPClient();
                }
                if(action==1){
callPingClient();
                }
            }


        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu % 2 == 0) {
                    lv.setVisibility(View.VISIBLE);
                    lv.setEnabled(true);
                } else {
                    lv.setVisibility(View.INVISIBLE);
                    lv.setEnabled(false);
                }
                menu++;
            }
        });


    }

    public void callARPClient() {
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
    public void callPingClient() {
        final PingScanner ps = new PingScanner();
        setContentView(R.layout.pingclient);
        final EditText et = findViewById(R.id.ping);
        final ListView pingres = findViewById(R.id.ping_results);
        final Button pingb = findViewById(R.id.ping_button);
        et.setEnabled(true);
        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        pingres.setAdapter(adapter);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });

        pingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listItems.clear();
                    hideKeyBoard();
                    final Handler handler = new Handler();
                    final PingScanner ps = new PingScanner();
                    handler.post(new Runnable() {
                        private int i = 0;

                        public void run() {

                            if (i <= 3) {
                                try {
                                    String a=ps.ping(et.getText() + "");
                                    listItems.add(String.format("%25s %10s"+"\n",et.getText(),a));
                                    adapter.notifyDataSetChanged();
                                    handler.postDelayed(this, 100);
                                    i++;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //  output.append(set + "\n");
                            }


                        }
                    });
                } catch (Exception e) {

                }
            }


        });
    }
    public void hideKeyBoard(){
        View view1 = this.getCurrentFocus();
        if(view1!= null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }
}

