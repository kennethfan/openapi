package io.github.kennethfan.openapi.client.encrypt;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static io.github.kennethfan.openapi.client.encrypt.RsaKeyGenerator.*;

public class RsaEncryptor {

    private Key key;

    private RsaEncryptor(Key key) {
        this.key = key;
    }

    /**
     * 加密
     *
     * @param plain
     * @return
     */
    public byte[] encrypt(byte[] plain) {
        try {
            // 加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, this.key);
            return cipher.doFinal(plain);
        } catch (Exception e) {
            throw new RuntimeException("encrypt error. ", e);
        }
    }

    /**
     * 解密
     *
     * @param crypt
     * @return
     */
    public byte[] decrypt(byte[] crypt) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, this.key);
            return cipher.doFinal(crypt);
        } catch (Exception e) {
            throw new RuntimeException("decrypt error. ", e);
        }
    }

    /**
     * @param content
     * @return
     */
    public static RsaEncryptor fromPublicKey(String content) {
        try {
            content = tripHeader(content, PUBLIC_KEY_HEADER);
            content = tripTail(content, PUBLIC_KEY_TAIL);

            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(content)));
            return new RsaEncryptor(publicKey);
        } catch (Exception e) {
            throw new RuntimeException("generate rsa encryptor error. ", e);
        }
    }

    /**
     * @param content
     * @return
     */
    public static RsaEncryptor fromPrivateKey(String content) {
        try {
            content = tripHeader(content, PRIVATE_KEY_HEADER);
            content = tripTail(content, PRIVATE_KEY_TAIL);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(content)));
            return new RsaEncryptor(privateKey);
        } catch (Exception e) {
            throw new RuntimeException("generate rsa encryptor error. ", e);
        }
    }

    public static String tripHeader(String input, String header) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(header)) {
            return input;
        }

        if (StringUtils.startsWith(input, header)) {
            return input.substring(header.length());
        }

        return input;
    }

    public static String tripTail(String input, String tail) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(tail)) {
            return input;
        }

        if (StringUtils.endsWith(input, tail)) {
            return input.substring(0, input.length() - tail.length());
        }

        return input;
    }
}
