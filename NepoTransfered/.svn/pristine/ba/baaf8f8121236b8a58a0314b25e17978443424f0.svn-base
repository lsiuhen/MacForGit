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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.MediaImageAdapter;
import com.nepo.nepotransfered.adapter.MediaMusicAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.loadsd.SDMusicLoadTask;
import com.nepo.nepotransfered.model.MediaImgModel;
import com.nepo.nepotransfered.model.MediaMusicModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.service.LocalMediaIntentService;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.loadview.GearLoadingRenderer;
import com.nepo.nepotransfered.view.loadview.LoadingDrawable;
import com.nepo.nepotransfered.view.tipview.TipsView;
import com.nepo.nepotransfered.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/5/26.
 * 音乐界面
 */
public class MediaMusicActivity extends BaseActivity implements XListView.IXListViewListener, MediaMusicAdapter.OnMusicCheckedLisenter {

    /**
     * 视图
     */
    private RelativeLayout media_music_notrans_rl;
    private RelativeLayout media_music_transed_rl;
    private RelativeLayout media_music_nodata_rl;

    private TextView media_music_notrans_tv, media_music_transed_tv;
    private View media_music_notrans_view, media_music_transed_view;

    private XListView media_music_listview;

    private RelativeLayout nav_ll;
    private LinearLayout media_music_send_control;
    private RelativeLayout media_music_send_item_control_rl;
    private ImageButton media_send_item_control_ibtn;
    private Button media_music_send_item_cencel_btn;
    private CheckBox media_music_send_control_checked;
    private TextView media_music_send_item_control_mDragView;
    private RelativeLayout music_content_rl, music_content_loading_rl;
    private ImageView music_content_loading_iv;
    private LoadingDrawable mGearDrawable;

