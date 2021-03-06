package org.designer.web.encryption.advice;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.designer.web.encryption.annotation.decrypt.*;
import org.designer.web.encryption.bean.DecryptAnnotationInfoBean;
import org.designer.web.encryption.bean.DecryptHttpInputMessage;
import org.designer.web.encryption.config.EncryptBodyProperty;
import org.designer.web.encryption.enums.DecryptBodyMethodEnum;
import org.designer.web.encryption.exception.DecryptBodyFailException;
import org.designer.web.encryption.exception.DecryptMethodNotFoundException;
import org.designer.web.encryption.util.AESEncryptUtil;
import org.designer.web.encryption.util.CheckUtils;
import org.designer.web.encryption.util.DESEncryptUtil;
import org.designer.web.encryption.util.RSA2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Stream;

@Order(1)
@ControllerAdvice
@Log4j2
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    public static final String OBJECT_START_STR = "{";

    @Autowired
    private EncryptBodyProperty encryptBodyProperty;

    public static boolean constantsDecryptAnnotation(MethodParameter methodParameter) {
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        boolean hasEncryptAnnotation = false;
        if (annotations.length > 0) {
            hasEncryptAnnotation = Stream.of(annotations).parallel().anyMatch(annotation ->
                    annotation instanceof DecryptBody ||
                            annotation instanceof SHA256WithRSADecryptBody ||
                            annotation instanceof AESDecryptBody ||
                            annotation instanceof DESDecryptBody ||
                            annotation instanceof RSADecryptBody
            );
        }
        return hasEncryptAnnotation || Stream.of(methodParameter.getMethod().getAnnotations()).parallel().anyMatch(annotation ->
                annotation instanceof DecryptBody ||
                        annotation instanceof SHA256WithRSADecryptBody ||
                        annotation instanceof AESDecryptBody ||
                        annotation instanceof DESDecryptBody ||
                        annotation instanceof RSADecryptBody
        );
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //??????????????????
        return constantsDecryptAnnotation(methodParameter);
        //?????????????????????
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (inputMessage.getBody() == null) {
            return inputMessage;
        }
        String body;
        try {
            body = IOUtils.toString(inputMessage.getBody(), encryptBodyProperty.getEncoding());
        } catch (Exception e) {
            throw new DecryptBodyFailException("??????????????????????????????????????????????????????????????????????????????????????????");
        }
        if (body == null || !body.startsWith(OBJECT_START_STR)) {
            throw new DecryptBodyFailException("is not the object");
        }
        String decryptBody = null;
        DecryptAnnotationInfoBean decryptAnnotationInfoBean = DecryptInnerAnnotationUtils.getDecryptAnnotationInfoBean(methodParameter);
        if (decryptAnnotationInfoBean != null) {
            //??????????????????
            decryptBody = switchDecrypt(body, decryptAnnotationInfoBean);
        }
        if (decryptBody == null) {
            throw new DecryptBodyFailException("????????????????????????????????????????????????");
        }
        try {
            InputStream inputStream = IOUtils.toInputStream(decryptBody, encryptBodyProperty.getEncoding());
            return new DecryptHttpInputMessage(inputStream, inputMessage.getHeaders());
        } catch (Exception e) {
            throw new DecryptBodyFailException("????????????????????????????????????????????????????????????????????????");
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * ?????????????????????????????????
     *
     * @param formatStringBody ?????????????????????
     * @param infoBean         ????????????
     * @return ????????????
     */
    private String switchDecrypt(String formatStringBody, DecryptAnnotationInfoBean infoBean) {
        //???????????????Map
        Map objectMap = JSONObject.parseObject(formatStringBody, Map.class);
        String sign = (String) objectMap.get(encryptBodyProperty.getSignField());
        Assert.hasText(sign, "??????????????????");
        //??????????????????
        DecryptBodyMethodEnum method = infoBean.getDecryptBodyMethodEnum();
        if (method == null) {
            throw new DecryptMethodNotFoundException();
        }
        //?????????????????????????????????
        String key = infoBean.getKey();
        switch (method) {
            case SHA256WithRSA:
                key = CheckUtils.checkAndGetKey(encryptBodyProperty.getPublicKey(), key, method.name() + "-KEY");
                //????????????
                return CheckUtils.verifySignAndReturn(formatStringBody, RSA2Util.buildRSAVerifyByPublicKey(encryptBodyProperty.convertRequestJsonToString(formatStringBody), key, sign));
            case DES:
                key = CheckUtils.checkAndGetKey(encryptBodyProperty.getPrivateKey(), key, method.name() + "-KEY");
                return DESEncryptUtil.decrypt(encryptBodyProperty.convertRequestJsonToString(formatStringBody), key);
            case AES:
                key = CheckUtils.checkAndGetKey(encryptBodyProperty.getPrivateKey(), key, method.name() + "-KEY");
                return AESEncryptUtil.decrypt(encryptBodyProperty.convertRequestJsonToString(formatStringBody), key);
            default:
                throw new DecryptBodyFailException();
        }
    }

    public static class DecryptInnerAnnotationUtils {

        public static DecryptAnnotationInfoBean getDecryptAnnotationInfoBean(MethodParameter methodParameter) {
            DecryptAnnotationInfoBean methodAnnotation = getMethodAnnotation(methodParameter);
            return methodAnnotation == null ? getClassAnnotation(methodParameter.getDeclaringClass()) : methodAnnotation;
        }

        /**
         * ??????Controller??????????????????????????????
         *
         * @param methodParameter ?????????method??????
         * @return ??????????????????
         */
        private static DecryptAnnotationInfoBean getMethodAnnotation(MethodParameter methodParameter) {

            if (methodParameter.getMethod().isAnnotationPresent(DecryptBody.class)) {
                DecryptBody decryptBody = methodParameter.getMethodAnnotation(DecryptBody.class);
                return DecryptAnnotationInfoBean.builder()
                        .decryptBodyMethodEnum(decryptBody.value())
                        .key(decryptBody.otherKey())
                        .build();
            }

            if (methodParameter.getMethod().isAnnotationPresent(SHA256WithRSADecryptBody.class)) {
                return DecryptAnnotationInfoBean.builder()
                        //????????????
                        .decryptBodyMethodEnum(DecryptBodyMethodEnum.SHA256WithRSA)
                        //????????????
                        .key(methodParameter.getMethodAnnotation(SHA256WithRSADecryptBody.class).otherKey())
                        .build();
            }

            if (methodParameter.getMethod().isAnnotationPresent(DESDecryptBody.class)) {
                return DecryptAnnotationInfoBean.builder()
                        //????????????
                        .decryptBodyMethodEnum(DecryptBodyMethodEnum.DES)
                        //????????????
                        .key(methodParameter.getMethodAnnotation(DESDecryptBody.class).otherKey())
                        .build();
            }
            if (methodParameter.getMethod().isAnnotationPresent(AESDecryptBody.class)) {
                return DecryptAnnotationInfoBean.builder()
                        .decryptBodyMethodEnum(DecryptBodyMethodEnum.AES)
                        .key(methodParameter.getMethodAnnotation(AESDecryptBody.class).otherKey())
                        .build();
            }
            return null;
        }

        /**
         * ??????Controller???????????????????????????
         *
         * @param clazz ????????????
         * @return ??????????????????
         */
        private static DecryptAnnotationInfoBean getClassAnnotation(Class clazz) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof DecryptBody) {
                        DecryptBody decryptBody = (DecryptBody) annotation;
                        return DecryptAnnotationInfoBean.builder()
                                .decryptBodyMethodEnum(decryptBody.value())
                                .key(decryptBody.otherKey())
                                .build();
                    }

                    if (annotation instanceof SHA256WithRSADecryptBody) {
                        return DecryptAnnotationInfoBean.builder()
                                .decryptBodyMethodEnum(DecryptBodyMethodEnum.SHA256WithRSA)
                                .key(((SHA256WithRSADecryptBody) annotation).otherKey())
                                .build();
                    }

                    if (annotation instanceof DESDecryptBody) {
                        return DecryptAnnotationInfoBean.builder()
                                .decryptBodyMethodEnum(DecryptBodyMethodEnum.DES)
                                .key(((DESDecryptBody) annotation).otherKey())
                                .build();
                    }

                    if (annotation instanceof AESDecryptBody) {
                        return DecryptAnnotationInfoBean.builder()
                                .decryptBodyMethodEnum(DecryptBodyMethodEnum.AES)
                                .key(((AESDecryptBody) annotation).otherKey())
                                .build();
                    }
                }
            }
            return null;
        }

    }


}
