package com.github.tifezh.kchart.model;

import java.util.List;

public class FootballModel {
    /**
     * errorCode : 0
     * message : 操作成功
     * result : {"businessCode":0,"data":{"leagueNameShort":"越青锦U19","homeTeamName":"槟椥U19","homeTeamFlag":"http://zq.win007.com/Image/team/null","awayTeamName":"平阳U19","awayTeamFlag":"http://zq.win007.com/Image/team/","midfieldTime":45,"homeTeamAttack":39,"awayTeamAttack":51,"homeTeamDangerousAttack":22,"awayTeamDangerousAttack":34,"homeTeamBallControl":37,"awayTeamBallControl":63,"homeTeamCornerKick":1,"awayTeamCornerKick":2,"homeTeamRedCard":0,"awayTeamRedCard":0,"homeTeamYellowCard":2,"awayTeamYellowCard":1,"homeTeamShotPositiveDoor":1,"awayTeamShotPositiveDoor":7,"homeTeamPolarizationDoor":1,"awayTeamPolarizationDoor":6,"homeTeamData":[{"time":4,"eventType":6,"sequenceStaus":1},{"time":14,"eventType":5,"sequenceStaus":1},{"time":15,"eventType":12,"sequenceStaus":1},{"time":18,"eventType":5,"sequenceStaus":1},{"time":27,"eventType":5,"sequenceStaus":1},{"time":27,"eventType":5,"sequenceStaus":1},{"time":28,"eventType":5,"sequenceStaus":1},{"time":35,"eventType":5,"sequenceStaus":1},{"time":36,"eventType":5,"sequenceStaus":1},{"time":40,"eventType":5,"sequenceStaus":1},{"time":42,"eventType":5,"sequenceStaus":1},{"time":44,"eventType":5,"sequenceStaus":1},{"time":45,"eventType":5,"sequenceStaus":1},{"time":50,"eventType":5,"sequenceStaus":2},{"time":52,"eventType":5,"sequenceStaus":2},{"time":54,"eventType":5,"sequenceStaus":2},{"time":55,"eventType":5,"sequenceStaus":2},{"time":60,"eventType":5,"sequenceStaus":2},{"time":62,"eventType":5,"sequenceStaus":2},{"time":73,"eventType":5,"sequenceStaus":2},{"time":79,"eventType":5,"sequenceStaus":2},{"time":83,"eventType":5,"sequenceStaus":2},{"time":85,"eventType":5,"sequenceStaus":2},{"time":85,"eventType":10,"sequenceStaus":2},{"time":87,"eventType":5,"sequenceStaus":2}],"awayTeamData":[{"time":0,"eventType":5,"sequenceStaus":1},{"time":3,"eventType":10,"sequenceStaus":1},{"time":7,"eventType":6,"sequenceStaus":1},{"time":8,"eventType":6,"sequenceStaus":1},{"time":8,"eventType":10,"sequenceStaus":1},{"time":9,"eventType":10,"sequenceStaus":1},{"time":11,"eventType":5,"sequenceStaus":1},{"time":16,"eventType":5,"sequenceStaus":1},{"time":18,"eventType":5,"sequenceStaus":1},{"time":19,"eventType":5,"sequenceStaus":1},{"time":19,"eventType":12,"sequenceStaus":1},{"time":21,"eventType":5,"sequenceStaus":1},{"time":22,"eventType":5,"sequenceStaus":1},{"time":25,"eventType":5,"sequenceStaus":1},{"time":29,"eventType":5,"sequenceStaus":1},{"time":31,"eventType":10,"sequenceStaus":1},{"time":34,"eventType":5,"sequenceStaus":1},{"time":38,"eventType":5,"sequenceStaus":1},{"time":38,"eventType":10,"sequenceStaus":1},{"time":39,"eventType":5,"sequenceStaus":1},{"time":40,"eventType":5,"sequenceStaus":1},{"time":41,"eventType":6,"sequenceStaus":1},{"time":43,"eventType":5,"sequenceStaus":1},{"time":0,"eventType":5,"sequenceStaus":1},{"time":0,"eventType":10,"sequenceStaus":1},{"time":47,"eventType":5,"sequenceStaus":2},{"time":50,"eventType":5,"sequenceStaus":2},{"time":53,"eventType":5,"sequenceStaus":2},{"time":53,"eventType":5,"sequenceStaus":2},{"time":55,"eventType":5,"sequenceStaus":2},{"time":57,"eventType":5,"sequenceStaus":2},{"time":58,"eventType":5,"sequenceStaus":2},{"time":59,"eventType":5,"sequenceStaus":2},{"time":60,"eventType":5,"sequenceStaus":2},{"time":63,"eventType":5,"sequenceStaus":2},{"time":64,"eventType":6,"sequenceStaus":2},{"time":65,"eventType":6,"sequenceStaus":2},{"time":64,"eventType":5,"sequenceStaus":2},{"time":67,"eventType":5,"sequenceStaus":2},{"time":68,"eventType":5,"sequenceStaus":2},{"time":68,"eventType":5,"sequenceStaus":2},{"time":69,"eventType":12,"sequenceStaus":2},{"time":70,"eventType":6,"sequenceStaus":2},{"time":74,"eventType":5,"sequenceStaus":2},{"time":75,"eventType":5,"sequenceStaus":2},{"time":75,"eventType":6,"sequenceStaus":2},{"time":75,"eventType":5,"sequenceStaus":2},{"time":81,"eventType":5,"sequenceStaus":2},{"time":82,"eventType":5,"sequenceStaus":2},{"time":40,"eventType":1,"sequenceStaus":1},{"time":70,"eventType":1,"sequenceStaus":2},{"time":76,"eventType":1,"sequenceStaus":2}],"detailsEventVos":[{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":76,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":70,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":12,"playerName":"","time":69,"sequenceStaus":2},{"matchId":1657475,"sign":1,"eventType":3,"playerName":"","time":63,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":40,"sequenceStaus":1},{"matchId":1657475,"sign":0,"eventType":3,"playerName":"","time":22,"sequenceStaus":1},{"matchId":1657475,"sign":0,"eventType":12,"playerName":"","time":19,"sequenceStaus":1},{"matchId":1657475,"sign":1,"eventType":12,"playerName":"","time":15,"sequenceStaus":1},{"matchId":1657475,"sign":1,"eventType":3,"playerName":"","time":11,"sequenceStaus":1}]},"encryptionStatus":0,"message":"success"}
     */

