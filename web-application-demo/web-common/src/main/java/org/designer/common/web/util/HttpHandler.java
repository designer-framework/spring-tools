package org.designer.common.web.util;

import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;
import org.designer.common.context.AppContext;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/19 4:14
 */
@Log4j2
public class HttpHandler implements SelectorHandler {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    private final Map<String, AppContext> appsContext = new HashMap<>();

    @Override
    public void putAppContext(String appName, AppContext appContext) {
        synchronized (appsContext) {
            appsContext.put(appName, appContext);
        }
    }

    @Override
    public void handler(SelectionKey selectionKey, Selector selector) throws Exception {
        if (selectionKey.isValid()) {
            if (selectionKey.isAcceptable()) {
                int current = atomicInteger.get();
                while (!atomicInteger.compareAndSet(current, current + 1)) {
                }
                handlerAcceptable(selector, selectionKey);
            } else if (selectionKey.isReadable()) {
                handlerReadable(selector, selectionKey);
            } else if (selectionKey.isWritable()) {
                //handlerWritable(selector, selectionKey);
            } else if (selectionKey.isValid()) {
                //log.debug("valid");
            } else if (selectionKey.isConnectable()) {
                //log.debug("connectable");
            }
        }
    }

    private void handlerAcceptable(Selector selector, SelectionKey publicSelectionKey) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("x 已就绪数据!");
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
        //返回刚创建的通道
        SocketChannel socketChannel = (SocketChannel) publicSelectionKey.channel();
        try {
            Tuple2<String, String> html = SocketMessageUtils.readHtml(socketChannel);
            //通过URI找到上下文
            AppContext appContext = appsContext.get(UrlUtils.getAppName(html._1()));
            if (appContext == null) {
                SocketMessageUtils.writeHtml(socketChannel, "404", 404);
            } else {
                log.info("3. 找到上下文!");
                //从上下文找到对应的方法
                MethodInvoke appRequestMapping = appContext.getAppRequestMapping(UrlUtils.getPath(html._1()));
                if (appRequestMapping == null) {
                    SocketMessageUtils.write404(socketChannel);
                    //socketChannel.register(selector, SelectionKey.OP_READ);
                } else {
                    //调用方法
                    log.info("4. 找到RequestMapping!");
                    Object result = appRequestMapping.invoke(html._2());
                    publicSelectionKey.attach(result);
                    handlerWritable(selector, publicSelectionKey);
                    log.info("5. 注册写事件!");
                }
            }
        } finally {
            log.info("6. 数据读取完毕!");
            socketChannel.close();
        }
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
        log.info("7. 开始写数据!");
        SocketChannel socketChannel = (SocketChannel) publicSelectionKey.channel();
        SocketMessageUtils.writeHtml(socketChannel, String.valueOf(publicSelectionKey.attachment()), 200);
        publicSelectionKey.attach(null);
        //socketChannel.register(selector, SelectionKey.OP_READ);
        log.info("8. 写数据完毕!");
    }

}
