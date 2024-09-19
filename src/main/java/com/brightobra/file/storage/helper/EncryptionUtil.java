package com.brightobra.file.storage.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

@Component
@ApplicationScope
@RequiredArgsConstructor
public class EncryptionUtil {

    private static final String AES = "AES";
    private static final String TRANSFORMATION = "AES";


    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }


    public static InputStream encrypt(InputStream data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = data.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        }

        // Return Base64 encoded encrypted data as InputStream
        byte[] encryptedData = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
        return new ByteArrayInputStream(encryptedData);
    }

    public static InputStream decrypt(InputStream encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decode Base64 first
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData.readAllBytes());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encryptedBytes);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        }

        // Return the decrypted data as InputStream
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Deprecated
    @SuppressWarnings("unused")
    public static InputStream encryptOld(InputStream data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return  new ByteArrayInputStream(Base64.getEncoder().encode(cipher.doFinal(data.readAllBytes())));
    }

    @Deprecated
    @SuppressWarnings("unused")
    public static InputStream decryptOld(InputStream encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new ByteArrayInputStream(cipher.doFinal(Base64.getDecoder().decode(encryptedData.toString())));
    }

}