    private int errorCode;
    private String message;
    private ResultBean result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * businessCode : 0
         * data : {"leagueNameShort":"越青锦U19","homeTeamName":"槟椥U19","homeTeamFlag":"http://zq.win007.com/Image/team/null","awayTeamName":"平阳U19","awayTeamFlag":"http://zq.win007.com/Image/team/","midfieldTime":45,"homeTeamAttack":39,"awayTeamAttack":51,"homeTeamDangerousAttack":22,"awayTeamDangerousAttack":34,"homeTeamBallControl":37,"awayTeamBallControl":63,"homeTeamCornerKick":1,"awayTeamCornerKick":2,"homeTeamRedCard":0,"awayTeamRedCard":0,"homeTeamYellowCard":2,"awayTeamYellowCard":1,"homeTeamShotPositiveDoor":1,"awayTeamShotPositiveDoor":7,"homeTeamPolarizationDoor":1,"awayTeamPolarizationDoor":6,"homeTeamData":[{"time":4,"eventType":6,"sequenceStaus":1},{"time":14,"eventType":5,"sequenceStaus":1},{"time":15,"eventType":12,"sequenceStaus":1},{"time":18,"eventType":5,"sequenceStaus":1},{"time":27,"eventType":5,"sequenceStaus":1},{"time":27,"eventType":5,"sequenceStaus":1},{"time":28,"eventType":5,"sequenceStaus":1},{"time":35,"eventType":5,"sequenceStaus":1},{"time":36,"eventType":5,"sequenceStaus":1},{"time":40,"eventType":5,"sequenceStaus":1},{"time":42,"eventType":5,"sequenceStaus":1},{"time":44,"eventType":5,"sequenceStaus":1},{"time":45,"eventType":5,"sequenceStaus":1},{"time":50,"eventType":5,"sequenceStaus":2},{"time":52,"eventType":5,"sequenceStaus":2},{"time":54,"eventType":5,"sequenceStaus":2},{"time":55,"eventType":5,"sequenceStaus":2},{"time":60,"eventType":5,"sequenceStaus":2},{"time":62,"eventType":5,"sequenceStaus":2},{"time":73,"eventType":5,"sequenceStaus":2},{"time":79,"eventType":5,"sequenceStaus":2},{"time":83,"eventType":5,"sequenceStaus":2},{"time":85,"eventType":5,"sequenceStaus":2},{"time":85,"eventType":10,"sequenceStaus":2},{"time":87,"eventType":5,"sequenceStaus":2}],"awayTeamData":[{"time":0,"eventType":5,"sequenceStaus":1},{"time":3,"eventType":10,"sequenceStaus":1},{"time":7,"eventType":6,"sequenceStaus":1},{"time":8,"eventType":6,"sequenceStaus":1},{"time":8,"eventType":10,"sequenceStaus":1},{"time":9,"eventType":10,"sequenceStaus":1},{"time":11,"eventType":5,"sequenceStaus":1},{"time":16,"eventType":5,"sequenceStaus":1},{"time":18,"eventType":5,"sequenceStaus":1},{"time":19,"eventType":5,"sequenceStaus":1},{"time":19,"eventType":12,"sequenceStaus":1},{"time":21,"eventType":5,"sequenceStaus":1},{"time":22,"eventType":5,"sequenceStaus":1},{"time":25,"eventType":5,"sequenceStaus":1},{"time":29,"eventType":5,"sequenceStaus":1},{"time":31,"eventType":10,"sequenceStaus":1},{"time":34,"eventType":5,"sequenceStaus":1},{"time":38,"eventType":5,"sequenceStaus":1},{"time":38,"eventType":10,"sequenceStaus":1},{"time":39,"eventType":5,"sequenceStaus":1},{"time":40,"eventType":5,"sequenceStaus":1},{"time":41,"eventType":6,"sequenceStaus":1},{"time":43,"eventType":5,"sequenceStaus":1},{"time":0,"eventType":5,"sequenceStaus":1},{"time":0,"eventType":10,"sequenceStaus":1},{"time":47,"eventType":5,"sequenceStaus":2},{"time":50,"eventType":5,"sequenceStaus":2},{"time":53,"eventType":5,"sequenceStaus":2},{"time":53,"eventType":5,"sequenceStaus":2},{"time":55,"eventType":5,"sequenceStaus":2},{"time":57,"eventType":5,"sequenceStaus":2},{"time":58,"eventType":5,"sequenceStaus":2},{"time":59,"eventType":5,"sequenceStaus":2},{"time":60,"eventType":5,"sequenceStaus":2},{"time":63,"eventType":5,"sequenceStaus":2},{"time":64,"eventType":6,"sequenceStaus":2},{"time":65,"eventType":6,"sequenceStaus":2},{"time":64,"eventType":5,"sequenceStaus":2},{"time":67,"eventType":5,"sequenceStaus":2},{"time":68,"eventType":5,"sequenceStaus":2},{"time":68,"eventType":5,"sequenceStaus":2},{"time":69,"eventType":12,"sequenceStaus":2},{"time":70,"eventType":6,"sequenceStaus":2},{"time":74,"eventType":5,"sequenceStaus":2},{"time":75,"eventType":5,"sequenceStaus":2},{"time":75,"eventType":6,"sequenceStaus":2},{"time":75,"eventType":5,"sequenceStaus":2},{"time":81,"eventType":5,"sequenceStaus":2},{"time":82,"eventType":5,"sequenceStaus":2},{"time":40,"eventType":1,"sequenceStaus":1},{"time":70,"eventType":1,"sequenceStaus":2},{"time":76,"eventType":1,"sequenceStaus":2}],"detailsEventVos":[{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":76,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":70,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":12,"playerName":"","time":69,"sequenceStaus":2},{"matchId":1657475,"sign":1,"eventType":3,"playerName":"","time":63,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":40,"sequenceStaus":1},{"matchId":1657475,"sign":0,"eventType":3,"playerName":"","time":22,"sequenceStaus":1},{"matchId":1657475,"sign":0,"eventType":12,"playerName":"","time":19,"sequenceStaus":1},{"matchId":1657475,"sign":1,"eventType":12,"playerName":"","time":15,"sequenceStaus":1},{"matchId":1657475,"sign":1,"eventType":3,"playerName":"","time":11,"sequenceStaus":1}]}
         * encryptionStatus : 0
         * message : success
         */

