package org.designer.web.encryption.exception;


public class DecryptMethodNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6709129986976730389L;

    public DecryptMethodNotFoundException() {
        super("解密方式未定义");
    }

    public DecryptMethodNotFoundException(String message) {
        super(message);
    }
}
