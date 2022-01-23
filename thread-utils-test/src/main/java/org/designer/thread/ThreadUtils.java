package org.designer.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @description:
 * @author: Designer
 * @date : 2021/11/30 0:00
 */
public class ThreadUtils implements ThreadUtilsFunction, AutoCloseable {

    private static final int CPU = Runtime.getRuntime().availableProcessors();

    private final CompletionService<Object> completionService;
    private final ThreadPoolExecutor threadPoolExecutor;

    private ThreadUtils(ThreadPoolExecutor threadPoolExecutor) {
        completionService = new ExecutorCompletionService<>(threadPoolExecutor);
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.currentThread();
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                thread.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        Object o = new Object();
        synchronized (o) {
            o.wait(2000);
            thread1.join();
        }
        Thread.sleep(3000);
    }

    public static ThreadUtils getInstance() {
        return new ThreadUtils(
                new ThreadPoolExecutor(
                        CPU
                        , CPU << 1
                        , 10
                        , TimeUnit.SECONDS
                        , new ArrayBlockingQueue<>(10000, false)
                        , new ThreadPoolExecutor.CallerRunsPolicy()
                )
        );
    }

    public <T> List<T> eval(List<Callable<T>> list, Consumer<T> callback) throws InterruptedException, ExecutionException {
        if (list != null && !list.isEmpty()) {
            list.forEach(tCallable -> {
                completionService.submit((Callable<Object>) tCallable);
            });
            int total = 0;
            int size = list.size();
            List<T> ts = new ArrayList<>();
            while (total != size) {
                Future<T> future;
                while ((future = (Future<T>) completionService.poll(1000, TimeUnit.MICROSECONDS)) != null) {
                    total++;
                    if (callback != null) {
                        callback.accept(future.get());
                    }
                    ts.add(future.get());
                }
            }
            return ts;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void evalRunnable(List<Runnable> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(tCallable -> {
                completionService.submit(tCallable, null);
            });
        }
    }

    @Override
    public <T> List<T> eval(List<Callable<T>> list) throws ExecutionException, InterruptedException {
        return eval(list, null);
    }

    @Override
    public void close() throws Exception {
        threadPoolExecutor.shutdown();
    }

}