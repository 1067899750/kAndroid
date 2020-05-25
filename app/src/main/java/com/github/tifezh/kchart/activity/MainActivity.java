package com.github.tifezh.kchart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.tifezh.kchart.R;

/**
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:42
 * <p>
 * <p>
 * .::::.
 * .::::::::.
 * :::::::::::
 * ..:::::::::::'
 * '::::::::::::'
 * .::::::::::
 * '::::::::::::::..
 * ..::::::::::::.
 * ``::::::::::::::::  <- touch me
 * ::::``:::::::::'        .:::.
 * ::::'   ':::::'       .::::::::.
 * .::::'      ::::     .:::::::'::::.
 * .:::'       :::::  .:::::::::' ':::::.
 * .::'        :::::.:::::::::'      ':::::.
 * .::'         ::::::::::::::'         ``::::.
 * ...:::           ::::::::::::'              ``::.
 * ```` ':.          ':::::::::'                  ::::..
 * '.:::::'                    ':'````..
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_loadmore:
                //k线图，支持加载更多
                intent.setClass(this, KlineActivity.class);
                break;

            case R.id.btn_minute:
                //分时图
                intent.setClass(this, TimeMainActivity.class);
                break;

            case R.id.btn_tate:
                //利率图
                intent.setClass(this, RateActivity.class);
                break;

            case R.id.btn_minute2:
                //月间价差图
                intent.setClass(this, LmeActivity.class);
                break;

            case R.id.btn_group_view:
                intent.setClass(this, GroupActivity.class);
                break;

            case R.id.btn_group_minute_view:
                intent.setClass(this, GroupMinuteActivity.class);
                break;

            case R.id.btn_football:
                //缩放
                intent.setClass(this, FootBallActivity.class);
                break;

            case R.id.btn_shade:
                //下部阴影
                intent.setClass(this, ScaleActivity.class);
                break;

            case R.id.btn_details:
                //明细表
                intent.setClass(this, RLDetailActivity.class);
                break;


            case R.id.btn_line:
                //指示线研究
                intent.setClass(this, LineActivity.class);
                break;

            case R.id.btn_suspend:
                //饼图
                intent.setClass(this, SuspendBtnActivity.class);
                break;

            case R.id.btn_chart:
                //饼图
                intent.setClass(this, BookChartMapActivity.class);
                break;

            case R.id.btn_direct:
                //直播间送爱心
                intent.setClass(this, DirectActivity.class);
                break;
            case R.id.btn_praise:
                intent.setClass(this, PraiseActivity.class);
                break;
        }
        startActivity(intent);
    }
}


















