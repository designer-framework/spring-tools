package org.designer.web.encryption;

import org.designer.web.encryption.annotation.EnableAutoEncrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"org.designer"})
@EnableAutoEncrypt
public class EncryptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncryptionApplication.class, args);
    }

}