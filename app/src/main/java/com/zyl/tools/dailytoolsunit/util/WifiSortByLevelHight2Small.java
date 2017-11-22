package com.zyl.tools.dailytoolsunit.util;

import android.net.wifi.ScanResult;

import java.util.Comparator;

/**
 * @param <T>
 * @author JasonZhao
 */
public class WifiSortByLevelHight2Small implements Comparator<Object> {

  @Override
  public int compare(Object lhs, Object rhs) {
    int flag = 0;
    if (lhs instanceof ScanResult) {
      ScanResult info1 = (ScanResult) lhs;
      ScanResult info2 = (ScanResult) rhs;
      if (info1.level < info2.level) {
        return 1;
      } else if (info1.level > info2.level) {
        return -1;
      } else {
        return 0;
      }
    }
    return flag;
  }

}
