package io.github.kennethfan.openapi.client.encrypt;

import java.security.*;
import java.util.Base64;

public class RsaKeyGenerator {

    public static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----\n";
    public static final String PUBLIC_KEY_TAIL = "\n-----END PUBLIC KEY-----";


    public static final String PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----\n";
    public static final String PRIVATE_KEY_TAIL = "\n-----END RSA PRIVATE KEY-----";

    private KeyPair keyPair;

    private RsaKeyGenerator(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public static RsaKeyGenerator newInstance() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            return new RsaKeyGenerator(keyPair);
        } catch (Exception e) {
            throw new RuntimeException("generator key error. ", e);
        }
    }

    public String dumpPublicKey() {
        PublicKey publicKey = this.keyPair.getPublic();

        return PUBLIC_KEY_HEADER + Base64.getEncoder().encodeToString(publicKey.getEncoded()) + PUBLIC_KEY_TAIL;
    }

    public String dumpPrivateKey() {
        PrivateKey privateKey = this.keyPair.getPrivate();

        return PRIVATE_KEY_HEADER + Base64.getEncoder().encodeToString(privateKey.getEncoded()) + PRIVATE_KEY_TAIL;
    }
}
