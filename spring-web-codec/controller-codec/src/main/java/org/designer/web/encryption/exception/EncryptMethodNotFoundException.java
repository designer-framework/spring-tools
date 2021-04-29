package org.designer.web.encryption.exception;


public class EncryptMethodNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7544378895417464188L;

    public EncryptMethodNotFoundException() {
        super("加密方式未定义");
    }

    public EncryptMethodNotFoundException(String message) {
        super(message);
    }
}
