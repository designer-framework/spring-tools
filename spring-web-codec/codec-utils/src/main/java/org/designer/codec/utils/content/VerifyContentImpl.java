package org.designer.codec.utils.content;

import org.designer.codec.utils.GetSign;
import org.designer.codec.utils.info.VerifyInfo;
import org.springframework.context.ApplicationContext;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/24 23:43
 * @description 加解密流程上下文
 */
public class VerifyContentImpl<R extends GetSign<S>, V extends VerifyInfo, S> extends AbstractContent<R, V, S> implements Verify<Boolean> {

    private final R content;

    public VerifyContentImpl(ApplicationContext applicationContext, R content, V verifyProperty, Convert<R, V, S> convert) {
        super(applicationContext, verifyProperty, convert);
        this.content = content;
    }

    @Override
    public Boolean getVerifyResult() {
        S sign = content.getSign();
        assert sign != null : "签名格式错误";
        return sign.equals(convert.convertContent(content, codecProperty));
    }

}
