package com.nepo.nepotransfered;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.TransferTypeAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.model.MessageModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.model.TransferProgressModel;
import com.nepo.nepotransfered.model.TransferTypeModel;
import com.nepo.nepotransfered.net.download.ObserverManage;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nepo.nepotransfered.utils.SystemBarTintUtils;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.dragview.DragGridView;
import com.nepo.nepotransfered.view.dragview.ProvinceItem;
import com.nepo.nepotransfered.view.dragview.ProvinceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lsiuhen on 2016/6/1.
 * 传输中心
 */
public class TransferActivity extends BaseActivity implements Observer {

    /**
     * 视图
     */
    private RelativeLayout nav_ll;
    private DragGridView transfer_gridView;
    private RelativeLayout transfer_notransfer_rl;
    private ImageButton transfer_notransfer_ibtn;
    private RelativeLayout transfer_transfeing_rl;
    private TextView transfer_transfeing_curr_tv;
    private TextView transfer_transfeing_total_tv;
    private RelativeLayout transfer_gohome_rl;
    private RelativeLayout transfer_help_rl;
    /**
     * 数据
     * @param savedInstanceState
     */
    //测试数据
    private List<ProvinceItem> items = new ArrayList<>();
    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;
    ProvinceModel model;
    //真实数据
    private List<TransferTypeModel> transferTypeModels;
    private List<TransferProgressModel> transferProgressModels;
    private TransferTypeAdapter transferTypeAdapter;
    public final int MSG_FRAGMENTCOMPLETE = 1;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_FRAGMENTCOMPLETE:
                    //有文件传输完成
                    TransferProgressModel model= (TransferProgressModel) msg.obj;
                    completeInitData(model.getTransType());
                    break;

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ObserverManage.getObserver().addObserver(this);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        // 沉浸式
        isImmersion(true);
        nav_ll= (RelativeLayout) findViewById(R.id.nav_ll);
        transfer_gridView= (DragGridView) findViewById(R.id.transfer_gridView);
        transfer_gridView= (DragGridView) findViewById(R.id.transfer_gridView);
        transfer_notransfer_rl= (RelativeLayout) findViewById(R.id.transfer_notransfer_rl);
        transfer_notransfer_ibtn= (ImageButton) findViewById(R.id.transfer_notransfer_ibtn);
        transfer_transfeing_rl= (RelativeLayout) findViewById(R.id.transfer_transfeing_rl);
        transfer_transfeing_curr_tv= (TextView) findViewById(R.id.transfer_transfeing_curr_tv);
        transfer_transfeing_total_tv= (TextView) findViewById(R.id.transfer_transfeing_total_tv);
        transfer_gohome_rl= (RelativeLayout) findViewById(R.id.transfer_gohome_rl);
        transfer_help_rl= (RelativeLayout) findViewById(R.id.transfer_help_rl);


    }

    @Override
    public void initData() {
        //查询传输进程队列中是否有队列、有队列并且进度可显示，显示进度；传输进程队列无数据清除本地显示进度Flag;
        transferProgressModels = TransferDB.getTransferDB(this).queryAllTransferProgress();
        if(transferProgressModels.size()<1){
            SPUtils.remove(this,Constant.SP_TRANSFER);
            transfer_notransfer_rl.setVisibility(View.VISIBLE);
            transfer_transfeing_rl.setVisibility(View.GONE);
        }else{
            String isVisi = SPUtils.get(TransferActivity.this, Constant.SP_TRANSFER, "").toString();
            String progressNum=SPUtils.get(TransferActivity.this,Constant.SP_TRANSPROGRESSNUM,"").toString();
            if(!TextUtils.isEmpty(isVisi)){
                transfer_notransfer_rl.setVisibility(View.GONE);
                transfer_transfeing_rl.setVisibility(View.VISIBLE);
                transfer_transfeing_curr_tv.setText(transferProgressModels.size()+"");
                transfer_transfeing_total_tv.setText("/"+progressNum);
            }else{
                transfer_notransfer_rl.setVisibility(View.VISIBLE);
                transfer_transfeing_rl.setVisibility(View.GONE);
            }
        }


        init("");
    }

    private void init(String type) {
        //TODO[可用代码]
        transferTypeModels= TransferDB.getTransferDB(this).queryAllType();
        transferTypeAdapter=new TransferTypeAdapter(this, transferTypeModels,type);
        transfer_gridView.setAdapter(transferTypeAdapter);
       //是否可拖动
        setIsDrag();

    }

    private void setIsDrag(){
        String flag=SPUtils.get(this, Constant.SP_TRANSPROGRESSSTATUS,"").toString();
        if(TextUtils.isEmpty(flag)){
            //无正在传输
            transfer_gridView.setIsDrag(true);
        }else{
            transfer_gridView.setIsDrag(false);
        }
    }


    private void completeInitData(String type){
        init(type);
        transferProgressModels = TransferDB.getTransferDB(this).queryAllTransferProgress();
        if(transferProgressModels.size()<1){
            init("");
            transfer_notransfer_rl.setVisibility(View.VISIBLE);
            transfer_transfeing_rl.setVisibility(View.GONE);
        }else{
            String progressNum=SPUtils.get(TransferActivity.this,Constant.SP_TRANSPROGRESSNUM,"").toString();
            transfer_transfeing_curr_tv.setText(transferProgressModels.size()+"");
            transfer_transfeing_total_tv.setText("/"+progressNum);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        setIsDrag();
        transferTypeModels= TransferDB.getTransferDB(this).queryAllType();
        transferTypeAdapter.refresh(transferTypeModels);
    }

    @Override
    public void initLisenter() {
        transfer_gohome_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openActivity(TransferActivity.this,MainActivity.class);
            }
        });
        transfer_help_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TransferActivity.this, "Help", Toast.LENGTH_SHORT).show();
            }
        });

        transfer_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(TransferActivity.this,TransferItemActivity.class);
                intent.putExtra(TransferItemActivity.EXTRA_TYPE,transferTypeModels.get(position).getTypeName());
                startActivity(intent);
            }
        });

        transfer_notransfer_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看传输进程队列中是否有数据存在，存在显示数字,如果没有就添加到传输进程队列中，并开始发送，全部数据
                //TODO 添加所有存在的待传输文件 到传输进程中
                TransferDB.getTransferDB(TransferActivity.this).insertToTransferProgress(transferTypeModels);
                //添加到全局变量，等待心跳包的轮询
                List<TransferProgressModel> transferProgressModels = TransferDB.getTransferDB(TransferActivity.this).queryAllTransferProgress();
               if(transferProgressModels.size()>0){
                   Constant.addToTransingList(transferProgressModels);
                   SPUtils.put(TransferActivity.this,Constant.SP_TRANSPROGRESSNUM,transferProgressModels.size()+"");
                   SPUtils.put(TransferActivity.this,Constant.SP_TRANSFER,Constant.SP_TRANSFER);
                   //视频
                   SPUtils.put(TransferActivity.this, Constant.SP_VIDEOSEEKCIRCLE, Constant.SP_VIDEOSEEKCIRCLE);
                   //图片
                   SPUtils.put(TransferActivity.this, Constant.SP_IMGSEEKCIRCLE, Constant.SP_IMGSEEKCIRCLE);
                   //音乐
                   SPUtils.put(TransferActivity.this, Constant.SP_MUSICSEEKCIRCLE, Constant.SP_MUSICSEEKCIRCLE);
                   transfer_notransfer_rl.setVisibility(View.GONE);
                   transfer_transfeing_rl.setVisibility(View.VISIBLE);
                   //TransferActivity.this.transferProgressModels = TransferDB.getTransferDB(TransferActivity.this).queryAllTransferProgress();
                   transfer_transfeing_curr_tv.setText(transferProgressModels.size()+"");
                   transfer_transfeing_total_tv.setText("/"+ transferProgressModels.size()+"");
               }
            }
        });

        transfer_transfeing_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transJsonArrayByList = Constant.getTransJsonArrayByList();
                Utils.Sout("json",transJsonArrayByList);
            }
        });

    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof MessageModel) {
            MessageModel model = (MessageModel) data;
            if (model.getMsgType().equals(Constant.transComplateMsg)) {
                Message message=new Message();
                message.what=MSG_FRAGMENTCOMPLETE;
                message.obj=model.getMsgModel();
                mHandler.sendMessage(message);

            }
        }
    }
}
