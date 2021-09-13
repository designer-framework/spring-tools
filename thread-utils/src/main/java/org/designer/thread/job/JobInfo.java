package org.designer.thread.job;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/23 23:31
 */
public interface JobInfo {

    String getJobId();

    String getJobName();

    LocalDateTime getCreateTime();

}
