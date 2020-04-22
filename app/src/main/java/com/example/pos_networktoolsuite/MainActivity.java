package com.example.pos_networktoolsuite;
import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pos_networktoolsuite.ssh.SSHClient;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    private Button btnClick;
    private ListView liste;
    TextView welcomev;
    ImageView iv;
    ImageView home;
    int listcolor=0;
    int menu = 0;
    int action =0;
    ListView lv;

public void Display(View v){
    if (menu % 2 ==0) {

        iv.setVisibility(View.INVISIBLE);
        welcomev.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
        lv.setEnabled(true);
    } else {
        iv.setVisibility(View.VISIBLE);
        welcomev.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.INVISIBLE);
        lv.setEnabled(false);
    }
    menu++;

}
public void init(){
    setContentView(R.layout.startpage);
    iv=findViewById(R.id.menuBar);
    menu=0;
    //   t= Typeface.createFromAsset(getAssets(),"fonts/Alcubierre.otf");
    final Context c = getApplicationContext();
    lv = findViewById(R.id.action);
    welcomev = findViewById(R.id.welcomeview);
    welcomev.setVisibility(View.INVISIBLE);
    welcomev.setBackgroundResource(R.drawable.customrect2);
    //  welcomev.setTypeface(t);
    ImageView i = findViewById(R.id.menuBar);
    final ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    listItems.add("ARP Scan");
    listItems.add("Ping Client");
    listItems.add("SSH Client");
    listItems.add("Portscan");
    listItems.add("Tracert");
    lv.setBackgroundResource(R.drawable.customrect);
    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems){
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Cast the list view each item as text view
            TextView item = (TextView) super.getView(position,convertView,parent);
            item.setTextColor(Color.parseColor("#ffffff"));
            item.setTypeface(t);
            item.setTextSize(20);
            return item;
        }
    };
    lv.setAdapter(adapter);
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
            if(action==2){

            }
        }


    });



}
    Typeface t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }
    public void callARPClient() {
        // Begin the transaction
        setContentView(R.layout.arpclient);
        ArrayAdapter<String> adapter;
        // Begin the transaction
        home=findViewById(R.id.imageView2);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               init();
            }
        });
        liste=findViewById(R.id.liste);
        btnClick=findViewById(R.id.button);
       final ArrayList<String> listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                item.setTextColor(Color.parseColor("#ffffff"));
                item.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.device,0,0,0);
     item.setTypeface(t);
                item.setBackgroundColor(Color.parseColor("#D0000000"));
                item.setTextSize(16);

                return item;
            }
        };
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
                            String set = "  "+ar.doInBackground(i);
                            if (!set.equals("  a")) {
                                listItems.add(String.format("%10s",set));
                                finalAdapter.notifyDataSetChanged();
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

        setContentView(R.layout.pingclient);
        final EditText et = findViewById(R.id.ping);
        final ListView pingres = findViewById(R.id.ping_results);
        final Button pingb = findViewById(R.id.ping_button);
        et.setEnabled(true);
        home=findViewById(R.id.imageViewhelp);
        home.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
            }
        });
        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList<String>();
        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);
                String s=item.getText()+"";
                if(s.contains("ms")) {
                    s = s.substring(s.indexOf("ms")-4, s.indexOf("ms"));
                    Double d = Double.parseDouble(s);
                    if (d < 200) {
                        item.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.fast, 0);
                    }
                    if (d >= 200 && d <= 400) {
                        item.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.slow, 0);
                    }
                }else{
                    item.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.nosuccess,0);
                }
                item.setTextColor(Color.parseColor("#ffffff"));
                item.setTypeface(t);

                item.setBackgroundColor(Color.parseColor("#D0000000"));
                item.setTextSize(18);
                return item;
            }
        };
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
                                    listItems.add(String.format("%s %10s"+"\n",et.getText(),a));
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
    public void callSSH(){
        SSHClient sc=new SSHClient();
        sc.onSSHconnect();
    }
    public void hideKeyBoard(){
        View view1 = this.getCurrentFocus();
        if(view1!= null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

}

