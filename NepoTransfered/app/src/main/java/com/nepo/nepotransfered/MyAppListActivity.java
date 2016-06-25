package com.nepo.nepotransfered;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.nepo.nepotransfered.adapter.MyappListAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.DownloadDB;
import com.nepo.nepotransfered.model.AppDownInfoModel;
import com.nepo.nepotransfered.model.AppInfosModel;
import com.nepo.nepotransfered.net.download.DownObserverMsg;
import com.nepo.nepotransfered.net.download.DownloadManager;
import com.nepo.nepotransfered.net.download.DownloadRequestCallBack;
import com.nepo.nepotransfered.net.download.DownloadService;
import com.nepo.nepotransfered.net.download.ObserverManage;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.xlistview.XListView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lsiuhen on 2016/5/30.
 */
public class MyAppListActivity extends BaseActivity implements Observer, MyappListAdapter.downClickLisenter {

    /**
     * 视图
     *
     * @param savedInstanceState
     */
    private RelativeLayout mNav_ll;
    private XListView myapp_list_listview;

    /**
     * 数据
     *
     * @param savedInstanceState
     */
    public final static String MYAPPEXTRA = "appListInfo";
    private List<AppInfosModel> appInfosList;
    private List<AppInfosModel> appInfosesSift;
    private MyappListAdapter myappListAdapter;
    private DownloadManager downloadManager;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                DownObserverMsg downObserverMsg = (DownObserverMsg) msg.obj;
                if (downObserverMsg.downId != null) {

                    AppInfosModel appInfosModel = new AppInfosModel();
                    appInfosModel.setDownId(downObserverMsg.downId);
                    appInfosModel.setDownProgress(downObserverMsg.progress);
                    myappListAdapter.setEntity(appInfosModel);

                    if (downObserverMsg.progress >= 100) {
                        AppDownInfoModel model = new AppDownInfoModel();
                        model.setId(downObserverMsg.downId);
                        model.setFileDownStatus(Constant.STATE_DOWNLOADED);
                        DownloadDB.getDownInfoDB(MyAppListActivity.this).updateIsDowned(model);
                    }
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapp_list);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        isImmersion(true);
        ObserverManage.getObserver().addObserver(this);
        appInfosList = (List<AppInfosModel>) getIntent().getSerializableExtra(MYAPPEXTRA);
        mNav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        myapp_list_listview = (XListView) findViewById(R.id.myapp_list_listview);

    }

    @Override
    public void initData() {
        if (downloadManager == null) {
            downloadManager = DownloadService.getDownloadManager(getApplicationContext());
        }
        List<AppDownInfoModel> downFileInfoList = DownloadDB.getDownInfoDB(this).queryAll();
        appInfosesSift = Utils.shiftData(appInfosList, downFileInfoList);
        myappListAdapter = new MyappListAdapter(this, appInfosesSift);
        myappListAdapter.setDownClickLisenter(this);
        myapp_list_listview.setAdapter(myappListAdapter);
    }

    @Override
    public void initLisenter() {
        myapp_list_listview.setPullRefreshEnable(false);
        myapp_list_listview.setPullLoadEnable(false);
        mNav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void downClick(AppInfosModel appInfosModel) {
        try {
            downloadManager.addNewDownload(appInfosModel.getAppUrl(), appInfosModel.getAppName(), Utils.getDiskDirByType(appInfosModel.getAppName()), true, true, new DownloadRequestCallBack());
            AppDownInfoModel appDownInfoModel = new AppDownInfoModel
                    (appInfosModel.getDownId(), appInfosModel.getAppName(),
                            Utils.getDiskDirByType(appInfosModel.getAppName()), Constant.TRANSTYPE_APP, Constant.STATE_NONE);
            //添加到自定义下载数据库
            long l = DownloadDB.getDownInfoDB(MyAppListActivity.this).inserDownFileInfo(appDownInfoModel);
            Utils.Sout("insert Down", l);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownObserverMsg) {
            DownObserverMsg msg = (DownObserverMsg) data;

            Utils.Sout("msg", msg.toString());

            Message message = new Message();
            message.obj = msg;
            message.what = 0;
            mHandler.sendMessage(message);
        }
    }


}
