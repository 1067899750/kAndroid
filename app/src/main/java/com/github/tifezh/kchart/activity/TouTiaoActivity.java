package com.github.tifezh.kchart.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.tifezh.kchart.BitmapProviderFactory;
import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.toutiao.SuperLikeLayout;

/**
 *
 * @description 模仿今日头条
 * @author puyantao
 * @date 2020/7/29 16:25
 */
public class TouTiaoActivity extends AppCompatActivity {
    private SuperLikeLayout superLikeLayout;
    private long duration = 200;
    private long lastClickTime;

    public static void StartTouTiaoActivity(Activity activity){
        Intent intent = new Intent(activity, TouTiaoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tou_tiao);
        superLikeLayout = findViewById(R.id.super_like_layout);
        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
        findViewById(R.id.iv_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() - lastClickTime > duration){ // 防抖
                    v.setSelected(!v.isSelected());
                }
                lastClickTime = System.currentTimeMillis();
                if(v.isSelected()){
                    int x = (int)(v.getX() + v.getWidth() / 2);
                    int y = (int)(v.getY() + v.getHeight() / 2);
                    superLikeLayout.launch(x, y);
                }
            }
        });
    }
}













