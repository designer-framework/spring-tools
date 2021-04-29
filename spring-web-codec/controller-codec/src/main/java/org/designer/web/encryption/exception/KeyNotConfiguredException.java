package org.designer.web.encryption.exception;


import lombok.NoArgsConstructor;


@NoArgsConstructor
public class KeyNotConfiguredException extends RuntimeException {

    private static final long serialVersionUID = 1065873854021060311L;

    public KeyNotConfiguredException(String message) {
        super(message);
    }
}
