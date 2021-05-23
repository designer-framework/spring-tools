package org.designer.thread.entity;

import lombok.Getter;
import lombok.Setter;
import org.designer.thread.enums.JobStatus;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 20:30
 */
@Getter
@Setter
public class AbstractJobInfo {

    private final String batchId;

    private final String jobId;

    private final LocalDateTime createTime;

    private LocalDateTime endTime;

    private JobStatus jobStatus;

    private long runtime;

    public AbstractJobInfo(String batchId, String jobId, LocalDateTime createTime) {
        this.batchId = batchId;
        this.jobId = jobId;
        this.createTime = createTime;
    }

    public void end() {
        endTime = LocalDateTime.now();
    }


}
