package com.github.tifezh.kchart.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.model.MinuteLine;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Description k线横屏信息显示
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:42
 */


public class TopLeftHorizontalView extends LinearLayout {
    @BindView(R.id.tvContractName)
    TextView tvContractName;//合约名
    @BindView(R.id.tvLastValue)
    TextView tvLastValue;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPercentValue)
    TextView tvPercentValue;
    @BindView(R.id.txtSell)
    TextView txtSell;
    @BindView(R.id.tvSellValue)
    TextView tvSellValue;//卖
    @BindView(R.id.tvSellCount)
    TextView tvSellCount;
    @BindView(R.id.rlSell)
    RelativeLayout rlSell;
    @BindView(R.id.txtBuy)
    TextView txtBuy;
    @BindView(R.id.tvBuyValue)
    TextView tvBuyValue;//买
    @BindView(R.id.tvBuyCount)
    TextView tvBuyCount;
    @BindView(R.id.rlBuy)
    RelativeLayout rlBuy;
    @BindView(R.id.txtInventory)
    TextView txtInventory;
    @BindView(R.id.tvInventoryValue)
    TextView tvInventoryValue;//持仓量
    @BindView(R.id.rlInventory)
    RelativeLayout rlInventory;
    @BindView(R.id.txtVolume)
    TextView txtVolume;
    @BindView(R.id.tvVolumeValue)
    TextView tvVolumeValue;//成交量
    @BindView(R.id.rlVolume)
    RelativeLayout rlVolume;
    @BindView(R.id.tvLMEValue)
    TextView tvLMEValue;
    @BindView(R.id.tvProfitValue)
    TextView tvProfitValue;
    @BindView(R.id.tvChangeInventoryValue)
    TextView tvChangeInventoryValue;
    @BindView(R.id.tvChangeVolumeValue)
    TextView tvChangeVolumeValue;

    private MinuteLine minuteLineEntity;

    public TopLeftHorizontalView(Context context) {
        super(context);
    }

    public TopLeftHorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_top_left_horizontal, this);
        ButterKnife.bind(this);
    }

    public void fillData(MinuteLine minuteLineEntity) {
        this.minuteLineEntity = minuteLineEntity;
        if (minuteLineEntity == null) {
            return;
        }


    }
}
