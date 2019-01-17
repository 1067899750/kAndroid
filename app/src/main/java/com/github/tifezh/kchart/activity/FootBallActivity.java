package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.model.FootballModel;
import com.github.tifezh.kchart.model.IFootBallModel;
import com.github.tifezh.kchart.model.RateModel;
import com.github.tifezh.kchart.model.TrendChartModel;
import com.github.tifezh.kchartlib.chart.football.FootBallView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2019-1-17 14:15
 */

public class FootBallActivity extends AppCompatActivity {
    @BindView(R.id.footBallView)
    FootBallView footBallView;

    private FootballModel mFootballModel;
    private List<IFootBallModel> mIHomeFootBallModels;
    private List<IFootBallModel> mIAwayFootBallModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_ball);
        ButterKnife.bind(this);

        mIHomeFootBallModels = new ArrayList<>();
        mIAwayFootBallModels = new ArrayList<>();
        mFootballModel = DataRequest.getFootBallData(this);

        initData();

    }

    public void initData() {
        List<FootballModel.ResultBean.DataBean.HomeTeamDataBean> homeTeamData = mFootballModel.getResult().getData().getHomeTeamData();
        List<FootballModel.ResultBean.DataBean.AwayTeamDataBean> awayTeamDataBeans = mFootballModel.getResult().getData().getAwayTeamData();
        for (int i = 0; i < homeTeamData.size(); i++) {
            IFootBallModel iFootBallModel = new IFootBallModel();
            iFootBallModel.homeSequenceStaus = homeTeamData.get(i).getTime();
            iFootBallModel.homeEventType = homeTeamData.get(i).getEventType();
            iFootBallModel.homeSequenceStaus = homeTeamData.get(i).getSequenceStaus();
            mIHomeFootBallModels.add(iFootBallModel);

        }

        for (int i = 0; i < awayTeamDataBeans.size(); i++) {
            IFootBallModel iFootBallModel = new IFootBallModel();
            iFootBallModel.homeSequenceStaus = awayTeamDataBeans.get(i).getTime();
            iFootBallModel.homeEventType = awayTeamDataBeans.get(i).getEventType();
            iFootBallModel.homeSequenceStaus = awayTeamDataBeans.get(i).getSequenceStaus();
            mIAwayFootBallModels.add(iFootBallModel);

        }

        footBallView.initData(mIHomeFootBallModels, mIAwayFootBallModels);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        footBallView.clearMemory();
    }


}















