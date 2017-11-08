package com.zyl.tools.dailytoolsunit.interf;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by zhaoyongliang on 2017/6/15.
 */

public interface IDatabaseTools {


    /**
     * 新增或者更新数据
     *
     * @param obj
     * @return
     */
    Object addOrUpdateData(Object obj) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    /**
     * 通过条件查询数据
     *
     * @param cls
     * @param conditions
     * @return
     */
    Object findDataByCondition(Class cls, String... conditions);

    /**
     * 通过id查询数据
     *
     * @param cls
     * @param id
     * @return
     */
    Object findDataById(Class cls, long id);

    /**
     * 按条件删除数据
     *
     * @param cls
     * @param conditions
     */
    void deleteData(Class cls, String... conditions);

    /**
     * 按id删除数据
     *
     * @param cls
     * @param id
     */
    void deleteData(Class cls, long id);

    /**
     * 重置数据
     *
     * @param cls
     * @param fields
     * @param values
     * @param conditions
     */
    void resetAll(Class cls, String[] fields, Object[] values, String... conditions);

    /**
     * 获取所有数据
     *
     * @param cls
     * @return
     */
    List<Object> findAllData(Class cls);

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @return
     */
    List<Object> findAllData(Class cls, String order);


    /**
     * 获取所有数据
     *
     * @param cls
     * @param offset
     * @param limit
     * @return
     */
    List<Object> findAllData(Class cls, int offset, int limit);

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @param offset
     * @param limit
     * @return
     */
    List<Object> findAllData(Class cls, String order, int offset, int limit);

    /**
     * 获取所有数据
     *
     * @param cls
     * @param conditions
     * @return
     */
    List<Object> findAllData(Class cls, String... conditions);

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @param conditions
     * @return
     */
    List<Object> findAllDataWithSort(Class cls, String order, String... conditions);

    /**
     * 获取所有数据
     *
     * @param cls
     * @param offset
     * @param limit
     * @param conditions
     * @return
     */
    List<Object> findAllData(Class cls, int offset, int limit, String... conditions);

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @param offset
     * @param limit
     * @param conditions
     * @return
     */
    List<Object> findAllData(Class cls, String order, int offset, int limit, String... conditions);

    /**
     * 获取数据数量
     *
     * @param cls
     * @return
     */
    int findCount(Class cls);

    /**
     * 获取数据数量
     *
     * @param cls
     * @param conditions
     * @return
     */
    int findCount(Class cls, String... conditions);
}
