package com.nepo.nepotransfered;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nepo.nepotransfered.utils.ActivityManager;
import com.nepo.nepotransfered.utils.SystemBarTintUtils;

/**
 * Created by lsiuhen on 2016/5/26.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
    }

    public abstract void initView();
    
    public abstract void initData();

    public abstract void initLisenter();

    public void openActivity(Context mContext,Class clazz){
        Intent intent=new Intent(mContext,clazz);
        startActivity(intent);
    }

    /**
     * 沉浸式导航
     * @param on
     */
    public void isImmersion(boolean on){
        if(on){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }
            SystemBarTintUtils tintManager = new SystemBarTintUtils(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setStatusBarTintResource(R.color.blue);
        }
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static final View.OnTouchListener TouchDark = new View.OnTouchListener() {

        public final float[] BT_SELECTED = new float[] {1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};
        public final float[] BT_NOT_SELECTED = new float[] {1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};

        @SuppressLint("NewApi")
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if (event.getAction()==MotionEvent.ACTION_DOWN){

//                v.getBackground().setColorFilter(
//                        new ColorMatrixColorFilter(BT_SELECTED));
//                 v.setBackgroundResource(R.drawable.home_myapp);
                v.setBackgroundColor(Color.parseColor("#22000000"));
            }else if(event.getAction()==MotionEvent.ACTION_UP){
//                v.getBackground().setColorFilter(
//                        new ColorMatrixColorFilter(BT_NOT_SELECTED));
                // v.setBackgroundResource(R.drawable.home_myapp);
                v.getBackground().setAlpha(1);
            }

            return false;
        }
    };

    public static final View.OnTouchListener TouchDarkMedia = new View.OnTouchListener() {

        public final float[] BT_SELECTED = new float[] {1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};
        public final float[] BT_NOT_SELECTED = new float[] {1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};

        @SuppressLint("NewApi")
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if (event.getAction()==MotionEvent.ACTION_DOWN){

                v.getBackground().setColorFilter(
                        new ColorMatrixColorFilter(BT_SELECTED));
                // v.setBackgroundResource(R.drawable.home_myapp);

            }else if(event.getAction()==MotionEvent.ACTION_UP){
                v.getBackground().setColorFilter(
                        new ColorMatrixColorFilter(BT_NOT_SELECTED));
                // v.setBackgroundResource(R.drawable.home_myapp);
            }



            return false;
        }
    };


}
