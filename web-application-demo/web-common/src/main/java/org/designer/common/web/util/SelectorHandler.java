package org.designer.common.web.util;

import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;
import org.designer.common.context.Context;
import org.designer.common.stream.SocketMessageUtils;
import org.designer.common.utils.MethodInvoke;
import org.designer.common.utils.UrlUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 6:53
 */
@Log4j2
public class SelectorHandler {

    private final Map<String, Context> appsContext = new HashMap<>();

    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public SelectorHandler() {
    }

    public void putAppContext(String appName, Context appContainer) {
        synchronized (appsContext) {
            appsContext.put(appName, appContainer);
        }
    }

    public void handler(SelectionKey selectionKey, Selector selector) throws Exception {
        if (selectionKey.isAcceptable()) {
            int current = atomicInteger.get();
            while (!atomicInteger.compareAndSet(current, current + 1)) {
            }
            handlerAcceptable(selector, selectionKey);
        } else if (selectionKey.isReadable()) {
            log.info(atomicInteger.get());
            handlerReadable(selector, selectionKey);
        } else if (selectionKey.isWritable()) {
            handlerWritable(selector, selectionKey);
        }
    }

    private void handlerAcceptable(Selector selector, SelectionKey publicSelectionKey) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("1. 准备读取数据!");
        ServerSocketChannel channel = (ServerSocketChannel) publicSelectionKey.channel();
        SocketChannel socketChannel = channel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * @param selector
     * @param publicSelectionKey
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private void handlerReadable(Selector selector, SelectionKey publicSelectionKey) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("2. 开始读取数据!");
        //返回刚创建的通道
        SocketChannel socketChannel = (SocketChannel) publicSelectionKey.channel();
        Tuple2<String, String> html = SocketMessageUtils.readHtml(socketChannel);
        //通过URI找到上下文
        Context context = appsContext.get(UrlUtils.getAppName(html._1()));
        if (context == null) {
            SocketMessageUtils.writeHtml(socketChannel, "404", 404);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            log.info("3. 找到上下文!");
            //从上下文找到对应的方法
            MethodInvoke appRequestMapping = context.getAppRequestMapping(UrlUtils.getPath(html._1()));
            if (appRequestMapping == null) {
                SocketMessageUtils.write404(socketChannel);
                socketChannel.register(selector, SelectionKey.OP_READ);
            } else {
                //调用方法
                log.info("4. 找到RequestMapping!");
                Object result = appRequestMapping.invoke(html._2());
                socketChannel.register(selector, SelectionKey.OP_WRITE, result);
            }
        }
        log.info("5. 数据读取完毕!");
    }


    /**
     * 写之前从Socket附件中读取数据, 然后在写之前做一些判断等逻辑, 最后将数据发送给用户.
     * 写完后将Socket重新注册到通道, 等待用户再次发送消息
     *
     * @param selector
     * @param publicSelectionKey
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private void handlerWritable(Selector selector, SelectionKey publicSelectionKey) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("6. 开始写数据!");
        SocketChannel socketChannel = (SocketChannel) publicSelectionKey.channel();
        SocketMessageUtils.writeHtml(socketChannel, String.valueOf(publicSelectionKey.attachment()), 200);
        publicSelectionKey.attach(null);
        socketChannel.close();
        //socketChannel.register(selector, SelectionKey.OP_READ);
        log.info("7. 写数据完毕!");
    }

}
