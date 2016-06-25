package com.nepo.nepotransfered.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.model.MediaImgModel;
import com.nepo.nepotransfered.net.ImageLoaders;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by lsiuhen on 2016/3/10.
 */
public class MediaImageAdapter extends BaseAdapter {


    private Context mContext;
    private List<MediaImgModel> mediaImageInfoList;
    private LayoutInflater mInflater;
    /**
     * 容器
     */
    private View mContainer = null;


    public MediaImageAdapter(Context mContext, List<MediaImgModel> mediaImageInfoList, View container) {
        this.mContext = mContext;
        this.mediaImageInfoList = mediaImageInfoList;
        mInflater = LayoutInflater.from(mContext);
        mContainer = container;


    }

    @Override
    public int getCount() {
        return mediaImageInfoList.size();
    }

    @Override
    public MediaImgModel getItem(int position) {
        if (position < 0 || position > mediaImageInfoList.size()) {
            return null;
        }

        return mediaImageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_media_image, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_media_image_ll = (LinearLayout) convertView.findViewById(R.id.item_media_image_ll);
        viewHolder.item_media_image_imageimg_iv = (ImageView) convertView.findViewById(R.id.item_media_image_imageimg_iv);
        viewHolder.item_media_image_imagename_tv = (TextView) convertView.findViewById(R.id.item_media_image_imagename_tv);
        viewHolder.item_media_image_size_tv = (TextView) convertView.findViewById(R.id.item_media_image_size_tv);

//        viewHolder.item_media_image_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO 跳转 子页面
////                Intent view = new Intent(mContext, MediaPicItemActivity.class);
////                view.putExtra("imgDirInfo", (Serializable) mediaImageInfoList.get(position));
////                mContext.startActivity(view);
//
//            }
//        });


        MediaImgModel item = getItem(position);
        if (item != null) {
            // 图片路径
            String path = item.getFirstImgPath();
            // 标题
            viewHolder.item_media_image_imagename_tv.setText(item.getDirName());
            // 计数
            viewHolder.item_media_image_size_tv.setText(mContext.getString(R.string.image_count, item.getImageCount()));
            viewHolder.item_media_image_imageimg_iv.setTag(path);
            // 加载图片

            //Picasso.with(mContext).load("file://"+new File(path).getAbsolutePath()).resize(60,60).placeholder(R.drawable.list_img_default_s).error(R.drawable.list_img_default_s).into(viewHolder.item_media_image_imageimg_iv);
            ImageLoaders.setsendimg("file://" + new File(path).getAbsolutePath(), viewHolder.item_media_image_imageimg_iv);

        }
        return convertView;
    }


    class ViewHolder {
        LinearLayout item_media_image_ll;
        ImageView item_media_image_imageimg_iv;
        TextView item_media_image_imagename_tv;
        TextView item_media_image_size_tv;


    }


}
