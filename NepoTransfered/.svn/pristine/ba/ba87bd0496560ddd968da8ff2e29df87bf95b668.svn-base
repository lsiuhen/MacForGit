package com.nepo.nepotransfered.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.nepo.nepotransfered.R;


/**
 * Created by lsiuhen on 2016/5/30.
 */
public abstract class BaseFragment extends Fragment {

    public abstract void initView(View view);

    public abstract void initData();

    public abstract void initLisenter();

    // 进度球进度更新
    public final int MSG_FRAGMENTPROGRESS=0;
    // 传输完成更新当前界面数字
    public final int MSG_FRAGMENTCOMPLETE=1;

    /**
     * 显示进度球中数字
     * @param size
     * @param transfer_num1
     * @param transfer_num2
     * @param transfer_num3
     */
    public void showImgByNumber(int size,ImageView transfer_num1,ImageView transfer_num2,ImageView transfer_num3){
        String number = String.valueOf(size);

        if (number.length() == 3) {
            // 3位数
            transfer_num1.setVisibility(View.VISIBLE);
            transfer_num2.setVisibility(View.VISIBLE);
            transfer_num3.setVisibility(View.VISIBLE);

            String one = number.substring(0, 1);
            changePicByNumber(one, transfer_num1);
            String two = number.substring(1, 2);
            changePicByNumber(two, transfer_num2);
            String three = number.substring(2, number.length());
            changePicByNumber(three, transfer_num3);

        } else if (number.length() == 2) {
            // 2位数

            transfer_num1.setVisibility(View.VISIBLE);
            transfer_num2.setVisibility(View.VISIBLE);
            transfer_num3.setVisibility(View.GONE);

            String one = number.substring(0, 1);
            changePicByNumber(one, transfer_num1);
            String two = number.substring(1, number.length());
            changePicByNumber(two, transfer_num2);

        } else {
            // 1位数
            transfer_num1.setVisibility(View.VISIBLE);
            transfer_num2.setVisibility(View.GONE);
            transfer_num3.setVisibility(View.GONE);

            String one = number.substring(0, number.length());
            changePicByNumber(one, transfer_num1);

        }
    }
    private void changePicByNumber(String numb, ImageView view) {
        switch (numb) {
            case "0":
                view.setImageResource(R.drawable.trans_zero);
                break;
            case "1":
                view.setImageResource(R.drawable.trans_one);
                break;
            case "2":
                view.setImageResource(R.drawable.trans_two);
                break;
            case "3":
                view.setImageResource(R.drawable.trans_three);
                break;
            case "4":
                view.setImageResource(R.drawable.trans_four);
                break;
            case "5":
                view.setImageResource(R.drawable.trans_five);
                break;
            case "6":
                view.setImageResource(R.drawable.trans_six);
                break;
            case "7":
                view.setImageResource(R.drawable.trans_serven);
                break;
            case "8":
                view.setImageResource(R.drawable.trans_eight);
                break;
            case "9":
                view.setImageResource(R.drawable.trans_nine);
                break;
        }
    }

}
