package com.matrix.um.handler.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.matrix.um.common.domain.TaskInfo;
import com.matrix.um.common.dto.SmsContentModel;
import com.matrix.um.common.enums.ChannelType;
import com.matrix.um.handler.domain.SmsParam;
import com.matrix.um.handler.script.SmsScript;
import com.matrix.um.support.dao.SmsRecordDao;
import com.matrix.um.support.domain.SmsRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yihaosun
 * @date 2022/7/5 17:52
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler implements Handler {

    public SmsHandler(SmsRecordDao smsRecordDao, SmsScript smsScript) {
        channelCode = ChannelType.SMS.getCode();
        this.smsRecordDao = smsRecordDao;
        this.smsScript = smsScript;
    }

    private final SmsRecordDao smsRecordDao;

    private final SmsScript smsScript;


    @Override
    public boolean handler(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();
        try {
            List<SmsRecord> recordList = smsScript.send(smsParam);
            if (!CollUtil.isEmpty(recordList)) {
                smsRecordDao.saveAll(recordList);
            }
            return true;
        } catch (Exception e) {
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }

    /**
     * 如果有输入链接，则把链接拼在文案后
     * <p>
     * PS: 这里可以考虑将链接 转 短链
     * PS: 如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }
}
