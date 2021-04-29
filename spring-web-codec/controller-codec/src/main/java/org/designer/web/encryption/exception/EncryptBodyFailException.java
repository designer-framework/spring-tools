package org.designer.web.encryption.exception;


public class EncryptBodyFailException extends RuntimeException {

    private static final long serialVersionUID = 409210876441103145L;

    public EncryptBodyFailException() {
        super("加密数据失败");
    }

    public EncryptBodyFailException(String message) {
        super(message);
    }
}