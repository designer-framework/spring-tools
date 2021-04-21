package org.designer.thread.report;

import org.springframework.util.MultiValueMap;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:00
 */
public interface Report<K, V> extends MultiValueMap<K, V> {
    int getSizeByStatus(K jobStatus);
}
