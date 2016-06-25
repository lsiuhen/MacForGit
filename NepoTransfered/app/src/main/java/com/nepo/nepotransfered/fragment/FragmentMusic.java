package com.nepo.nepotransfered.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.TransferItemActivity;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.app.NepoApplication;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.model.MessageModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.model.TransferProgressModel;
import com.nepo.nepotransfered.net.download.ObserverManage;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.circleview.SeekCircle;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lsiuhen on 2016/5/30.
 * <p/>
 * 音乐
 */
public class FragmentMusic extends BaseFragment implements Observer {
    /**
     * 视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    private RelativeLayout fragment_music_contain_rl;
    private SeekCircle fragment_music_seekCircle;
    private ImageView transfer_num1;
    private ImageView transfer_num2;
    private ImageView transfer_num3;
    private TextView transfer_status_tv;

    /**
     * 数据
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    private List<TransferModel> transferModelList;

    private String isVisiable = "";


    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_FRAGMENTPROGRESS:
                    TransferProgressModel model = (TransferProgressModel) msg.obj;
                    if(model.getTransType().equals(Constant.TRANSTYPE_MUSIC)){
                        fragment_music_seekCircle.setProgress(model.getTransProgress());

                        if(model.getTransProgress()==100){
                            fragment_music_seekCircle.setProgress(0);
                        }

                    }
                    break;
                case MSG_FRAGMENTCOMPLETE:
                    initData();
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        ObserverManage.getObserver().addObserver(this);
        initView(view);
//        initData();
        initLisenter();
        return view;
    }

    @Override
    public void initView(View view) {
        fragment_music_contain_rl = (RelativeLayout) view.findViewById(R.id.fragment_music_contain_rl);
        fragment_music_seekCircle = (SeekCircle) view.findViewById(R.id.fragment_music_seekCircle);
        transfer_num1 = (ImageView) view.findViewById(R.id.transfer_num1);
        transfer_num2 = (ImageView) view.findViewById(R.id.transfer_num2);
        transfer_num3 = (ImageView) view.findViewById(R.id.transfer_num3);
        transfer_status_tv = (TextView) view.findViewById(R.id.transfer_status_tv);
        fragment_music_seekCircle.setEnabled(false);
        fragment_music_seekCircle.setMax(100);
        isVisiable = SPUtils.get(getActivity(), Constant.SP_MUSICSEEKCIRCLE, "").toString();
        if (!TextUtils.isEmpty(isVisiable)) {
            fragment_music_seekCircle.setVisibility(View.VISIBLE);
        }else{
            fragment_music_seekCircle.setVisibility(View.GONE);
        }

    }

    @Override
    public void initData() {
        if(getActivity()!=null){
            transferModelList = TransferDB.getTransferDB(getActivity()).queryTransferByType(Constant.TRANSTYPE_MUSIC);
            if (transferModelList.size() < 1) {
                Utils.Sout("act", NepoApplication.getInstance()+","+getActivity());
                    SPUtils.remove(getActivity(), Constant.SP_MUSICSEEKCIRCLE);

            }
            //显示进度球中数字
            showImgByNumber(transferModelList.size(),transfer_num1,transfer_num2,transfer_num3);

        }

    }

    @Override
    public void initLisenter() {
        fragment_music_contain_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Music", Toast.LENGTH_SHORT).show();
                isVisiable = SPUtils.get(getActivity(), Constant.SP_MUSICSEEKCIRCLE, "").toString();
                if(TextUtils.isEmpty(isVisiable)){

                    SPUtils.put(getActivity(), Constant.SP_MUSICSEEKCIRCLE, Constant.SP_MUSICSEEKCIRCLE);
                    SPUtils.put(getActivity(),Constant.SP_TRANSFER,Constant.SP_TRANSFER);
                    fragment_music_seekCircle.setVisibility(View.VISIBLE);
                    //TODO 添加本类型的文件数据进入传输进程队列中，开始发送数据
                    TransferDB.getTransferDB(getActivity()).insertToTransferProgressByType(Constant.TRANSTYPE_MUSIC);
                    List<TransferProgressModel> transferProgressModels = TransferDB.getTransferDB(getActivity()).queryAllTransferProgress();
                    //添加到全局变量，等待心跳包的轮询
                    Constant.addToTransingList(transferProgressModels);
                    String progressNum=SPUtils.get(getActivity(),Constant.SP_TRANSPROGRESSNUM,"").toString();
                    if(!TextUtils.isEmpty(progressNum)){
                        //int temp;
                        int pnum=Integer.valueOf(progressNum);
                        pnum+=transferProgressModels.size();
                        SPUtils.put(getActivity(),Constant.SP_TRANSPROGRESSNUM,pnum+"");
                    }else{
                        SPUtils.put(getActivity(),Constant.SP_TRANSPROGRESSNUM,transferProgressModels.size()+"");
                    }


                }else{
                    // 进入传输进程，准备开始传输
                    Intent intent=new Intent(getActivity(),TransferItemActivity.class);
                    intent.putExtra(TransferItemActivity.EXTRA_TYPE,Constant.TRANSTYPE_MUSIC);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //System.out.println("-- start");
        initData();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof MessageModel){
            MessageModel model= (MessageModel) data;
            if(model.getMsgType().equals(Constant.transFragmentMsg)){
                Message message = new Message();
                message.what = MSG_FRAGMENTPROGRESS;
                message.obj = model.getMsgModel();
                mHandler.sendMessage(message);
            }else if (model.getMsgType().equals(Constant.transComplateMsg)){
                TransferProgressModel msgModel = model.getMsgModel();
                if(msgModel.getTransType().equals(Constant.TRANSTYPE_MUSIC)){
                    mHandler.sendEmptyMessage(MSG_FRAGMENTCOMPLETE);
                }


            }
        }
    }
}
