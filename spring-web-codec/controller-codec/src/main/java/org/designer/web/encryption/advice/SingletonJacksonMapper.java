package org.designer.web.encryption.advice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * activiti-examples-parent
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/3/13 18:58
 * @description
 */
public class SingletonJacksonMapper implements ApplicationContextAware, InitializingBean {

    private static ObjectMapper objectMapper;

    private ApplicationContext applicationContext;

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Jackson2ObjectMapperBuilder builder = applicationContext.getBean(Jackson2ObjectMapperBuilder.class);
        objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
