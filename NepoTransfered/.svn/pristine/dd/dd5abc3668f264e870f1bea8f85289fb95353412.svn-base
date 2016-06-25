package com.nepo.nepotransfered;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.utils.SPUtils;

/**
 * Created by lsiuhen on 2016/5/26.
 */
public class MediaActivity extends BaseActivity {

    /**
     * 视图
     * @param savedInstanceState
     */
    private RelativeLayout nav_ll;

    private RelativeLayout music_item_ll;
    private RelativeLayout video_item_ll;
    private RelativeLayout pic_item_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initView();
        initLisenter();
    }

    @Override
    public void initView() {
        isImmersion(true);
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        music_item_ll= (RelativeLayout) findViewById(R.id.music_item_ll);
        video_item_ll= (RelativeLayout) findViewById(R.id.video_item_ll);
        pic_item_ll= (RelativeLayout) findViewById(R.id.pic_item_ll);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initLisenter() {
        music_item_ll.setOnTouchListener(TouchDarkMedia);
        video_item_ll.setOnTouchListener(TouchDarkMedia);
        pic_item_ll.setOnTouchListener(TouchDarkMedia);

        music_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flag=SPUtils.get(MediaActivity.this, Constant.SP_TRANSPROGRESSSTATUS,"").toString();
               if(TextUtils.isEmpty(flag)){
                   //无正在传输
                   openActivity(MediaActivity.this,MediaMusicActivity.class);
               }else{
                   //正在传输中，不可添加队列，给予提示
                   Toast.makeText(MediaActivity.this, "正在传输中", Toast.LENGTH_SHORT).show();
               }

            }
        });
        pic_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flag=SPUtils.get(MediaActivity.this, Constant.SP_TRANSPROGRESSSTATUS,"").toString();
                if(TextUtils.isEmpty(flag)){
                    //无正在传输
                    openActivity(MediaActivity.this,MediaImgActivity.class);
                }else{
                    //正在传输中，不可添加队列，给予提示
                    Toast.makeText(MediaActivity.this, "正在传输中", Toast.LENGTH_SHORT).show();
                }

            }
        });
        video_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flag=SPUtils.get(MediaActivity.this, Constant.SP_TRANSPROGRESSSTATUS,"").toString();
                if(TextUtils.isEmpty(flag)){
                    //无正在传输
                    openActivity(MediaActivity.this,MediaVideoActivity.class);
                }else{
                    //正在传输中，不可添加队列，给予提示
                    Toast.makeText(MediaActivity.this, "正在传输中", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
