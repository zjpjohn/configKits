package com.zjp.config.zk;

import java.util.Map;

/**
 * author:zjprevenge
 * time: 2016/8/1
 * copyright all reserved
 */
public interface DataHandle {
    /**
     * 数据处理
     *
     * @param propsMap     数据
     * @param zkNodeAction zk节点操作类型
     */
    void handle(Map<String, String> propsMap, ZKNodeAction zkNodeAction);
}
