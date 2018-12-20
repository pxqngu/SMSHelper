package com.pxqngu.smshelper.bean;

import cn.bmob.v3.BmobObject;

public class YunPianConfig extends BmobObject{
    private String parameter_name;
    private String value;
    private String notes;

    public String getParameter_name() {
        return parameter_name;
    }

    public void setParameter_name(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
