package org.designer.thread.entity;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/23 23:31
 */
public interface JobInfo {

    String getBatchId();

    String getJobId();

    LocalDateTime getCreateTime();

}
