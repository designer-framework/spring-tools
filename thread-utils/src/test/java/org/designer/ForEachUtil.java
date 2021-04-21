package org.designer;

import lombok.SneakyThrows;
import org.designer.thread.entity.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 0:39
 */
public class ForEachUtil {

    @SneakyThrows
    public static void foreach(int count, Runnable tCallable) {
        CountDownLatch countDownLatch = new CountDownLatch(count);
        List<Thread> threads = new ArrayList<>();
        IntStream.range(0, count)
                .forEach(value -> {
                    Thread thread = new Thread(tCallable);
                    threads.add(thread);
                    countDownLatch.countDown();
                });
        countDownLatch.await();
        threads.forEach(Thread::start);
    }

    @SneakyThrows
    public static List<Job<String>> listThread(int count, Job<String> tCallable) {
        CountDownLatch countDownLatch = new CountDownLatch(count);
        List<Job<String>> threads = new ArrayList<>();
        IntStream.range(0, count)
                .forEach(value -> {
                    threads.add(tCallable);
                    countDownLatch.countDown();
                });
        countDownLatch.await();
        return threads;
    }

}
