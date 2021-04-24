package  org.designer.app.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 6:26
 */
public class WriteMessage {

    public static void writeHtml(OutputStream outputStream) throws IOException {
        String msg = "你好小明";
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type: text/html; charset=utf-8");
        printWriter.println("Content-Length:" + msgByte.length);
        printWriter.println();
        printWriter.println(msg);
        printWriter.flush();
    }

}