    /**
     * 数据
     *
     * @param
     */
    private ArrayList<MediaMusicModel> mediaMusicInfoList;
    private ArrayList<MediaMusicModel> constantMediaMusicInfoList;
    private MediaMusicAdapter mediaMusicAdapter;
    private MusicReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_music);
        initView();
        initLisenter();
        initData();

    }


    @Override
    public void initView() {
        isImmersion(true);
        mGearDrawable = new LoadingDrawable(new GearLoadingRenderer(this));
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        music_content_rl = (RelativeLayout) findViewById(R.id.music_content_rl);
        music_content_loading_rl = (RelativeLayout) findViewById(R.id.music_content_loading_rl);
        music_content_loading_iv = (ImageView) findViewById(R.id.music_content_loading_iv);
        media_music_send_item_control_rl = (RelativeLayout) findViewById(R.id.media_send_item_control_rl);
        media_music_notrans_rl = (RelativeLayout) findViewById(R.id.media_music_notrans_rl);
        media_music_transed_rl = (RelativeLayout) findViewById(R.id.media_music_transed_rl);
        media_music_nodata_rl = (RelativeLayout) findViewById(R.id.media_music_nodata_rl);
        media_music_listview = (XListView) findViewById(R.id.media_music_listview);
        media_music_send_control = (LinearLayout) findViewById(R.id.include_music_send_control);
        media_send_item_control_ibtn = (ImageButton) findViewById(R.id.media_send_item_control_ibtn);
        media_music_send_item_cencel_btn = (Button) findViewById(R.id.media_send_item_cencel_btn);
        media_music_send_control_checked = (CheckBox) findViewById(R.id.media_send_item_control_checked);
        media_music_send_item_control_mDragView = (TextView) findViewById(R.id.media_send_item_control_mDragView);
        media_music_notrans_tv = (TextView) findViewById(R.id.media_music_notrans_tv);
        media_music_transed_tv = (TextView) findViewById(R.id.media_music_transed_tv);
        media_music_notrans_view = findViewById(R.id.media_music_notrans_view);
        media_music_transed_view = findViewById(R.id.media_music_transed_view);
        music_content_loading_iv.setImageDrawable(mGearDrawable);

        TipsView.create(this).attach(media_music_send_item_control_mDragView, new TipsView.DragListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete() {
                media_music_send_control_checked.setChecked(false);
                mediaMusicAdapter.selectAllItem(false);
                mediaMusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
            }
        });

    }

    @Override
    public void initData() {
        myReceiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter(Constant.RECEIVER_LOCALMUSIC_ACTION);
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
                loadMusic();
            }
        } else {
            // 低于Android 6.0系统，正常加载
            loadMusic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            loadMusic();
        } else {
            // Permission Denied
            Toast.makeText(MediaMusicActivity.this, "请允许权限请求!!!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadMusic() {

        Intent service = new Intent(this, LocalMediaIntentService.class);
        service.setData(Uri.parse(LocalMediaIntentService.EXTRA_MUSIC));
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
        media_music_notrans_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 未传输过的数据
                if (Utils.isFastClick()) {
                    return;
                }
                media_music_notrans_tv.setTextColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.blue));
                media_music_notrans_view.setBackgroundColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.blue));
                media_music_transed_tv.setTextColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.black));
                media_music_transed_view.setBackgroundColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.gray));
                List<TransferModel> transferModelList = TransferDB.getTransferDB(MediaMusicActivity.this).querydExistTransedAll(Constant.TRANSTYPE_MUSIC);
                if (transferModelList != null && transferModelList.size() > 0) {
                    mediaMusicInfoList = Utils.siftNoTransMusic(constantMediaMusicInfoList, transferModelList);
                    if (mediaMusicInfoList != null && mediaMusicInfoList.size() > 0) {
                        setNoDataGone();
                        mediaMusicAdapter = new MediaMusicAdapter(MediaMusicActivity.this, mediaMusicInfoList, 0);
                        mediaMusicAdapter.setCheckLisenter(MediaMusicActivity.this);
                        media_music_listview.setAdapter(mediaMusicAdapter);
                    } else {
                        setNoDataVisiable();
                    }

                } else {
                    if (mediaMusicInfoList != null && mediaMusicInfoList.size() > 0) {
                        setNoDataGone();
                        mediaMusicAdapter = new MediaMusicAdapter(MediaMusicActivity.this, mediaMusicInfoList, 0);
                        mediaMusicAdapter.setCheckLisenter(MediaMusicActivity.this);
                        media_music_listview.setAdapter(mediaMusicAdapter);
                    } else {
                        setNoDataVisiable();
                    }
                }


            }
        });
        media_music_transed_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 传输完成
                if (Utils.isFastClick()) {
                    return;
                }
                media_music_notrans_tv.setTextColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.black));
                media_music_notrans_view.setBackgroundColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.gray));
                media_music_transed_tv.setTextColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.blue));
                media_music_transed_view.setBackgroundColor(ContextCompat.getColor(MediaMusicActivity.this, R.color.blue));

                List<TransferModel> transferModelList = TransferDB.getTransferDB(MediaMusicActivity.this).querydExistTransedAll(Constant.TRANSTYPE_MUSIC);
                if (transferModelList != null && transferModelList.size() > 0) {
                    List<MediaMusicModel> musicGroupList = Utils.siftTransedMusic(constantMediaMusicInfoList, transferModelList);
                    setNoDataGone();
                    mediaMusicAdapter = new MediaMusicAdapter(MediaMusicActivity.this, musicGroupList, 1);
                    mediaMusicAdapter.setCheckLisenter(MediaMusicActivity.this);
                    media_music_listview.setAdapter(mediaMusicAdapter);
                } else {
                    setNoDataVisiable();
                }
                media_music_send_control.setVisibility(View.GONE);
            }
        });
        media_send_item_control_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 添加到传输队列
                if (mediaMusicAdapter.getSelectedItemPositions().length > 0) {
                    for (int i = 0; i < mediaMusicAdapter.getSelectedAudioPaths().length; i++) {
                        MediaMusicModel mediaMusicModel = mediaMusicAdapter.getSelectedAudioPaths()[i];
                        TransferModel transferModel = new TransferModel();
                        transferModel.setTransType(Constant.TRANSTYPE_MUSIC);
                        transferModel.setTransPath(mediaMusicModel.getPath());
                        transferModel.setTransBitmap(mediaMusicModel.getBitmap());
                        long l = TransferDB.getTransferDB(MediaMusicActivity.this).insertToTransfer(transferModel.getTransType(), transferModel);
                        Utils.Sout("long", l);
                    }

                    Toast.makeText(MediaMusicActivity.this, "添加到传输列表成功", Toast.LENGTH_SHORT).show();
                    openActivity(MediaMusicActivity.this, TransferActivity.class);
                    finish();

                }


            }
        });
        media_music_send_control_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaMusicAdapter.selectAllItem(media_music_send_control_checked.isChecked());
                mediaMusicAdapter.notifyDataSetChanged();
            }
        });
        media_music_send_item_cencel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_music_send_control_checked.setChecked(false);
                mediaMusicAdapter.selectAllItem(false);
                mediaMusicAdapter.notifyDataSetChanged();
            }
        });
        media_music_listview.setPullRefreshEnable(false);
        media_music_listview.setPullLoadEnable(false);
        media_music_listview.setXListViewListener(this);
    }

    private void setNoDataVisiable() {
        media_music_listview.setVisibility(View.GONE);
        media_music_send_control.setVisibility(View.GONE);
        media_music_nodata_rl.setVisibility(View.VISIBLE);
    }

    private void setNoDataGone() {
        media_music_listview.setVisibility(View.VISIBLE);
        media_music_send_control.setVisibility(View.VISIBLE);
        media_music_nodata_rl.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void checkMusic() {
        if (mediaMusicAdapter.getSelectedItemPositions().length > 0) {
            media_music_send_item_control_mDragView.setVisibility(View.VISIBLE);
            media_music_send_item_control_mDragView.setText(mediaMusicAdapter.getSelectedItemPositions().length + "");
        } else {
            media_music_send_item_control_mDragView.setVisibility(View.GONE);
        }

        if (mediaMusicAdapter.getSelectedItemPositions().length == mediaMusicInfoList.size()) {
            media_music_send_control_checked.setChecked(true);
        } else {
            media_music_send_control_checked.setChecked(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGearDrawable.start();
        music_content_rl.setVisibility(View.GONE);
        music_content_loading_rl.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.RECEIVER_LOCALMUSIC_ACTION)) {
                mGearDrawable.stop();
                music_content_rl.setVisibility(View.VISIBLE);
                music_content_loading_rl.setVisibility(View.GONE);

                constantMediaMusicInfoList = (ArrayList<MediaMusicModel>) intent.getSerializableExtra(Constant.RECEIVER_LOCALMUSIC_ACTION);
                mediaMusicInfoList = constantMediaMusicInfoList;
                if (mediaMusicInfoList != null && mediaMusicInfoList.size() > 0) {
                    List<TransferModel> transferCompleteModelList = TransferDB.getTransferDB(MediaMusicActivity.this).querydExistTransedAll(Constant.TRANSTYPE_MUSIC);
                    if (transferCompleteModelList != null && transferCompleteModelList.size() > 0) {
                        mediaMusicInfoList = Utils.siftNoTransMusic(constantMediaMusicInfoList, transferCompleteModelList);
                        setNoDataGone();
                        mediaMusicAdapter = new MediaMusicAdapter(MediaMusicActivity.this, mediaMusicInfoList, 0);
                        mediaMusicAdapter.setCheckLisenter(MediaMusicActivity.this);
                        media_music_listview.setAdapter(mediaMusicAdapter);
                    } else {
                        setNoDataGone();
                        mediaMusicAdapter = new MediaMusicAdapter(MediaMusicActivity.this, mediaMusicInfoList, 0);
                        mediaMusicAdapter.setCheckLisenter(MediaMusicActivity.this);
                        media_music_listview.setAdapter(mediaMusicAdapter);
                    }

                } else {
                    setNoDataVisiable();
                }
            }
        }
    }


}
