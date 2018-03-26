package com.ibicnCloud.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;

public class FieldUtil extends FieldUtils{
    /**
     * 此方法是将从数据库中得到的值VALUE，根据字段FIELDNAME，赋值到object中
     * @param object 需要操作的对象
     * @param fieldName 字段的名称
     * @param value 要放入字段的值
     * @throws
     * @throws Exception
     */
    public static void setFiled(Object object, String fieldName, Object value) throws Exception {
        if(object != null && fieldName != null && value != null) {
            if (fieldName.indexOf(".") >= 0) { //如果返回字段中包含引用，先将引用实例化
                String referenceName = fieldName.substring(0,fieldName.indexOf("."));
                fieldName = fieldName.substring(fieldName.indexOf(".") + 1);

                //首先从object中取得引用在实例，判断是否为空，如果空，则实例化新在应引用；否则直接使用引用，最后赋值
                String getMethodName = "get" + referenceName.substring(0, 1).toUpperCase() + referenceName.substring(1);
                java.lang.reflect.Method getMethod = object.getClass().getMethod(getMethodName, null);
                Object reference = getMethod.invoke(object, null);
                if (reference == null) {
                    java.lang.reflect.Field fd = object.getClass().getDeclaredField(referenceName);
                    reference = fd.getType().newInstance(); //实例化引用对象
                }
                String SetMethodName = "set" + referenceName.substring(0, 1).toUpperCase() + referenceName.substring(1);
                java.lang.reflect.Method setMethod = object.getClass().getMethod(SetMethodName, reference.getClass());
                setMethod.invoke(object, reference);
                //设置引用的字段
                setFiled(reference,fieldName,value);
            } else { //如果返回字段中包含没有引用，则直接赋值
                String SetMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                if(value != null){
                    String className = value.getClass().getName();
                    Method setMethod = null;
                    if (className.equals("java.sql.Timestamp")) {
                        setMethod = object.getClass().getMethod(SetMethodName, java.util.Date.class);
                    } else if (className.equals("java.lang.Integer")) {
                        setMethod = object.getClass().getMethod(SetMethodName, java.lang.Integer.TYPE);
                    }else {
                        setMethod = object.getClass().getMethod(SetMethodName, value.getClass());
                    }
                    setMethod.invoke(object, value);
                }
            }
        }

    }

    public static void lazyInit(Object object, String fieldName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (fieldName.indexOf(".") >= 0) { //如果返回字段中包含引用，先将引用实例化
            String referenceName = fieldName.substring(0,fieldName.indexOf("."));
            fieldName = fieldName.substring(fieldName.indexOf(".") + 1);

            //首先从object中取得引用在实例，判断是否为空，如果空，则实例化新在应引用；否则直接使用引用，最后赋值
            String getMethodName = "get" + referenceName.substring(0, 1).toUpperCase() + referenceName.substring(1);
            java.lang.reflect.Method getMethod = object.getClass().getMethod(getMethodName, null);
            String typeName = getMethod.getReturnType().getName();
            Object reference = getMethod.invoke(object, null);
            if (reference != null) {
                if (typeName.equals("java.util.Set")) {
                    java.util.Set set = (java.util.Set) reference;
                    set.size();
                } else {
                    reference.toString();
                }
                lazyInit(reference, fieldName);
            }
        } else { //如果返回字段中包含没有引用，则直接赋值
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getMethod = object.getClass().getMethod(getMethodName);
            String typeName = getMethod.getReturnType().getName();
            Object value = getMethod.invoke(object, null);
            if (value != null) {
                if (typeName.equals("java.util.Set")) {
                    java.util.Set set = (java.util.Set) value;
                    set.size();
                } else {
                    value.toString();
                }
            }
        }
    }
}
