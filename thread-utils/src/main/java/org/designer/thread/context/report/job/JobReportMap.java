package org.designer.thread.context.report.job;

import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 22:50
 */
public class JobReportMap<K, T> extends LinkedMultiValueMap<K, T> {

    private static final long serialVersionUID = -4656831751786181978L;

    public int getSizeByKey(K jobStatus) {
        return get(jobStatus) != null ? get(jobStatus).size() : 0;
    }

    /**
     * 所有任务数量
     *
     * @return
     */
    @Override
    public int size() {
        return values()
                .stream()
                .map(List::size)
                .mapToInt(value -> value)
                .sum();
    }


}
