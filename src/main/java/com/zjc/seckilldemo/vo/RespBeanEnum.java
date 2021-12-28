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
    ORDER_NOT_EXIST(500503,"订单不存在"),;
    private final Integer code;
    private final String message;
}
