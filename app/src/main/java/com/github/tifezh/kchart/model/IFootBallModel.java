package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.football.IFootball;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2019-1-17 14:15
 */

public class IFootBallModel implements IFootball {

    public int homeTime; //事件触发时间
    public int homeEventType;//事件类型(1:入球,2:红牌,3:黄牌,4:进攻时间,5:危险进攻时间,6:射正球门时间,
    // 7:点球,8:乌龙,9:两黄变红,10:射偏球门时间,11:换人,12:角球,15:中场时间事件)
    public int homeSequenceStaus; //场次状态(1上半场,2下半场) ,


    public int awayTime; //事件触发时间
    public int awayEventType;//事件类型(1:入球,2:红牌,3:黄牌,4:进攻时间,5:危险进攻时间,6:射正球门时间,
    // 7:点球,8:乌龙,9:两黄变红,10:射偏球门时间,11:换人,12:角球,15:中场时间事件)
    public int awaySequenceStaus; //场次状态(1上半场,2下半场) ,

    @Override
    public int getHomeDate() {
        return homeTime;
    }

    @Override
    public int getHomeEventType() {
        return homeEventType;
    }

    @Override
    public int getHomeSequenceStaus() {
        return homeSequenceStaus;
    }

    @Override
    public int getAwayDate() {
        return awayTime;
    }

    @Override
    public int getAwayEventType() {
        return awayEventType;
    }

    @Override
    public int getAwaySequenceStaus() {
        return awaySequenceStaus;
    }
}
