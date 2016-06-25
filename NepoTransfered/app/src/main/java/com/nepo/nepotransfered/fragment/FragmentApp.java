package com.nepo.nepotransfered.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.view.circleview.SeekCircle;

/**
 * Created by lsiuhen on 2016/5/30.
 *
 * 车载应用
 *
 */
public class FragmentApp extends BaseFragment {

    /**
     * 视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    private SeekCircle fragment_app_seekCircle;
    private ImageView transfer_num1;
    private ImageView transfer_num2;
    private ImageView transfer_num3;
    private TextView transfer_status_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, null);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        fragment_app_seekCircle= (SeekCircle) view.findViewById(R.id.fragment_app_seekCircle);
        transfer_num1= (ImageView) view.findViewById(R.id.transfer_num1);
        transfer_num2= (ImageView) view.findViewById(R.id.transfer_num2);
        transfer_num3= (ImageView) view.findViewById(R.id.transfer_num3);
        transfer_status_tv= (TextView) view.findViewById(R.id.transfer_status_tv);

        fragment_app_seekCircle.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initLisenter() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //System.out.println("-- app resume");

    }

    @Override
    public void onStart() {
        super.onStart();
        //System.out.println("-- app start");
    }

}
