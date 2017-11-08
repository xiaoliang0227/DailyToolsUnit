package com.zyl.tools.dailytoolsunit.exception;

import android.util.Log;

import com.zyl.tools.dailytoolsunit.util.ToolsUnitLogUtil;

/**
 * 
 * @author JasonZhao
 *
 */
public class NoneNetworkErrorException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final String TAG = "NoneNetworkErrorException";

  @Override
  public void printStackTrace() {
    ToolsUnitLogUtil.debug(TAG, "none network error exception");
    super.printStackTrace();
  }

  @Override
  public String getMessage() {
    ToolsUnitLogUtil.debug(TAG, "none network error exception");
    return super.getMessage();
  }

  

}
