package org.designer.web.encryption.exception;


public class DecryptBodyFailException extends RuntimeException {

    private static final long serialVersionUID = 3344749252386404251L;

    public DecryptBodyFailException() {
        super("解密数据失败");
    }

    public DecryptBodyFailException(String message) {
        super(message);
    }
}