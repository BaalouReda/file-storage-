package com.brightobra.file.storage.configuration;

import org.springframework.beans.factory.annotation.Value;

public class AppConstant {

    public static String publicKeyPath;
    public static  String privateKeyPath ;

    @Value("${secret.key.public}")
    public static void setPublicKeyPath(String publicKeyPath) {
        AppConstant.publicKeyPath = publicKeyPath;
    }

    @Value("${secret.key.private}")
    public static void setPrivateKeyPath(String privateKeyPath) {
        AppConstant.privateKeyPath = privateKeyPath;
    }
}
