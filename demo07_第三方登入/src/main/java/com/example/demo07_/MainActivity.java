package com.example.demo07_;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/*实现qq的第三方登入
1.搭建环境(添加jar包,添加res图片,布局,Values资源,添加权限,配置activity信息,秀给key值,签名配置,application初始化)
2.写布局
3.登入的代码
* */
public class MainActivity extends AppCompatActivity {
    private ImageView iv_login;
    private TextView tv_result;
    //定义装平台的容器
    private ArrayList<SnsPlatform> platforms = new ArrayList<>();
    private SHARE_MEDIA[] list = {SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
    private UMShareAPI umShareAPI;
    private ImageView iv_share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_login = (ImageView) findViewById(R.id.iv_login);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        tv_result = (TextView) findViewById(R.id.tv_result);
//三方平台,添加到遍历的集合中
        initPlatforms();
        //获取UM的对象
        umShareAPI = UMShareAPI.get(MainActivity.this);
//获取是否授权
        final boolean isauth = UMShareAPI.get(this).isAuthorize(this, platforms.get(0).mPlatform);
        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isauth) {
                    Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    umShareAPI.deleteOauth(MainActivity.this, platforms.get(0).mPlatform, authListener);
                } else {
                    umShareAPI.doOauthVerify(MainActivity.this, platforms.get(0).mPlatform, authListener);
                }
                umShareAPI.getPlatformInfo(MainActivity.this, platforms.get(0).mPlatform, authListener);
            }
        });
        //三方获取用户资料时是否每次都要进行授权
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
    }

    private void initPlatforms() {
        //集合清空
        platforms.clear();
        //循环,把数组的数据添加到集合中
        for (SHARE_MEDIA e : list) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())) {
                platforms.add(e.toSnsPlatform());
            }
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调，可以用来处理等待框，或相关的文字提示
        }

        @Override//授权成功时回调
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //获取用户授权后的信息
            Set<String> strings = data.keySet();
            String temp = "";
            for (String key : strings) {
                temp = temp + key + ":" + data.get(key) + "\n";
            }
            tv_result.setText(temp);

            String profile_image_url = data.get("profile_image_url");
            //这步骤是用imageloder加载图片的
            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)  .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainActivity.this)
                    .defaultDisplayImageOptions(displayImageOptions)
                    .build();
            ImageLoader.getInstance().init(config);
            ImageLoader.getInstance().displayImage(profile_image_url, iv_share);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
