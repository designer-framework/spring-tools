package org.designer.common.web.server;

import lombok.extern.log4j.Log4j2;
import org.designer.common.web.util.SelectorHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 5:38
 */
@Log4j2
public class Server extends Thread {

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_APP_NAME = "DEFAULT-" + DEFAULT_PORT;
    /**
     * 服务端口
     */
    private final int port;
    private final SelectorHandler selectorHandler;

    /**
     * 不手写线程池了
     */
    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public Server(SelectorHandler selectorHandler, int port, String appName) {
        super(appName + "-" + port);
        this.selectorHandler = selectorHandler;
        this.port = port;
        Runtime.getRuntime().addShutdownHook(new Thread(EXECUTOR::shutdown));
    }

    public Server(SelectorHandler selectorHandler, int port) {
        super(DEFAULT_APP_NAME);
        this.selectorHandler = selectorHandler;
        this.port = port;
        Runtime.getRuntime().addShutdownHook(new Thread(EXECUTOR::shutdown));
    }

    private static Selector newSelector(int port) throws IOException {
        //打开通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //NIO
        serverSocketChannel.configureBlocking(false);
        //端口绑定
        serverSocketChannel.bind(new InetSocketAddress(port));
        //选择器
        Selector selector = Selector.open();
        //判断通道中是否存在选择器, 如果不存在选择器, 则将通道注册到选择器中, 将通道和选择器包装成SelectionKey
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        return selector;
    }

    @Override
    public void run() {
        startSelectorPolling();
    }

    public void startSelectorPolling() {
        try {
            Selector selector = newSelector(port);
            while (true) {
                //阻塞,直到有请求进来
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    //取出选择器[和我们刚创建的选择器不是一个对象]
                    SelectionKey publicSelectionKey = iterator.next();
                    iterator.remove();
                    try {
                        selectorHandler.handler(publicSelectionKey, selector);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void startWhilePolling(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));
            while (true) {
                //阻塞
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    EXECUTOR.execute(() -> {
                        try {
                            SocketMessageUtils.write404(socket.getOutputStream());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
