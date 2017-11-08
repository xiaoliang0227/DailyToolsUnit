package com.zyl.tools.dailytoolsunit.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.zyl.tools.dailytoolsunit.interf.IOtherTools;
import com.zyl.tools.dailytoolsunit.util.SecurityUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public class OtherTools implements IOtherTools {

    private static OtherTools instance;

    public static OtherTools getInstance() {
        if (null == instance) {
            instance = new OtherTools();
        }
        return instance;
    }

    /**
     * 将异常转换为可读字符串
     *
     * @param ex
     * @return
     */
    @Override
    public String wholeExceptionInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }

    /**
     * 注销广播
     *
     * @param context
     * @param receiver
     */
    @Override
    public void unregistReceiver(Context context, BroadcastReceiver receiver) {
        try {
            if (null != receiver) {
                context.unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将对象转换为Map集合
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Override
    public Map<String, Object> convertObjectToMap(Object obj) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class<?> pomclass = obj.getClass();
        pomclass = obj.getClass();
        Method[] methods = obj.getClass().getMethods();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Method m : methods) {
            if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
                Object value = (Object) m.invoke(obj);
                String key = m.getName().substring(3);
                if (key.length() > 1) {
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                } else {
                    key = key.toLowerCase();
                }
                map.put(key, (Object) value);
            }
        }
        return map;
    }

    /**
     * 结束所有异步任务
     *
     * @param taskList
     */
    @Override
    public void stopTasks(List<AsyncTask> taskList) {
        if (null == taskList || taskList.isEmpty()) return;
        for (AsyncTask task : taskList) {
            stopTask(task);
        }
    }

    /**
     * 结束异步任务
     *
     * @param task
     */
    @Override
    public void stopTask(AsyncTask task) {
        if (null != task && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
        }
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    @Override
    public String getDeviceName() {
        return new Build().MODEL;
    }

    @Override
    public int getColumns(int size, int per) {
        return size / per + (size % per > 0 ? 1 : 0);
    }

    @Override
    public int getColumns(long size, int per) {
        return (int) (size / per + (size % per > 0 ? 1 : 0));
    }

    /**
     * 停止计时器
     *
     * @param timer
     */
    @Override
    public void stopTimer(Timer timer) {
        if (null != timer) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public PackageInfo getVersionInfo(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(),
                0);
    }

    @Override
    public String getPhoneIME(Context context) {
        String ime = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (TextUtils.isEmpty(ime)) {
            ime = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return ime;
    }

    /**
     * 判断屏幕是否亮
     *
     * @param context
     * @return
     */
    @Override
    public boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 页面是否可见
     *
     * @param act
     * @return
     */
    @Override
    public boolean isActivityVisible(Activity act) {
        if (act != null) {
            Class klass = act.getClass();
            while (klass != null) {
                try {
                    Field field = klass.getDeclaredField("mResumed");
                    field.setAccessible(true);
                    Object obj = field.get(act);
                    return (Boolean) obj;
                } catch (NoSuchFieldException exception1) {

                } catch (IllegalAccessException exception2) {

                }
                klass = klass.getSuperclass();
            }
        }
        return false;
    }

    /**
     * 页面是否状态正常
     *
     * @param act
     * @return
     */
    @Override
    public boolean isActivityAlive(Activity act) {
        boolean flag = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                flag = isActivityVisible(act) && !(act.isFinishing() || act.isDestroyed());
            } else {
                flag = isActivityVisible(act) && !act.isFinishing();
            }
            if (null != act.getWindow()) {
                flag &= act.getWindow().isActive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取顶部页面的名称
     *
     * @param context
     * @return
     */
    @Override
    public String getCurrentTopActivityName(Context context) {
        String name = "";
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            name = cn.getClassName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 时间段
     *
     * @param hour
     * @return
     */
    @Override
    public String getTimeRangeTip(int hour) {
        String tip = "";
        if (hour >= 0 && hour <= 5) {
            tip = "凌晨";
        } else if (hour > 5 && hour <= 12) {
            tip = "上午";
        } else if (hour > 12 && hour <= 18) {
            tip = "下午";
        } else if (hour > 18 && hour <= 23) {
            tip = "晚上";
        }
        return tip;
    }

    /**
     * 获取apk文件的版本号
     *
     * @param activity
     * @param apkFile
     * @return
     */
    @Override
    public int getVersionCodeFromAPK(Activity activity, String apkFile) {
        int version = 0;
        File file = new File(apkFile);
        if (file.exists()) {
            PackageManager pm = activity.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkFile, 0);
            version = info.versionCode;
        }
        return version;
    }

    /**
     * 安装apk文件
     *
     * @param activity
     * @param apkFile
     */
    @Override
    public void installApk(Activity activity, String apkFile) {
        Uri uri = Uri.fromFile(new File(apkFile));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    /**
     * 停止线程
     *
     * @param thread
     */
    @Override
    public void stopThread(Thread thread) {
        try {
            if (null != thread && !thread.isInterrupted()) {
                thread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取app的证书信息
     *
     * @param context
     * @return
     */
    @Override
    public String getCertificateSHA1Fingerprint(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = context.getPackageName();
        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //X509 证书，X.509 是一种非常通用的证书格式
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            //加密算法的类，这里的参数可以使 MD4,MD5 等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");
            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());

            //字节到十六进制的格式转换
            BigInteger bi = new BigInteger(1, publicKey);
            hexString = bi.toString(16);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    /**
     * 本地化存储Long数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     */
    @Override
    public void setLongCache(Context context, String root, String key, long value) {
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地化存储的Long数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @return
     */
    @Override
    public long getLongCache(Context context, String root, String key, long defaultValue) {
        long value = defaultValue;
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            value = sp.getLong(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 本地化存储String数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     */
    @Override
    public void setStringCache(Context context, String root, String key, String value) {
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地化存储String数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     * @param aesKey
     */
    @Override
    public void setStringCache(Context context, String root, String key, String value, String aesKey) {
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, TextUtils.isEmpty(value) ? value : SecurityUtil.getInstance().encrypt(value, aesKey));
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地化存储的String数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @return
     */
    @Override
    public String getStringCache(Context context, String root, String key, String defaultValue) {
        String value = defaultValue;
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            value = sp.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

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
    @Override
    public String getStringCache(Context context, String root, String key, String defaultValue, String aesKey) {
        String value = defaultValue;
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            String tmpValue = sp.getString(key, defaultValue);
            if (!TextUtils.isEmpty(tmpValue)) {
                value = SecurityUtil.getInstance().decrypt(tmpValue, aesKey);
                if (TextUtils.isEmpty(value)) {
                    value = tmpValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 本地化存储Boolean数据
     *
     * @param context
     * @param root
     * @param key
     * @param value
     */
    @Override
    public void setBooleanCache(Context context, String root, String key, boolean value) {
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地化存储的Boolean数据
     *
     * @param context
     * @param root
     * @param key
     * @param defaultValue
     * @return
     */
    @Override
    public Boolean getBooleanCache(Context context, String root, String key, boolean defaultValue) {
        Boolean value = defaultValue;
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            value = sp.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 清空本地化存储
     *
     * @param context
     * @param root
     * @param pattern
     */
    @Override
    public void clearAllLocalCookie(Context context, String root, String pattern) {
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            Map<String, Object> values = (Map<String, Object>) sp.getAll();
            Set<String> keySet = values.keySet();
            for (String key : keySet) {
                if (key.indexOf(pattern) >= 0) {
                    setStringCache(context, root, key, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否存在某种类型的本地存储
     *
     * @param context
     * @param root
     * @param pattern
     * @return
     */
    @Override
    public boolean isContainesKeyWithPattern(Context context, String root, String pattern) {
        boolean flag = false;
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            Map<String, Object> values = (Map<String, Object>) sp.getAll();
            Set<String> keySet = values.keySet();
            for (String key : keySet) {
                if (key.indexOf(pattern) >= 0) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 加密数据
     *
     * @param context
     * @param root
     * @param aesKey
     */
    @Override
    public void encryptAllStringValues(Context context, String root, String aesKey) {
        try {
            SharedPreferences sp = context.getSharedPreferences(root, 0);
            Map<String, Object> caches = (Map<String, Object>) sp.getAll();
            Set<Map.Entry<String, Object>> entrySet = caches.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> item = iterator.next();
                if (item.getValue() != null && item.getValue() instanceof String) {
                    String decodeVal = SecurityUtil.getInstance().decrypt(item.getValue().toString(), aesKey);
                    if (TextUtils.isEmpty(decodeVal)) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(item.getKey(), SecurityUtil.getInstance().encrypt(item.getValue().toString(), aesKey));
                        editor.commit();
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
