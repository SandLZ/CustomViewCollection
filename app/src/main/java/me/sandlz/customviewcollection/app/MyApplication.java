package me.sandlz.customviewcollection.app;

import android.app.Application;

import org.xutils.x;

/**
 * Created by liuzhu on 2016/11/29.
 * Description :
 * Usage :
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
