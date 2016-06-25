package com.nepo.nepotransfered.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.net.ImageLoaders;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.AutoLayout.SquareLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class MediaImageItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mediaImageInfoList;
    private LayoutInflater mInflater;

    /**
     * 存储每个条目勾选的状态
     */
    private SparseBooleanArray mCheckedStates = null;

    private Boolean isVisibale = false;

    /**
     * 记录每个子项的高度。
     */
    private int mItemHeight = 0;

    private OnImageItemCheckedLisenter checkLisenter;


    public void setCheckLisenter(OnImageItemCheckedLisenter checkLisenter) {
        this.checkLisenter = checkLisenter;
    }

    public Boolean getIsVisibale() {
        return isVisibale;
    }

    public void setIsVisibale(Boolean isVisibale) {
        this.isVisibale = isVisibale;
        notifyDataSetChanged();
    }

    public MediaImageItemAdapter(Context mContext, ArrayList<String> mediaImageInfoList) {
        this.mContext = mContext;
        this.mediaImageInfoList = mediaImageInfoList;
        mInflater = LayoutInflater.from(mContext);

        mCheckedStates = new SparseBooleanArray();
        if (mediaImageInfoList != null) {
            // 初始状态时所有条目都不勾选
            for (int i = 0; i < mediaImageInfoList.size(); i++) {
                mCheckedStates.put(i, false);
            }
        }

    }

    /**
     * 全选或全不选
     *
     * @param selectAll true表示全选,false表示全不选
     */
    public void selectAllItem(boolean selectAll) {
        if (selectAll) {
            for (int i = 0; i < mediaImageInfoList.size(); i++) {
                mCheckedStates.put(i, true);
            }
        } else {
            for (int i = 0; i < mediaImageInfoList.size(); i++) {
                mCheckedStates.put(i, false);
            }
        }
        notifyDataSetChanged();
        checkLisenter.checkImage();


    }

    /**
     * 改变指定位置条目的选择状态，如果已经处于勾选状态则取消勾选，如果处于没有勾选则勾选
     *
     * @param position 要改变的条目选择状态的位置
     */
    public void toggleCheckedState(int position) {
        if (position >= 0 && position < mCheckedStates.size()) {
            if (mCheckedStates.get(position)) {
                mCheckedStates.put(position, false);
            } else {
                mCheckedStates.put(position, true);
            }
            notifyDataSetChanged();
        }
    }


    public String[] getSelectedAudioPaths() {
        int[] checkedPostions = getSelectedItemPositions();
        String[] selectedAudioPaths = new String[checkedPostions.length];
        for (int i = 0; i < checkedPostions.length; i++) {
            selectedAudioPaths[i] = getItem(checkedPostions[i]).toString();
        }
        return selectedAudioPaths;
    }


    /**
     * 获得已选择的条目们在列表中的位置
     *
     * @return 所有已选择的条目在列表中的位置
     */
    public int[] getSelectedItemPositions() {
        int count = 0;
        for (int i = 0; i < mCheckedStates.size(); i++) {
            if (mCheckedStates.get(i)) {
                count++;
            }
        }
        int[] checkedPostions = new int[count];
        for (int i = 0, j = 0; i < mCheckedStates.size(); i++) {
            if (mCheckedStates.get(i)) {
                checkedPostions[j] = i;
                j++;
            }
        }
        return checkedPostions;
    }

    /**
     * 设置item子项的高度。
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mediaImageInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaImageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_media_image_item, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_media_image_item_rl = (LinearLayout) convertView.findViewById(R.id.item_media_image_item_rl);
        viewHolder.item_media_image_imageimg_iv = (ImageView) convertView.findViewById(R.id.item_media_image_item_imageimg_iv);
        viewHolder.item_media_image_check = (CheckBox) convertView.findViewById(R.id.item_media_image_item_check);

        // String path = mediaImageInfoList.get(position);

        // 加载图片
        //viewHolder.item_media_image_imageimg_iv.setTag(path);

        try {

            ImageLoaders.setsendimg("file://" + new File(mediaImageInfoList.get(position)).getAbsolutePath(), viewHolder.item_media_image_imageimg_iv);

        } catch (Exception e) {
            Utils.Sout("imgex", e);
            e.printStackTrace();
        }

        viewHolder.item_media_image_imageimg_iv.setColorFilter(null);

        // if(isVisibale){
        if (mCheckedStates.get(position)) {
            viewHolder.item_media_image_check.setChecked(true);
            viewHolder.item_media_image_imageimg_iv.setColorFilter(Color.parseColor("#77000000"));
        } else {
            viewHolder.item_media_image_check.setChecked(false);
            viewHolder.item_media_image_imageimg_iv.setColorFilter(null);
        }
        // }


        viewHolder.item_media_image_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckedState(position);

                checkLisenter.checkImage();

            }
        });

        return convertView;
    }


    class ViewHolder {
        LinearLayout item_media_image_item_rl;
        ImageView item_media_image_imageimg_iv;
        CheckBox item_media_image_check;


    }

    public interface OnImageItemCheckedLisenter {
        void checkImage();
    }


}
