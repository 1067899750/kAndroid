package com.github.tifezh.kchart.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.tifezh.kchart.BitmapProviderFactory;
import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.toutiao.SuperLikeLayout;
import com.github.tifezh.kchartlib.toutiao.PraiseRelativeLayout;

/**
 * @author puyantao
 * @description 模仿今日头条
 * @date 2020/7/29 16:25
 */
public class TouTiaoActivity extends AppCompatActivity {
    private SuperLikeLayout superLikeLayout;
    private PraiseRelativeLayout relativeLayout;

    public static void StartTouTiaoActivity(Activity activity) {
        Intent intent = new Intent(activity, TouTiaoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tou_tiao);
        superLikeLayout = findViewById(R.id.super_like_layout);
        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
        relativeLayout = findViewById(R.id.praise_rl);
        relativeLayout.setOnPraiseClickListener(new PraiseRelativeLayout.OnPraiseClickListener() {
            @Override
            public void onClick(View v) {
                superLikeLayout.clickView(v);
            }

            @Override
            public void onLongClick(View v) {
                superLikeLayout.longClickView(v);
            }

            @Override
            public void onCancelClick(View v) {
                superLikeLayout.stop();
            }
        });
    }
}













