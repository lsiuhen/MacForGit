package com.nepo.nepotransfered.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.model.MediaMusicModel;

import java.util.List;

/**
 * Created by lsiuhen on 2016/3/10.
 */
public class MediaMusicAdapter extends BaseAdapter {


    private Context mContext;
    private List<MediaMusicModel> mediaMusicInfoList;
    private LayoutInflater mInflater;

    private int ISTRANSED;

    /**
     * 存储每个条目勾选的状态
     */
    private SparseBooleanArray mCheckedStates = null;

    private Boolean isVisibale = false;

    private OnMusicCheckedLisenter checkLisenter;

    public void setCheckLisenter(OnMusicCheckedLisenter checkLisenter) {
        this.checkLisenter = checkLisenter;
    }

    public Boolean getIsVisibale() {
        return isVisibale;
    }

    public void setIsVisibale(Boolean isVisibale) {
        this.isVisibale = isVisibale;
        notifyDataSetChanged();
    }

    public MediaMusicAdapter(Context mContext, List<MediaMusicModel> mediaMusicInfoList, int ISTRANSED) {
        this.mContext = mContext;
        this.mediaMusicInfoList = mediaMusicInfoList;
        this.ISTRANSED = ISTRANSED;
        mInflater = LayoutInflater.from(mContext);

        mCheckedStates = new SparseBooleanArray();
        if (mediaMusicInfoList != null) {
            // 初始状态时所有条目都不勾选
            for (int i = 0; i < mediaMusicInfoList.size(); i++) {
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
            for (int i = 0; i < mediaMusicInfoList.size(); i++) {
                mCheckedStates.put(i, true);
            }
        } else {
            for (int i = 0; i < mediaMusicInfoList.size(); i++) {
                mCheckedStates.put(i, false);
            }
        }
        notifyDataSetChanged();
        checkLisenter.checkMusic();


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


    /**
     * 获取选中的item 数据
     * @return
     */
    public MediaMusicModel[] getSelectedAudioPaths() {
        int[] checkedPostions = getSelectedItemPositions();
        MediaMusicModel[] selectedAudioPaths = new MediaMusicModel[checkedPostions.length];
        for (int i = 0; i < checkedPostions.length; i++) {
            selectedAudioPaths[i] = getItem(checkedPostions[i]);
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


    @Override
    public int getCount() {
        return mediaMusicInfoList.size();
    }

    @Override
    public MediaMusicModel getItem(int position) {
        return mediaMusicInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_media_music, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item_media_music_musicimg_iv = (ImageView) convertView.findViewById(R.id.item_media_music_musicimg_iv);
        viewHolder.item_media_music_check = (CheckBox) convertView.findViewById(R.id.item_media_music_check);
        viewHolder.item_media_music_songname_tv = (TextView) convertView.findViewById(R.id.item_media_music_songname_tv);
        viewHolder.item_media_music_artist_tv = (TextView) convertView.findViewById(R.id.item_media_music_artist_tv);
        viewHolder.item_media_music_check_ll = (LinearLayout) convertView.findViewById(R.id.item_media_music_check_ll);


        viewHolder.item_media_music_songname_tv.setText(mediaMusicInfoList.get(position).getName());
        viewHolder.item_media_music_artist_tv.setText(mediaMusicInfoList.get(position).getArtist());
        viewHolder.item_media_music_musicimg_iv.setImageBitmap(mediaMusicInfoList.get(position).getBitmap());

        if (ISTRANSED==0){
            viewHolder.item_media_music_check.setVisibility(View.VISIBLE);
        }else{
            viewHolder.item_media_music_check.setVisibility(View.GONE);
        }


        // if(isVisibale){
        if (mCheckedStates.get(position)) {
            viewHolder.item_media_music_check.setChecked(true);
        } else {
            viewHolder.item_media_music_check.setChecked(false);
        }
        // }


        viewHolder.item_media_music_check_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckedState(position);
                checkLisenter.checkMusic();

            }
        });

        return convertView;
    }


    class ViewHolder {
        ImageView item_media_music_musicimg_iv;
        TextView item_media_music_songname_tv;
        TextView item_media_music_artist_tv;
        CheckBox item_media_music_check;
        LinearLayout item_media_music_check_ll;


    }


    public interface OnMusicCheckedLisenter {
        void checkMusic();
    }


}
