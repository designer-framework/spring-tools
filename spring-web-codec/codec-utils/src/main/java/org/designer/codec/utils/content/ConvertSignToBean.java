package org.designer.codec.utils.content;

import org.designer.codec.utils.DefaultBeanConvertToParam;
import org.designer.codec.utils.info.CodecBaseInfo;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/25 20:59
 * @description 对象拼接成字符串
 */
public class ConvertSignToBean<T> implements Convert<T, CodecBaseInfo, String> {

    public ConvertSignToBean() {
    }

    @Override
    public String convertContent(T content, CodecBaseInfo codecBaseInfo) {
        String result = new DefaultBeanConvertToParam().generateParamStr(content);
        return result;
    }
}
