package org.designer.codec.utils.v1.annotation;

import org.designer.codec.utils.v1.codec.enums.RequestOperatorType;
import org.designer.codec.utils.v1.codec.enums.ResponseOperatorType;

import java.lang.annotation.*;

/**
 * @description:
 * @author: Designer
 * @date : 2022/1/22 15:28
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Codec {

    /**
     * request加密
     *
     * @return
     */
    RequestOperatorType reqOperator();

    /**
     * response加密
     *
     * @return
     */
    ResponseOperatorType resOperator();

}