        private int businessCode;
        private DataBean data;
        private int encryptionStatus;
        private String message;

        public int getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(int businessCode) {
            this.businessCode = businessCode;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getEncryptionStatus() {
            return encryptionStatus;
        }

        public void setEncryptionStatus(int encryptionStatus) {
            this.encryptionStatus = encryptionStatus;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static class DataBean {
            /**
             * leagueNameShort : 越青锦U19
             * homeTeamName : 槟椥U19
             * homeTeamFlag : http://zq.win007.com/Image/team/null
             * awayTeamName : 平阳U19
             * awayTeamFlag : http://zq.win007.com/Image/team/
             * midfieldTime : 45
             * homeTeamAttack : 39
             * awayTeamAttack : 51
             * homeTeamDangerousAttack : 22
             * awayTeamDangerousAttack : 34
             * homeTeamBallControl : 37
             * awayTeamBallControl : 63
             * homeTeamCornerKick : 1
             * awayTeamCornerKick : 2
             * homeTeamRedCard : 0
             * awayTeamRedCard : 0
             * homeTeamYellowCard : 2
             * awayTeamYellowCard : 1
             * homeTeamShotPositiveDoor : 1
             * awayTeamShotPositiveDoor : 7
             * homeTeamPolarizationDoor : 1
             * awayTeamPolarizationDoor : 6
             * homeTeamData : [{"time":4,"eventType":6,"sequenceStaus":1},{"time":14,"eventType":5,"sequenceStaus":1},{"time":15,"eventType":12,"sequenceStaus":1},{"time":18,"eventType":5,"sequenceStaus":1},{"time":27,"eventType":5,"sequenceStaus":1},{"time":27,"eventType":5,"sequenceStaus":1},{"time":28,"eventType":5,"sequenceStaus":1},{"time":35,"eventType":5,"sequenceStaus":1},{"time":36,"eventType":5,"sequenceStaus":1},{"time":40,"eventType":5,"sequenceStaus":1},{"time":42,"eventType":5,"sequenceStaus":1},{"time":44,"eventType":5,"sequenceStaus":1},{"time":45,"eventType":5,"sequenceStaus":1},{"time":50,"eventType":5,"sequenceStaus":2},{"time":52,"eventType":5,"sequenceStaus":2},{"time":54,"eventType":5,"sequenceStaus":2},{"time":55,"eventType":5,"sequenceStaus":2},{"time":60,"eventType":5,"sequenceStaus":2},{"time":62,"eventType":5,"sequenceStaus":2},{"time":73,"eventType":5,"sequenceStaus":2},{"time":79,"eventType":5,"sequenceStaus":2},{"time":83,"eventType":5,"sequenceStaus":2},{"time":85,"eventType":5,"sequenceStaus":2},{"time":85,"eventType":10,"sequenceStaus":2},{"time":87,"eventType":5,"sequenceStaus":2}]
             * awayTeamData : [{"time":0,"eventType":5,"sequenceStaus":1},{"time":3,"eventType":10,"sequenceStaus":1},{"time":7,"eventType":6,"sequenceStaus":1},{"time":8,"eventType":6,"sequenceStaus":1},{"time":8,"eventType":10,"sequenceStaus":1},{"time":9,"eventType":10,"sequenceStaus":1},{"time":11,"eventType":5,"sequenceStaus":1},{"time":16,"eventType":5,"sequenceStaus":1},{"time":18,"eventType":5,"sequenceStaus":1},{"time":19,"eventType":5,"sequenceStaus":1},{"time":19,"eventType":12,"sequenceStaus":1},{"time":21,"eventType":5,"sequenceStaus":1},{"time":22,"eventType":5,"sequenceStaus":1},{"time":25,"eventType":5,"sequenceStaus":1},{"time":29,"eventType":5,"sequenceStaus":1},{"time":31,"eventType":10,"sequenceStaus":1},{"time":34,"eventType":5,"sequenceStaus":1},{"time":38,"eventType":5,"sequenceStaus":1},{"time":38,"eventType":10,"sequenceStaus":1},{"time":39,"eventType":5,"sequenceStaus":1},{"time":40,"eventType":5,"sequenceStaus":1},{"time":41,"eventType":6,"sequenceStaus":1},{"time":43,"eventType":5,"sequenceStaus":1},{"time":0,"eventType":5,"sequenceStaus":1},{"time":0,"eventType":10,"sequenceStaus":1},{"time":47,"eventType":5,"sequenceStaus":2},{"time":50,"eventType":5,"sequenceStaus":2},{"time":53,"eventType":5,"sequenceStaus":2},{"time":53,"eventType":5,"sequenceStaus":2},{"time":55,"eventType":5,"sequenceStaus":2},{"time":57,"eventType":5,"sequenceStaus":2},{"time":58,"eventType":5,"sequenceStaus":2},{"time":59,"eventType":5,"sequenceStaus":2},{"time":60,"eventType":5,"sequenceStaus":2},{"time":63,"eventType":5,"sequenceStaus":2},{"time":64,"eventType":6,"sequenceStaus":2},{"time":65,"eventType":6,"sequenceStaus":2},{"time":64,"eventType":5,"sequenceStaus":2},{"time":67,"eventType":5,"sequenceStaus":2},{"time":68,"eventType":5,"sequenceStaus":2},{"time":68,"eventType":5,"sequenceStaus":2},{"time":69,"eventType":12,"sequenceStaus":2},{"time":70,"eventType":6,"sequenceStaus":2},{"time":74,"eventType":5,"sequenceStaus":2},{"time":75,"eventType":5,"sequenceStaus":2},{"time":75,"eventType":6,"sequenceStaus":2},{"time":75,"eventType":5,"sequenceStaus":2},{"time":81,"eventType":5,"sequenceStaus":2},{"time":82,"eventType":5,"sequenceStaus":2},{"time":40,"eventType":1,"sequenceStaus":1},{"time":70,"eventType":1,"sequenceStaus":2},{"time":76,"eventType":1,"sequenceStaus":2}]
             * detailsEventVos : [{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":76,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":70,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":12,"playerName":"","time":69,"sequenceStaus":2},{"matchId":1657475,"sign":1,"eventType":3,"playerName":"","time":63,"sequenceStaus":2},{"matchId":1657475,"sign":0,"eventType":1,"playerName":"","time":40,"sequenceStaus":1},{"matchId":1657475,"sign":0,"eventType":3,"playerName":"","time":22,"sequenceStaus":1},{"matchId":1657475,"sign":0,"eventType":12,"playerName":"","time":19,"sequenceStaus":1},{"matchId":1657475,"sign":1,"eventType":12,"playerName":"","time":15,"sequenceStaus":1},{"matchId":1657475,"sign":1,"eventType":3,"playerName":"","time":11,"sequenceStaus":1}]
             */

            private String leagueNameShort;//联赛名称 ,
            private String homeTeamName; //主队名称
            private String homeTeamFlag; //主队图标
            private String awayTeamName; //客队名称
            private String awayTeamFlag; //: 客队图标
            private int midfieldTime; //中场时间
            private int homeTeamAttack;//主队进攻
            private int awayTeamAttack;//客队进攻
            private int homeTeamDangerousAttack; //主队危险进攻
            private int awayTeamDangerousAttack; // 客队危险进攻
            private int homeTeamBallControl; //主队控球球权
            private int awayTeamBallControl; //客队控球球权
            private int homeTeamCornerKick;//: 主队角球
            private int awayTeamCornerKick;//: 客队角球
            private int homeTeamRedCard; //主队控红牌数
            private int awayTeamRedCard; //客队红牌数 ,
            private int homeTeamYellowCard;//主队黄牌数
            private int awayTeamYellowCard;//: 客队黄牌数
            private int homeTeamShotPositiveDoor;// 主队射正球门
            private int awayTeamShotPositiveDoor;// 客队射正球门
            private int homeTeamPolarizationDoor; // 主队射偏球门
            private int awayTeamPolarizationDoor;// 客队射偏球门
            private List<HomeTeamDataBean> homeTeamData; //主队进攻/射门数据
            private List<AwayTeamDataBean> awayTeamData;// 客队进攻/射门数据 ,
            private List<DetailsEventVosBean> detailsEventVos; //比赛事件详情列表 ,

            public String getLeagueNameShort() {
                return leagueNameShort;
            }

            public void setLeagueNameShort(String leagueNameShort) {
                this.leagueNameShort = leagueNameShort;
            }

            public String getHomeTeamName() {
                return homeTeamName;
            }

            public void setHomeTeamName(String homeTeamName) {
                this.homeTeamName = homeTeamName;
            }

            public String getHomeTeamFlag() {
                return homeTeamFlag;
            }

            public void setHomeTeamFlag(String homeTeamFlag) {
                this.homeTeamFlag = homeTeamFlag;
            }

            public String getAwayTeamName() {
                return awayTeamName;
            }

            public void setAwayTeamName(String awayTeamName) {
                this.awayTeamName = awayTeamName;
            }

            public String getAwayTeamFlag() {
                return awayTeamFlag;
            }

            public void setAwayTeamFlag(String awayTeamFlag) {
                this.awayTeamFlag = awayTeamFlag;
            }

            public int getMidfieldTime() {
                return midfieldTime;
            }

            public void setMidfieldTime(int midfieldTime) {
                this.midfieldTime = midfieldTime;
            }

            public int getHomeTeamAttack() {
                return homeTeamAttack;
            }

            public void setHomeTeamAttack(int homeTeamAttack) {
                this.homeTeamAttack = homeTeamAttack;
            }

            public int getAwayTeamAttack() {
                return awayTeamAttack;
            }

            public void setAwayTeamAttack(int awayTeamAttack) {
                this.awayTeamAttack = awayTeamAttack;
            }

            public int getHomeTeamDangerousAttack() {
                return homeTeamDangerousAttack;
            }

            public void setHomeTeamDangerousAttack(int homeTeamDangerousAttack) {
                this.homeTeamDangerousAttack = homeTeamDangerousAttack;
            }

            public int getAwayTeamDangerousAttack() {
                return awayTeamDangerousAttack;
            }

            public void setAwayTeamDangerousAttack(int awayTeamDangerousAttack) {
                this.awayTeamDangerousAttack = awayTeamDangerousAttack;
            }

            public int getHomeTeamBallControl() {
                return homeTeamBallControl;
            }

            public void setHomeTeamBallControl(int homeTeamBallControl) {
                this.homeTeamBallControl = homeTeamBallControl;
            }

            public int getAwayTeamBallControl() {
                return awayTeamBallControl;
            }

            public void setAwayTeamBallControl(int awayTeamBallControl) {
                this.awayTeamBallControl = awayTeamBallControl;
            }

            public int getHomeTeamCornerKick() {
                return homeTeamCornerKick;
            }

            public void setHomeTeamCornerKick(int homeTeamCornerKick) {
                this.homeTeamCornerKick = homeTeamCornerKick;
            }

            public int getAwayTeamCornerKick() {
                return awayTeamCornerKick;
            }

            public void setAwayTeamCornerKick(int awayTeamCornerKick) {
                this.awayTeamCornerKick = awayTeamCornerKick;
            }

            public int getHomeTeamRedCard() {
                return homeTeamRedCard;
            }

            public void setHomeTeamRedCard(int homeTeamRedCard) {
                this.homeTeamRedCard = homeTeamRedCard;
            }

            public int getAwayTeamRedCard() {
                return awayTeamRedCard;
            }

            public void setAwayTeamRedCard(int awayTeamRedCard) {
                this.awayTeamRedCard = awayTeamRedCard;
            }

            public int getHomeTeamYellowCard() {
                return homeTeamYellowCard;
            }

            public void setHomeTeamYellowCard(int homeTeamYellowCard) {
                this.homeTeamYellowCard = homeTeamYellowCard;
            }

            public int getAwayTeamYellowCard() {
                return awayTeamYellowCard;
            }

            public void setAwayTeamYellowCard(int awayTeamYellowCard) {
                this.awayTeamYellowCard = awayTeamYellowCard;
            }

            public int getHomeTeamShotPositiveDoor() {
                return homeTeamShotPositiveDoor;
            }

            public void setHomeTeamShotPositiveDoor(int homeTeamShotPositiveDoor) {
                this.homeTeamShotPositiveDoor = homeTeamShotPositiveDoor;
            }

            public int getAwayTeamShotPositiveDoor() {
                return awayTeamShotPositiveDoor;
            }

            public void setAwayTeamShotPositiveDoor(int awayTeamShotPositiveDoor) {
                this.awayTeamShotPositiveDoor = awayTeamShotPositiveDoor;
            }

            public int getHomeTeamPolarizationDoor() {
                return homeTeamPolarizationDoor;
            }

            public void setHomeTeamPolarizationDoor(int homeTeamPolarizationDoor) {
                this.homeTeamPolarizationDoor = homeTeamPolarizationDoor;
            }

            public int getAwayTeamPolarizationDoor() {
                return awayTeamPolarizationDoor;
            }

            public void setAwayTeamPolarizationDoor(int awayTeamPolarizationDoor) {
                this.awayTeamPolarizationDoor = awayTeamPolarizationDoor;
            }

            public List<HomeTeamDataBean> getHomeTeamData() {
                return homeTeamData;
            }

            public void setHomeTeamData(List<HomeTeamDataBean> homeTeamData) {
                this.homeTeamData = homeTeamData;
            }

            public List<AwayTeamDataBean> getAwayTeamData() {
                return awayTeamData;
            }

            public void setAwayTeamData(List<AwayTeamDataBean> awayTeamData) {
                this.awayTeamData = awayTeamData;
            }

            public List<DetailsEventVosBean> getDetailsEventVos() {
                return detailsEventVos;
            }

            public void setDetailsEventVos(List<DetailsEventVosBean> detailsEventVos) {
                this.detailsEventVos = detailsEventVos;
            }

            public static class HomeTeamDataBean {
                /**
                 * time : 4
                 * eventType : 6
                 * sequenceStaus : 1
                 */

                private int time; //事件触发时间
                private int eventType; //事件类型(1:入球,2:红牌,3:黄牌,4:进攻时间,5:危险进攻时间,6:射正球门时间,
                // 7:点球,8:乌龙,9:两黄变红,10:射偏球门时间,11:换人,12:角球,15:中场时间事件)
                private int sequenceStaus; //场次状态(1上半场,2下半场) ,

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getEventType() {
                    return eventType;
                }

                public void setEventType(int eventType) {
                    this.eventType = eventType;
                }

                public int getSequenceStaus() {
                    return sequenceStaus;
                }

                public void setSequenceStaus(int sequenceStaus) {
                    this.sequenceStaus = sequenceStaus;
                }
            }

            public static class AwayTeamDataBean {
                /**
                 * time : 0
                 * eventType : 5
                 * sequenceStaus : 1
                 */

                private int time; //事件触发时间
                private int eventType;//事件类型(1:入球,2:红牌,3:黄牌,4:进攻时间,5:危险进攻时间,6:射正球门时间,
                // 7:点球,8:乌龙,9:两黄变红,10:射偏球门时间,11:换人,12:角球,15:中场时间事件)
                private int sequenceStaus; //场次状态(1上半场,2下半场) ,

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getEventType() {
                    return eventType;
                }

                public void setEventType(int eventType) {
                    this.eventType = eventType;
                }

                public int getSequenceStaus() {
                    return sequenceStaus;
                }

                public void setSequenceStaus(int sequenceStaus) {
                    this.sequenceStaus = sequenceStaus;
                }
            }

            public static class DetailsEventVosBean {
                /**
                 * matchId : 1657475
                 * sign : 0
                 * eventType : 1
                 * playerName :
                 * time : 76
                 * sequenceStaus : 2
                 */

                private int matchId; //赛程id ,
                private int sign; //主客队标志:1:主队事件，0:客队事件 ,
                private int eventType;//事件类型(1:入球,2:红牌,3:黄牌,4:进攻时间,5:危险进攻时间,6:射正球门时间,
                // 7:点球,8:乌龙,9:两黄变红,10:射偏球门时间,11:换人,12:角球,15:中场时间事件)
                private String playerName; // 事件涉及球员 ,
                private int time;// 事件发生时间
                private int sequenceStaus;//场次状态(1上半场,2下半场) ,

                public int getMatchId() {
                    return matchId;
                }

                public void setMatchId(int matchId) {
                    this.matchId = matchId;
                }

                public int getSign() {
                    return sign;
                }

                public void setSign(int sign) {
                    this.sign = sign;
                }

                public int getEventType() {
                    return eventType;
                }

                public void setEventType(int eventType) {
                    this.eventType = eventType;
                }

                public String getPlayerName() {
                    return playerName;
                }

                public void setPlayerName(String playerName) {
                    this.playerName = playerName;
                }

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public int getSequenceStaus() {
                    return sequenceStaus;
                }

                public void setSequenceStaus(int sequenceStaus) {
                    this.sequenceStaus = sequenceStaus;
                }
            }
        }
    }
}
