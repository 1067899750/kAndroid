package com.github.tifezh.kchart.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.chart.direct.HeartType;
import com.github.tifezh.kchartlib.chart.direct.MeiHeartView;

/**
 * @author puyantao
 * @description 直播间送爱心
 * @date 2020/5/21 10:15
 */
public class DirectActivity extends AppCompatActivity {
    private MeiHeartView mMeiHeartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);
        mMeiHeartView = findViewById(R.id.heart_view);


        setHeartBitmap();

        mMeiHeartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeiHeartView.addHeart();
            }
        });

        intervalAddHeart();

    }

    private void intervalAddHeart() {
        mMeiHeartView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMeiHeartView.performClick();
                intervalAddHeart();
            }
        }, 1000);
    }


    public void setHeartBitmap() {
        SparseArray<Bitmap> bitmapArray = new SparseArray<>();
        Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_0);
        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_1);
        Bitmap bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_2);
        Bitmap bitmap4 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_3);
        Bitmap bitmap5 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_4);
        Bitmap bitmap6 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_5);
        Bitmap bitmap7 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_heart_6);
        bitmapArray.put(HeartType.BLUE, bitmap1);
        bitmapArray.put(HeartType.GREEN, bitmap2);
        bitmapArray.put(HeartType.YELLOW, bitmap3);
        bitmapArray.put(HeartType.PINK, bitmap4);
        bitmapArray.put(HeartType.BROWN, bitmap5);
        bitmapArray.put(HeartType.PURPLE, bitmap6);
        bitmapArray.put(HeartType.RED, bitmap7);
        mMeiHeartView.setHeartBitmap(bitmapArray);
    }


}











