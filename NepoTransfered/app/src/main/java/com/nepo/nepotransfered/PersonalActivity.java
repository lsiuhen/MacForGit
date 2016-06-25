package com.nepo.nepotransfered;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nepo.nepotransfered.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lsiuhen on 2016/6/1.
 * 个人中心
 */
public class PersonalActivity extends BaseActivity {

    /**
     * 视图
     * @param savedInstanceState
     */
    private RelativeLayout nav_ll;
    private CircleImageView personeal_personimg_iv;
    private TextView personal_platenumber_tv;
    private TextView personeal_km_tv;
    private TextView personeal_day_km_tv;
    private TextView personeal_avg_km_tv;
    private TextView personal_list_insurance_tv;
    private TextView personal_list_update_tv;
    private RelativeLayout personal_list_insurance_rl;
    private RelativeLayout personal_list_search_rl;
    private RelativeLayout personal_list_maintenance_rl;
    private RelativeLayout personal_list_update_rl;
    private RelativeLayout personal_list_about_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        // 沉浸式
        isImmersion(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//        SystemBarTintUtils tintManager = new SystemBarTintUtils(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setNavigationBarTintEnabled(false);
//        tintManager.setStatusBarTintResource(R.color.green);
        // 布局控件
        nav_ll= (RelativeLayout) findViewById(R.id.nav_ll);
        personal_list_insurance_rl= (RelativeLayout) findViewById(R.id.personal_list_insurance_rl);
        personal_list_search_rl= (RelativeLayout) findViewById(R.id.personal_list_search_rl);
        personal_list_maintenance_rl= (RelativeLayout) findViewById(R.id.personal_list_maintenance_rl);
        personal_list_update_rl= (RelativeLayout) findViewById(R.id.personal_list_update_rl);
        personal_list_about_rl= (RelativeLayout) findViewById(R.id.personal_list_about_rl);
        personeal_personimg_iv= (CircleImageView) findViewById(R.id.personeal_personimg_iv);
        personal_platenumber_tv= (TextView) findViewById(R.id.personal_platenumber_tv);
        personeal_km_tv= (TextView) findViewById(R.id.personeal_km_tv);
        personeal_day_km_tv= (TextView) findViewById(R.id.personeal_day_km_tv);
        personeal_avg_km_tv= (TextView) findViewById(R.id.personeal_avg_km_tv);
        personal_list_insurance_tv= (TextView) findViewById(R.id.personal_list_insurance_tv);
        personal_list_update_tv= (TextView) findViewById(R.id.personal_list_update_tv);

    }

    @Override
    public void initData() {
        String versionCode = Utils.getVersionCode(this);
        if(versionCode!=null){
            personal_list_update_tv.setText(versionCode);
        }
        String picurl = SPUtils.get(this, Constant.SP_PICURL, "").toString();
        String plateNumber=SPUtils.get(this,Constant.SP_PLATEOPENNUMBER,"").toString();
        personal_platenumber_tv.setText(plateNumber);
        if (!TextUtils.isEmpty(picurl)) {
            Picasso.with(this)
                    .load("file://" + new File(picurl))
                    .placeholder(R.drawable.personal_top_head)
                    .error(R.drawable.personal_top_head)
                    .into(personeal_personimg_iv);
        }else{
            personeal_personimg_iv.setBackgroundResource(R.drawable.personal_top_head);
        }
    }

    @Override
    public void initLisenter() {
        nav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        personeal_personimg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PersonalActivity.this,PersonalDetailActivity.class);
            }
        });
        personal_list_insurance_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PersonalActivity.this,PersonalDetailActivity.class);
            }
        });
        personal_list_search_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PersonalActivity.this,PersonalDetailActivity.class);
            }
        });
        personal_list_maintenance_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PersonalActivity.this,PersonalDetailActivity.class);
            }
        });
        personal_list_update_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PersonalActivity.this, PersonalDetailActivity.class);
            }
        });
        personal_list_about_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PersonalActivity.this, PersonalDetailActivity.class);
            }
        });
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

}