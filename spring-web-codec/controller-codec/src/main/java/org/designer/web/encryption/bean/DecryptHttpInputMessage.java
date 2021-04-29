package org.designer.web.encryption.bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;


@NoArgsConstructor
@AllArgsConstructor
public class DecryptHttpInputMessage implements HttpInputMessage {

    private InputStream body;

    private HttpHeaders headers;

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
