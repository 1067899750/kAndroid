package com.github.tifezh.kchart.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.tifezh.kchart.BitmapProviderFactory;
import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.chart.praise.BezierPraiseAnimator;
import com.github.tifezh.kchartlib.toutiao.PraiseRelativeLayout;

/**
 *
 * @description 直播间点赞
 * @author puyantao
 * @date 2020/5/25 9:07
 */
public class PraiseActivity extends AppCompatActivity {
    private PraiseRelativeLayout relativeLayout;
    private BezierPraiseAnimator mPraiseAnimator;


    public static void startPraiseActivity(Activity activity){
        Intent intent = new Intent(activity, PraiseActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);


        mPraiseAnimator = new BezierPraiseAnimator(this);
        mPraiseAnimator.setProvider(BitmapProviderFactory.getHDProvider(this));

        relativeLayout = findViewById(R.id.praise_rl);
        relativeLayout.setOnPraiseClickListener(new PraiseRelativeLayout.OnPraiseClickListener() {
            @Override
            public void onClick(View v) {
                mPraiseAnimator.clickView(v);
            }

            @Override
            public void onLongClick(View v) {
                mPraiseAnimator.longClickView(v);
            }

            @Override
            public void onCancelClick(View v) {
                mPraiseAnimator.stop();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPraiseAnimator.onDestroy();
    }
}














