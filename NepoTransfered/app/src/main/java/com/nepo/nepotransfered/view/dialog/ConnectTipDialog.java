package com.nepo.nepotransfered.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nepo.nepotransfered.R;

/**
 * Created by shen on 16/6/24.
 */
public class ConnectTipDialog extends Dialog {
    private Context mContext;
    private ImageView mCancel;
    public ConnectTipDialog(Context context) {
        super(context,R.style.ExitDialog);
        mContext=context;
    }

    public ConnectTipDialog(Context context, int theme) {
        super(context, theme);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        //设置为我们的布局
        this.setCanceledOnTouchOutside(true);
        //设置为点击对话框之外的区域对话框不消失
        mCancel= (ImageView) findViewById(R.id.dialog_cancel);
        //设置事件

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectTipDialog.this.dismiss();
            }
        });

    }
}
