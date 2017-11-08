package com.zyl.tools.dailytoolsunit.interf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public interface IViewTools {

    /**
     * 屏幕当中显示提示，经过Toast.LENGTH_LONG后自动消失
     *
     * @param context
     * @param msg
     */
    void showLongToastAtCenter(Context context, String msg);


    /**
     * 屏幕当中显示提示，经过Toast.LENGTH_SHORT后自动消失
     *
     * @param context
     * @param msg
     */
    void showShortToastAtCenter(Context context, String msg);

    /**
     * 获取屏幕密度
     *
     * @param act
     * @return
     */
    float getScreenDensity(Activity act);

    /**
     * 获取屏幕长度
     *
     * @param act
     * @return
     */
    int getScreenWidth(Activity act);

    /**
     * 获取屏幕高度
     *
     * @param act
     * @return
     */
    int getScreenHeight(Activity act);

    /**
     * 获取ListView的所有条目的高度
     *
     * @param listView
     * @param items
     * @return
     */
    int getItemHeightofListView(ListView listView, int items);

    /**
     * 单位转换：dp convert to px
     *
     * @param context
     * @param dimens
     * @return
     */
    float dp2px(Context context, int dimens);

    /**
     * 设置操作栏 文字颜色
     *
     * @param context
     * @param toolbar
     * @param color
     */
    void setActionBarOverflowButtonColor(Context context, Toolbar toolbar, int color);

    /**
     * 显示系统无线设置页面
     *
     * @param context
     */
    void showWifiSettig(Context context);

    /**
     * 切换fragment
     *
     * @param activity
     * @param fragment
     * @param containerId
     * @param inAnimation
     * @param outAnimation
     * @param animateEnable
     */
    void translateFragment(final AppCompatActivity activity, final Fragment fragment, int containerId,
                           int inAnimation, int outAnimation, final boolean animateEnable);

    /**
     * 切换fragment
     *
     * @param activity
     * @param fragment
     * @param containerId
     * @param inAnimation
     * @param outAnimation
     * @param animateEnable
     */
    void translateFragment(final FragmentActivity activity, final Fragment fragment, int containerId,
                           int inAnimation, int outAnimation, final boolean animateEnable);

    /**
     * 获取字符串资源
     *
     * @param context
     * @param resId
     * @return
     */
    String getStringResource(Context context, int resId);

    void setDialogAttribute(Context context, Dialog dialog, int gravity, int maxHeight, int y);

    void setDialogAttribute(Context context, Dialog dialog, int gravity, int maxHeight, int y, float dim);

    void setDialogAttribute(Context context, Dialog dialog, int gravity, int maxHeight);

    void setTopDialogAttribute(Context context, Dialog dialog);

    void setBottomDialogAttribute(Context context, Dialog dialog);

    void setCenterDialogAttribute(Context context, Dialog dialog, float scale);

    /**
     * 获取状态栏高度
     *
     * @param act
     * @return
     */
    int getStatusBarHeight(Activity act);

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    int getStatusBarHeight(Context context);

    /**
     * 隐藏键盘
     *
     * @param act
     */
    void hideKeyboard(Activity act);

    /**
     * 获取View的高度
     *
     * @param view
     * @return
     */
    int getViewHeight(View view);

    /**
     * 获取View的宽度
     *
     * @param view
     * @return
     */
    int getViewWidth(View view);

    /**
     * 获取导航栏的高度
     *
     * @param context
     * @return
     */
    int getNavigationBarHeight(Context context);

    /**
     * 获取ActionBar的高度
     *
     * @param context
     * @return
     */
    int getActionBarHeight(Context context);

    /**
     * 页面跳转
     *
     * @param from
     * @param to
     */
    void globalPageJump(Context from, Class<?> to);

    /**
     * 设置ImageView的背景
     *
     * @param context
     * @param view
     * @param bitmap
     */
    void setImageBackground(Context context, View view, Bitmap bitmap);

    /**
     * 设置ImageView的内容
     *
     * @param context
     * @param view
     * @param bitmap
     */
    void setImageSrc(Context context, View view, Bitmap bitmap);

    /**
     * 设置错误提醒
     *
     * @param edit
     * @param str
     */
    void setError(EditText edit, String str);

    Drawable getDrawableByResource(Context context, int resId);

    void setDrawableBackground(Context context, View view, int resId);

    void setImageDrawable(Context context, ImageView view, int resId);

    /**
     * 是否可以在Fragment中显示窗体
     *
     * @param fragment
     * @param dialog
     * @return
     */
    boolean shouldShowDialogInFragment(Fragment fragment, Dialog dialog);

    /**
     * 显示应用程序详情页面
     *
     * @param context
     */
    void showAppDetailSetting(Context context);

    /**
     * 设置状态栏颜色
     *
     * @param act
     * @param color
     */
    void initStatusbarColor(Activity act, int color);

    /**
     * 将异常转换为可读字符串
     *
     * @param ex
     * @return
     */
    String wholeExceptionInfo(Throwable ex);

    /**
     * 获取颜色
     *
     * @param context
     * @param resId
     * @return
     */
    int getColorResource(Context context, int resId);

    /**
     * 将光标移动到末端
     *
     * @param et
     */
    void movePointerToEnd(EditText et);

    /**
     * 隐藏窗体
     *
     * @param dialog
     */

    void dismissDialog(final Dialog dialog);
}
