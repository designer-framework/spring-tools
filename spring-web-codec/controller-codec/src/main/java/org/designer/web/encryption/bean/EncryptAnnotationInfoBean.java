package org.designer.web.encryption.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.designer.web.encryption.enums.EncryptBodyMethodEnum;
import org.designer.web.encryption.enums.SHAEncryptTypeEnum;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncryptAnnotationInfoBean {

    private EncryptBodyMethodEnum encryptBodyMethodEnum;

    private String key;

    private SHAEncryptTypeEnum shaEncryptTypeEnum;

}
