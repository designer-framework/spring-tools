package org.designer.web.encryption.advice;

import io.vavr.NotImplementedError;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.designer.web.encryption.annotation.encrypt.*;
import org.designer.web.encryption.bean.EncryptAnnotationInfoBean;
import org.designer.web.encryption.config.EncryptBodyProperty;
import org.designer.web.encryption.enums.EncryptBodyMethodEnum;
import org.designer.web.encryption.enums.SHAEncryptTypeEnum;
import org.designer.web.encryption.exception.EncryptBodyFailException;
import org.designer.web.encryption.exception.EncryptMethodNotFoundException;
import org.designer.web.encryption.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;


@Order(1)
@ControllerAdvice
@Log4j2
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private EncryptBodyProperty encryptBodyProperty;

    public static boolean constantsEncryptAnnotation(MethodParameter methodParameter) {
        boolean hasEncryptAnnotation = false;
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        if (annotations != null && annotations.length > 0) {
            hasEncryptAnnotation = Stream.of(annotations).parallel().anyMatch(annotation ->
                    annotation instanceof EncryptBody ||
                            annotation instanceof SHA256WithRSAEncryptBody ||
                            annotation instanceof AESEncryptBody ||
                            annotation instanceof DESEncryptBody ||
                            annotation instanceof RSAEncryptBody
            );
        }
        return hasEncryptAnnotation ? true : Stream.of(methodParameter.getMethod().getAnnotations()).parallel().anyMatch(annotation ->
                annotation instanceof EncryptBody ||
                        annotation instanceof SHA256WithRSAEncryptBody ||
                        annotation instanceof AESEncryptBody ||
                        annotation instanceof DESEncryptBody ||
                        annotation instanceof RSAEncryptBody
        );
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> convertClass) {
        return constantsEncryptAnnotation(methodParameter);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }
        EncryptAnnotationInfoBean encryptAnnotationInfoBean = EncryptInnerAnnotationUtils.getEncryptAnnotationInfoBean(methodParameter);
        if (encryptAnnotationInfoBean != null) {
            try {
                return switchEncrypt(body, encryptAnnotationInfoBean);
            } catch (Exception e) {
                e.printStackTrace();
                throw new EncryptBodyFailException(e.getMessage());
            }
        }
        throw new EncryptBodyFailException();
    }

    /**
     * ?????????????????????????????????
     *
     * @param restObject ?????????????????????????????????????????????
     * @param infoBean   ????????????
     * @return ????????????
     */
    private Object switchEncrypt(Object restObject, EncryptAnnotationInfoBean infoBean) throws Exception {
        if (encryptBodyProperty.encryptRestSupport(restObject)) {
            //???????????? ??????MD5???
            EncryptBodyMethodEnum encryptMethod = infoBean.getEncryptBodyMethodEnum();
            //????????????
            String key = infoBean.getKey();
            if (encryptMethod == null) {
                throw new EncryptMethodNotFoundException();
            }
            String formatStringBody = encryptBodyProperty.convertResponseBodyToString(restObject);
            String sign;
            switch (encryptMethod) {
                case SHA256withRSA:
                    key = CheckUtils.checkAndGetKey(encryptBodyProperty.getPrivateKey(), key, encryptMethod + "-KEY");
                    sign = RSA2Util.buildRSAEncryptByPrivateKey(formatStringBody, key);
                    break;
                case MD5:
                    sign = MD5EncryptUtil.encrypt(formatStringBody);
                    break;
                case AES:
                    key = CheckUtils.checkAndGetKey(encryptBodyProperty.getPrivateKey(), key, encryptMethod + "-KEY");
                    sign = AESEncryptUtil.encrypt(formatStringBody, key);
                    break;
                case DES:
                    key = CheckUtils.checkAndGetKey(encryptBodyProperty.getPrivateKey(), key, encryptMethod + "-KEY");
                    sign = DESEncryptUtil.encrypt(formatStringBody, key);
                    break;
                case RSA:
                    throw new NotImplementedError(String.format("????????????%s??????", encryptMethod));
                case SHA:
                    SHAEncryptTypeEnum shaEncryptTypeEnum = infoBean.getShaEncryptTypeEnum();
                    if (shaEncryptTypeEnum == null) {
                        shaEncryptTypeEnum = SHAEncryptTypeEnum.SHA256;
                    }
                    sign = SHAEncryptUtil.encrypt(formatStringBody, shaEncryptTypeEnum);
                    break;
                default:
                    throw new EncryptBodyFailException();
            }
            return insertSignToBody(restObject, sign);
        }
        return restObject;
    }

    private <R, T> T insertSignToBody(R restObject, String sign) {
        return encryptBodyProperty.writeSignToBody(restObject, sign);
    }

    /**
     * ??????String?????????
     *
     * @param body
     * @param sign
     * @return
     */
    private String signStringBody(String body, String sign) {
        StringBuilder stringBuffer = new StringBuilder(body);
        stringBuffer.insert(body.length() - 1, ",\"sign\":\"" + sign + "\"");
        return stringBuffer.toString();
    }

    public static class EncryptInnerAnnotationUtils {

        static EncryptAnnotationInfoBean getEncryptAnnotationInfoBean(MethodParameter methodParameter) {
            EncryptAnnotationInfoBean methodAnnotation = getMethodAnnotation(methodParameter);
            return methodAnnotation == null ? getClassAnnotation(methodParameter.getDeclaringClass()) : methodAnnotation;
        }

        /**
         * ?????????????????????????????????????????????
         *
         * @param methodParameter ???????????????
         * @return ??????????????????
         */
        private static EncryptAnnotationInfoBean getMethodAnnotation(MethodParameter methodParameter) {
            if (methodParameter.getMethod().isAnnotationPresent(EncryptBody.class)) {
                EncryptBody encryptBody = methodParameter.getMethodAnnotation(EncryptBody.class);
                return EncryptAnnotationInfoBean.builder()
                        .encryptBodyMethodEnum(encryptBody.value())
                        .key(encryptBody.otherKey())
                        .shaEncryptTypeEnum(encryptBody.shaType())
                        .build();
            }
            if (methodParameter.getMethod().isAnnotationPresent(SHA256WithRSAEncryptBody.class)) {
                return EncryptAnnotationInfoBean.builder()
                        .key(methodParameter.getMethodAnnotation(SHA256WithRSAEncryptBody.class).otherKey())
                        .encryptBodyMethodEnum(EncryptBodyMethodEnum.SHA256withRSA)
                        .build();
            }
            if (methodParameter.getMethod().isAnnotationPresent(MD5EncryptBody.class)) {
                return EncryptAnnotationInfoBean.builder()
                        .encryptBodyMethodEnum(EncryptBodyMethodEnum.MD5)
                        .build();
            }
            if (methodParameter.getMethod().isAnnotationPresent(SHAEncryptBody.class)) {
                return EncryptAnnotationInfoBean.builder()
                        .encryptBodyMethodEnum(EncryptBodyMethodEnum.SHA)
                        .shaEncryptTypeEnum(methodParameter.getMethodAnnotation(SHAEncryptBody.class).value())
                        .build();
            }
            if (methodParameter.getMethod().isAnnotationPresent(DESEncryptBody.class)) {
                return EncryptAnnotationInfoBean.builder()
                        .encryptBodyMethodEnum(EncryptBodyMethodEnum.DES)
                        .key(methodParameter.getMethodAnnotation(DESEncryptBody.class).otherKey())
                        .build();
            }
            if (methodParameter.getMethod().isAnnotationPresent(AESEncryptBody.class)) {
                return EncryptAnnotationInfoBean.builder()
                        .encryptBodyMethodEnum(EncryptBodyMethodEnum.AES)
                        .key(methodParameter.getMethodAnnotation(AESEncryptBody.class).otherKey())
                        .build();
            }
            return null;
        }

        /**
         * ??????????????????????????????????????????
         *
         * @param clazz ????????????
         * @return ??????????????????
         */
        private static EncryptAnnotationInfoBean getClassAnnotation(Class clazz) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof SHA256WithRSAEncryptBody) {
                        return EncryptAnnotationInfoBean.builder()
                                .encryptBodyMethodEnum(EncryptBodyMethodEnum.SHA256withRSA)
                                .key(((SHA256WithRSAEncryptBody) annotation).otherKey())
                                .build();
                    }
                    if (annotation instanceof EncryptBody) {
                        EncryptBody encryptBody = (EncryptBody) annotation;
                        return EncryptAnnotationInfoBean.builder()
                                .encryptBodyMethodEnum(encryptBody.value())
                                .key(encryptBody.otherKey())
                                .shaEncryptTypeEnum(encryptBody.shaType())
                                .build();
                    }
                    if (annotation instanceof MD5EncryptBody) {
                        return EncryptAnnotationInfoBean.builder()
                                .encryptBodyMethodEnum(EncryptBodyMethodEnum.MD5)
                                .build();
                    }
                    if (annotation instanceof SHAEncryptBody) {
                        return EncryptAnnotationInfoBean.builder()
                                .encryptBodyMethodEnum(EncryptBodyMethodEnum.SHA)
                                .shaEncryptTypeEnum(((SHAEncryptBody) annotation).value())
                                .build();
                    }
                    if (annotation instanceof DESEncryptBody) {
                        return EncryptAnnotationInfoBean.builder()
                                .encryptBodyMethodEnum(EncryptBodyMethodEnum.DES)
                                .key(((DESEncryptBody) annotation).otherKey())
                                .build();
                    }
                    if (annotation instanceof AESEncryptBody) {
                        return EncryptAnnotationInfoBean.builder()
                                .encryptBodyMethodEnum(EncryptBodyMethodEnum.AES)
                                .key(((AESEncryptBody) annotation).otherKey())
                                .build();
                    }
                }
            }
            return null;
        }

    }

}
