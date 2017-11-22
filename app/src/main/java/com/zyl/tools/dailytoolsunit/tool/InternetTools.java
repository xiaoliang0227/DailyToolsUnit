package com.zyl.tools.dailytoolsunit.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zyl.tools.dailytoolsunit.enumeration.HttpPostType;
import com.zyl.tools.dailytoolsunit.enumeration.InternetConnectType;
import com.zyl.tools.dailytoolsunit.enumeration.WifiPowerLevel;
import com.zyl.tools.dailytoolsunit.exception.NoneNetworkErrorException;
import com.zyl.tools.dailytoolsunit.interf.IInternetTools;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitCommonConsts;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitLogUtil;
import com.zyl.tools.dailytoolsunit.util.WifiAdmin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public class InternetTools implements IInternetTools {

    private static final String TAG = "InternetTools";

    private static InternetTools instance;

    public static InternetTools getInstance() {
        if (null == instance) {
            instance = new InternetTools();
        }
        return instance;
    }

    /**
     * 检查是否连接无线网络
     *
     * @param context
     * @return
     */
    @Override
    public boolean isWifiConnected(Context context) {
        if (null == context)
            return false;
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 无线网络是否开启
     *
     * @param context
     * @return
     */
    @Override
    public boolean isWifiEnable(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.isWifiEnabled();
    }

    /**
     * 检查移动网络或者无线网络是否启用
     *
     * @param context
     * @return
     */
    @Override
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return null != info && info.isConnectedOrConnecting();
    }

    /**
     * 检查是否可以访问外网
     *
     * @param context
     * @param uri
     * @param timeout
     * @return
     */
    @Override
    public boolean isAccessInternet(Context context, String uri, int timeout) {
        boolean flag = false;
        if (!isOnline(context)) {
            return flag;
        }
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * get 请求操作
     *
     * @param context
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     * @throws NoneNetworkErrorException
     */
    @Override
    public Map<String, Object> doGet(Context context, String url, Map<String, Object> headers,
                                     Map<String, Object> params) throws IOException, NoneNetworkErrorException {
        return doGet(context, url, headers, params, 0, 0);
    }

    /**
     * get 请求操作
     *
     * @param context
     * @param url
     * @param headers
     * @param params
     * @param connectionTimeout
     * @param soTimeout
     * @return
     * @throws IOException
     * @throws NoneNetworkErrorException
     */
    @Override
    public Map<String, Object> doGet(Context context, String url, Map<String, Object> headers,
                                     Map<String, Object> params, int connectionTimeout, int soTimeout)
            throws IOException, NoneNetworkErrorException {
        // check network
        if (!isOnline(context)) {
            throw new NoneNetworkErrorException();
        }
        // format url
        String tmpUrl = url;
        String paramsStr = formatParams(params, null);
        if (!TextUtils.isEmpty(paramsStr)) {
            tmpUrl += ("?" + paramsStr);
        }

        ToolsUnitLogUtil.debug(TAG, "request get url:" + tmpUrl);

        // get HttpURLConnecton Object
        URL uri = new URL(tmpUrl);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setRequestMethod(ToolsUnitCommonConsts.REQUEST_METHOD_GET);

        // set headers
        setHeaders(connection, headers);
        // set timeout
        setTimeout(connection, connectionTimeout, soTimeout);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("responseHeader", connection.getHeaderFields());
        int responseCode = connection.getResponseCode();
        responseData.put("responseCode", responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseData.put("responseMessage", connection.getResponseMessage());
            responseData.put("responseBody", readInputStreamToString(connection));
        }
        ToolsUnitLogUtil.debug(TAG, "request get response:" + responseData);
        return responseData;
    }

    /**
     * post 请求操作
     *
     * @param context
     * @param url
     * @param type
     * @param headers
     * @param params
     * @return
     * @throws IOException
     * @throws NoneNetworkErrorException
     */
    @Override
    public Map<String, Object> doPost(Context context, String url, HttpPostType type, Map<String,
            Object> headers, Map<String, Object> params) throws IOException, NoneNetworkErrorException {
        return doPost(context, url, type, headers, params, 0, 0);
    }

    /**
     * post 请求操作
     *
     * @param context
     * @param url
     * @param type
     * @param headers
     * @param params
     * @param connectionTimeout
     * @param soTimeout
     * @return
     * @throws IOException
     * @throws NoneNetworkErrorException
     */
    @Override
    public Map<String, Object> doPost(Context context, String url, HttpPostType type, Map<String,
            Object> headers, Map<String, Object> params, int connectionTimeout, int soTimeout)
            throws IOException, NoneNetworkErrorException {
        // check network
        if (!isOnline(context)) {
            throw new NoneNetworkErrorException();
        }

        ToolsUnitLogUtil.debug(TAG, "request post url:" + url);
        // get HttpURLConnecton Object
        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setRequestMethod(ToolsUnitCommonConsts.REQUEST_METHOD_POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        // set headers
        setHeaders(connection, headers);
        // set timeout
        setTimeout(connection, connectionTimeout, soTimeout);

        // set data
        String dataStr = formatParams(params, type);
        ToolsUnitLogUtil.debug(TAG, "post dataStr:" + dataStr);
        if (!TextUtils.isEmpty(dataStr)) {
            byte[] data = dataStr.getBytes();
            // 设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "application/json");
            // 设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));

            // 获得输出流,向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("responseHeader", connection.getHeaderFields());
        int responseCode = connection.getResponseCode();
        responseData.put("responseCode", responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseData.put("responseMessage", connection.getResponseMessage());
            responseData.put("responseBody", readInputStreamToString(connection));
        }
        ToolsUnitLogUtil.debug(TAG, "request post response:" + responseData);
        return responseData;
    }

    private String formatParams(Map<String, Object> params, HttpPostType type) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (HttpPostType.JSON.equals(type)) {
            Gson gson = new Gson();
            sb.append(gson.toJson(params));
        } else {
            Object tmp = null;
            if (null != params && !params.isEmpty()) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    tmp = entry.getValue();
                    if (null != tmp) {
                        sb.append(entry.getKey()).append("=").append(URLEncoder.encode(tmp.toString(), ToolsUnitCommonConsts.DEFAULT_CHARSET)).append("&");
                    }
                }

                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 1);
                }
            }
        }

        return sb.toString();
    }

    private void setHeaders(HttpURLConnection connection, Map<String, Object> headers) {
        if (null == connection || null == headers || headers.isEmpty()) return;
        Object tmp = null;
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            tmp = entry.getValue();
            if (null != tmp) {
                connection.setRequestProperty(entry.getKey(), tmp.toString());
            }
        }
    }

    private void setTimeout(HttpURLConnection connection, int connectionTimeout, int soTimeout) {
        if (null == connection) return;
        connection.setConnectTimeout(connectionTimeout == 0 ? ToolsUnitCommonConsts.DEFAULT_TIMEOUT_CONNECTION : connectionTimeout);
        connection.setReadTimeout(soTimeout == 0 ? ToolsUnitCommonConsts.DEFAULT_TIMEOUT_SOCKET : soTimeout);
    }

    /**
     * @param connection object; note: before calling this function,
     *                   ensure that the connection is already be open, and any writes to
     *                   the connection's output stream should have already been completed.
     * @return String containing the body of the connection response or
     * null if the input stream could not be read correctly
     */
    private static String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuilder sb = new StringBuilder();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            ToolsUnitLogUtil.debug(TAG, "Error reading InputStream");
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    ToolsUnitLogUtil.debug(TAG, "Error closing InputStream");
                }
            }
        }

        return result;
    }

    /**
     * 获取当前上网类型
     *
     * @param context
     * @return
     */
    @Override
    public InternetConnectType checkNetworkInfo(Context context) {
        InternetConnectType type = InternetConnectType.TYPE_NONE;
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                type = InternetConnectType.TYPE_WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                type = InternetConnectType.TYPE_MOBILE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 获取当前连接的无线网络信息
     *
     * @param context
     * @return
     */
    @Override
    public Map<String, String> getCurrentSystemWifiInfo(Context context) {
        Map<String, String> wifiInfo = new HashMap<>();
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wm.getConnectionInfo();
        wifiInfo.put("bssid", info.getBSSID());
        wifiInfo.put("networkId", String.valueOf(info.getNetworkId()));
        wifiInfo.put("currentMac", formatCurrentWifiMacAddress());
        wifiInfo.put("ssid", formatCurrentWifiSsid(info.getSSID(), "\""));
        DhcpInfo dhcpInfo = wm.getDhcpInfo();
        wifiInfo.put("gateway", formatCurrentWifiIpAddress(dhcpInfo.gateway));
        wifiInfo.put("networkId", String.valueOf(info.getNetworkId()));
        return wifiInfo;
    }

    private String formatCurrentWifiMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    int tmp = b & 0xFF;
                    String macPart = Integer.toHexString(tmp);
                    if (macPart.length() < 2) {
                        macPart = "0".concat(macPart);
                    }
                    res1.append(macPart).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    private String formatCurrentWifiSsid(String str, String... format) {
        if (format.length == 0)
            return str;
        if (TextUtils.isEmpty(str))
            return str;
        str = str.trim();
        for (String item : format) {
            str = str.replaceAll(item, "");
        }
        return str;
    }

    private String formatCurrentWifiIpAddress(int ip) {
        String ipString = "";
        if (ip != 0) {
            ipString = ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "."
                    + (ip >> 16 & 0xff) + "." + (ip >> 24 & 0xff));
        }
        return ipString;
    }

    /**
     * 获取当前连接的无线网络信息
     *
     * @param context
     * @return
     */
    @Override
    public ScanResult getCurrentWifiResult(Context context) {
        Map<String, String> currentWifi = getCurrentSystemWifiInfo(context);
        ScanResult sr = null;
        WifiAdmin wifiAdmin = new WifiAdmin(context);
        wifiAdmin.startScan();
        List<ScanResult> srList = wifiAdmin.getWifiList();
        if (null != srList && !srList.isEmpty()) {
            for (ScanResult item : srList) {
                if (item.SSID.equals(currentWifi.get("ssid"))) {
                    sr = item;
                    break;
                }
            }
        }
        return sr;
    }

    /**
     * 获取当前网关信息
     *
     * @param context
     * @return
     */
    @Override
    public String getCurrentGateway(Context context) {
        Map<String, String> wifiInfo = getCurrentSystemWifiInfo(context);
        if (wifiInfo == null || TextUtils.isEmpty(wifiInfo.get("gateway")))
            return ToolsUnitCommonConsts.DEFAULT_GATEWAY;
        return wifiInfo.get("gateway");
    }

    /**
     * 获取手机的上网方式
     *
     * @param context
     * @return
     */
    @Override
    public String checkPhoneInternetType(Context context) {
        String type = "WIFI";
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (mobile.equals(NetworkInfo.State.CONNECTED)) {
                type = "MOBILE";
            } else if (wifi.equals(NetworkInfo.State.CONNECTED)) {
                type = "WIFI";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 执行ping操作
     *
     * @param host
     * @return
     */
    @Override
    public String startPing(final String host) {
        String pingResult = null;
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String cmd = String.format("ping -c 3 -i 0.2 -W 200 %s", host);
                return executeCmd(cmd, false);
            }
        };
        ExecutorService exeSer = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = exeSer.submit(task);
            pingResult = future.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exeSer.shutdown();
        }
        return pingResult;
    }

    private String executeCmd(String cmd, boolean sudo) {
        String result = "";
        Process process = null;
        try {
            if (!sudo)
                process = Runtime.getRuntime().exec(cmd);
            else {
                process = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            }
            int status = process.waitFor();
            streamToString(process.getInputStream());
            result = status == 0 ? "success" : "failure";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String streamToString(InputStream inputStream) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        String res = "";
        while ((s = stdInput.readLine()) != null) {
            res += s + "\n";
        }
        return res;
    }

    /**
     * 获取当前连接无线网络的BSSID
     *
     * @param context
     * @return
     */
    @Override
    public String getCurrentBssid(Context context) {
        String bssid = null;
        try {
            bssid = getCurrentSystemWifiInfo(context).get("bssid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bssid;
    }

    /**
     * 无线信号强度
     *
     * @param sr
     * @return
     */
    @Override
    public WifiPowerLevel getWifiPowerLevel(ScanResult sr) {
        WifiPowerLevel level = WifiPowerLevel.BAD;
        if (sr.level < -85) {
            level = WifiPowerLevel.BAD;
        } else if (sr.level >= -85 && sr.level < -60) {
            level = WifiPowerLevel.NORMAL;
        } else if (sr.level >= -60 && sr.level < -40) {
            level = WifiPowerLevel.GOOD;
        } else if (sr.level >= -40) {
            level = WifiPowerLevel.VERY_GOOD;
        }
        return level;
    }

    /**
     * 将json数据转为实体对象
     *
     * @param response
     * @param key
     * @param dest
     * @return
     */
    @Override
    public Object convertJsonToObject(Map<String, Object> response, String key, Class<?> dest) {
        return new Gson().fromJson(response.get(key).toString(), dest);
    }
}
