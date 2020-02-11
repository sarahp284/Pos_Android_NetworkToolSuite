package networkscan.clients;

import android.net.wifi.WifiManager;

public class PingScanner {



    public void scanNetwork()
    {
        WifiManager mWifiManager = (WifiManager)
        WifiInfo  mWifiInfo = mWifiManager.getConnectionInfo();
        String subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);
    }

}
