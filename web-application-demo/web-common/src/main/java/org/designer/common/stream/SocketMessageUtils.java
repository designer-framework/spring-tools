package org.designer.common.stream;

import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 7:00
 */
@Log4j2
public class SocketMessageUtils {

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String ENTER = "\r\n";
    private static final String ERROR = "ERROR";
    private static final String NOT_FOUNT = "NOT_FOUNT";

    /**
     * Socket流转html
     *
     * @param socketChannel
     * @return
     * @throws IOException
     */
    public static Tuple2<String, String> readHtml(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int readLineLen;
        while ((readLineLen = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            if (byteBuffer.hasRemaining()) {
                byte[] array = byteBuffer.array();
                byteArrayOutputStream.write(array, 0, readLineLen);
                byteBuffer.clear();
            }
        }
        //ByteBuffer转字节流
        return readHtml(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    /**
     * 字节流转html
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static Tuple2<String, String> readHtml(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder headerBody = new StringBuilder();
        String line;
        StringBuilder body = new StringBuilder();
        int bodyLength = 0;
        boolean isHead = true;
        while ((line = bufferedReader.readLine()) != null) {
            //head
            if (isHead) {
                headerBody.append(line);
                headerBody.append(ENTER);
                if (line.startsWith(CONTENT_LENGTH)) {
                    bodyLength = Integer.parseInt(line.split(": ")[1]);
                }
                if (line.equals(ENTER) || "".equals(line)) {
                    isHead = false;
                }
            } else {
                //body
                body.append(line);
                body.append(ENTER);
                if (body.length() == bodyLength) {
                    break;
                }
            }
        }
        String htmlHead = headerBody.toString();
        String htmlBody = body.toString();
        log.debug(htmlHead + htmlBody);
        return new Tuple2<>(htmlHead, htmlBody);
    }

    private static byte[] getByte(String msg) {
        return msg.getBytes(StandardCharsets.UTF_8);
    }

    public static int getByteLength(String msg) {
        return getByte(msg).length;
    }

    public static void write500(SocketChannel socketChannel, String msg) throws IOException {
        writeHtml(socketChannel, NOT_FOUNT, 404);
    }

    public static void write404(SocketChannel socketChannel) throws IOException {
        writeHtml(socketChannel, NOT_FOUNT, 404);
    }

    public static void writeHtml(SocketChannel socketChannel, String msg, int status) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(status);
        builder.append("\r\n");
        builder.append("Content-Type: text/html; charset=utf-8");
        builder.append("\r\n");
        builder.append("Content-Length: ").append(getByteLength(msg));
        builder.append("\r\n");
        builder.append("\r\n");
        builder.append(msg);
        socketChannel.write(ByteBuffer.wrap(builder.toString().getBytes(StandardCharsets.UTF_8)));
    }


}
