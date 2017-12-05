package com.zyl.tools.dailytoolsunit.tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zyl.tools.dailytoolsunit.R;
import com.zyl.tools.dailytoolsunit.interf.IViewTools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public class ViewTools implements IViewTools {

    public static ViewTools getInstance() {
        return SingleHolder.instance;
    }

    private ViewTools() {

    }

    private static class SingleHolder {
        private static final ViewTools instance = new ViewTools();
    }

    /**
     * 屏幕当中显示提示，经过Toast.LENGTH_LONG后自动消失
     *
     * @param context
     * @param msg
     */
    @Override
    public void showLongToastAtCenter(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 屏幕当中显示提示，经过Toast.LENGTH_SHORT后自动消失
     *
     * @param context
     * @param msg
     */
    @Override
    public void showShortToastAtCenter(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 获取屏幕密度
     *
     * @param act
     * @return
     */
    @Override
    public float getScreenDensity(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 获取屏幕长度
     *
     * @param act
     * @return
     */
    @Override
    public int getScreenWidth(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param act
     * @return
     */
    @Override
    public int getScreenHeight(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取ListView的所有条目的高度
     *
     * @param listView
     * @param items
     * @return
     */
    @Override
    public int getItemHeightofListView(ListView listView, int items) {
        ListAdapter mAdapter = listView.getAdapter();
        int listviewElementsHeight = 0;
        for (int i = 0; i < items; i++) {
            View childView = mAdapter.getView(i, null, listView);
            childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listviewElementsHeight += childView.getMeasuredHeight();
        }
        return listviewElementsHeight;
    }

    /**
     * 单位转换：dp convert to px
     *
     * @param context
     * @param dimens
     * @return
     */
    @Override
    public float dp2px(Context context, int dimens) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimens, context.getResources().getDisplayMetrics());
    }

    /**
     * 设置操作栏 文字颜色
     *
     * @param context
     * @param toolbar
     * @param color
     */
    @Override
    public void setActionBarOverflowButtonColor(Context context, Toolbar toolbar, int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if (null != drawable) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

    /**
     * 显示系统无线设置页面
     *
     * @param context
     */
    @Override
    public void showWifiSettig(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

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
    @Override
    public void translateFragment(AppCompatActivity activity, Fragment fragment, int containerId,
                                  int inAnimation, int outAnimation, boolean animateEnable) {
        if (animateEnable) {
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(inAnimation,
                    outAnimation).replace(containerId, fragment).commit();
        } else {
            activity.getSupportFragmentManager().beginTransaction().replace(containerId, fragment)
                    .commit();
        }
    }

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
    @Override
    public void translateFragment(FragmentActivity activity, Fragment fragment, int containerId,
                                  int inAnimation, int outAnimation, boolean animateEnable) {
        if (animateEnable) {
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(inAnimation,
                    outAnimation).replace(containerId, fragment).commit();
        } else {
            activity.getSupportFragmentManager().beginTransaction().replace(containerId, fragment)
                    .commit();
        }
    }

    /**
     * 获取字符串资源
     *
     * @param context
     * @param resId
     * @return
     */
    @Override
    public String getStringResource(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public void setDialogAttribute(Context context, Dialog dialog, int gravity, int maxHeight, int y) {
        int width = getScreenWidth((Activity) context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.gravity = gravity;
        if (0 != y) {
            lp.y = y;
        }
        if (0 != maxHeight) {
            lp.height = maxHeight;
        }
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void setDialogAttribute(Context context, Dialog dialog, int gravity, int maxHeight, int y, float dim) {
        int width = getScreenWidth((Activity) context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.gravity = gravity;
        if (0 != y) {
            lp.y = y;
        }
        if (0 != maxHeight) {
            lp.height = maxHeight;
        }
        lp.dimAmount = dim;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void setDialogAttribute(Context context, Dialog dialog, int gravity, int maxHeight) {
        setDialogAttribute(context, dialog, gravity, maxHeight, 0);
    }

    @Override
    public void setTopDialogAttribute(Context context, Dialog dialog) {
        int width = getScreenWidth((Activity) context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.gravity = Gravity.TOP;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void setBottomDialogAttribute(Context context, Dialog dialog) {
        int width = getScreenWidth((Activity) context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void setCenterDialogAttribute(Context context, Dialog dialog, float scale) {
        int width = getScreenWidth((Activity) context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (width * scale);
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 获取状态栏高度
     *
     * @param act
     * @return
     */
    @Override
    public int getStatusBarHeight(Activity act) {
        int result = 0;
        int resourceId = act.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = act.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏键盘
     *
     * @param act
     */
    @Override
    public void hideKeyboard(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    @Override
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取View的高度
     *
     * @param view
     * @return
     */
    @Override
    public int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取View的宽度
     *
     * @param view
     * @return
     */
    @Override
    public int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    /**
     * 获取导航栏的高度
     *
     * @param context
     * @return
     */
    @Override
    public int getNavigationBarHeight(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && !hasMenuKey) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取ActionBar的高度
     *
     * @param context
     * @return
     */
    @Override
    public int getActionBarHeight(Context context) {
        int actionBarHeight;
        int[] abSzAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            abSzAttr = new int[]{android.R.attr.actionBarSize};
        } else {
            abSzAttr = new int[]{R.attr.actionBarSize};
        }
        TypedArray a = context.obtainStyledAttributes(abSzAttr);
        actionBarHeight = a.getDimensionPixelSize(0, -1);
        return actionBarHeight;
    }

    /**
     * 页面跳转
     *
     * @param from
     * @param to
     */
    @Override
    public void globalPageJump(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        from.startActivity(intent);
    }

    /**
     * 设置ImageView的背景
     *
     * @param context
     * @param view
     * @param bitmap
     */
    @Override
    public void setImageBackground(Context context, View view, Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
        } else {
            view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
        }
    }

    /**
     * 设置ImageView的内容
     *
     * @param context
     * @param view
     * @param bitmap
     */
    @Override
    public void setImageSrc(Context context, View view, Bitmap bitmap) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
        }
    }

    /**
     * 设置错误提醒
     *
     * @param edit
     * @param str
     */
    @Override
    public void setError(EditText edit, String str) {
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        edit.setError(str);
    }

    @Override
    public Drawable getDrawableByResource(Context context, int resId) {
        if (resId == 0) return null;
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(resId, context.getTheme());
        } else {
            drawable = context.getResources().getDrawable(resId);
        }
        return drawable;
    }

    @Override
    public void setDrawableBackground(Context context, View view, int resId) {
        Drawable drawable = getDrawableByResource(context, resId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setImageDrawable(Context context, ImageView view, int resId) {
        view.setImageDrawable(getDrawableByResource(context, resId));
    }

    /**
     * 是否可以在Fragment中显示窗体
     *
     * @param fragment
     * @param dialog
     * @return
     */
    @Override
    public boolean shouldShowDialogInFragment(Fragment fragment, Dialog dialog) {
        boolean flag = fragment.isAdded() && fragment.isVisible();
        if (null != dialog) {
            flag &= !dialog.isShowing();
        }
        return flag;
    }

    /**
     * 显示应用程序详情页面
     *
     * @param context
     */
    @Override
    public void showAppDetailSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    /**
     * 设置状态栏颜色
     *
     * @param act
     * @param color
     */
    @Override
    public void initStatusbarColor(Activity act, int color) {
        int tmpColor = 0x00000000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tmpColor = act.getResources().getColor(color, act.getTheme());
        } else {
            tmpColor = act.getResources().getColor(color);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            act.getWindow().setStatusBarColor(tmpColor);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(act);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(tmpColor);
        }
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
     * 获取颜色
     *
     * @param context
     * @param resId
     * @return
     */
    @Override
    public int getColorResource(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resId);
        } else {
            return context.getResources().getColor(resId);
        }
    }

    /**
     * 将光标移动到末端
     *
     * @param et
     */
    @Override
    public void movePointerToEnd(EditText et) {
        if (!TextUtils.isEmpty(et.getText())) {
            et.setSelection(et.getText().length());
        }
    }

    /**
     * 隐藏窗体
     *
     * @param dialog
     */
    @Override
    public void dismissDialog(Dialog dialog) {
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
