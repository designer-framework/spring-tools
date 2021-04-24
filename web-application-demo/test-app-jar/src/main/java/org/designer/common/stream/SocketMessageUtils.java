package org.designer.common.stream;

import java.io.IOException;
import java.net.Socket;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 7:00
 */
public class SocketMessageUtils {

    public static void chat(Socket socket) throws IOException {
        ReadMessage.readHtml(socket.getInputStream());
        WriteMessage.writeHtml(socket.getOutputStream());
    }

}
