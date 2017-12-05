package com.zyl.tools.dailytoolsunit.interf;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public interface IOtherTools {

    /**
     * 将异常转换为可读字符串
     *
     * @param ex
     * @return
     */
    String wholeExceptionInfo(Throwable ex);

    /**
     * 注销广播
     *
     * @param context
     * @param receiver
     */
    void unregistReceiver(Context context, BroadcastReceiver receiver);

    /**
     * 将对象转换为Map集合
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    Map<String, Object> convertObjectToMap(Object obj) throws
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException;

    /**
     * 结束所有异步任务
     *
     * @param taskList
     */
    void stopTasks(List<AsyncTask> taskList);

    /**
     * 结束异步任务
     *
     * @param task
     */
    void stopTask(AsyncTask task);

    /**
     * 获取设备名称
     *
     * @return
     */
    String getDeviceName();

    int getColumns(int size, int per);

    int getColumns(long size, int per);

    /**
     * 停止计时器
     *
     * @param timer
     */
    void stopTimer(Timer timer);

    PackageInfo getVersionInfo(Context context) throws PackageManager.NameNotFoundException;

    String getPhoneIME(Context context);

    /**
     * 判断屏幕是否亮
     *
     * @param context
     * @return
     */
    boolean isScreenOn(Context context);

    /**
     * 页面是否可见
     *
     * @param act
     * @return
     */
    boolean isActivityVisible(Activity act);

    /**
     * 页面是否状态正常
     *
     * @param act
     * @return
     */
    boolean isActivityAlive(Activity act);

    /**
     * 获取顶部页面的名称
     *
     * @param context
     * @return
     */
    String getCurrentTopActivityName(Context context);

    /**
     * 时间段
     *
     * @param hour
     * @return
     */
    String getTimeRangeTip(int hour);

    /**
     * 获取apk文件的版本号
     *
     * @param activity
     * @param apkFile
     * @return
     */
    int getVersionCodeFromAPK(Activity activity, String apkFile);

    /**
     * 安装apk文件
     *
     * @param activity
     * @param apkFile
     */
    void installApk(Activity activity, String apkFile);

    /**
     * 停止线程
     *
     * @param thread
     */
    void stopThread(Thread thread);

    /**
     * 获取app的证书信息
     *
     * @param context
     * @return
     */
    String getCertificateSHA1Fingerprint(Context context);

    /**
     * 本地化存储Long数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     */
    void setLongCache(Context context, String root, String key, long value);

    /**
     * 获取本地化存储的Long数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @return
     */
    long getLongCache(Context context, String root, String key, long defaultValue);

    /**
     * 本地化存储String数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     */
    void setStringCache(Context context, String root, String key, String value);

    /**
     * 本地化存储String数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     * @param aesKey
     */
    void setStringCache(Context context, String root, String key, String value, String aesKey);

    /**
     * 获取本地化存储的String数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @return
     */
    String getStringCache(Context context, String root, String key, String defaultValue);

    /**
     * 获取本地化存储的String数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @param aesKey
     * @return
     */
    String getStringCache(Context context, String root, String key, String defaultValue, String aesKey);

    /**
     * 本地化存储Boolean数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     */
    void setBooleanCache(Context context, String root, String key, boolean value);

    /**
     * 获取本地化存储的Boolean数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @return
     */
    Boolean getBooleanCache(Context context, String root, String key, boolean defaultValue);

    /**
     * 清空本地化存储
     *
     * @param context
     * @param root
     * @param pattern
     */
    void clearAllLocalCookie(Context context, String root, String pattern);

    /**
     * 是否存在某种类型的本地存储
     *
     * @param context
     * @param root
     * @param pattern
     * @return
     */
    boolean isContainesKeyWithPattern(Context context, String root, String pattern);

    /**
     * 加密数据
     *
     * @param context
     * @param root
     * @param aesKey
     */
    void encryptAllStringValues(Context context, String root, String aesKey);

    /**
     * 关闭IO
     *
     * @param closeable
     */
    void closeIO(Closeable closeable);

}
