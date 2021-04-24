package  org.designer.app.web.util;

import lombok.extern.log4j.Log4j2;
import org.designer.app.stream.SocketMessageUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 6:53
 */
@Log4j2
public class SelectorHandler {

    public static void handler(SelectionKey selectionKey, Selector selector) throws IOException {
        if (selectionKey.isConnectable()) {
            log.info("0");
        } else if (selectionKey.isAcceptable()) {
            log.info("1");
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = channel.accept();
            SocketMessageUtils.chat(socketChannel.socket());
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            log.info("2");
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            Socket socket = channel.socket();
            channel.configureBlocking(false);
            SocketMessageUtils.chat(socket);
            socket.getChannel().register(selector, SelectionKey.OP_WRITE);
        } else if (selectionKey.isWritable()) {
            log.info("3");
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel accept = channel.accept();
            accept.register(selector, SelectionKey.OP_READ);
            SocketMessageUtils.chat(channel.accept().socket());
        } else if (selectionKey.isValid()) {
            log.info("4");
        }
    }

}
