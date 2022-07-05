package com.matrix.um.handler.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * SmsParam
 *
 * @author yihaosun
 * @date 2022/7/5 17:21
 */
@Data
@Builder
public class SmsParam {
    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 需要发送的手机号
     */
    private Set<String> phones;

    /**
     * 发送文案
     */
    private String content;

    /**
     * 发送账号
     */
    private Integer sendAccount;
}