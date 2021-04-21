package org.designer.thread.report;

import lombok.extern.log4j.Log4j2;
import org.designer.thread.entity.JobResult;
import org.designer.thread.enums.JobStatus;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/20 23:10
 */
@Log4j2
public class ReportContextImpl<T> implements ReportContext<T> {

    private final Report<JobStatus, JobResult<T>> report = new ReportMap<>();

    @Override
    public void submit(JobResult<T> tJobResult) {
        if (tJobResult.getJobStatus() == JobStatus.SUBMIT) {
            JobResult<T> result = new JobResult<>(tJobResult.getJobName());
            result.exception(new IllegalStateException());
            report.add(result.getJobStatus(), tJobResult);
        } else {
            report.add(tJobResult.getJobStatus(), tJobResult);
        }
        tJobResult.end();
        log.debug(tJobResult.toString());
    }

    @Override
    public Report<JobStatus, JobResult<T>> getReport() {
        return report;
    }

    @Override
    public String toString() {
        return report.toString();
    }

}
