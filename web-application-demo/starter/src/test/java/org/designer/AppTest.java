package org.designer;

import org.designer.common.bean.AppContext;
import org.designer.common.utils.AppUtils;
import org.designer.module.bean.AppContextInfo;
import org.designer.service.AppsContext;
import org.designer.starter.utils.StarterUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        List<AppContextInfo> apps = AppUtils.getApps(AppContextInfo.class);
        Map<String, AppContext> contexts = StarterUtils.listToMap(apps);
        AppsContext.loadApps(contexts);
    }

}
