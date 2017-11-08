package com.zyl.tools.dailytoolsunit.interf;

import android.content.Context;
import android.net.wifi.ScanResult;

import com.zyl.tools.dailytoolsunit.enumeration.HttpPostType;
import com.zyl.tools.dailytoolsunit.enumeration.InternetConnectType;
import com.zyl.tools.dailytoolsunit.enumeration.WifiPowerLevel;
import com.zyl.tools.dailytoolsunit.exception.NoneNetworkErrorException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public interface IInternetTools {

    /**
     * 检查是否连接无线网络
     *
     * @param context
     * @return
     */
    boolean isWifiConnected(Context context);

    /**
     * 无线网络是否开启
     *
     * @param context
     * @return
     */
    boolean isWifiEnable(Context context);

    /**
     * 检查移动网络或者无线网络是否启用
     *
     * @param context
     * @return
     */
    boolean isOnline(Context context);

    /**
     * 检查是否可以访问外网
     *
     * @param context
     * @param uri
     * @param timeout
     * @return
     */
    boolean isAccessInternet(Context context, String uri, int timeout);

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
    Map<String, Object> doGet(Context context, String url, Map<String, Object> headers, Map<String, Object> params)
            throws IOException, NoneNetworkErrorException;

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
    Map<String, Object> doGet(Context context, String url, Map<String, Object> headers, Map<String, Object> params,
                              int connectionTimeout, int soTimeout) throws IOException, NoneNetworkErrorException;

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
    Map<String, Object> doPost(Context context, String url, HttpPostType type, Map<String, Object> headers,
                               Map<String, Object> params) throws IOException, NoneNetworkErrorException;

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
    Map<String, Object> doPost(Context context, String url, HttpPostType type, Map<String, Object> headers,
                               Map<String, Object> params, int connectionTimeout, int soTimeout) throws IOException, NoneNetworkErrorException;

    /**
     * 获取当前上网类型
     *
     * @param context
     * @return
     */
    InternetConnectType checkNetworkInfo(Context context);

    /**
     * 获取当前连接的无线网络信息
     *
     * @param context
     * @return
     */
    Map<String, String> getCurrentSystemWifiInfo(Context context);

    /**
     * 获取当前连接的无线网络信息
     *
     * @param context
     * @return
     */
    ScanResult getCurrentWifiResult(Context context);

    /**
     * 获取当前网关信息
     *
     * @param context
     * @return
     */
    String getCurrentGateway(Context context);

    /**
     * 获取手机的上网方式
     *
     * @param context
     * @return
     */
    String checkPhoneInternetType(Context context);

    /**
     * 执行ping操作
     *
     * @param host
     * @return
     */
    String startPing(final String host);

    /**
     * 获取当前连接无线网络的BSSID
     *
     * @param context
     * @return
     */
    String getCurrentBssid(Context context);

    /**
     * 无线信号强度
     *
     * @param sr
     * @return
     */
    WifiPowerLevel getWifiPowerLevel(ScanResult sr);

    /**
     * 将json数据转为实体对象
     *
     * @param response
     * @param key
     * @param dest
     * @return
     */
    Object convertJsonToObject(Map<String, Object> response, String key, Class<?> dest);

}
