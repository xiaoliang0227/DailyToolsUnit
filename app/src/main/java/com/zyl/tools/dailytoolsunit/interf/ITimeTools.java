package com.zyl.tools.dailytoolsunit.interf;

import java.util.Date;

/**
 * Created by zhaoyongliang on 2017/6/13.
 */

public interface ITimeTools {

    /**
     * 格式化时间
     *
     * @param time
     * @param pattern
     * @return
     */
    String format(long time, String pattern);

    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    String format(Date date, String pattern);

}
