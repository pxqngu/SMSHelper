package com.pxqngu.smshelper;

import android.app.Application;

import com.pxqngu.smshelper.adapter.PhoneListAdapter;
import com.pxqngu.smshelper.bean.YunPianConfig;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMSHelper extends Application {
    public static List<String> phoneList = new ArrayList<>();
    public static PhoneListAdapter phoneListAdapter;
    public static YunPianConfig yunPianConfig;
    public static Map<String,String> config;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
