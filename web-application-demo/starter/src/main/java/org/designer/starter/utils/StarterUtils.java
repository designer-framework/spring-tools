package org.designer.starter.utils;

import org.designer.common.bean.AppContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 0:49
 */
public class StarterUtils {

    public static <T extends AppContext> Map<String, AppContext> listToMap(List<T> apps) {
        return apps.stream()
                .collect(Collectors.toMap(AppContext::getAppName, contextInfo -> contextInfo, (old, news) -> news));
    }

}
