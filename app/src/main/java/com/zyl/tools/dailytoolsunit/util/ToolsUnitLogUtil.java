package com.zyl.tools.dailytoolsunit.util;

import android.util.Log;

/**
 * @author JasonZhao
 */
public class ToolsUnitLogUtil {

    public static void debug(String tag, String msg) {
        if (!ToolsUnitCommonConsts.SHOW_DEGUG) return;
        Log.d("ToolsUnitLogUtil:" + tag, msg);
    }
}
