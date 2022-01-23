package org.designer.thread;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: Designer
 * @date : 2021/11/30 0:00
 */
public interface ThreadUtilsFunction {

    void evalRunnable(List<Runnable> list);

    <T> List<T> eval(List<Callable<T>> list) throws ExecutionException, InterruptedException;

    /**
     * @param call
     * @param <T>
     * @return
     */
    default <T> List<T> eval(Callable<T> call) throws ExecutionException, InterruptedException {
        return eval(Collections.singletonList(call));
    }

}
