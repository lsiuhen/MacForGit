package com.nepo.nepotransfered;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.HttpHandler;
import com.nepo.nepotransfered.adapter.DownloadAdapter;
import com.nepo.nepotransfered.adapter.DownloadCompleteAdapter;
import com.nepo.nepotransfered.db.DownloadDB;
import com.nepo.nepotransfered.model.AppDownInfoModel;
import com.nepo.nepotransfered.net.download.DownloadInfo;
import com.nepo.nepotransfered.net.download.DownloadManager;
import com.nepo.nepotransfered.net.download.DownloadService;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/5/28.
 * 下载中心
 *
 */
public class DownloadCenterActivity extends BaseActivity {

    /**
     * 视图
     * @param savedInstanceState
     */
    private RelativeLayout download_transed_rl;
    private RelativeLayout download_notrans_rl;
    private TextView download_notrans_tv,download_transed_tv;
    private View download_notrans_view,download_transed_view;
    private XListView download_listview;
    private RelativeLayout nav_ll;

    /**
     * 数据
     * @param savedInstanceState
     */
    private DownloadManager downloadManager;
    private List<DownloadInfo> downloadInfoList;
    private List<DownloadInfo> downloadingInfoList;
    private List<AppDownInfoModel> downloadCompleteInfoList;
    private DownloadAdapter downloadAdapter;
    private DownloadCompleteAdapter downloadCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        isImmersion(true);
        nav_ll= (RelativeLayout) findViewById(R.id.nav_ll);
        download_transed_rl= (RelativeLayout) findViewById(R.id.download_transed_rl);
        download_notrans_rl= (RelativeLayout) findViewById(R.id.download_notrans_rl);
        download_notrans_tv= (TextView) findViewById(R.id.download_notrans_tv);
        download_transed_tv= (TextView) findViewById(R.id.download_transed_tv);
        download_notrans_view= findViewById(R.id.download_notrans_view);
        download_transed_view= findViewById(R.id.download_transed_view);
        download_listview= (XListView) findViewById(R.id.download_listview);
    }

    @Override
    public void initData() {
        if (downloadManager == null) {
            downloadManager = DownloadService.getDownloadManager(this);
        }
        downloadInfoList = downloadManager.getDownloadInfoList();
        initDownloadData();

    }

    private void initDownloadData(){
        downloadInfoList = downloadManager.getDownloadInfoList();
        downloadingInfoList=new ArrayList<>();
        for (int i = 0,len=downloadInfoList.size(); i < len; i++) {
            if(downloadInfoList.get(i).getState() != HttpHandler.State.SUCCESS){
                downloadingInfoList.add(downloadInfoList.get(i));
            }
        }
        downloadAdapter =new DownloadAdapter(this,downloadingInfoList,downloadManager);
        download_listview.setAdapter(downloadAdapter);
    }

    private void initDownloadCompleateData(){
        downloadCompleteInfoList = DownloadDB.getDownInfoDB(this).queryAll();
        downloadCompleteAdapter=new DownloadCompleteAdapter(this,downloadCompleteInfoList);
        download_listview.setAdapter(downloadCompleteAdapter);
    }

    @Override
    public void initLisenter() {
        nav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        download_notrans_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                initDownloadData();
                download_notrans_tv.setTextColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.blue));
                download_notrans_view.setBackgroundColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.blue));
                download_transed_tv.setTextColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.black));
                download_transed_view.setBackgroundColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.gray));
            }
        });
        download_transed_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                initDownloadCompleateData();
                download_notrans_tv.setTextColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.black));
                download_notrans_view.setBackgroundColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.gray));
                download_transed_tv.setTextColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.blue));
                download_transed_view.setBackgroundColor(ContextCompat.getColor(DownloadCenterActivity.this,R.color.blue));
            }
        });
        download_listview.setPullRefreshEnable(false);
        download_listview.setPullLoadEnable(false);

    }


}
