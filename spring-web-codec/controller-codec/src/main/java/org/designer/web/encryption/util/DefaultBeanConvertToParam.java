package org.designer.web.encryption.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.designer.web.encryption.annotation.ToSortString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * wisdom
 *
 * @author Designer
 * @version 1.0.0
 * @date 2020/4/18 0:31
 * @description TODO 分离配置文件, 将加密逻辑剥离。因为本类已超出自身职责范围
 */
@Data
@Log4j2
@Accessors(chain = true)
public class DefaultBeanConvertToParam {

    protected String publicKey;

    protected String privateKey;
    protected boolean isIgnoreNull = true;
    protected boolean sorted = true;
    private String separator = "&";
    private String keyValueSeparator = "=";
    private String nullParamPlaceholder;
    private boolean appendKey;
    private boolean appendKeyName;
    private String keyName;

    public DefaultBeanConvertToParam() {
    }

    /**
     * 将Map转换成指定格式的字符串
     *
     * @param objParamMap       待加签集合
     * @param separator         集合之间的连接符
     * @param keyValueSeparator key和value之间的连接符
     * @param hasKey            是否包含加密串中的签名key
     * @param keyName           签名key的名字
     * @param keyValue          签名key的值
     * @return
     */
    protected static String join(
            Map<String, Object> objParamMap,
            String separator,
            String keyValueSeparator,
            boolean isIgnoreNull,
            String nullParamPlaceholder,
            boolean hasKey,
            boolean appendKeyName,
            String keyName,
            String keyValue) {
        StringBuffer buffer = new StringBuffer();
        boolean isFirst = true;
        // map排序，加签
        for (Map.Entry mapEntry : objParamMap.entrySet()) {
            boolean empty = StringUtils.isEmpty(mapEntry.getValue());
            // 第一次不需要&符号
            if (isFirst) {
                // 忽略空值
                if (isIgnoreNull && !empty) {
                    buffer.append(mapEntry.getKey())
                            .append(keyValueSeparator)
                            .append(mapEntry.getValue());
                    // 不忽略空值，用指定占位符替换
                } else if (isIgnoreNull && empty) {
                    continue;
                } else if (!isIgnoreNull && empty) {
                    buffer.append(mapEntry.getKey())
                            .append(keyValueSeparator)
                            .append(nullParamPlaceholder);
                } else if (!isIgnoreNull && !empty) {
                    buffer.append(mapEntry.getKey())
                            .append(keyValueSeparator)
                            .append(mapEntry.getValue());
                }
                isFirst = false;
                continue;
            }
            // 忽略空值
            if (isIgnoreNull && !empty) {
                buffer.append(separator)
                        .append(mapEntry.getKey())
                        .append(keyValueSeparator)
                        .append(mapEntry.getValue());
            } else if (isIgnoreNull && empty) {
                continue;
            } else if (!isIgnoreNull && empty) {
                buffer.append(separator)
                        .append(mapEntry.getKey())
                        .append(keyValueSeparator)
                        .append(nullParamPlaceholder);
            } else if (!isIgnoreNull && !empty) {
                buffer.append(separator)
                        .append(mapEntry.getKey())
                        .append(keyValueSeparator)
                        .append(mapEntry.getValue());
            }
        }
        // 是否需要追加key
        if (hasKey) {
            // 是否需要在追加key的同时追加key的名字（不是key的值）   name=value
            if (appendKeyName) {
                return buffer.append(separator)
                        .append(keyName)
                        .append(keyValueSeparator)
                        .append(keyValue)
                        .toString();
                // 不需要key的name，直接追加key的value
            } else {
                return buffer.append(keyValue).toString();
            }
            // 不需要任何与key相关的字符
        } else {
            return buffer.toString();
        }
    }

    /**
     * 对象拼接成字符串
     *
     * @return
     */
    public String generateParamStr(Object targetObject) {
        Assert.notNull(targetObject, "参数为空");
        // 加密，将对象转换为Map,拼接成string
        return join(targetObject, separator, keyValueSeparator, isIgnoreNull, nullParamPlaceholder, appendKey, appendKeyName, keyName, privateKey);
    }

    protected String join(
            Map<String, Object> objParamMap,
            boolean isIgnoreNull,
            String nullParamPlaceholder,
            boolean appendKey,
            boolean appendKeyName,
            String keyName,
            String keyValue) {
        return join(
                objParamMap,
                separator,
                keyValueSeparator,
                isIgnoreNull,
                nullParamPlaceholder == null ? "" : nullParamPlaceholder,
                appendKey,
                appendKeyName,
                keyName,
                keyValue);
    }

    /**
     * 将Map转换成指定格式的加密串
     *
     * @param obj               待加签对象
     * @param separator         集合之间的连接符
     * @param keyValueSeparator key和value之间的连接符
     * @param isIgnoreNull      是否忽略null
     * @param hasKey            是否包含加密串中的签名key
     * @param keyName           签名key的名字
     * @param keyValue          签名key的值
     * @return
     */
    protected String join(
            Object obj,
            String separator,
            String keyValueSeparator,
            boolean isIgnoreNull,
            String nullParamPlaceholder,
            boolean hasKey,
            boolean appendKeyName,
            String keyName,
            String keyValue) {
        // 对象转map,再拼接
        return join(
                convertObjToMap(obj),
                separator,
                keyValueSeparator,
                isIgnoreNull,
                nullParamPlaceholder,
                hasKey,
                appendKeyName,
                keyName,
                keyValue);
    }

    /**
     * JavaBean对象转换为Map
     * // FIXME: 2020/4/19 obj 需要支持多种对象类型转换成拼接字符串的方式
     *
     * @param obj
     * @return
     */
    public Map<String, Object> convertObjToMap(Object obj) {
        Map<String, Object> treeMap = new TreeMap<>();
        if (obj instanceof Map) {
            if (obj instanceof SortedMap) {
                return (Map<String, Object>) obj;
            } else if (sorted) {
                treeMap.putAll((Map<String, Object>) obj);
                return treeMap;
            }
            return (Map<String, Object>) obj;
        }
        if (obj == null) {
            return null;
        }
        try {
            Class<?> objClass = obj.getClass();
            while (objClass != null) {
                Field[] fields = objClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    try {
                        Field f = objClass.getDeclaredField(fields[i].getName());
                        f.setAccessible(true);
                        Object o = f.get(obj);
                        if (!"serialVersionUID".equals(fields[i].getName()) && !StringUtils.isEmpty(o)) {
                            ToSortString annotation = f.getAnnotation(ToSortString.class);
                            if (annotation == null) {
                                treeMap.put(fields[i].getName(), o);
                            } else {
                                treeMap.put(fields[i].getName(), JSON.toJSONString(o));
                            }
                        }
                    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                objClass = objClass.getSuperclass();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return treeMap;
    }

}
