package com.matrix.um.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * channel->Handler的映射关系
 *
 * @author yihaosun
 * @date 2022/7/5 17:38
 */
@Component
public class HandlerHolder {

    private Map<Integer, Handler> handlers = new HashMap<>(32);

    public void putHandler(Integer channelCode, Handler handler) {
        handlers.put(channelCode, handler);
    }

    public Handler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}