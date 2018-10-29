package com.github.tifezh.kchart.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.model.ShareBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareDialog extends Dialog {

    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.wexinfriend)
    LinearLayout wexinfriend;
    @BindView(R.id.wexinfriends)
    LinearLayout wexinfriends;
    @BindView(R.id.qqfriend)
    LinearLayout qqfriend;
    @BindView(R.id.sinwebo)
    LinearLayout sinwebo;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    private Bitmap bitmap = null;
    private ShareBean shareBean;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    public ShareDialog(@NonNull Context context, int themeResId, Bitmap bitmap) {
        super(context, themeResId);
        this.bitmap = bitmap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_pictureshare, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        shareBean = new ShareBean("", "", "", "");
        shareBean.setBitmap(bitmap);
        ivShare.setImageBitmap(bitmap);
    }


    @Override
    public void onDetachedFromWindow() {
        if (isShowing())
            dismiss();
        super.onDetachedFromWindow();
    }

    @Override
    public void show() {
        super.show();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
    }

    @OnClick({R.id.wexinfriend, R.id.wexinfriends, R.id.qqfriend, R.id.sinwebo, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wexinfriend:

                this.dismiss();
                break;
            case R.id.wexinfriends:

                this.dismiss();
                break;
            case R.id.qqfriend:

                this.dismiss();
                break;
            case R.id.sinwebo:

                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }
}
