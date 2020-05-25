package com.github.tifezh.kchart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.chart.praise.BezierPraiseAnimator;

/**
 *
 * @description 直播间点赞
 * @author puyantao
 * @date 2020/5/25 9:07
 */
public class PraiseActivity extends AppCompatActivity {
    private ImageView mIvPraise;
    private BezierPraiseAnimator mPraiseAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);


        mPraiseAnimator = new BezierPraiseAnimator(this);
        mIvPraise = findViewById(R.id.iv_praise);
        mIvPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPraiseAnimator.startAnimation(mIvPraise);
            }
        });

    }
}














