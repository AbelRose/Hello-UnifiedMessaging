package com.matrix.um.handler.script;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.matrix.um.common.enums.SmsStatus;
import com.matrix.um.handler.domain.SmsParam;
import com.matrix.um.handler.domain.TencentSmsParam;
import com.matrix.um.support.AccountUtils;
import com.matrix.um.support.domain.SmsRecord;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * TencentSmsScript
 *
 * @author yihaosun
 * @date 2022/7/5 17:17
 */
@Service
@Slf4j
public class TencentSmsScript implements SmsScript {
    private static final Integer PHONE_NUM = 11;
    private static final String SMS_ACCOUNT_KEY = "smsAccount";
    private static final String PREFIX = "sms_";

    private final AccountUtils accountUtils;

    public TencentSmsScript(AccountUtils accountUtils) {
        this.accountUtils = accountUtils;
    }

    @Override
    @SneakyThrows
    public List<SmsRecord> send(SmsParam smsParam) {
        TencentSmsParam tencentSmsParam = accountUtils.getAccount(smsParam.getSendAccount(),
                SMS_ACCOUNT_KEY, PREFIX, TencentSmsParam.builder().build());
        SmsClient client = init(tencentSmsParam);
        SendSmsRequest request = assembleReq(smsParam, tencentSmsParam);
        SendSmsResponse response = client.SendSms(request);
        return assembleSmsRecord(smsParam, response, tencentSmsParam);
    }


    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, SendSmsResponse response, TencentSmsParam tencentSmsParam) {
        if (response == null || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return Collections.emptyList();
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();
        for (SendStatus sendStatus : response.getSendStatusSet()) {
            // 腾讯返回的电话号有前缀，这里取巧直接翻转获取手机号
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(tencentSmsParam.getSupplierId())
                    .supplierName(tencentSmsParam.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .reportContent(sendStatus.getCode())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();
            smsRecordList.add(smsRecord);
        }
        return smsRecordList;
    }

    /**
     * 组装发送短信参数
     */
    private SendSmsRequest assembleReq(SmsParam smsParam, TencentSmsParam account) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(account.getSmsSdkAppId());
        req.setSignName(account.getSignName());
        req.setTemplateId(account.getTemplateId());
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 初始化 client
     *
     * @param account account
     */
    private SmsClient init(TencentSmsParam account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getRegion(), clientProfile);
    }
}
