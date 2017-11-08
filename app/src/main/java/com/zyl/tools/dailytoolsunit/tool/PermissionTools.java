package com.zyl.tools.dailytoolsunit.tool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.zyl.tools.dailytoolsunit.interf.IPermissionTools;

/**
 * Created by zhaoyongliang on 2017/6/15.
 */

public class PermissionTools implements IPermissionTools {

    private static PermissionTools instance;

    public static PermissionTools getInstance() {
        if (null == instance) {
            instance = new PermissionTools();
        }
        return instance;
    }

    /**
     * 权限是否被授权
     *
     * @param context
     * @param permission
     * @return
     */
    @Override
    public boolean isPermissionGranted(Context context, String permission) {
        boolean flag = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    /**
     * 检查是否需要申请权限
     *
     * @param context
     * @param permission
     * @return
     */
    @Override
    public boolean shouldRequestPermission(Context context, String permission) {
        if (!isOverMarshmallow()) return false;
        return !isPermissionGranted(context, permission);
    }

    /**
     * sdk版本是否高于Marshmallow
     *
     * @return
     */
    @Override
    public boolean isOverMarshmallow() {
        boolean flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        return flag;
    }

    /**
     * Activity 中申请权限
     *
     * @param activity
     * @param requestCode
     * @param permissions
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(Activity activity, int requestCode, String... permissions) {
        String[] permissionArr = permissions.clone();
        ActivityCompat.requestPermissions(activity, permissionArr, requestCode);
    }

    /**
     * Fragment 中申请权限
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(Fragment fragment, int requestCode, String... permissions) {
        String[] permissionArr = permissions.clone();
        fragment.requestPermissions(permissionArr, requestCode);
    }

    /**
     * 是否需要显示弹出申请权限的原因
     *
     * @param activity
     * @param permission
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return activity.shouldShowRequestPermissionRationale(permission);
    }

    /**
     * 是否需要显示弹出申请权限的原因
     *
     * @param fragment
     * @param permission
     * @return
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean shouldShowRequestPermissionRationale(Fragment fragment, String permission) {
        return fragment.shouldShowRequestPermissionRationale(permission);
    }
}
