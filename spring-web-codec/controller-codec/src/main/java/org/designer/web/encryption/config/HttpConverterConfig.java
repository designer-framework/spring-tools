package org.designer.web.encryption.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Designer
 * @version V1.0.0
 * @date 2020/6/9 17:10
 * @description 控制层消息转换器
 */
@Configuration
public class HttpConverterConfig implements WebMvcConfigurer {

    /**
     * String消息直接返回，防止进行重复的转JSON操作。通常系统中会有已经定义好的转换器，而且优先级通常比本类的转换器要高
     *
     * @return
     */
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter() {

            @Override
            protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                if (object instanceof String) {
                    Charset charset = getDefaultCharset();
                    StreamUtils.copy((String) object, charset, outputMessage.getBody());
                } else {
                    super.writeInternal(object, type, outputMessage);
                }
            }
        };
    }

    /**
     * HttpMessageConverter 调用链中的一者
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = mappingJackson2HttpMessageConverter();
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        //mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(mediaTypes);
        converters.add(new StringHttpMessageConverter());
        converters.add(converter);
    }
}
