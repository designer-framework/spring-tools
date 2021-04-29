package org.designer.web.encryption.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.designer.web.encryption.enums.DecryptBodyMethodEnum;


/**
 * @author Designer
 * @version V1.0.0
 * @date 2020/3/13 17:20
 * @description 通过Controller类或者方法得到加密解密注解信息，构建成解密属性对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecryptAnnotationInfoBean {


    /**
     * 解密方式
     */
    private DecryptBodyMethodEnum decryptBodyMethodEnum;

    /**
     * 解密所需私钥
     */
    private String key;

}
