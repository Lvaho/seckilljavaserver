package com.zjc.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用状态码
    SUCCESS(200,"success"),
    ERROR(500,"服务端异常"),
    //登录模块5002xx
    SESSION_ERROR(500210,"session不存在或者已经失效"),
    LOGINVO_ERROR(500211,"用户名或者密码错误"),
    BIND_ERROR(500212,"手机号码格式错误"),
    //注册模块5003xx
    ID_ALREADY_REGISTER(500313,"身份证已被使用"),
    MOBILE_ALREADY_REGISTER(500314,"手机号已被注册"),
    //秒杀模块5xx
    EMPTY_STOCK(500501,"库存不足"),
    REPEATE_ERROR(500502,"不能重复秒杀"),
    ORDER_NOT_EXIST(500503,"订单不存在"),
    REQUEST_ILLEGAL(500504,"非法请求"),
    ACCESS_LIMIT_REACHED(500505,"请求次数过多请稍后再试"),
    ORDER_PAY_FAIL(500506,"支付失败，请联系客服处理"),
    ORDER_ALREADY_PAYED(500507,"订单已经支付"),
    //余额模块
    CHARGENUM_SMALLER_THAN_ZERO(500601,"充值金额小于0"),
    DEPOSIT_NOT_ENOUGH_TO_PAY_ORDER(500602,"余额不足以支付订单"),
    SETTLEMENT_FAILED(500603,"秒杀订单结算失败"),
    //准入初筛模块
    USER_DEFAULTER(500701,"用户被列入失信人名单"),
    USER_WORKSTATR_BAD(500702,"用户工作状态异常"),
    USER_OVERDUE(500703,"用户有逾期记录"),
    USER_ACTION_REFUSE(500704,"您无法执行此操作");
    private final Integer code;
    private final String message;
}

