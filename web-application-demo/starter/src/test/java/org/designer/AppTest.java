package org.designer;

import org.designer.common.bean.ContextInfo;
import org.designer.common.context.AppsContext;
import org.designer.common.utils.AppUtils;
import org.designer.common.utils.Utils;
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
        List<ContextInfo> apps = AppUtils.getApps(ContextInfo.class);
        Map<String, ContextInfo> contexts = Utils.listToMap(apps);
        AppsContext appsContext = new AppsContext(contexts);
        appsContext.loadApps();
    }
}
