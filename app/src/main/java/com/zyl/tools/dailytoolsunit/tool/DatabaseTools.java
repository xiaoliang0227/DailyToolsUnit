package com.zyl.tools.dailytoolsunit.tool;

import android.content.ContentValues;

import com.zyl.tools.dailytoolsunit.interf.IDatabaseTools;

import org.litepal.crud.DataSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhaoyongliang on 2017/6/15.
 */

public class DatabaseTools implements IDatabaseTools {

    private static DatabaseTools instance;

    public static DatabaseTools getInstance() {
        if (null == instance) {
            instance = new DatabaseTools();
        }
        return instance;
    }

    /**
     * 新增或者更新数据
     *
     * @param obj
     * @return
     */
    @Override
    public Object addOrUpdateData(Object obj) throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        if (null == obj) {
            return null;
        }
        long id = 0;
        // setUpdateTime、getId
        Class cls = obj.getClass();
        Class parentCls = cls.getSuperclass();

        Method getId = cls.getMethod("getId");
        Method save = parentCls.getMethod("save");
        Method update = parentCls.getMethod("update", long.class);
        try {
            Method setUpdateTime = cls.getMethod("setUpdateTime");
            setUpdateTime.invoke(obj, System.currentTimeMillis());
        } catch (Exception e) {
        }

        id = Integer.valueOf(getId.invoke(obj).toString());
        if (id == 0) {
            save.invoke(obj);
        } else {
            update.invoke(obj, id);
        }
        return obj;
    }

    /**
     * 重置数据
     *
     * @param cls
     * @param fields
     * @param values
     * @param conditions
     */
    @Override
    public void resetAll(Class cls, String[] fields, Object[] values, String... conditions) {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            Object value = values[i];
            if (value instanceof String) {
                cv.put(field, value.toString());
            } else if (value instanceof Integer) {
                cv.put(field, Integer.valueOf(value.toString()));
            } else if (value instanceof Long) {
                cv.put(field, Long.valueOf(value.toString()));
            } else if (value instanceof Short) {
                cv.put(field, Short.valueOf(value.toString()));
            } else if (value instanceof Boolean) {
                cv.put(field, Boolean.valueOf(value.toString()));
            } else if (value instanceof Byte) {
                cv.put(field, Byte.valueOf(value.toString()));
            } else if (value instanceof Float) {
                cv.put(field, Float.valueOf(value.toString()));
            } else if (value instanceof Double) {
                cv.put(field, Double.valueOf(value.toString()));
            } else if (value instanceof byte[]) {
                cv.put(field, value.toString().getBytes());
            }
        }

        DataSupport.updateAll(cls, cv, conditions);
    }

    /**
     * 通过条件查询数据
     *
     * @param cls
     * @param conditions
     * @return
     */
    @Override
    public Object findDataByCondition(Class cls, String... conditions) {
        List list = DataSupport.where(conditions).find(cls);
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 通过id查询数据
     *
     * @param cls
     * @param id
     * @return
     */
    @Override
    public Object findDataById(Class cls, long id) {
        return DataSupport.find(cls, id);
    }

    /**
     * 按条件删除数据
     *
     * @param cls
     * @param conditions
     */
    @Override
    public void deleteData(Class cls, String... conditions) {
        DataSupport.deleteAll(cls, conditions);
    }

    /**
     * 按id删除数据
     *
     * @param cls
     * @param id
     */
    @Override
    public void deleteData(Class cls, long id) {
        DataSupport.delete(cls, id);
    }

    /**
     * 获取所有数据
     *
     * @param cls
     * @return
     */
    @Override
    public List<Object> findAllData(Class cls) {
        return DataSupport.findAll(cls);
    }

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @return
     */
    @Override
    public List<Object> findAllData(Class cls, String order) {
        return DataSupport.order(order).find(cls);
    }

    /**
     * 获取所有数据
     *
     * @param cls
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Object> findAllData(Class cls, int offset, int limit) {
        return DataSupport.offset(offset).limit(limit).find(cls);
    }

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Object> findAllData(Class cls, String order, int offset, int limit) {
        return DataSupport.order(order).offset(offset).limit(limit).find(cls);
    }


    /**
     * 获取所有数据
     *
     * @param cls
     * @param conditions
     * @return
     */
    @Override
    public List<Object> findAllData(Class cls, String... conditions) {
        return DataSupport.where(conditions).find(cls);
    }

    /**
     * 获取所有数据
     *
     * @param cls
     * @param order
     * @param conditions
     * @return
     */
    @Override
    public List<Object> findAllDataWithSort(Class cls, String order, String... conditions) {
        return DataSupport.order(order).where(conditions).find(cls);
    }

    /**
     * 获取所有数据
     *
     * @param cls
     * @param offset
     * @param limit
     * @param conditions
     * @return
     */
    @Override
    public List<Object> findAllData(Class cls, int offset, int limit, String... conditions) {
        return DataSupport.where(conditions).offset(offset).limit(limit).find(cls);
    }

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
    @Override
    public List<Object> findAllData(Class cls, String order, int offset, int limit, String... conditions) {
        return DataSupport.where(conditions).order(order).offset(offset).limit(limit).find(cls);
    }

    /**
     * 获取数据数量
     *
     * @param cls
     * @return
     */
    @Override
    public int findCount(Class cls) {
        return DataSupport.count(cls);
    }

    /**
     * 获取数据数量
     *
     * @param cls
     * @param conditions
     * @return
     */
    @Override
    public int findCount(Class cls, String... conditions) {
        return DataSupport.where(conditions).count(cls);
    }
}
