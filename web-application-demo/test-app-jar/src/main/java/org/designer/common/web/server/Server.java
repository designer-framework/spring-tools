package org.designer.common.web.server;

import lombok.extern.log4j.Log4j2;
import org.designer.common.stream.SocketMessageUtils;
import org.designer.common.web.util.SelectorHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 5:38
 */
@Log4j2
public class Server {

    /**
     * 不手写线程池了
     */
    private final Executor EXECUTOR = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        Server server = new Server();
        server.startSelectorPolling(8080);
    }

    public void startSelectorPolling(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            //
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                //阻塞
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    SelectorHandler.handler(next, selector);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param port
     */
    public void startWhilePolling(int port) {
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
                            SocketMessageUtils.chat(socket);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
