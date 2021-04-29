package org.designer.codec.utils.content;

import org.designer.codec.utils.config.Encrypt;
import org.designer.codec.utils.info.CodecBaseInfo;
import org.designer.codec.utils.utils.Encryption;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/24 23:43
 * @description 抽象对象上下文
 */
public abstract class AbstractContent<A, B extends CodecBaseInfo, C> {

    private static AtomicReference<Encrypt> encryptAtomicReference = new AtomicReference<>();

    protected Convert<A, B, C> convert;

    protected B codecProperty;

    public AbstractContent(ApplicationContext applicationContext, B codecProperty, Convert<A, B, C> convert) {
        this.codecProperty = codecProperty;
        this.convert = convert;
        if (encryptAtomicReference.get() == null) {
            encryptAtomicReference.getAndSet(applicationContext.getBean(Encrypt.class));
        }
    }

    protected Encryption getEncryption(String signType) {
        return Optional.ofNullable(encryptAtomicReference.get().getEncrypt(signType)).orElseThrow(() -> new IllegalArgumentException(String.format("未实现%s加密", signType)));
    }

}
