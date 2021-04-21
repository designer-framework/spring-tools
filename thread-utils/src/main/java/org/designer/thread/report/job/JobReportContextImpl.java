package org.designer.thread.report.job;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;
import org.designer.thread.exception.JobStatusException;

/**
 * @description: 任务处理报告上下文
 * @author: Designer
 * @date : 2021/4/20 23:10
 */
@Log4j2
public class JobReportContextImpl<T> implements JobReportContext<JobStatus, JobResult<T>> {

    private final JobReportMap<JobStatus, JobResult<T>> jobReport = new JobReportMap<>();

    @Override
    public void submit(JobResult<T> tJobResult) {
        if (tJobResult.getJobStatus() == JobStatus.SUBMIT) {
            JobResult<T> result = new JobResult<>(tJobResult.getJobName());
            result.exception(new JobStatusException(tJobResult.getJobName()));
            jobReport.add(result.getJobStatus(), tJobResult);
        } else {
            jobReport.add(tJobResult.getJobStatus(), tJobResult);
        }
        tJobResult.end();
        log.debug(tJobResult.toString());
    }

    @Override
    public String toString() {
        return jobReport.toString();
    }

    @Override
    public int getSizeByKey(JobStatus jobStatus) {
        return jobReport.getSizeByKey(jobStatus);
    }

    @Override
    public int size() {
        return jobReport.size();
    }

}
