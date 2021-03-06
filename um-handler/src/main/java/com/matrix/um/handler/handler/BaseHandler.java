package com.matrix.um.handler.handler;

import com.matrix.um.common.enums.AnchorState;
import com.matrix.um.common.domain.AnchorInfo;
import com.matrix.um.common.domain.TaskInfo;
import com.matrix.um.support.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author yihaosun
 * @date 2022/7/5 17:38
 */
public abstract class BaseHandler implements Handler {
    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;

    @Autowired
    private HandlerHolder handlerHolder;

    @Autowired
    private LogUtils logUtils;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            logUtils.print(AnchorInfo.builder().state(AnchorState.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            return;
        }
        logUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo taskInfo
     * @return boolean
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
