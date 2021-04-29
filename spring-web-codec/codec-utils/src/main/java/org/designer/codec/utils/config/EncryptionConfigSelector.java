package org.designer.codec.utils.config;

import lombok.extern.log4j.Log4j2;
import org.designer.codec.utils.annotation.EnableEncryptTools;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/19 0:35
 * @description
 */
@Log4j2
public class EncryptionConfigSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        String[] imports;
        Class<EnableEncryptTools> enableEncryptUtilsClass = EnableEncryptTools.class;
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(annotationMetadata.getAnnotationAttributes(enableEncryptUtilsClass.getName(), false));
        if (annotationAttributes.containsKey("enable") && annotationAttributes.getBoolean("enable")) {
            imports = new String[]{EncryptionConfig.class.getName()};
        } else {
            log.warn("[" + EncryptionConfig.class.getName() + "]未加载，如需加载请将[{}]enable参数设置为true", enableEncryptUtilsClass.getName());
            imports = new String[0];
        }
        return imports;
    }
}
