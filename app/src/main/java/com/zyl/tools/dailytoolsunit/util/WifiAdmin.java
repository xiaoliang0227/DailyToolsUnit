package com.zyl.tools.dailytoolsunit.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Updated by JasonZhao on 16/29/8.
 */

public class WifiAdmin {

    private static final String TAG = "WifiAdmin";

    private WifiManager wifiManager;

    private WifiInfo wifiInfo;

    private List<ScanResult> wifiList;

    private List<WifiConfiguration> configurationList;

    private OnWifiAdminListener listener;

    private int networkId;

    private boolean alreadToggleWifi = false;

    public WifiAdmin(Context context) {
        alreadToggleWifi = false;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    public WifiAdmin(Context context, OnWifiAdminListener listener) {
        this(context);
        this.listener = listener;
    }

    public WifiInfo getCurrentWifiInfo() {
        return wifiInfo;
    }

    private Method getEnableMethod() {
        try {
            Class<?> wifiClass = Class.forName(wifiManager.getClass().getName());
            Field field = wifiClass.getDeclaredField("mService");
            field.setAccessible(true);

            Object iWifiManager = field.get(wifiManager);
            Method[] methods = iWifiManager.getClass().getDeclaredMethods();
            if (null != methods && methods.length != 0) {
                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase("enableNetwork")) {
                        return method;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean connectWifi(int wifiId) {
        if (Build.VERSION.SDK_INT >= 21) {
            Method method = getEnableMethod();
            if (null != method) {
                return newConnect(method, wifiId);
            } else {
                return connect(wifiId);
            }
        }

        return connect(wifiId);
    }

    private boolean newConnect(Method method, int wifiId) {
        boolean flag = true;
        Class<?> wifiClass = null;
        try {
            wifiClass = Class.forName(wifiManager.getClass().getName());
            Field field = wifiClass.getDeclaredField("mService");
            field.setAccessible(true);

            Object iWifiManager = field.get(wifiManager);
            method.setAccessible(true);
            flag = (Boolean) method.invoke(iWifiManager, new Object[]{wifiId, true});
            baseWifiOperation();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return flag;
    }

    private boolean connect(int wifiId) {
        boolean flag = true;
        flag &= wifiManager.enableNetwork(wifiId, true);
        baseWifiOperation();
        return flag;
    }

    private void baseWifiOperation() {
        toggleWifi();
        wifiManager.saveConfiguration();
        wifiManager.disconnect();
        wifiManager.reconnect();
    }

    private void toggleWifi() {
        if (alreadToggleWifi) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alreadToggleWifi = true;
            wifiManager.setWifiEnabled(false);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wifiManager.setWifiEnabled(true);
        }
    }

    public void startScan() {
        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
        configurationList = wifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> getWifiList() {
        return wifiList;
    }

    public boolean isWifiEnable() {
        return wifiManager.isWifiEnabled();
    }

    public int addNewNetwork(WifiConfiguration config) {
        return wifiManager.addNetwork(config);
    }

    public List<WifiConfiguration> getConfigurationList() {
        return configurationList;
    }

    public void connectToWifi(final String ssid, final String password, final String capacities) {
        if (TextUtils.isEmpty(ssid) && TextUtils.isEmpty(password))
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doConnect(ssid, password, capacities);
            }
        }).start();
    }

    private void doConnect(String ssid, String password, String capacities) {
        WifiConfiguration config = null;
        String security = getSecurity(capacities);
        WifiConfiguration tmpConfig = checkIsWifiConfigExsits(ssid, security);
        config = formatConfig(ssid, password, tmpConfig, security);
        resetWifiPriority(config);
        if (tmpConfig == null) {
            networkId = addNewNetwork(config);
            Log.d(TAG, "add new network:" + ssid);
        } else {
            if (TextUtils.isEmpty(password)) {
                networkId = tmpConfig.networkId;
            } else {
                networkId = wifiManager.updateNetwork(config);
                Log.d(TAG, "update network:" + ssid);
                if (networkId == -1) {
                    networkId = addNewNetwork(config);
                }
            }
        }
        boolean flag = networkId != -1;
        if (null != listener) {
            listener.addNewNetworkdResult(ssid, flag);
        }
        if (flag) {
            if (null != listener) {
                listener.enableNetworkResult(ssid, connectWifi(networkId), networkId);
            } else {
                connectWifi(networkId);
            }
        } else {
            networkId = null != tmpConfig ? tmpConfig.networkId : networkId;
            connectWifi(networkId);
        }
    }

    private void resetWifiPriority(WifiConfiguration config) {
        wifiManager.startScan();
        if (null != configurationList && !configurationList.isEmpty()) {
            List<WifiConfiguration> tmpList = configurationList;
            Collections.sort(tmpList, new WifiSortByPriorityHight2Small());
            config.priority = tmpList.get(0).priority + 1;
        }
    }

    private WifiConfiguration checkIsWifiConfigExsits(String ssid, String security) {
        startScan();
        if (null != configurationList && !configurationList.isEmpty()) {
            disableOthers(ssid);
            for (WifiConfiguration item : configurationList) {
                if (item.SSID.replaceAll("\"", "").equals(ssid)) {
                    if (security.equals(getWifiConfigSecurity(item))) {
                        Log.d(TAG, "wifi configuration security type:" + security);
                        return item;
                    }
                }
            }
        }
        return null;
    }

    private String getWifiConfigSecurity(WifiConfiguration config) {
        // 0:NONE,1:WPA_PSK,2:WPA_EAP,3:IEEE8021X
        String type = "NONE";
        String security = config.allowedKeyManagement.toString();
        Log.e(TAG, "allowedKeyManagement:" + security);
        if (security.contains("1")) {
            type = "PSK";
        } else if (security.contains("2")) {
            type = "EAP";
        } else if (security.contains("3")) {
            type = "IEEE8021X";
        } else {
            if (config.wepKeys[0] != null) {
                type = "WEP";
            }
        }
        return type;
    }

    private void disableOthers(String ssid) {
        // 如果6.0 删除所有的相同ssid的配置
        if (Build.VERSION_CODES.M == Build.VERSION.SDK_INT) {
            for (WifiConfiguration item : configurationList) {
                if (item.SSID.replaceAll("\"", "").equals(ssid) || item.SSID.equals(ssid)) {
                    wifiManager.removeNetwork(item.networkId);
                }
            }
        } else {
            for (WifiConfiguration item : configurationList) {
                if (!item.SSID.replaceAll("\"", "").equals(ssid)) {
                    wifiManager.disableNetwork(item.networkId);
                }
            }
        }

    }

    private WifiConfiguration formatConfig(String ssid, String password, WifiConfiguration tmpConfig, String security) {
        WifiConfiguration config = null;
        if (null != tmpConfig) {
            config = tmpConfig;
        } else {
            config = new WifiConfiguration();
        }
        config.SSID = "\"".concat(ssid).concat("\"");
        config.status = WifiConfiguration.Status.ENABLED;
        config.priority = tmpConfig == null ? 40 : tmpConfig.priority + 1;
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        if ("WEP".equalsIgnoreCase(security)) {
            Log.d(TAG, "current wifi is:WEP");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.wepKeys[0] = "\"".concat(password).concat("\"");
            config.wepTxKeyIndex = 0;
        } else if ("PSK".equalsIgnoreCase(security)) {
            Log.d(TAG, "current wifi is:PSK");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.preSharedKey = "\"".concat(password).concat("\"");
        } else {
            Log.d(TAG, "current wifi is:OPEN");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        }
        return config;
    }

    public String getSecurity(String capabilities) {
        final String[] securityModes = {"WEP", "PSK", "EAP"};

        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (capabilities.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return "NONE";
    }

    public void disconnect() {
        wifiManager.disconnect();
    }

    // interface
    public interface OnWifiAdminListener {
        void addNewNetworkdResult(String ssid, boolean result);

        void enableNetworkResult(String ssid, boolean result, int networkId);
    }
}
