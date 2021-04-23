package org.designer.xml.bean;

import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/23 22:24
 */
@Setter
@ToString
public class XmlBean implements Serializable {

    private static final long serialVersionUID = 4155876011708332363L;
    private String arg1;
    private String arg2;

    public XmlBean(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

}
