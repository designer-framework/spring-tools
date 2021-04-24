package org.designer.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 23:56
 */
@Data
public class ContextInfo implements Serializable {

    private static final long serialVersionUID = 8071157237872656153L;

    private String appPath;

    private String main;

    private String version;

    private String appName;
}
