package org.designer.thread.report.job;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 1:05
 */
public interface JobReportContext<K, V> extends JobReport<K, V> {

    /**
     * 讲任务处理结果提交至报告中
     *
     * @param tJobResult
     */
    void submitReport(V tJobResult);

    void start();


    /**
     * 呈从线程池获取已完成的任务, 没有则返回空
     *
     * @param count
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    boolean pollJob(int count) throws Exception;

    boolean pollJob(int count, long timeout, TimeUnit timeUnit) throws Exception;

    void waitResult(boolean waitResult) throws InterruptedException;

}
