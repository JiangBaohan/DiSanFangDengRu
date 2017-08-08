package com.example.demo07_;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * data:2017/8/5
 * author:汉堡(Administrator)
 * function:
 */

public class MyApp extends Application {
   // 在application中初始化sdk，这个初始化最好放在application的程序入口中，防止意外发生：
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);


    }
    {//在application文件中配置三方平台的appkey,配置代码生成的,这个appKey 必须和清单中
        //<data android:scheme="tencent100424468"/>值一样,tencent不可缺少
        PlatformConfig.setQQZone("1106036236", "mjFCi0oxXZKZEWJs");
    }

}
