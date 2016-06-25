package com.nepo.nepotransfered;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.TransferItemAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.model.MessageModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.model.TransferProgressModel;
import com.nepo.nepotransfered.net.download.ObserverManage;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nepo.nepotransfered.view.xlistview.XListView;


import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lsiuhen on 2016/6/3.
 */
public class TransferItemActivity extends BaseActivity implements TransferItemAdapter.OnTransferDelClickLisenter, Observer {

    /**
     * 视图
     */
    private RelativeLayout nav_ll;
    private TextView title;
    private XListView transfer_media_music_listview;

    /**
     * 数据
     *
     * @param savedInstanceState
     */
    private List<TransferModel> transferModelList;
    private TransferItemAdapter transferItemAdapter;
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    // 进度更新
    public final int MSG_FRAGMENTPROGRESS = 0;
    public final int MSG_FRAGMENTCOMPLETE = 1;
    private String type;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_FRAGMENTPROGRESS:
                    TransferProgressModel model = (TransferProgressModel) msg.obj;
                    if (transferItemAdapter != null) {
                        transferItemAdapter.setEntity(model);
                    }
                    break;
                case MSG_FRAGMENTCOMPLETE:
                    transferModelList = TransferDB.getTransferDB(TransferItemActivity.this).queryTransferByType(type);
                    transferItemAdapter.refresh(transferModelList);
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_item);
        ObserverManage.getObserver().addObserver(this);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        isImmersion(true);
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        title = (TextView) findViewById(R.id.title);
        transfer_media_music_listview = (XListView) findViewById(R.id.transfer_media_music_listview);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra(EXTRA_TYPE);
        title.setText(type);
        transferModelList = TransferDB.getTransferDB(this).queryTransferByType(type);
        transferItemAdapter = new TransferItemAdapter(this, transferModelList);
        transferItemAdapter.setOnTransferDelClickLisenter(this);
        transfer_media_music_listview.setAdapter(transferItemAdapter);
    }

    @Override
    public void initLisenter() {
        transfer_media_music_listview.setPullRefreshEnable(false);
        transfer_media_music_listview
                .setPullLoadEnable(false);
        nav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDel(TransferModel model) {
        String flag = SPUtils.get(TransferItemActivity.this, Constant.SP_TRANSPROGRESSSTATUS, "").toString();
        if (TextUtils.isEmpty(flag)) {
            //无正在传输
            //从传输队列中删除
            int line = TransferDB.getTransferDB(this).deletTransferModel(model);
            if (line != -1) {
                Toast.makeText(TransferItemActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                transferModelList = TransferDB.getTransferDB(this).queryTransferByType(type);
                transferItemAdapter.refresh(transferModelList);
            }
        } else {
            //正在传输中，不可删除队列，给予提示
            Toast.makeText(TransferItemActivity.this, "正在传输中", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof MessageModel) {
            MessageModel model = (MessageModel) data;
            if (model.getMsgType().equals(Constant.transFragmentMsg)) {
                Message message = new Message();
                message.what = MSG_FRAGMENTPROGRESS;
                message.obj = model.getMsgModel();
                mHandler.sendMessage(message);
            } else if (model.getMsgType().equals(Constant.transComplateMsg)) {
                mHandler.sendEmptyMessage(MSG_FRAGMENTCOMPLETE);

            }
        }
    }
}
