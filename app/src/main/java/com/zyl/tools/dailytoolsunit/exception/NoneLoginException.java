package com.zyl.tools.dailytoolsunit.exception;

import com.zyl.tools.dailytoolsunit.util.ToolsUnitLogUtil;

/**
 * @author JasonZhao
 */
public class NoneLoginException extends Exception {

    private static final long serialVersionUID = 1L;

    private static final String TAG = "NoneLoginException";

    @Override
    public void printStackTrace() {
        ToolsUnitLogUtil.debug(TAG, "none login exception");
        super.printStackTrace();
    }

    @Override
    public String getMessage() {
        ToolsUnitLogUtil.debug(TAG, "none login exception");
        return super.getMessage();
    }


}
