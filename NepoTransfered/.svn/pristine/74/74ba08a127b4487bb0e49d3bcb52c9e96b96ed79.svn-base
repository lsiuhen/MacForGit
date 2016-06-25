package com.nepo.nepotransfered;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.MediaImageAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.model.MediaImgModel;
import com.nepo.nepotransfered.loadsd.SDImgLoadTask;
import com.nepo.nepotransfered.service.LocalMediaIntentService;
import com.nepo.nepotransfered.view.xlistview.XListView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class MediaImgActivity extends BaseActivity {

    /**
     * 视图
     *
     * @param savedInstanceState
     */
    private RelativeLayout nav_ll;
    private RelativeLayout media_pic_nodata_rl;
    private XListView media_image_listview;


    /**
     * 数据
     *
     * @param savedInstanceState
     */
    public ArrayList<MediaImgModel> mediaImgArrayList;
    private MediaImageAdapter imageAdapter;
    private ImgReceiver myReceiver;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_img);
        initView();
        initLisenter();
        initData();

    }

    @Override
    public void initView() {
        isImmersion(true);
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        media_pic_nodata_rl = (RelativeLayout) findViewById(R.id.media_pic_nodata_rl);
        media_image_listview = (XListView) findViewById(R.id.media_image_listview);

    }

    @Override
    public void initData() {
        myReceiver = new ImgReceiver();
        IntentFilter filter = new IntentFilter(Constant.RECEIVER_LOCALIMG_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, filter);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //动态申请WRITE_EXTERNAL_STORAGE权限 [系统位于6.0以上,需申请运行时权限]
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
                return;
            } else {
                // 低于Android 6.0系统，正常加载
                loadImg();
            }
        } else {
            // 低于Android 6.0系统，正常加载
            loadImg();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            loadImg();
        } else {
            // Permission Denied
            Toast.makeText(MediaImgActivity.this, "请允许权限请求!!!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadImg() {
        Intent service = new Intent(this, LocalMediaIntentService.class);
        service.setData(Uri.parse(LocalMediaIntentService.EXTRA_PIC));
        startService(service);
    }

    @Override
    public void initLisenter() {
        nav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        media_image_listview.setPullRefreshEnable(false);
        media_image_listview.setPullLoadEnable(false);

        media_image_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaImgModel mediaImgModel = mediaImgArrayList.get(position - 1);
                //Toast.makeText(MediaImgActivity.this, ""+mediaImgModel, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaImgActivity.this, MediaImgItemActivity.class);
                intent.putExtra("imgDirInfo", (Serializable) mediaImgModel);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    public class ImgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.RECEIVER_LOCALIMG_ACTION)) {
                mediaImgArrayList = (ArrayList<MediaImgModel>) intent.getSerializableExtra(Constant.RECEIVER_LOCALIMG_ACTION);
                if (mediaImgArrayList != null && mediaImgArrayList.size() > 0) {
                    media_pic_nodata_rl.setVisibility(View.GONE);
                    media_image_listview.setVisibility(View.VISIBLE);
                    imageAdapter = new MediaImageAdapter(MediaImgActivity.this, mediaImgArrayList, media_image_listview);
                    media_image_listview.setAdapter(imageAdapter);
                } else {
                    media_pic_nodata_rl.setVisibility(View.VISIBLE);
                    media_image_listview.setVisibility(View.GONE);
                }
            }
        }
    }


}
