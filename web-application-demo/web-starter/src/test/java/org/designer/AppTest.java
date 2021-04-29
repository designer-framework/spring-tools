package org.designer;

import org.designer.starter.run.WebApplication;
import org.designer.starter.run.main.AppMain;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * .不同的类加载器加载出来的对象在内存中是各自独立存在的
     * .包括常量在内, 不同类加载器加载的常量同样有各自独立的值
     */
    @Test
    public void startWebApp() {
        WebApplication.run(AppMain.class, new String[]{});
    }

}
