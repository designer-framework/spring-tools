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
        log.info("x ???????????????!");
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
        //????????????????????????
        SocketChannel socketChannel = (SocketChannel) publicSelectionKey.channel();
        try {
            Tuple2<String, String> html = SocketMessageUtils.readHtml(socketChannel);
            //??????URI???????????????
            AppContext appContext = appsContext.get(UrlUtils.getAppName(html._1()));
            if (appContext == null) {
                SocketMessageUtils.writeHtml(socketChannel, "404", 404);
            } else {
                log.info("3. ???????????????!");
                //?????????????????????????????????
                MethodInvoke appRequestMapping = appContext.getAppRequestMapping(UrlUtils.getPath(html._1()));
                if (appRequestMapping == null) {
                    SocketMessageUtils.write404(socketChannel);
                    //socketChannel.register(selector, SelectionKey.OP_READ);
                } else {
                    //????????????
                    log.info("4. ??????RequestMapping!");
                    Object result = appRequestMapping.invoke(html._2());
                    publicSelectionKey.attach(result);
                    handlerWritable(selector, publicSelectionKey);
                    log.info("5. ???????????????!");
                }
            }
        } finally {
            log.info("6. ??????????????????!");
            socketChannel.close();
        }
    }


    /**
     * ????????????Socket?????????????????????, ??????????????????????????????????????????, ??????????????????????????????.
     * ????????????Socket?????????????????????, ??????????????????????????????
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
        log.info("7. ???????????????!");
        SocketChannel socketChannel = (SocketChannel) publicSelectionKey.channel();
        SocketMessageUtils.writeHtml(socketChannel, String.valueOf(publicSelectionKey.attachment()), 200);
        publicSelectionKey.attach(null);
        //socketChannel.register(selector, SelectionKey.OP_READ);
        log.info("8. ???????????????!");
    }

}
