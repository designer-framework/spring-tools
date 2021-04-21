package org.designer.thread.report;

import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 22:50
 */
public class ReportMap<K, T> extends LinkedMultiValueMap<K, T> implements Report<K, T> {

    private static final long serialVersionUID = -4656831751786181978L;

    @Override
    public int getSizeByStatus(K jobStatus) {
        return Objects.requireNonNull(get(jobStatus)).size();
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
