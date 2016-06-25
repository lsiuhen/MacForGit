package com.nepo.nepotransfered.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.TransferItemActivity;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.model.TransferTypeModel;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.LoadingIndicator.AVLoadingIndicatorView;
import com.nepo.nepotransfered.view.dragview.BaseDragAdapter;
import com.nepo.nepotransfered.view.dragview.BaseItem;
import com.nepo.nepotransfered.view.dragview.ListToJson;
import com.nepo.nepotransfered.view.dragview.ProvinceItem;

import java.util.List;

/**
 * Created by lsiuhen on 2016/6/3.
 * 传输类型 adapter
 */
public class TransferTypeAdapter extends BaseDragAdapter {

    private Context context;
    private int dropPosition = -1;
    private List<TransferTypeModel> transferTypeModels;
    private TransferTypeModel selectItem;
    private String type;

    public TransferTypeAdapter(Context context,List<TransferTypeModel> transferTypeModels,String type){
        this.context = context;
        this.transferTypeModels = transferTypeModels;
        selectItem = transferTypeModels.get(0);
        this.type=type;
    }

    public void refresh(List<TransferTypeModel> transferTypeModels){
        this.transferTypeModels=transferTypeModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return transferTypeModels == null ? 0 : transferTypeModels.size();
    }

    @Override
    public TransferTypeModel getItem(int position) {
        if (null != transferTypeModels && transferTypeModels.size() != 0){
            return transferTypeModels.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_transfer_type,parent,false);
        TextView textView = (TextView) view.findViewById(R.id.title_tv);
        AVLoadingIndicatorView tip = (AVLoadingIndicatorView) view.findViewById(R.id.tip_view);

        final TransferTypeModel item = transferTypeModels.get(position);
        textView.setText(item.getTransferListLength()+"");
        if (dropPosition == position){
            view.setVisibility(View.GONE);
        }

        view.setBackgroundResource(item.getTransferBgResourceId());
        view.setTag(item);

        if(!TextUtils.isEmpty(type)){
            if(item.getTypeName().equals(type)){
                tip.setVisibility(View.VISIBLE);
            }else{
                tip.setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public void addItem(BaseItem item) {
//        provinceList.add((ProvinceItem) item);
//        notifyDataSetChanged();
    }

    @Override
    public void exchange(int dragPosition, int dropPosition) {
        // TODO: 16-3-22 互换位置 【可用代码】
        this.dropPosition = dropPosition;
        TransferTypeModel dragItem = getItem(dragPosition);
        if (dragPosition < dropPosition) {
            transferTypeModels.add(dropPosition + 1, dragItem);
            transferTypeModels.remove(dragPosition);
        } else {
            transferTypeModels.add(dropPosition, dragItem);
            transferTypeModels.remove(dragPosition + 1);
        }
        //保存交换好的数据；到数据库
        TransferDB.getTransferDB(context).insertTransTypeBySortList(transferTypeModels);

        notifyDataSetChanged();
    }

    @Override
    public void removeItem(BaseItem item) {
//        if (provinceList.contains(item)){
//            provinceList.remove(item);
//            dropPosition = -1;
//            notifyDataSetChanged();
//        }
    }

    @Override
    public void removePosition(int position) {
//        if (position >=0 && position<provinceList.size()){
//            provinceList.remove(position);
//            mEditor.putString(Constant.PROVINCE, ListToJson.toJson(provinceList).toString());
//            mEditor.commit();
//            notifyDataSetChanged();
//        }
    }

    @Override
    public void dragEnd() {
// TODO: 16-3-26 拖动完成的回调【可用代码】
        int position = 0;
        for (int i = 0; i < transferTypeModels.size(); i++) {
            if (selectItem.getTypeId()==transferTypeModels.get(i).getTypeId()){
                position = i;
                break;
            }
        }

        this.dropPosition = -1;
        if (null != listener){
            listener.exchangeOtherAdapter(transferTypeModels, position);
        }
        notifyDataSetChanged();
    }

    private changeListener listener;

    public void setListener(changeListener listener){
        this.listener = listener;
    }

    public interface changeListener{

         void exchangeOtherAdapter(List<TransferTypeModel> data,int position);

         void setCurrentPosition();
    }

}
