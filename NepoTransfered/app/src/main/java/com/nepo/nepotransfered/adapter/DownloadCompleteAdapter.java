package com.nepo.nepotransfered.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.model.AppDownInfoModel;

import java.util.List;

/**
 * Created by lsiuhen on 2016/5/30.
 */
public class DownloadCompleteAdapter extends BaseAdapter {

    private List<AppDownInfoModel> list;
    private Context mContext;
    private LayoutInflater mInflater;

    public DownloadCompleteAdapter(Context mContext, List<AppDownInfoModel> list) {
        this.list = list;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }

    }

    @Override
    public AppDownInfoModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_downloadlist_notransfer_item, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item_download_notransfer_control_del_iv= (ImageView) convertView.findViewById(R.id.item_download_notransfer_content_del_iv);
        viewHolder.item_download_notransfer_content_status_tv= (TextView) convertView.findViewById(R.id.item_download_notransfer_content_status_tv);
        viewHolder.item_download_notransfer_content_name_tv= (TextView) convertView.findViewById(R.id.item_download_notransfer_content_name_tv);
        viewHolder.item_download_notransfer_content_name_tv.setText(list.get(position).getFileName());



        return convertView;
    }

    class ViewHolder {
        ImageView item_download_notransfer_control_del_iv;
        TextView item_download_notransfer_content_name_tv;
        TextView item_download_notransfer_content_status_tv;
    }


}
