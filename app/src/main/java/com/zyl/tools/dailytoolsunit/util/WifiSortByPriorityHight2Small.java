package com.zyl.tools.dailytoolsunit.util;

import android.net.wifi.WifiConfiguration;

import java.util.Comparator;

/**
 * @param <T>
 * @author JasonZhao
 */
public class WifiSortByPriorityHight2Small implements Comparator<Object> {

  @Override
  public int compare(Object lhs, Object rhs) {
    int flag = 0;
    if (lhs instanceof WifiConfiguration) {
      WifiConfiguration info1 = (WifiConfiguration) lhs;
      WifiConfiguration info2 = (WifiConfiguration) rhs;
      if (info1.priority < info2.priority) {
        return 1;
      } else if (info1.priority > info2.priority) {
        return -1;
      } else {
        return 0;
      }
    }
    return flag;
  }

}
