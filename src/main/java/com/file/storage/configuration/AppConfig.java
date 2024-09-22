package com.file.storage.configuration;


import com.file.storage.helper.PgpDecryptionUtil;
import com.file.storage.helper.PgpEncryptionUtil;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Configuration
public class AppConfig {

    private static  final String passkey = "J@m!la1998";

    @Bean
    public PgpEncryptionUtil pgpEncryptionUtil(){
        return PgpEncryptionUtil.builder()
                .armor(true)
                .compressionAlgorithm(CompressionAlgorithmTags.ZIP)
                .symmetricKeyAlgorithm(SymmetricKeyAlgorithmTags.AES_128)
                .withIntegrityCheck(true)
                .build();
    }

    @Bean
    public PgpDecryptionUtil pgpDecryptionUtil() throws IOException, PGPException {
        File keyFile = new File("src/main/resources/secret/private.pgp");
        URL privateKey = keyFile.toURI().toURL();
        return new PgpDecryptionUtil(privateKey.openStream(),passkey);
    }

}
