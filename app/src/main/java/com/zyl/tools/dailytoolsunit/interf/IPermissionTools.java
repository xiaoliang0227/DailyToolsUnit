package com.zyl.tools.dailytoolsunit.interf;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by zhaoyongliang on 2017/6/15.
 */

public interface IPermissionTools {

    /**
     * 权限是否被授权
     *
     * @param context
     * @param permission
     * @return
     */
    boolean isPermissionGranted(Context context, String permission);

    /**
     * 检查是否需要申请权限
     *
     * @param context
     * @param permission
     * @return
     */
    boolean shouldRequestPermission(Context context, String permission);

    /**
     * sdk版本是否高于Marshmallow
     *
     * @return
     */
    boolean isOverMarshmallow();

    /**
     * Activity 中申请权限
     *
     * @param activity
     * @param requestCode
     * @param permissions
     */
    void requestPermission(Activity activity, int requestCode, String... permissions);

    /**
     * Fragment 中申请权限
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    void requestPermission(Fragment fragment, int requestCode, String... permissions);

    /**
     * 是否需要显示弹出申请权限的原因
     *
     * @param activity
     * @param permission
     * @return
     */
    boolean shouldShowRequestPermissionRationale(Activity activity, String permission);

    /**
     * 是否需要显示弹出申请权限的原因
     *
     * @param fragment
     * @param permission
     * @return
     */
    boolean shouldShowRequestPermissionRationale(Fragment fragment, String permission);
}
