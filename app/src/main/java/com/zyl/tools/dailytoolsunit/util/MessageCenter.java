package com.zyl.tools.dailytoolsunit.util;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JasonZhao on 15/11/9.
 */
public class MessageCenter extends Handler {

  private static final String TAG = MessageCenter.class.getName();

  private static MessageCenter mesCen;

  private Map<String, Object> msgObj = new HashMap<String, Object>();

  public static void initInstance() {
    if (null == mesCen) {
      mesCen = new MessageCenter();
    }
  }

  public static MessageCenter getInstance() {
    return mesCen;
  }

  /**
   * Subclasses must implement this to receive messages.
   *
   * @param msg
   */
  @Override
  public void handleMessage(Message msg) {
    super.handleMessage(msg);
  }
}
