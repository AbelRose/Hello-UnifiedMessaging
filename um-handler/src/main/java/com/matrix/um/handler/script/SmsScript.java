package com.matrix.um.handler.script;

import com.matrix.um.handler.domain.SmsParam;
import com.matrix.um.support.domain.SmsRecord;

import java.util.List;

/**
 * 短信脚本 接口
 *
 * @author yihaosun
 * @date 2022/7/5 17:19
 */
public interface SmsScript {
    /**
     * 发送短信
     * @param smsParam smsParam
     * @return 渠道商接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam);
}