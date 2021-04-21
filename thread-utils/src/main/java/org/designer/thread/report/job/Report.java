package org.designer.thread.report.job;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:05
 */
public interface Report<K, V> {

    /**
     * 通过任务的key获取任务总数量
     *
     * @param jobStatus
     * @return
     */
    int getSizeByKey(K jobStatus);

    /**
     * 任务总数量[仅供参考]
     *
     * @return
     */
    public int size();

}