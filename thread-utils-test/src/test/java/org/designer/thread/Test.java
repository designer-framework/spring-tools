package org.designer.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: Designer
 * @date : 2021/11/30 0:47
 */
public class Test {

    //
    static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        try (ThreadUtils instance = ThreadUtils.getInstance()) {
            List<Object> eval = instance.eval(Arrays.asList(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return 123;
                }
            }), o -> {
                atomicInteger.incrementAndGet();
            });
            eval.forEach(o -> {
                System.out.println(o);
            });
            System.out.println(atomicInteger.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
