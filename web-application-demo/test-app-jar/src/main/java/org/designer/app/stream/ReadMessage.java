package  org.designer.app.stream;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 6:25
 */
@Log4j2
public class ReadMessage {

    public static String readHtml(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (StringUtils.isEmpty(line.trim())) {
                break;
            }
            stringBuffer.append(line);
            stringBuffer.append("\r\n");
        }
        String html = stringBuffer.toString();
        log.info(html);
        return html;
    }

}
