package com.nepo.nepotransfered;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.FragmentPagerAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.fragment.FragmentApp;
import com.nepo.nepotransfered.fragment.FragmentImg;
import com.nepo.nepotransfered.fragment.FragmentMap;
import com.nepo.nepotransfered.fragment.FragmentMusic;
import com.nepo.nepotransfered.fragment.FragmentRom;
import com.nepo.nepotransfered.fragment.FragmentVideo;
import com.nepo.nepotransfered.model.MessageModel;
import com.nepo.nepotransfered.model.TransferProgressModel;
import com.nepo.nepotransfered.net.download.ObserverManage;
import com.nepo.nepotransfered.service.SocketServerService;
import com.nepo.nepotransfered.utils.ActivityManager;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.dialog.ConnectTipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends BaseActivity implements Observer {


    /**
     * 视图
     */
    private RelativeLayout main_trans_ll;
    private RelativeLayout main_adver_ll;
    private LinearLayout home_center_media_ll;
    private LinearLayout home_center_myapp_ll;
    private LinearLayout home_center_transmission_ll;
    private LinearLayout home_center_personal_ll;
    private RelativeLayout home_conn_status;
    private ImageButton home_connec_ibtn;
    private ImageButton home_adver_connec_ibtn;
    private ViewPager home_pager;
    private View home_indicator_view1;
    private View home_indicator_view2;
    private View home_indicator_view3;
    private View home_indicator_view4;
    private View home_indicator_view5;
    private View home_indicator_view6;
    /*指示点集合*/
    private List<View> viewList;
    /**
     * 数据
     */
    /* Fragment集合*/
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private long mExitTime;
    /***
     * 传输服务
     */
    private Intent mServiceIntent;

    private final int MSG_STARTTRANS=0;
    private final int MSG_COMPLETE=1;
    private final int MSG_CONECTION=2;

    private ConnectTipDialog tipDialog;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_STARTTRANS:
                    //判断让viewpager显示为当前的正在传输的进度球上
                    //TODO 待确定
                    TransferProgressModel model = (TransferProgressModel) msg.obj;
                    switch (model.getTransType()) {
                        case Constant.TRANSTYPE_MUSIC:
                            home_pager.setCurrentItem(0);
                            break;

                        case Constant.TRANSTYPE_PIC:
                            home_pager.setCurrentItem(1);
                            break;

                        case Constant.TRANSTYPE_VIDEO:
                            home_pager.setCurrentItem(2);
                            break;

                        case Constant.TRANSTYPE_APP:
                            home_pager.setCurrentItem(3);
                            break;

                        case Constant.TRANSTYPE_OFFMAP:
                            home_pager.setCurrentItem(4);
                            break;

                        case Constant.TRANSTYPE_ROM:
                            home_pager.setCurrentItem(5);
                            break;
                    }
                    break;
                case MSG_COMPLETE:
                    //initFragmentCopy();
                    List<String> stringList = TransferDB.getTransferDB(MainActivity.this).querydExistNoTransAllGroupBy();
                    if(stringList.size()<1){
                        //没有传输数据，显示广告
                        main_adver_ll.setVisibility(View.VISIBLE);
                        main_trans_ll.setVisibility(View.GONE);
                        //初始化标示
                        SPUtils.remove(MainActivity.this,Constant.SP_VIDEOSEEKCIRCLE);
                        SPUtils.remove(MainActivity.this,Constant.SP_IMGSEEKCIRCLE);
                        SPUtils.remove(MainActivity.this,Constant.SP_MUSICSEEKCIRCLE);
                        SPUtils.remove(MainActivity.this,Constant.SP_TRANSPROGRESSNUM);
                        SPUtils.remove(MainActivity.this,Constant.SP_TRANSPROGRESSSTATUS);
                    }

                    break;
                case MSG_CONECTION:
                    // 连接成功 隐藏广告，打开传输界面
                    home_connec_ibtn.setBackgroundResource(R.drawable.home_connection);
                    home_adver_connec_ibtn.setBackgroundResource(R.drawable.home_connection);
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;

            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObserverManage.getObserver().addObserver(this);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        isImmersion(true);
        viewList = new ArrayList<>();
        main_trans_ll = (RelativeLayout) findViewById(R.id.main_trans_ll);
        main_adver_ll = (RelativeLayout) findViewById(R.id.main_adver_ll);
        home_center_media_ll = (LinearLayout) findViewById(R.id.home_center_media_ll);
        home_center_myapp_ll = (LinearLayout) findViewById(R.id.home_center_myapp_ll);
        home_center_transmission_ll = (LinearLayout) findViewById(R.id.home_center_transmission_ll);
        home_center_personal_ll = (LinearLayout) findViewById(R.id.home_center_personal_ll);
        home_conn_status = (RelativeLayout) findViewById(R.id.home_conn_status);
        home_connec_ibtn = (ImageButton) findViewById(R.id.home_connec_ibtn);
        home_adver_connec_ibtn = (ImageButton) findViewById(R.id.home_adver_connec_ibtn);
        home_pager = (ViewPager) findViewById(R.id.home_pager);
        home_indicator_view1 = findViewById(R.id.home_indicator_view1);
        home_indicator_view2 = findViewById(R.id.home_indicator_view2);
        home_indicator_view3 = findViewById(R.id.home_indicator_view3);
        home_indicator_view4 = findViewById(R.id.home_indicator_view4);
        home_indicator_view5 = findViewById(R.id.home_indicator_view5);
        home_indicator_view6 = findViewById(R.id.home_indicator_view6);


        viewList.add(home_indicator_view1);
        viewList.add(home_indicator_view2);
        viewList.add(home_indicator_view3);
        viewList.add(home_indicator_view4);
        viewList.add(home_indicator_view5);
        viewList.add(home_indicator_view6);
        home_indicator_view1.setVisibility(View.GONE);
        home_indicator_view2.setVisibility(View.GONE);
        home_indicator_view3.setVisibility(View.GONE);
        home_indicator_view4.setVisibility(View.GONE);
        home_indicator_view5.setVisibility(View.GONE);
        home_indicator_view6.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        tipDialog=new ConnectTipDialog(this);
        mServiceIntent = new Intent(this, SocketServerService.class);
        startService(mServiceIntent);
        initFragmentCopy();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        // int count = PAGE_NUMBER;

        FragmentManager manager;
        FragmentTransaction transaction;

		/* 获取manager */
        manager = this.getSupportFragmentManager();
        /* 创建事物 */
        transaction = manager.beginTransaction();

        FragmentMusic firstFragment = new FragmentMusic();
        FragmentImg secondFragment = new FragmentImg();
        FragmentVideo threeFragment = new FragmentVideo();
        FragmentApp fourFragment = new FragmentApp();
        FragmentMap fiveFragment = new FragmentMap();
        FragmentRom sixFragment = new FragmentRom();

         /*创建一个Bundle用来存储数据，传递到Fragment中*/
        Bundle bundle = new Bundle();
        /*往bundle中添加数据*/
        bundle.putString("url_data", "");
        /*把数据设置到Fragment中*/

        fragments.add(firstFragment);
        fragments.add(secondFragment);
        fragments.add(threeFragment);
        fragments.add(fourFragment);
        fragments.add(fiveFragment);
        fragments.add(sixFragment);

        transaction.commitAllowingStateLoss();

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragments);
        home_pager.setAdapter(mAdapter);
        home_pager.setOnPageChangeListener(pageListener);
        home_pager.setCurrentItem(0);
    }

    private void initFragmentCopy() {
        List<String> stringList = TransferDB.getTransferDB(this).querydExistNoTransAllGroupBy();
//
        if (stringList.size() > 0) {
            main_adver_ll.setVisibility(View.GONE);
            main_trans_ll.setVisibility(View.VISIBLE);
            //有数据
            fragments.clear();//清空
            home_indicator_view1.setVisibility(View.GONE);
            home_indicator_view2.setVisibility(View.GONE);
            home_indicator_view3.setVisibility(View.GONE);
            home_indicator_view4.setVisibility(View.GONE);
            home_indicator_view5.setVisibility(View.GONE);
            home_indicator_view6.setVisibility(View.GONE);

            // int count = PAGE_NUMBER;
            FragmentManager manager;
            FragmentTransaction transaction;
//
		/* 获取manager */
            manager = this.getSupportFragmentManager();
        /* 创建事物 */
            transaction = manager.beginTransaction();
            for (int i = 0, size = stringList.size(); i < size; i++) {
                if (stringList.get(i).equals(Constant.TRANSTYPE_MUSIC)) {
                    FragmentMusic firstFragment = new FragmentMusic();
                    fragments.add(firstFragment);
                    home_indicator_view1.setVisibility(View.VISIBLE);
                }
                if (stringList.get(i).equals(Constant.TRANSTYPE_PIC)) {
                    FragmentImg secondFragment = new FragmentImg();
                    fragments.add(secondFragment);
                    home_indicator_view2.setVisibility(View.VISIBLE);
                }
                if (stringList.get(i).equals(Constant.TRANSTYPE_VIDEO)) {
                    FragmentVideo threeFragment = new FragmentVideo();
                    fragments.add(threeFragment);
                    home_indicator_view3.setVisibility(View.VISIBLE);
                }
                if (stringList.get(i).equals(Constant.TRANSTYPE_APP)) {
                    FragmentApp fourFragment = new FragmentApp();
                    fragments.add(fourFragment);
                    home_indicator_view4.setVisibility(View.VISIBLE);
                }
                if (stringList.get(i).equals(Constant.TRANSTYPE_OFFMAP)) {
                    FragmentMap fiveFragment = new FragmentMap();
                    fragments.add(fiveFragment);
                    home_indicator_view5.setVisibility(View.VISIBLE);
                }
                if (stringList.get(i).equals(Constant.TRANSTYPE_ROM)) {
                    FragmentRom sixFragment = new FragmentRom();
                    fragments.add(sixFragment);
                    home_indicator_view6.setVisibility(View.VISIBLE);
                }

            }

            if(stringList.size()<2){
                home_indicator_view1.setVisibility(View.GONE);
                home_indicator_view2.setVisibility(View.GONE);
                home_indicator_view3.setVisibility(View.GONE);
                home_indicator_view4.setVisibility(View.GONE);
                home_indicator_view5.setVisibility(View.GONE);
                home_indicator_view6.setVisibility(View.GONE);
            }

            if (fragments.size() > 0) {
                transaction.commitAllowingStateLoss();
                FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragments);
                home_pager.setAdapter(mAdapter);
                home_pager.setOnPageChangeListener(pageListener);
                home_pager.setCurrentItem(0);

            }
        }else{
            //没有传输数据，显示广告
            main_adver_ll.setVisibility(View.VISIBLE);
            main_trans_ll.setVisibility(View.GONE);
        }



    }

    @Override
    public void initLisenter() {
        home_center_media_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.this,MediaActivity.class);
            }
        });
        home_center_myapp_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.this,MyAppActivity.class);
            }
        });
        home_center_transmission_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.this,TransferActivity.class);
            }
        });
        home_center_personal_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.this,PersonalActivity.class);
            }
        });

        home_connec_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.show();
                Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                Object tag = home_connec_ibtn.getTag();
                Utils.Sout("tag",tag);
                if(tag!=null){
                    //连接成功
                }else{
                    //连接失败、未连接
                }
            }
        });
        home_adver_connec_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.show();
                Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                Object tag = home_adver_connec_ibtn.getTag();
                Utils.Sout("tag",tag);
                if(tag!=null){
                    //连接成功
                }else{
                    //连接失败、未连接
                }
            }
        });

        home_center_media_ll.setOnTouchListener(TouchDark);
        home_center_myapp_ll.setOnTouchListener(TouchDark);
        home_center_transmission_ll.setOnTouchListener(TouchDark);
        home_center_personal_ll.setOnTouchListener(TouchDark);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                mServiceIntent = new Intent(this, SocketServerService.class);
                stopService(mServiceIntent);
