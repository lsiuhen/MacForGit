package com.nepo.nepotransfered.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.model.AppInfosModel;
import com.nepo.nepotransfered.net.AsyncImageLoader;
import com.nepo.nepotransfered.utils.Utils;

import java.util.List;

/**
 * Created by lsiuhen on 2016/5/30.
 */
public class MyappListAdapter extends BaseAdapter {


    private Context mContext;
    private List<AppInfosModel> appInfoList;
    private LayoutInflater mInflater;
    public AsyncImageLoader asyncImageLoaders;
    private downClickLisenter downClickLisenter;
    private AppInfosModel entity;

    public void setEntity(AppInfosModel entitiy) {
        this.entity = entitiy;
        if (entitiy != null) {
            for (int i = 0; i < appInfoList.size(); i++) {
                if (entitiy.getDownId().equals(appInfoList.get(i).getDownId())) {
                    appInfoList.get(i).setDownProgress(entitiy.getDownProgress());
                }
            }
            notifyDataSetChanged();
        }
    }

    public void setDownClickLisenter(MyappListAdapter.downClickLisenter downClickLisenter) {
        this.downClickLisenter = downClickLisenter;
    }


    public MyappListAdapter(Context mContext, List<AppInfosModel> appInfoList) {
        this.mContext = mContext;
        this.appInfoList = appInfoList;
        asyncImageLoaders = new AsyncImageLoader();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (appInfoList != null) {
            return appInfoList.size();
        } else {
            return 0;
        }

    }

    @Override
    public AppInfosModel getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_myapplist, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_myapplist_iv = (ImageView) convertView.findViewById(R.id.item_myapplist_iv);
        viewHolder.item_myapplist_tv = (TextView) convertView.findViewById(R.id.item_myapplist_tv);
        viewHolder.item_myapplist_size_tv = (TextView) convertView.findViewById(R.id.item_myapplist_size_tv);
        viewHolder.item_myapplist_down_complate_rl = (RelativeLayout) convertView.findViewById(R.id.item_myapplist_down_complate_rl);
        viewHolder.item_myapplist_down_complate_tv_rl = (TextView) convertView.findViewById(R.id.item_myapplist_down_complate_tv_rl);
        viewHolder.item_myapplist_down_loading_rl = (RelativeLayout) convertView.findViewById(R.id.item_myapplist_down_loading_rl);
        viewHolder.item_myapplist_down_progressb = (ProgressBar) convertView.findViewById(R.id.item_myapplist_down_progressb);
        viewHolder.item_myapplist_down_tv = (TextView) convertView.findViewById(R.id.item_myapplist_down_tv);

        viewHolder.item_myapplist_tv.setText(getItem(position).getAppName());
        viewHolder.item_myapplist_size_tv.setText(getItem(position).getAppFileSize());
        viewHolder.item_myapplist_down_progressb.setProgress(getItem(position).getDownProgress());

        final ViewHolder finalViewHolder = viewHolder;
        asyncImageLoaders.loadDrawable(Integer.valueOf(Utils.subStr(getItem(position).getAppIconUrl())), getItem(position).getAppIconUrl(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void onLoaded(int viewId, Drawable drawable) {
                finalViewHolder.item_myapplist_iv.setBackground(drawable);
            }

            @Override
            public void onError(int viewId, Exception e) {

            }
        });

        if (getItem(position).getDownProgress() >= 100) {
            viewHolder.item_myapplist_down_loading_rl.setVisibility(View.GONE);
            viewHolder.item_myapplist_down_complate_rl.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_myapplist_down_loading_rl.setVisibility(View.VISIBLE);
            viewHolder.item_myapplist_down_complate_rl.setVisibility(View.GONE);
        }


//        if (getItem(position).getDownStatus() == Constant.STATE_DOWNLOADED) {
//            viewHolder.item_myapplist_down_loading_rl.setVisibility(View.GONE);
//            viewHolder.item_myapplist_down_complate_rl.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.item_myapplist_down_loading_rl.setVisibility(View.VISIBLE);
//            viewHolder.item_myapplist_down_complate_rl.setVisibility(View.GONE);
//        }

        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.item_myapplist_down_loading_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder1.item_myapplist_down_tv.setText("等待中");
                downClickLisenter.downClick(getItem(position));
            }
        });


        return convertView;
    }

    public interface downClickLisenter {
        void downClick(AppInfosModel appInfos);
    }


    class ViewHolder {
        ImageView item_myapplist_iv;
        TextView item_myapplist_tv;
        TextView item_myapplist_size_tv;
        // 下载完成显示/隐藏
        RelativeLayout item_myapplist_down_complate_rl;
        TextView item_myapplist_down_complate_tv_rl;
        // 下载中显示/隐藏
        RelativeLayout item_myapplist_down_loading_rl;
        TextView item_myapplist_down_tv;
        ProgressBar item_myapplist_down_progressb;

    }


}
