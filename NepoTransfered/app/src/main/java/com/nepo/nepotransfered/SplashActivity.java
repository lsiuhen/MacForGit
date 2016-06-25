package com.nepo.nepotransfered;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.nepo.nepotransfered.model.TransferModel;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by lsiuhen on 2016/5/26.
 */
public class SplashActivity extends BaseActivity {


    private ImageView img;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                openActivity(SplashActivity.this,MainActivity.class);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(0,1000);
//        img= (ImageView) findViewById(R.id.img);

        //测试SVN是否配置成功

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {



    }

    @Override
    public void initLisenter() {

    }


}