//
//                mServiceIntent = new Intent(this, DownloadService.class);
//                stopService(mServiceIntent);

                ActivityManager.getInstance().exit();

            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            home_pager.setCurrentItem(position);
            switch (position) {
                case 0:
                    // currentIndex = 1;
                    home_indicator_view1.setBackgroundResource(R.drawable.home_selectline);
                    home_indicator_view2.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view3.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view4.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view5.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view6.setBackgroundResource(R.drawable.home_noselectline);


                    break;
                case 1:
                    // currentIndex = 2;
                    home_indicator_view1.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view2.setBackgroundResource(R.drawable.home_selectline);
                    home_indicator_view3.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view4.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view5.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view6.setBackgroundResource(R.drawable.home_noselectline);
                    break;
                case 2:
                    //  currentIndex = 3;
                    home_indicator_view1.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view2.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view3.setBackgroundResource(R.drawable.home_selectline);
                    home_indicator_view4.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view5.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view6.setBackgroundResource(R.drawable.home_noselectline);
                    break;
                case 3:
                    //  currentIndex = 4;
                    home_indicator_view1.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view2.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view3.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view4.setBackgroundResource(R.drawable.home_selectline);
                    home_indicator_view5.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view6.setBackgroundResource(R.drawable.home_noselectline);
                    break;
                case 4:
                    //  currentIndex = 5;
                    home_indicator_view1.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view2.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view3.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view4.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view5.setBackgroundResource(R.drawable.home_selectline);
                    home_indicator_view6.setBackgroundResource(R.drawable.home_noselectline);
                    break;
                case 5:
                    //  currentIndex = 6;
                    home_indicator_view1.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view2.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view3.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view4.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view5.setBackgroundResource(R.drawable.home_noselectline);
                    home_indicator_view6.setBackgroundResource(R.drawable.home_selectline);
                    break;
            }
        }
    };


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("-- onRestart");
        initFragmentCopy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("-- onResume");
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof MessageModel) {
            MessageModel model = (MessageModel) data;
            if (model.getMsgType().equals(Constant.transConnectionMsg)) {

                mHandler.sendEmptyMessage(MSG_CONECTION);

            } else if (model.getMsgType().equals(Constant.transAddMsg)) {
                //handler.sendEmptyMessage(1);
            } else if (model.getMsgType().equals(Constant.transStartTransMsg)) {
                Message message = new Message();
                message.what = MSG_STARTTRANS;
                message.obj = model.getMsgModel();
                //mHandler.sendMessage(message);

            }else if(model.getMsgType().equals(Constant.transComplateMsg)){
                mHandler.sendEmptyMessage(MSG_COMPLETE);
            }
        }
    }
}
