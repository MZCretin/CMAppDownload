package com.roll.codemao.app.data.api;

/**
 * Created by cretin on 2018/3/29.
 * h5页面常量
 */

public class URLConstant {
    /**
     * 首页新手专区
     */
    public static final String HOME_NEWUSER = ApiConfig.BASE_URL + "/weixin/advert/newhand?is_app=1";

    /**
     * 首页安全保障
     */
    public static final String HOME_SAFE = ApiConfig.BASE_URL + "/weixin/about/appsafe?is_app=1";
    /**
     * 注册协议
     */
    public static final String REGISTER_PROTOCOL = ApiConfig.BASE_URL + "/weixin/about/reg_agreement.html";
    /**
     * 新浪支付协议
     */
    public static final String PAY_PROTOCOL = ApiConfig.BASE_URL + "/weixin/about/pay_agreement.html";
    /**
     * 关于房融界
     */
    public static final String ABOUT_FRJIE = ApiConfig.BASE_URL + "/Weixin/Find/f_newabout";
    /**
     * 媒体报道详情
     */
    public static final String MEITIBAODAO_DETAIL = ApiConfig.BASE_URL + "/Weixin/find/detail_notice";

    /**
     * 常见问题
     */
    public static final String COMMOM_QUESTION = ApiConfig.BASE_URL + "/Weixin/About/help?is_app=1";

    /**
     * 常见问题
     */
    public static final String OTHER_AWARD_DETAILS = ApiConfig.BASE_URL + "/Weixin/Wozi/other_price_detail";

    /**
     * 体验金详情
     */
    public static final String EXPERIENCE_MONEY = ApiConfig.BASE_URL + "/weixin/advert/experience?is_app=1";

    /**
     * 积分抽奖页面
     */
    public static final String JIFEN_LOTTERY = ApiConfig.BASE_URL + "/weixin/mall/lottery";

    /**
     * 夺宝规则
     */
    public static final String DUOBAO_RULE = ApiConfig.BASE_URL + "/weixin/mall/duobao_rule";

    /**
     * 中奖计算
     */
    public static final String DUOBAO_CALCULATE = ApiConfig.BASE_URL + "/weixin/mall/duobao_calculate?id=";

    /**
     * 了解存管详情
     */
    public static final String CUNGUAN_DETAIL = ApiConfig.BASE_URL + "/Weixin/advert/special_cunguan?isApp=1";

    /**
     * 登录账户
     */
    public static final String LOGIN_USER = ApiConfig.BASE_URL + "/Weixin/Member/change_login_account?is_app=1";

    /**
     * 充值页面其他方式
     */
    public static final String CHONGZHI_OTHER_WAY = ApiConfig.BASE_URL + "/Weixin/Charge/transfer_accounts?is_app=1";

    /**
     * 蜗仔空间入口
     */
    public static final String WZKJ = ApiConfig.BASE_URL + "/Weixin/feed/index?is_app=1";

    /**
     * 大风控数据
     */
    public static final String DFKSJ = ApiConfig.BASE_URL + "/weixin/advert/special_fengkong?is_app=1";

    /**
     * 运营报告
     */
    public static final String YYBG = ApiConfig.BASE_URL + "/weixin/tinvest/business?is_app=1";

    /**
     * 银行存管
     */
    public static final String YHCG = ApiConfig.BASE_URL + "/weixin/tinvest/bank-deposit?is_app=1";

    /**
     * 信息纰漏
     */
    public static final String XXPL = ApiConfig.BASE_URL + "/weixin/tinvest/info_disclosure?is_app=1";
}
