package com.nepo.nepotransfered;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.MediaImageItemAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.model.MediaImgModel;
import com.nepo.nepotransfered.model.MediaVideoModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.utils.Utils;
import com.nepo.nepotransfered.view.tipview.TipsView;

import org.w3c.dom.Text;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class MediaImgItemActivity extends BaseActivity implements MediaImageItemAdapter.OnImageItemCheckedLisenter {

    /**
     * 视图
     *
     * @param savedInstanceState
     */
    private RelativeLayout nav_ll;
    private RelativeLayout media_pic_send_item_control_rl;
    private ImageButton media_send_item_control_ibtn;
    private TextView media_pic_send_item_control_mDragView;

    private GridView media_pic_item_gridview;

    private Button media_pic_send_item_cencel_btn;

    private CheckBox media_pic_send_item_control_checked;

    private TextView title;

    private MediaImgModel imageInfo;

    private MediaImageItemAdapter imageItemAdapter;

    private int mImageThumbSize;
    private int mImageThumbSpacing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_img_item);
        initView();
        initLisenter();
        initData();


    }

    @Override
    public void initView() {
        isImmersion(true);
        nav_ll = (RelativeLayout) findViewById(R.id.nav_ll);
        media_pic_send_item_control_rl = (RelativeLayout) findViewById(R.id.media_send_item_control_rl);
        media_send_item_control_ibtn = (ImageButton) findViewById(R.id.media_send_item_control_ibtn);
        media_pic_send_item_control_mDragView = (TextView) findViewById(R.id.media_send_item_control_mDragView);
        media_pic_item_gridview = (GridView) findViewById(R.id.media_pic_item_gridview);
        media_pic_send_item_cencel_btn = (Button) findViewById(R.id.media_send_item_cencel_btn);
        media_pic_send_item_control_checked = (CheckBox) findViewById(R.id.media_send_item_control_checked);
        title = (TextView) findViewById(R.id.title);

        TipsView.create(this).attach(media_pic_send_item_control_mDragView, new TipsView.DragListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete() {
                media_pic_send_item_control_checked.setChecked(false);
                imageItemAdapter.selectAllItem(false);
                imageItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
            }
        });

    }

    @Override
    public void initData() {
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        imageInfo = (MediaImgModel) getIntent().getSerializableExtra("imgDirInfo");
        title.setText(imageInfo.getDirName());
        imageItemAdapter = new MediaImageItemAdapter(this, imageInfo.getImages());
        imageItemAdapter.setCheckLisenter(this);
        media_pic_item_gridview.setAdapter(imageItemAdapter);
//        media_pic_item_gridview.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        final int numColumns = (int) Math.floor(media_pic_item_gridview
//                                .getWidth()
//                                / (mImageThumbSize + mImageThumbSpacing));
//                        if (numColumns > 0) {
//                            int columnWidth = (media_pic_item_gridview.getWidth() / numColumns)
//                                    + mImageThumbSpacing;
//                            imageItemAdapter.setItemHeight(columnWidth);
//                            media_pic_item_gridview.getViewTreeObserver()
//                                    .removeGlobalOnLayoutListener(this);
//                        }
//                    }
//                });
    }

    @Override
    public void initLisenter() {
        nav_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        media_pic_send_item_control_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageItemAdapter.selectAllItem(media_pic_send_item_control_checked.isChecked());
                imageItemAdapter.notifyDataSetChanged();
            }
        });
        media_send_item_control_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 添加到传输队列

                //TODO 添加到传输队列
                if (imageItemAdapter.getSelectedItemPositions().length > 0) {
                    for (int i = 0; i < imageItemAdapter.getSelectedAudioPaths().length; i++) {
                        String paths = imageItemAdapter.getSelectedAudioPaths()[i];
                        TransferModel transferModel = new TransferModel();
                        transferModel.setTransType(Constant.TRANSTYPE_PIC);
                        transferModel.setTransPath(paths);
                        long l = TransferDB.getTransferDB(MediaImgItemActivity.this).insertToTransfer(transferModel.getTransType(), transferModel);
                        Utils.Sout("long", l);
                    }

                    Toast.makeText(MediaImgItemActivity.this, "添加到传输列表成功", Toast.LENGTH_SHORT).show();
                    openActivity(MediaImgItemActivity.this, TransferActivity.class);
                    finish();

                }


            }
        });
        media_pic_send_item_cencel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // x 取消选择
                media_pic_send_item_control_checked.setChecked(false);
                imageItemAdapter.selectAllItem(false);
                imageItemAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void checkImage() {
        if (imageItemAdapter.getSelectedItemPositions().length > 0) {
            media_pic_send_item_control_mDragView.setVisibility(View.VISIBLE);
            media_pic_send_item_control_mDragView.setText(imageItemAdapter.getSelectedItemPositions().length + "");

        } else {
            media_pic_send_item_control_mDragView.setVisibility(View.GONE);
        }

        if (imageItemAdapter.getSelectedItemPositions().length == imageInfo.getImages().size()) {
            media_pic_send_item_control_checked.setChecked(true);
        } else {
            media_pic_send_item_control_checked.setChecked(false);
        }
    }
}
