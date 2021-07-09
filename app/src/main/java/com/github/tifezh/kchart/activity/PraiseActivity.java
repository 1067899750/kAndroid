package com.github.tifezh.kchart.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView mPraiseIv;
    private TextView mPraiseTv;

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
        mPraiseIv = findViewById(R.id.praise_iv);
        mPraiseTv = findViewById(R.id.praise_tv);
        relativeLayout = findViewById(R.id.praise_rl);
        final long[] lastClickTimeMillis = {0};
        final boolean[] isPraise = {false};
        relativeLayout.setOnPraiseClickListener(new PraiseRelativeLayout.OnPraiseClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClickTimeMillis[0] < 800) {
                    mPraiseIv.setImageResource(R.drawable.give_praise);
                    mPraiseAnimator.clickView(v);
                    mPraiseTv.setText("1");
                    mPraiseTv.setTextColor(Color.parseColor("#FF3434"));
                    Log.d("--->", "1");
                } else {
                    if (isPraise[0]) {
                        mPraiseIv.setImageResource(R.drawable.dismiss_praise);
                        mPraiseTv.setText("赞");
                        Log.d("--->", "2");
                        mPraiseTv.setTextColor(Color.parseColor("#979797"));
                        isPraise[0] = false;
                    } else {
                        mPraiseIv.setImageResource(R.drawable.give_praise);
                        mPraiseAnimator.clickView(v);
                        mPraiseTv.setText("1");
                        Log.d("--->", "3");
                        mPraiseTv.setTextColor(Color.parseColor("#FF3434"));
                        isPraise[0] = true;
                    }
                }
                lastClickTimeMillis[0] = System.currentTimeMillis();
            }

            @Override
            public void onLongClick(View v) {
                mPraiseIv.setImageResource(R.drawable.give_praise);
                mPraiseTv.setText("1");
                mPraiseTv.setTextColor(Color.parseColor("#FF3434"));
                Log.d("--->", "4");
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














