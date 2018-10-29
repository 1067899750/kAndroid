package com.github.tifezh.kchart.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.tifezh.kchart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvOrVolumeaPopwindow extends PopupWindow {
    @BindView(R.id.tvInventoryNum)
    TextView tvInventoryNum;
    @BindView(R.id.tvInventory)
    TextView tvInventory;
    @BindView(R.id.tvVolumeNum)
    TextView tvVolumeNum;
    @BindView(R.id.tvVolume)
    TextView tvVolume;
    @BindView(R.id.tvInventoryTitle)
    TextView tvInventoryTitle;

    @BindView(R.id.tvVolumeTitle)
    TextView tvVolumeTitle;


    @BindView(R.id.llInvOrVolume)
    LinearLayout llInvOrVolume;
    private Context mContext;
//    private int screenWidth = LinearLayout.LayoutParams.MATCH_PARENT;//得到屏幕宽度

    public InvOrVolumeaPopwindow(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public InvOrVolumeaPopwindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public InvOrVolumeaPopwindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void initView() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuwindow_invorvolumea, null, false);
        setContentView(contentView);
        ButterKnife.bind(contentView);

//        setWidth(screenWidth);
//        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true); //能否获得焦点
        // 设置PopupWindow的背景
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        setTouchable(true);
//        setAnimationStyle(R.style.AnimBottom);
//        showAsDropDown(mView);

    }

    public void setShow(View mView, int screenWidth) {
        setWidth(screenWidth);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        showAsDropDown(mView);
    }

    public void setTextValue(String volume, String volumeNum, String inventory, String inventoryNum) {
        tvVolume.setText(volume);
        tvVolumeNum.setText(volumeNum);
        tvInventory.setText(inventory);
        tvInventoryNum.setText(inventoryNum);
    }


}
