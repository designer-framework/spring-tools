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
public class ConvertBeanToAppendString<T> implements Convert<T, CodecBaseInfo, String> {

    private DefaultBeanConvertToParam defaultBeanConvertToParam;

    public ConvertBeanToAppendString() {
        defaultBeanConvertToParam = new DefaultBeanConvertToParam();
    }

    public ConvertBeanToAppendString(DefaultBeanConvertToParam defaultBeanConvertToParam) {
        this.defaultBeanConvertToParam = new DefaultBeanConvertToParam();
    }

    public DefaultBeanConvertToParam getDefaultBeanConvertToParam() {
        return defaultBeanConvertToParam;
    }

    public void setDefaultBeanConvertToParam(DefaultBeanConvertToParam defaultBeanConvertToParam) {
        this.defaultBeanConvertToParam = defaultBeanConvertToParam;
    }

    @Override
    public String convertContent(T content, CodecBaseInfo codecBaseInfo) {
        return defaultBeanConvertToParam.generateParamStr(content);
    }
}
