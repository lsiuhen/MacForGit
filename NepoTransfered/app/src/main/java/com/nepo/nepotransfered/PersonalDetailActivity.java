package com.nepo.nepotransfered;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.utils.PictureChooser;
import com.nepo.nepotransfered.utils.SPUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lsiuhen on 2016/6/2.
 * 个人信息详情
 */
public class PersonalDetailActivity extends BaseActivity {

    /**
     * 视图
     *
     * @param savedInstanceState
     */
    private RelativeLayout nav_ll;
    private RelativeLayout car_list_cartype_rl;
    private RelativeLayout personal_list_platenumber_open_rl;
    private RelativeLayout car_list_address_rl;
    private CircleImageView personal_list_carimg_iv;
    private TextView personal_list_cartype_tv;
    private TextView personal_list_platenumber_open_tv;
    private EditText personal_list_platenumber_et;
    private EditText personal_list_framenumber_et;
    private EditText personal_list_enginenumber_et;
    private TextView personal_list_address_tv;
    private Button personal_detail_save_btn;

    /**
     * 数据
     *
     * @param savedInstanceState
     */
    private String photoPath = null; //记录头像地址，要上传的时候可以将地址读成文件上传
    private PictureChooser pictureChooser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        initView();
        initLisenter();
        initData();
    }

    @Override
    public void initView() {
        isImmersion(true);
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        car_list_cartype_rl = (RelativeLayout) findViewById(R.id.car_list_cartype_rl);
        personal_list_platenumber_open_rl = (RelativeLayout) findViewById(R.id.personal_list_platenumber_open_rl);
        car_list_address_rl = (RelativeLayout) findViewById(R.id.car_list_address_rl);
        personal_list_carimg_iv = (CircleImageView) findViewById(R.id.personal_list_carimg_iv);
        personal_list_cartype_tv = (TextView) findViewById(R.id.personal_list_cartype_tv);
        personal_list_platenumber_open_tv = (TextView) findViewById(R.id.personal_list_platenumber_open_tv);
        personal_list_address_tv = (TextView) findViewById(R.id.personal_list_address_tv);
        personal_list_platenumber_et = (EditText) findViewById(R.id.personal_list_platenumber_et);
        personal_list_framenumber_et = (EditText) findViewById(R.id.personal_list_framenumber_et);
        personal_list_enginenumber_et = (EditText) findViewById(R.id.personal_list_enginenumber_et);
        personal_detail_save_btn = (Button) findViewById(R.id.personal_detail_save_btn);
    }

    @Override
    public void initData() {
        String picurl = SPUtils.get(this, Constant.SP_PICURL, "").toString();
        if (!TextUtils.isEmpty(picurl)) {
            Picasso.with(this)
                    .load("file://" + new File(picurl))
                    .placeholder(R.drawable.personal_top_head)
                    .error(R.drawable.personal_top_head)
                    .into(personal_list_carimg_iv);
        }else{
            personal_list_carimg_iv.setBackgroundResource(R.drawable.personal_top_head);
        }

        String plateNumber = SPUtils.get(this, Constant.SP_PLATENUMBER, "").toString();
        if (!TextUtils.isEmpty(plateNumber)) {
            personal_list_platenumber_et.setText(plateNumber);
        }
        String frameNumber = SPUtils.get(this, Constant.SP_FRAMENUMBER, "").toString();
        if (!TextUtils.isEmpty(plateNumber)) {
            personal_list_framenumber_et.setText(frameNumber);
        }
        String engineNumber = SPUtils.get(this, Constant.SP_ENGINENUMBER, "").toString();
        if (!TextUtils.isEmpty(plateNumber)) {
            personal_list_enginenumber_et.setText(engineNumber);
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
        car_list_cartype_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalDetailActivity.this, "车系 Onclick ", Toast.LENGTH_SHORT).show();
            }
        });
        personal_list_platenumber_open_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalDetailActivity.this, "车牌 Onclick ", Toast.LENGTH_SHORT).show();
//                personal_list_platenumber_open_tv.setText("选择的省会简称");
            }
        });
        car_list_address_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalDetailActivity.this, "地址 Onclick ", Toast.LENGTH_SHORT).show();
