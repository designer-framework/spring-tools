package org.designer.module.bean;

import lombok.Data;
import org.designer.common.bean.AppContext;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 23:56
 */
@Data
public class AppContextInfo implements AppContext {

    private static final long serialVersionUID = 8071157237872656153L;

    private String appPath;

    private String main;

    private String version;

    private String appName;

}
