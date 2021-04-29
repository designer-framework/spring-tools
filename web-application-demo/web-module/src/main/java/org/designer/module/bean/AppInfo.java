package org.designer.module.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.designer.common.bean.App;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 23:56
 */
@Getter
@Setter
@ToString
public class AppInfo implements App {

    private static final long serialVersionUID = 8071157237872656153L;

    private String appPath;

    private String main;

    private String version;

    private String appName;

    private int port;

}
