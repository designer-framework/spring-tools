package org.designer.codec.utils.content;

import org.designer.codec.utils.SetSign;
import org.designer.codec.utils.info.EncryptInfo;
import org.springframework.context.ApplicationContext;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/24 23:43
 * @description 加解密流程上下文
 */
public class EncryptContentImpl<R extends SetSign<R, S>, E extends EncryptInfo, S> extends AbstractContent<R, E, S> implements Content<R> {

    private R content;

    public EncryptContentImpl(ApplicationContext applicationContext, R content, E codecProperty, Convert<R, E, S> convert) {
        super(applicationContext, codecProperty, convert);
        this.content = content;
    }

    @Override
    public R getContent() {
        return content.setSign(convert.convertContent(content, codecProperty));
    }

}
