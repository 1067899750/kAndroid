package com.github.tifezh.kchart.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.model.Explain;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExplainDialog extends Dialog {
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.rvChoosemh)
    RecyclerView rvChoosemh;
    private List<Explain> list = new ArrayList();

    public ExplainDialog(@NonNull Context context) {
        super(context);
    }

    public ExplainDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_explain, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        setRvChoosemh();
    }

    //初始化配置
    public void setRvChoosemh() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChoosemh.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 20; i++) {
            Explain explain = new Explain();
            explain.setName("单位名称" + i);
            explain.setValue("都是和利好的交易所" + i);
            list.add(explain);
        }
//        ExplainDialogAdapter  explainDialogAdapter = new ExplainDialogAdapter(getContext());
//        explainDialogAdapter.setData(list);
//        rvChoosemh.setAdapter(explainDialogAdapter);
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
        p.width = (d.getWidth() - 100); //设置dialog的宽度为当前手机屏幕的宽度
        p.height = d.getHeight() - 350;
        getWindow().setAttributes(p);
    }

    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        dismiss();
    }
}
