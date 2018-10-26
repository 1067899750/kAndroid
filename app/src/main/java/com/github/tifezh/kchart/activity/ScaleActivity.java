package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.tifezh.kchart.view.LearnView;

public class ScaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scale);
//        setContentView(new ScaleView(this));
        setContentView(new LearnView(this));
    }
}
