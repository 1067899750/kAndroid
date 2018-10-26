package com.github.tifezh.kchart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.tifezh.kchart.R;
/**
 *
 *
 *   Description
 *   Author puyantao
 *   Email 1067899750@qq.com
 *   Date 2018-10-26 17:42
 *
 *
 *                        .::::.
 *                     .::::::::.
 *                    :::::::::::
 *                 ..:::::::::::'
 *               '::::::::::::'
 *                 .::::::::::
 *            '::::::::::::::..
 *               ..::::::::::::.
 *             ``::::::::::::::::  <- touch me
 *               ::::``:::::::::'        .:::.
 *             ::::'   ':::::'       .::::::::.
 *           .::::'      ::::     .:::::::'::::.
 *            .:::'       :::::  .:::::::::' ':::::.
 *          .::'        :::::.:::::::::'      ':::::.
 *         .::'         ::::::::::::::'         ``::::.
 *      ...:::           ::::::::::::'              ``::.
 *     ```` ':.          ':::::::::'                  ::::..
 *                        '.:::::'                    ':'````..
 *
*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(MainActivity.this, KlineActivity.class));

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_loadmore:
                intent.setClass(this, KlineActivity.class);//k线图，支持加载更多
                break;
            case R.id.btn_minute:
                intent.setClass(this, TimeMainActivity.class);//分时图
                break;
            case R.id.btn_minute2:
                intent.setClass(this, LmeActivity.class);//月间价差图
                break;
            case R.id.btn_scale:
                intent.setClass(this, ScaleActivity.class);//缩放
                break;
        }
        startActivity(intent);
    }
}
