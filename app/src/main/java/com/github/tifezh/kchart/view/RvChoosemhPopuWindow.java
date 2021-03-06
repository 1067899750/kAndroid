package com.github.tifezh.kchart.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.model.RvChoosemh;

import java.util.ArrayList;
import java.util.List;


/**
 * Description：
 * Author: css
 * Email: 1175558532@qq.com
 * Date: 2018-8-30 9:18
 */

public class RvChoosemhPopuWindow extends PopupWindow {
    private Context mContext;
    private View mView;
    private RecyclerView rvChoosemh;
    private OnClickListener mOnClickListener;
//    private RvChoosemhAdapter rvChoosemhAdapter;
    private List<RvChoosemh> rvList = new ArrayList<>();
    private String name[] = {"30m", "1h", "2h", "3h", "4h", "周K", "月K", "季K", "年K"};
    private int screenWidth = LinearLayout.LayoutParams.MATCH_PARENT;//得到屏幕宽度

    public RvChoosemhPopuWindow(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public RvChoosemhPopuWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public RvChoosemhPopuWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    private void initView() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuwindow_rvchoosemh, null, false);
        rvChoosemh = contentView.findViewById(R.id.rvChoosemh);
        setContentView(contentView);
        setRvChoosemh();//初始化数据
//        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

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

        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(view, view.getWidth() / 2 - 50, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移

//        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
//            int[] location = new int[2];
//            mView.getLocationOnScreen(location);
//            int y = location[1];
//            showAtLocation(mView, Gravity.NO_GRAVITY, (mView.getWidth() / 2) + 50, y - dip2px(mContext, 30));//- (view.getHeight() / 3)
//        } else {
//            showAsDropDown(mView);
//        }

//        int[] location = new int[2];
//        mView.getLocationOnScreen(location);
//        int y = location[1];
//        showAtLocation(mView, Gravity.NO_GRAVITY, (mView.getWidth() / 2) + 50, y - dip2px(mContext, 30));//- (view.getHeight() / 3)

    }
    public void setShow(View mView, int screenWidth) {
        setWidth(screenWidth);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        showAsDropDown(mView);
    }
    //初始化配置
    public void setRvChoosemh() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChoosemh.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < name.length; i++) {
            RvChoosemh rvChoosemh = new RvChoosemh();
            rvChoosemh.setChoose(false);
            rvChoosemh.setValue(name[i]);
            rvList.add(rvChoosemh);
        }
//        rvChoosemhAdapter = new RvChoosemhAdapter(mContext, true);
//        rvChoosemhAdapter.setData(rvList);
//        rvChoosemhAdapter.setMyItemLister(new RvChoosemhAdapter.MyItemLister() {
//            @Override
//            public void setItem(View v, List<RvChoosemh> data, int position) {
//                dismiss();
//                String mm = data.get(position).getValue();
//                mOnClickListener.onClick(v, mm);
//            }
//        });
//        rvChoosemh.setAdapter(rvChoosemhAdapter);
    }

    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setMyClick(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public interface OnClickListener {
        void onClick(View view, String name);
    }

}
























