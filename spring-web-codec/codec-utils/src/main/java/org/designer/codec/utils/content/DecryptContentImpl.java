package org.designer.codec.utils.content;

import org.designer.codec.utils.GetSign;
import org.designer.codec.utils.info.DecryptInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/24 23:43
 * @description 逆向解密实现
 */
public class DecryptContentImpl<S, D extends DecryptInfo, R extends GetSign<S>> extends AbstractContent<S, D, R> implements Content<R> {

    private R content;

    public DecryptContentImpl(ApplicationContext applicationContext, R content, D decryptInfo, Convert<S, D, R> convert) {
        super(applicationContext, decryptInfo, convert);
        this.content = content;
    }

    @Override
    public R getContent() {
        Assert.notNull(content.getSign(), "sign can not be null");
        return convert.convertContent(content.getSign(), codecProperty);
    }

}
