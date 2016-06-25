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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.MediaMusicAdapter;
import com.nepo.nepotransfered.adapter.MediaVideoAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.loadsd.SDMusicLoadTask;
import com.nepo.nepotransfered.loadsd.SDVideoLoadTask;
import com.nepo.nepotransfered.model.MediaMusicModel;
import com.nepo.nepotransfered.model.MediaVideoModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.service.LocalMediaIntentService;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.tipview.TipsView;
import com.nepo.nepotransfered.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class MediaVideoActivity extends BaseActivity implements MediaVideoAdapter.OnVideoCheckedLisenter{

    /**
     * 视图
     */
    private RelativeLayout media_video_notrans_rl;
    private RelativeLayout media_video_transed_rl;
    private RelativeLayout media_video_nodata_rl;
    private TextView media_video_notrans_tv, media_video_transed_tv;
    private View media_video_notrans_view, media_video_transed_view;

    private RelativeLayout nav_ll;
    private XListView media_video_listview;

    private RelativeLayout media_video_send_item_control_rl;
    private ImageButton media_send_item_control_ibtn;
    private Button media_video_send_cencel_btn;

    private LinearLayout media_video_send_control;

    private CheckBox media_video_send_control_checked;

    private TextView media_video_send_item_control_mDragView;


    /**
     * 数据
     * @param savedInstanceState
     */
    private ArrayList<MediaVideoModel> mediaVideoInfoList;
    private ArrayList<MediaVideoModel> constantMediaVideoInfoList;
    private MediaVideoAdapter videoAdapter;
    private VideoReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_video);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        isImmersion(true);
        media_video_notrans_rl = (RelativeLayout) findViewById(R.id.media_video_notrans_rl);
        media_video_transed_rl = (RelativeLayout) findViewById(R.id.media_video_transed_rl);
        media_video_nodata_rl = (RelativeLayout) findViewById(R.id.media_video_nodata_rl);
        media_video_send_item_control_rl = (RelativeLayout) findViewById(R.id.media_send_item_control_rl);
        media_send_item_control_ibtn= (ImageButton) findViewById(R.id.media_send_item_control_ibtn);
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        media_video_listview = (XListView) findViewById(R.id.media_video_listview);
        media_video_send_control = (LinearLayout) findViewById(R.id.include_video_send_control);
        media_video_send_cencel_btn = (Button) findViewById(R.id.media_send_item_cencel_btn);
        media_video_send_control_checked = (CheckBox) findViewById(R.id.media_send_item_control_checked);
        media_video_send_item_control_mDragView = (TextView) findViewById(R.id.media_send_item_control_mDragView);
        media_video_notrans_tv = (TextView) findViewById(R.id.media_video_notrans_tv);
        media_video_transed_tv = (TextView) findViewById(R.id.media_video_transed_tv);
        media_video_transed_view = findViewById(R.id.media_video_transed_view);
        media_video_notrans_view = findViewById(R.id.media_video_notrans_view);

        TipsView.create(this).attach(media_video_send_item_control_mDragView, new TipsView.DragListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete() {
                media_video_send_control_checked.setChecked(false);
                videoAdapter.selectAllItem(false);
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
            }
        });

    }

    @Override
    public void initData() {

        myReceiver = new VideoReceiver();
        IntentFilter filter = new IntentFilter(Constant.RECEIVER_LOCALVIEDO_ACTION);
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
                loadVideo();
            }
        } else {
            // 低于Android 6.0系统，正常加载
            loadVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            loadVideo();
        } else {
            // Permission Denied
            Toast.makeText(MediaVideoActivity.this, "请允许权限请求!!!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadVideo() {
        Intent service = new Intent(this, LocalMediaIntentService.class);
        service.setData(Uri.parse(LocalMediaIntentService.EXTRA_VIDEO));
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

        media_video_notrans_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 未传输过的数据
                if (Utils.isFastClick()) {
                    return;
                }
                media_video_notrans_tv.setTextColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.blue));
                media_video_notrans_view.setBackgroundColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.blue));
                media_video_transed_tv.setTextColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.black));
                media_video_transed_view.setBackgroundColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.gray));
                List<TransferModel> transferModelList = TransferDB.getTransferDB(MediaVideoActivity.this).querydExistTransedAll(Constant.TRANSTYPE_VIDEO);
                if (transferModelList != null && transferModelList.size() > 0) {
                    mediaVideoInfoList = Utils.siftNoTransVideo(constantMediaVideoInfoList, transferModelList);
                    if (mediaVideoInfoList != null && mediaVideoInfoList.size() > 0) {
                        setNoDataGone();
                        videoAdapter = new MediaVideoAdapter(MediaVideoActivity.this, mediaVideoInfoList, 0);
                        videoAdapter.setCheckLisenter(MediaVideoActivity.this);
                        media_video_listview.setAdapter(videoAdapter);
                    } else {
                        setNoDataVisiable();
                    }
                } else {
                    if (mediaVideoInfoList != null && mediaVideoInfoList.size() > 0) {
                        setNoDataGone();
                        videoAdapter = new MediaVideoAdapter(MediaVideoActivity.this, mediaVideoInfoList, 0);
                        videoAdapter.setCheckLisenter(MediaVideoActivity.this);
                        media_video_listview.setAdapter(videoAdapter);
                    } else {
                        setNoDataVisiable();
                    }
                }

            }
        });

        media_video_transed_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                media_video_notrans_tv.setTextColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.black));
                media_video_notrans_view.setBackgroundColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.gray));
                media_video_transed_tv.setTextColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.blue));
                media_video_transed_view.setBackgroundColor(ContextCompat.getColor(MediaVideoActivity.this,R.color.blue));

                List<TransferModel> transferModelList = TransferDB.getTransferDB(MediaVideoActivity.this).querydExistTransedAll(Constant.TRANSTYPE_VIDEO);
                if (transferModelList != null && transferModelList.size() > 0) {
                    List<MediaVideoModel> videoGroupList = Utils.siftTransedVideo(constantMediaVideoInfoList, transferModelList);
                    setNoDataGone();
                    videoAdapter = new MediaVideoAdapter(MediaVideoActivity.this, videoGroupList, 1);
                    videoAdapter.setCheckLisenter(MediaVideoActivity.this);
                    media_video_listview.setAdapter(videoAdapter);
                } else {
                    setNoDataVisiable();
                }
                media_video_send_control.setVisibility(View.GONE);
            }
        });
        media_video_send_control_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoAdapter.selectAllItem(media_video_send_control_checked.isChecked());
                videoAdapter.notifyDataSetChanged();
            }
        });
        media_send_item_control_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO 添加到传输队列
                if (videoAdapter.getSelectedItemPositions().length > 0) {
                    for (int i = 0; i < videoAdapter.getSelectedAudioPaths().length; i++) {
                        MediaVideoModel mediaVideoModel = videoAdapter.getSelectedAudioPaths()[i];
                        TransferModel transferModel=new TransferModel();
                        transferModel.setTransType(Constant.TRANSTYPE_VIDEO);
                        transferModel.setTransPath(mediaVideoModel.getPath());
                        transferModel.setTransBitmap(mediaVideoModel.getBitmap());
                        long l = TransferDB.getTransferDB(MediaVideoActivity.this).insertToTransfer(transferModel.getTransType(), transferModel);
                        Utils.Sout("long",l);
                    }

                    Toast.makeText(MediaVideoActivity.this, "添加到传输列表成功", Toast.LENGTH_SHORT).show();
                    openActivity(MediaVideoActivity.this,TransferActivity.class);
                    finish();

                }


            }
        });
        media_video_send_cencel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_video_send_control_checked.setChecked(false);
                videoAdapter.selectAllItem(false);
                videoAdapter.notifyDataSetChanged();
            }
        });
        media_video_listview.setPullRefreshEnable(false);
        media_video_listview.setPullLoadEnable(false);
    }


    private void setNoDataVisiable() {
        media_video_listview.setVisibility(View.GONE);
        media_video_send_control.setVisibility(View.GONE);
        media_video_nodata_rl.setVisibility(View.VISIBLE);
    }

    private void setNoDataGone() {
        media_video_listview.setVisibility(View.VISIBLE);
        media_video_send_control.setVisibility(View.VISIBLE);
        media_video_nodata_rl.setVisibility(View.GONE);
    }


    @Override
    public void checkVideo() {
        if (videoAdapter.getSelectedItemPositions().length > 0) {
            media_video_send_item_control_mDragView.setVisibility(View.VISIBLE);
            media_video_send_item_control_mDragView.setText(videoAdapter.getSelectedItemPositions().length+"");
        } else {
            media_video_send_item_control_mDragView.setVisibility(View.GONE);
        }

        if (videoAdapter.getSelectedItemPositions().length == mediaVideoInfoList.size()) {
            media_video_send_control_checked.setChecked(true);
        } else {
            media_video_send_control_checked.setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    public class VideoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.RECEIVER_LOCALVIEDO_ACTION)) {
                constantMediaVideoInfoList = (ArrayList<MediaVideoModel>) intent.getSerializableExtra(Constant.RECEIVER_LOCALVIEDO_ACTION);
                mediaVideoInfoList = constantMediaVideoInfoList;
                if (mediaVideoInfoList != null && mediaVideoInfoList.size() > 0) {
                    List<TransferModel> transferModelList = TransferDB.getTransferDB(MediaVideoActivity.this).querydExistTransedAll(Constant.TRANSTYPE_VIDEO);
                    if (transferModelList != null && transferModelList.size() > 0) {
                        mediaVideoInfoList = Utils.siftNoTransVideo(constantMediaVideoInfoList, transferModelList);
                        setNoDataGone();
                        videoAdapter = new MediaVideoAdapter(MediaVideoActivity.this, mediaVideoInfoList, 0);
                        videoAdapter.setCheckLisenter(MediaVideoActivity.this);
                        media_video_listview.setAdapter(videoAdapter);
                    } else {
                        setNoDataGone();
                        videoAdapter = new MediaVideoAdapter(MediaVideoActivity.this, mediaVideoInfoList, 0);
                        videoAdapter.setCheckLisenter(MediaVideoActivity.this);
                        media_video_listview.setAdapter(videoAdapter);
                    }
                } else {
                    setNoDataVisiable();
                }
            }
        }
    }

}
