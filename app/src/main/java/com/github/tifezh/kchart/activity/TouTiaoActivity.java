package com.github.tifezh.kchart.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.github.tifezh.kchart.BitmapProviderFactory;
import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.widget.StarBar;
import com.github.tifezh.kchart.widget.YStarView;
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
    private StarBar mStarBar;

    public static void startTouTiaoActivity(Activity activity) {
        Intent intent = new Intent(activity, TouTiaoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tou_tiao);
        superLikeLayout = findViewById(R.id.super_like_layout);
        mStarBar = findViewById(R.id.starBar1);
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

      initStartBar();
    }

    private void initStartBar() {
        //获取星数
        mStarBar.getStarMark();

        mStarBar.setClickAble(false);
        mStarBar.setStarCount(3);
        mStarBar.setRating(3);


        mStarBar.setClickAble(false);//设置星星是否可以点击和滑动改变
//        starBar3.setIntegerMark(true);// 设置是否需要整数评分
        mStarBar.setStarMark(3);//设置星数



        YStarView yStarView = (YStarView) findViewById(R.id.starBar4);
        yStarView.setStarCount(5);//星星总数
        yStarView.setRating(3);//设置星星亮的颗数
        yStarView.setChange(true);//设置星星是否可以点击和滑动改变
        yStarView.setHalf(true);//设置半星
        yStarView.setStar(R.drawable.ic_full,R.drawable.ic_empty);//设置星星显示样式
//         yStarView.getRating()+"颗星";

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        superLikeLayout.onDestroy();
    }
}