//                personal_list_address_tv.setText("选择的地址信息");
            }
        });

        personal_detail_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(PersonalDetailActivity.this, "保存信息 Onclick ", Toast.LENGTH_SHORT).show();
                if (checkInfoComplate()) {
                    saveInfo();
                    finish();
                }


            }
        });
        personal_list_carimg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Toast.makeText(PersonalDetailActivity.this, "版本高于6.0", Toast.LENGTH_SHORT).show();
                    //return;
                }

                //选择头像
                showChoosePictureDialog();
            }
        });

    }

    /**
     * 检查保存信息完整性
     */
    private boolean checkInfoComplate() {
        String plateNumber = personal_list_platenumber_et.getText().toString().trim();
        String frameNumber = personal_list_framenumber_et.getText().toString().trim();
        String engineNumber = personal_list_enginenumber_et.getText().toString().trim();

        if (!TextUtils.isEmpty(plateNumber)) {

            return true;
        } else {
            personal_list_platenumber_et.setError("请输入车牌号");

        }

        if (!TextUtils.isEmpty(frameNumber)) {

            return true;
        } else {
            personal_list_framenumber_et.setError("请输入车架号");

        }

        if (!TextUtils.isEmpty(engineNumber)) {

            return true;
        } else {
            personal_list_enginenumber_et.setError("请输入发动机号");

        }


        return false;
    }


    /**
     * 保存信息到本地
     * TODO 上传到服务器
     */
    private void saveInfo() {
        if (photoPath != null && !TextUtils.isEmpty(photoPath)) {
            SPUtils.put(this, Constant.SP_PICURL, photoPath);
        }
        String plateNumber = personal_list_platenumber_et.getText().toString().trim();
        String frameNumber = personal_list_framenumber_et.getText().toString().trim();
        String engineNumber = personal_list_enginenumber_et.getText().toString().trim();
        String plateOpenNumber = personal_list_platenumber_open_tv.getText().toString().trim();
        if (!TextUtils.isEmpty(plateNumber)) {
            SPUtils.put(this, Constant.SP_PLATENUMBER, plateNumber);
            plateNumber = plateOpenNumber + plateNumber;
            SPUtils.put(this, Constant.SP_PLATEOPENNUMBER, plateNumber);
        }
        if (!TextUtils.isEmpty(frameNumber)) {
            SPUtils.put(this, Constant.SP_FRAMENUMBER, frameNumber);
        }
        if (!TextUtils.isEmpty(engineNumber)) {
            SPUtils.put(this, Constant.SP_ENGINENUMBER, engineNumber);
        }

        Toast.makeText(PersonalDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

    }

    /**
     * 显示选择图片的窗口
     */
    private void showChoosePictureDialog() {
        final String[] pictureFrom = {"拍照上传", "相册选择"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择图片来源");
        builder.setItems(pictureFrom, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null == pictureChooser) {
                    pictureChooser = new PictureChooser(PersonalDetailActivity.this);
                    //图片名称，会做存储
                    pictureChooser.setCameraPicName("headPhoto.jpg");
                    //是否裁剪，裁剪的比例和宽高
                    pictureChooser.setIsClip(true, 1, 1, 0, 0);
                    //是否压缩，压缩后的最大质量（单位kb）和图片宽高
                    pictureChooser.setIsCompressor(true, 500, 240, 240);
                }
                if (0 == which) {
                    //设置选择相机
                    pictureChooser.setmPictureFrom(PictureChooser.PictureFrom.CAMERA);
                }
                if (1 == which) {
                    //设置选择相册
                    pictureChooser.setmPictureFrom(PictureChooser.PictureFrom.GALLERY);
                }
                pictureChooser.execute(new PictureChooser.OnPicturePickListener() {
                    /**
                     * 这里是获取到图片路径
                     * @param filePath 选中的图片在手机文件系统里的路径
                     */
                    @Override
                    public void senFile(String filePath) {
                        //这里改成自己的图片加载框架，我只是简单做一下案例
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        personal_list_carimg_iv.setImageBitmap(bitmap);
                        //把图片路径存储起来，上传的时候要用到
                        Toast.makeText(PersonalDetailActivity.this, "" + filePath, Toast.LENGTH_SHORT).show();
                        photoPath = filePath;
                    }

                    /**
                     * 如果有做压缩处理，这里会返回压缩后的路径
                     * @param filePath 选中的图片在手机文件系统里的路径
                     */
                    @Override
                    public void compressorSuccess(String filePath) {
                        photoPath = filePath;
                    }
                });
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //要做页面回调的响应处理，把操作的数据传给图片选择操作类
        pictureChooser.onActivityResult(requestCode, resultCode, data);
    }
}
