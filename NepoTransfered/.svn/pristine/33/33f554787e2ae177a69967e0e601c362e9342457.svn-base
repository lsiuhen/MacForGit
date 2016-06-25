package com.nepo.nepotransfered.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.net.download.BaseDownloadHolder;
import com.nepo.nepotransfered.net.download.DownloadInfo;
import com.nepo.nepotransfered.net.download.DownloadManager;
import com.nepo.nepotransfered.net.download.DownloadRequestCallBack;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lsiuhen on 2016/5/30.
 */
public class DownloadAdapter extends BaseAdapter {

    private List<DownloadInfo> downloadInfoList;
    private Context mContext;
    private LayoutInflater mInflater;
    private DownloadManager downloadManager;

    public DownloadAdapter(Context mContext, List<DownloadInfo> downloadInfoList, DownloadManager downloadManager) {
        this.downloadInfoList = downloadInfoList;
        this.mContext = mContext;
        this.downloadManager = downloadManager;
        mInflater = LayoutInflater.from(mContext);
    }


    public void setData(List<DownloadInfo> list){
        downloadInfoList.clear();
        downloadInfoList.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (downloadInfoList != null) {
            return downloadInfoList.size();
        } else {
            return 0;
        }

    }

    @Override
    public DownloadInfo getItem(int position) {
        return downloadInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BaseDownloadHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_downloadlist_item, null);
            holder = new ViewHodler();
            convertView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }

        ((ViewHodler) holder).item_download_content_name_tv = (TextView) convertView.findViewById(R.id.item_download_content_name_tv);
        ((ViewHodler) holder).item_download_content_status_tv = (TextView) convertView.findViewById(R.id.item_download_content_status_tv);
        ((ViewHodler) holder).item_download_content_pb = (ProgressBar) convertView.findViewById(R.id.item_download_content_pb);
        ((ViewHodler) holder).item_download_control_pause_iv = (ImageView) convertView.findViewById(R.id.item_download_control_pause_iv);
        ((ViewHodler) holder).item_download_control_del_iv = (ImageView) convertView.findViewById(R.id.item_download_control_del_iv);

        final DownloadInfo downloadInfo = downloadInfoList.get(position);
        if (downloadInfo.getState() != HttpHandler.State.SUCCESS) {
            holder.update(downloadInfo);
            setHolderData((ViewHodler) holder, downloadInfo, downloadManager);
        }


        return convertView;
    }


    /**
     * 为holder设置相关参数
     *
     * @param holder
     * @param downloadInfo
     */
    private void setHolderData(final ViewHodler holder, final DownloadInfo downloadInfo, final DownloadManager downloadManager) {
        if (downloadInfo != null) {
            holder.item_download_content_name_tv.setText(downloadInfo.getFileName());
            holder.item_download_content_status_tv.setText(downloadInfo.getState() + "");
            //暂停、继续、重试按钮点击事件
            holder.item_download_control_pause_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (downloadInfo.getState() == HttpHandler.State.CANCELLED || downloadInfo.getState() == HttpHandler.State.FAILURE) {
                            downloadManager.resumeDownload(downloadInfo, new DownloadRequestCallBack());
                            holder.item_download_control_pause_iv.setImageResource(R.drawable.trans_item_pause);
                        } else if (downloadInfo.getState() == HttpHandler.State.LOADING) {
                            downloadManager.stopDownload(downloadInfo);
                            holder.item_download_control_pause_iv.setImageResource(R.drawable.trans_item_start);
                        }
                        //更新Listview数据
                        // initData();
                        notifyDataSetChanged();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });
            //取消按钮点击事件
            holder.item_download_control_del_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        downloadManager.removeDownload(downloadInfo);
                        File file = new File(downloadInfo.getFileSavePath());
                        if (file.exists()) {
                            file.delete();
                        }
                        //更新Listview数据
                        //initData();
                        notifyDataSetChanged();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //*****************设置更新进度回调**********************
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null) {
            DownloadManager.ManagerCallBack requestCallBack = (DownloadManager.ManagerCallBack) handler.getRequestCallBack();
            if (requestCallBack.getBaseCallBack() == null) {
                requestCallBack.setBaseCallBack(new DownloadRequestCallBack());
            }
            requestCallBack.setUserTag(new WeakReference<>(holder));
        }
    }

    //下载item hodler
    private class ViewHodler extends BaseDownloadHolder {
        public TextView item_download_content_name_tv;//应用名称
        public TextView item_download_content_status_tv;//状态
        public ProgressBar item_download_content_pb;//进度条
        public ImageView item_download_control_pause_iv;//取消按钮
        public ImageView item_download_control_del_iv;//暂停继续重试按钮

        @Override
        public void refresh() {
            //更新下载进度
            if (downloadInfo != null) {
                if (downloadInfo.getFileLength() > 0) {
                    //根据当前下载大小和APP大小计算出的进度百分比
                    int prosress = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
                    if (prosress >= 100) {
                        item_download_content_pb.setProgress(100);
                        if(item_download_content_pb.getProgress()>=100){
                            downloadInfoList.remove(downloadInfo);
                        }
                        notifyDataSetChanged();
                    } else {
                        item_download_content_pb.setProgress(prosress);
                        item_download_content_status_tv.setText(downloadInfo.getState() + "");
                    }
                } else {
                    //APP大小为0时（一般不会遇到除非数据出错）
                    item_download_content_pb.setProgress(0);
                }
            }
        }

    }


}
