package com.cecs544.BugReporter.crypto;


import com.cecs544.BugReporter.exceptions.CryptoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class CryptoUtil {
    @Value("${spring.crypto.key}")
    private String key;

    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;

    private static final Logger log = LogManager.getLogger(CryptoUtil.class);

//    @PostConstruct
//    public void init() throws CryptoException {
//        System.out.println(decrypt("fG1G4U9UhYSB9G9uv5ZEAr9dCZpPjaMn3uNfo4wObLSBstSBcDEi/Yxeg8TLflA0"));
//        System.out.println(decrypt("iqSh19gxu6B5ia8mpUv8upJpos6r+mzCkI94Ywec8goy75eL/sQRawvfKWljBbdPQ9vnWfbEZMYyTq/VkcXByw=="));
//    }
    public String encrypt(String input) throws CryptoException {
        log.debug("CryptoUtil - encrypt - Entry - ");

        if(input == null)
            return null;

        try{
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), new byte[8], ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

            byte[] cipherText = cipher.doFinal(input.getBytes("UTF-8"));
            byte[] encryptedData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

            log.debug("CryptoUtil - encrypt - Exit - ");
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (UnsupportedEncodingException | IllegalBlockSizeException| BadPaddingException| InvalidKeySpecException| NoSuchPaddingException| NoSuchAlgorithmException| InvalidAlgorithmParameterException| InvalidKeyException exception){
            log.error("Failed to encrypt text - " + exception.getMessage());
            throw new CryptoException(exception);
        }
    }

    public String decrypt(String input) throws CryptoException {
        log.debug("CryptoUtil - decrypt - Entry - ");

        try{
            byte[] encryptedData = Base64.getDecoder().decode(input);
            byte[] iv = new byte[16];
            System.arraycopy(encryptedData, 0, iv, 0, iv.length);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), new byte[8], ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);

            byte[] cipherText = new byte[encryptedData.length - 16];
            System.arraycopy(encryptedData, 16, cipherText, 0, cipherText.length);

            byte[] decryptedText = cipher.doFinal(cipherText);

            log.debug("CryptoUtil - decrypt - Exit - ");
            return new String(decryptedText, StandardCharsets.UTF_8);

        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException exception){
            log.error("Failed to encrypt text - " + exception.getMessage());
            throw new CryptoException(exception);
        }
    }
}
