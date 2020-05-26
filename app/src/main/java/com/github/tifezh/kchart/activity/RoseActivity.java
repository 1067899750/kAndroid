package com.github.tifezh.kchart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.chart.rose.RoseGiftSurfaceView;

/**
 *
 * @description
 * @author puyantao
 * @date 2020/5/26 14:38
 */
public class RoseActivity extends AppCompatActivity {
    private RoseGiftSurfaceView mRoseGiftSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rose);
        mRoseGiftSurfaceView = findViewById(R.id.rose);
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mRoseGiftSurfaceView.startAnimation();
    }
}
