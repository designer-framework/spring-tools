package org.designer.di.configuration;

import lombok.extern.log4j.Log4j2;
import org.designer.di.annotation.EnabledAutoFactoryDi;
import org.designer.di.enums.NamingType;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 20:36
 */
@Log4j2
public class ImportFactoryDiSelector implements ImportSelector {

    private static final String ENABLE = "enable";

    private static final String HANDLER_TYPE = "type";

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> list = new LinkedList<>();
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnabledAutoFactoryDi.class.getName());
        if (annotationAttributes != null && Boolean.parseBoolean(String.valueOf(annotationAttributes.get(ENABLE)))) {
            switch (NamingType.valueOf(String.valueOf(annotationAttributes.get(HANDLER_TYPE)))) {
                case CUSTOMIZE:
                    list.add(CustomizeStrategyHandlerSelector.class.getName());
                    break;
                default:
                    break;
            }
            list.add(AutoFactoryFactoryDiConfiguration.class.getName());
            return list.toArray(new String[0]);
        } else {
            log.warn("提示: " + annotationMetadata.getClassName() + ", @EnabledAutoFactoryDi注解未生效, 可能导致空指针错误, 如需生效请将" + ENABLE + "修改为tue");
        }
        return new String[0];
    }

}
