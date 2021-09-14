package org.designer.thread.report.job;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:05
 */
public interface JobReport<K, V> {

    /**
     * 通过任务的key获取任务总数量
     *
     * @param jobStatus
     * @return
     */
    int getSizeByKey(K jobStatus);

    Map<String, List<V>> getExceptionInfo(K jobStatus);

    List<V> getJobByStatus(K jobStatus);

    /**
     * 任务总数量[仅供参考]
     *
     * @return
     */
    int size();

    JobReport<K, V> waitResult() throws InterruptedException;

}
