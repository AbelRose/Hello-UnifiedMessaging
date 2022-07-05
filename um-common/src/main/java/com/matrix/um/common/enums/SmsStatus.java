package com.matrix.um.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * SmsStatus
 *
 * @author yihaosun
 * @date 2022/7/5 17:25
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsStatus {
    SEND_SUCCESS(10,"调用渠道接口发送成功"),
    RECEIVE_SUCCESS(20,"用户收到短信(收到渠道短信回执，状态成功)"),
    RECEIVE_FAIL(30, "用户收不到短信(收到渠道短信回执，状态失败)");

    private final Integer code;
    private final String description;
}