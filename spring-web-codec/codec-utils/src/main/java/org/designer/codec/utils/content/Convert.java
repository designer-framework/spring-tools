package org.designer.codec.utils.content;


import org.designer.codec.utils.info.CodecBaseInfo;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/24 23:51
 * @description 实体类转换规则
 */
@FunctionalInterface
public interface Convert<T, P extends CodecBaseInfo, V> {

    /**
     * @param content 待加解密的对象
     * @param p       调用加解密会用到的参数
     * @return 加解密后得到的结果
     */
    V convertContent(T content, P p);


}

