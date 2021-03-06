package com.example.pos_networktoolsuite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.fragment.app.FragmentActivity;
import com.example.pos_networktoolsuite.beans.OpenPort;
import com.example.pos_networktoolsuite.beans.PortReader;
import com.example.pos_networktoolsuite.networkscan.PortScan;
import com.example.pos_networktoolsuite.ssh.SSHClient;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    private Button btnClick;
    private ListView liste;
    TextView welcomev;
    ImageView iv;
    ImageView home;
    int menu = 0;
    int action = 0;
    SSHClient sc = new SSHClient();
    ListView lv;

    //Method for disabling and enabling menu

    public void Display(View v) {
        if (menu % 2 == 0) {

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

    // Init method for startpage
    public void init() {
        setContentView(R.layout.startpage);
        iv = findViewById(R.id.menuBar);
        menu = 0;
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
        //lv.setBackgroundResource(R.drawable.customrect);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);
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
                if (action == 1) {
                    callPingClient();
                }
                if (action == 2) {
                    callSSHDialog();
                }
                if (action == 3) {
                    callportscan();
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
// Method for calling the arp scan and setting the arp page layout

    public void callARPClient() {
        //initialize variables
        setContentView(R.layout.arpclient);
        ArrayAdapter<String> adapter;
        home = findViewById(R.id.imageView2);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
            }
        });
        liste = findViewById(R.id.liste);
        btnClick = findViewById(R.id.button);
        final ArrayList<String> listItems = new ArrayList<String>();
        //set textview settings
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);

                item.setTextColor(Color.parseColor("#ffffff"));
                item.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.device, 0, 0, 0);
                item.setTypeface(t);
                item.setBackgroundColor(Color.parseColor("#D0000000"));
                item.setTextSize(16);

                return item;
            }
        };
        liste.setAdapter(adapter);
        final ArrayAdapter<String> finalAdapter = adapter;
        //run arp scan on network
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
                            String set = "  " + ar.doInBackground(i);
                            if (!set.equals("  a")) {
                                listItems.add(String.format("%10s", set));
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

    // calling ping client and visualize output
    public void callPingClient() {
//initialize variables

        setContentView(R.layout.pingclient);
        final EditText et = findViewById(R.id.ping);
        final ListView pingres = findViewById(R.id.ping_results);
        final Button pingb = findViewById(R.id.ping_button);
        et.setEnabled(true);
        home = findViewById(R.id.imageViewhelp);
        home.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
            }
        });
        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList<String>();
        //set textview settings and append visual sign signaling ping success
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);
                String s = item.getText() + "";
                if (s.contains("ms")) {
                    s = s.substring(s.indexOf("ms") - 4, s.indexOf("ms"));
                    Double d = Double.parseDouble(s);
                    if (d < 200) {
                        item.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.fast, 0);
                    }
                    if (d >= 200 && d <= 400) {
                        item.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.slow, 0);
                    }
                } else {
                    item.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.nosuccess, 0);
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
//check ip, perform ping scan and set output
        pingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = et.getText() + "";
                String base = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
                String regex = base + "\\." + base + "\\."
                        + base + "\\." + base;
                if (input.matches(regex)) {
                    try {
                        listItems.clear();
                        hideKeyBoard();
                        final Handler handler = new Handler();
                        final PingScanner ps = new PingScanner();
                        handler.post(new Runnable() {
                            private int i = 0;
                            //ProgressDialog mProgressDialog=null;
                            public void run() {
                                if (i <= 3) {
                                  //  mProgressDialog  = ProgressDialog.show(MainActivity.this, "Please wait","Long operation starts...", true);
                                    try {

                                        String a = ps.ping(input);
                                        listItems.add(String.format("%s %10s" + "\n", et.getText(), a));
                                        adapter.notifyDataSetChanged();
                                        handler.postDelayed(this, 100);
                                        i++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //  output.append(set + "\n");
                                }else{
                                  //  mProgressDialog.dismiss();
                                }


                            }
                        });
                    } catch (Exception e) {

                    }
                } else {
                    alert("Data was input the wrong format!", "Wrong input format!");
                    hideKeyBoard();
                }
            }


        });
    }

    //setting ssh page layout and adding event handler to connectg button
    public void callSSHDialog() {
        setContentView(R.layout.sshterminal);
        home = findViewById(R.id.imageViewhelp2);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
            }
        });
        Button connect = findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sshdialog();
            }
        });


    }

    //Method hides keyboard after input
    public void hideKeyBoard() {
        View view1 = this.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

    //Method creates connect dialogue and calls ssh Client class
    public void sshdialog() {
//initializing Alert Dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message).setTitle(R.string.connect);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null, false);
        String name = "";
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //initializing variables
                EditText etun = view.findViewById(R.id.username);
                EditText etpw = view.findViewById(R.id.pw);
                EditText ethost = view.findViewById(R.id.host);
                String uname = etun.getText() + "";
                String pw = etpw.getText() + "";
                String host = ethost.getText() + "";
                String base = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
                String regex = base + "\\." + base + "\\."
                        + base + "\\." + base;
                if (host.matches(regex)) {
                    try {
                        //set values and check if dest ip is reachable
                        sc.setValues(uname, host, pw);
                        IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
                        request.setHost(host);
                        IcmpPingResponse help = IcmpPingUtil.executePingRequest(request);
                        String returnm = help + "";
                        if (returnm.contains("successFlag: true")) {
                            alert("Connection successful", "Success!");

                        } else {
                            alert("Connection unscuccessful. Host unreachable!", "Host unreachable");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    hideKeyBoard();

                } else {
                    alert("Data was input the wrong format!", "Wrong input format!");
                    hideKeyBoard();
                }
            }
        });

        Button send = findViewById(R.id.send);
        final TextView input = findViewById(R.id.input);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                final Handler handler = new Handler();
                sc.onSSHconnect(input.getText() + "");
                handler.post(new Runnable() {
                    public void run() {
                        //call command execution and connection in SSH Client class
                        try {
                            String output = sc.getOutput();
                           // Log.w("output",output);
                            TextView terminal = findViewById(R.id.terminal);
                            terminal.setText(output);
                            handler.postDelayed(this, 100);
                        } catch (Exception e) {

                        }
                    }
                });

            }
        });


        AlertDialog ad = builder.create();
        ad.show();

    }

    //sets portscan layout page and calls portscan method
    public void callportscan() {
        setContentView(R.layout.portscan);
        home = findViewById(R.id.imageV2);
        home.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
            }
        });
        Button ps_button = findViewById(R.id.ps_button);
        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);
                String s = item.getText() + "";
                item.setTextColor(Color.parseColor("#ffffff"));
                item.setTypeface(t);
                item.setBackgroundColor(Color.parseColor("#D0000000"));
                item.setTextSize(18);
                return item;
            }
        };
        ps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                EditText et1 = findViewById(R.id.ps);
                ListView lv = findViewById(R.id.ps_results);
                lv.setAdapter(adapter);
                final String ip = et1.getText() + "";
                final PortReader pr = new PortReader();
                InputStream is = getResources().openRawResource(R.raw.test);
                try {
                    pr.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //check ip
                String base = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
                String regex = base + "\\." + base + "\\."
                        + base + "\\." + base;
                if (ip.matches(regex)) {
                    final PortScan ps = new PortScan(pr.getPorts());
                    final Handler handler = new Handler();
                    listItems.clear();
                    handler.post(new Thread() {
                        int h = 0;
                        public void run() {
//perform portscan and list open ports
                            if (h == 0) {
                                for (int i = 1; i < 400; i++) {
                                    OpenPort op;
                                    op = ps.startPortscan(ip, i, 80);
                                    if (op.getPort() == 0) {
                                        listItems.add("Address unreachable");
                                        adapter.notifyDataSetChanged();
                                        break;
                                    } else {
                                        if (op.getIsOpen()) {
                                            if (pr.getPorts().containsKey(op.getPort() + "")) {
                                                listItems.add(op.getPort() + "  " + pr.getPorts().get(op.getPort() + ""));
                                                adapter.notifyDataSetChanged();
                                                handler.postDelayed(this, 25);
                                            }
                                        }
                                    }
                                }
                                Log.w("done", "done");
                              //  mProgressDialog.dismiss();
                                h++;
                            }
                        }
                    });


                } else {
                    alert("Data was input the wrong format!", "Wrong input format!");
                    hideKeyBoard();
                }

            }
        });

    }

    //deletes text from editviews after double click
    public void moveText(View v) {
        EditText et = (EditText) v;
        et.setText("");
    }

    //Creating alerts if input is malformatted or host is unreachable
    public void alert(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        final LayoutInflater inflater = LayoutInflater.from(this);
        String name = "";
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.create().show();
    }

}





