package com.matrix.um.handler.handler;

import com.matrix.um.common.domain.TaskInfo;

/**
 * @author yihaosun
 * @date 2022/7/5 17:39
 */
public interface Handler {
    /**
     * 处理器
     * @param taskInfo taskInfo
     */
    void doHandler(TaskInfo taskInfo);
}