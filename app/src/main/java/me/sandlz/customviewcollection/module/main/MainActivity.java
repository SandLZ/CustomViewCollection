package me.sandlz.customviewcollection.module.main;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.xutils.view.annotation.ContentView;

import me.sandlz.customviewcollection.R;
import me.sandlz.customviewcollection.base.BaseActivity;
import rx.functions.Action1;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            Log.d("zliu","同意网络、存储权限");
                        }else {
                            Log.d("zliu","未同意网络或存储权限");
                        }
                    }
                });

    }
}
