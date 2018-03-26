package com.ibicnCloud.util;

/**
 * 临时用类，用于存放值 和 类型
 *
 * @author zhangmeifu
 *
 */
public class ListValues {
    private String values;

    private String type;

    private String[] arrValue;

    public ListValues(String values, String type) {
        this.values = values;
        this.type = type;
    }

    public ListValues(int values, String type) {
        this.values = String.valueOf(values);
        this.type = type;
    }

    public ListValues(String[] arrValue, String type) {
        this.arrValue = arrValue;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String[] getArrValue() {
        return arrValue;
    }

    public void setArrValue(String[] arrValue) {
        this.arrValue = arrValue;
    }

}
