package org.designer.common.utils;

import org.designer.common.bean.ContextInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:49
 */
public class Utils {

    public static Map<String, ContextInfo> listToMap(List<ContextInfo> apps) {
        return apps.stream()
                .collect(Collectors.toMap(ContextInfo::getAppName, contextInfo -> contextInfo, (old, news) -> news));

    }
}
