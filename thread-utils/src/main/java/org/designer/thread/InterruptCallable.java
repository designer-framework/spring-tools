package org.designer.thread;


import lombok.extern.log4j.Log4j2;
import org.designer.thread.interrupt.BaseInterrupt;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @description: 邮件线程
 * @author: Designer
 * @date : 2021/3/8 0:52
 */
@Log4j2
public class InterruptCallable<T> implements Callable<Optional<T>> {

    private final String jobName;

    private final Callable<Optional<T>> optionalCallable;

    private final BaseInterrupt baseInterrupt;

    public InterruptCallable(BaseInterrupt baseInterrupt, Callable<Optional<T>> optionalCallable, String jobName) {
        this.optionalCallable = optionalCallable;
        this.jobName = jobName;
        this.baseInterrupt = baseInterrupt;
    }

    @Override
    public Optional<T> call() throws Exception {
        if (baseInterrupt.getInterrupt()) {
            log.debug("线程池被关闭, 任务{}被丢弃！", jobName);
            return Optional.empty();
        } else {
            Optional<T> streamList = optionalCallable.call();
            if (streamList.isPresent()) {
                synchronized (baseInterrupt) {
                    baseInterrupt.interrupt();
                }
                return streamList;
            }
            return Optional.empty();
        }
    }

}

