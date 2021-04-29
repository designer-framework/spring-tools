package org.designer.codec.utils;

import lombok.extern.log4j.Log4j2;
import org.designer.codec.utils.content.Convert;
import org.designer.codec.utils.content.DecryptContentImpl;
import org.designer.codec.utils.content.EncryptContentImpl;
import org.designer.codec.utils.content.VerifyContentImpl;
import org.designer.codec.utils.info.DecryptInfo;
import org.designer.codec.utils.info.EncryptInfo;
import org.designer.codec.utils.info.VerifyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Designer
 * @title: EncryptUtil
 * @projectName wisdom
 * @description: 桥接模式，适配器模式
 * @date 2019/5/2817:28
 */
@Log4j2
public class EncryptionConvertUtil implements EncryptionConvert {

    private ApplicationContext applicationContext;

    @Autowired
    public EncryptionConvertUtil(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected Boolean afterAssertResult(Boolean result) throws IllegalArgumentException {
        // TODO
        Assert.isTrue(result, "验签失败");
        return result;
    }

    @Override
    public <R extends SetSign<R, S>, S> R encrypt(R content, EncryptInfo encryptInfo, Convert<R, EncryptInfo, S> convert) {
        return new EncryptContentImpl<>(applicationContext, content, encryptInfo, convert).getContent();
    }

    @Override
    public <R extends GetSign<S>, S> R decrypt(R content, DecryptInfo decryptInfo, Convert<S, DecryptInfo, R> convert) {
        return new DecryptContentImpl<>(applicationContext, content, decryptInfo, convert).getContent();
    }

    /**
     * 验签
     */
    @Override
    public <R extends GetSign<S>, S> Boolean verify(R content, VerifyInfo verifyInfo, Convert<R, VerifyInfo, S> convert) {
        return afterAssertResult(new VerifyContentImpl<>(applicationContext, content, verifyInfo, convert).getVerifyResult());
    }

}
