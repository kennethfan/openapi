package io.github.kennethfan.openapi.client.client;

import io.github.kennethfan.openapi.client.encrypt.RsaEncryptor;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class ClientHelper {

    private static final String DELIMITER = "\n";

    private String appKey;

    private String secret;

    private RsaEncryptor rsaEncryptor;

    public void addAuthorization(String url, String body, Map<String, Object> header) {
        String nonceStr = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();

        String sign = generateSign(url, body, nonceStr, timestamp);
        header.put(ClientAuthConstants.AUTHORIZATION_KEY, sign);
        header.put(ClientAuthConstants.APP_KEY, this.appKey);
        header.put(ClientAuthConstants.TIMESTAMP_KEY, timestamp);
        header.put(ClientAuthConstants.NONCE_STR_KEY, nonceStr);
    }

    public String generateSign(String url, String body, String nonceStr, long timestamp) {
        return generateSign(url, body, nonceStr, String.valueOf(timestamp));
    }

    public String generateSign(String url, String body, String nonceStr, String timestamp) {
        String msg = buildMessage(url, body);
        String waitSignStr = this.appKey +
                this.secret +
                msg +
                nonceStr +
                timestamp;

        return MD5.create().digestHex(waitSignStr);
    }

    private String buildMessage(String url, String body) {
        return URLUtil.encode(url) + StringUtils.defaultString(body, StringUtils.EMPTY);
    }

    /**
     * 加密数据
     *
     * @param body
     * @return
     */
    public String encryptBody(String body) {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        final int bodyLength = bytes.length;
        final int maxPlainLength = rsaEncryptor.getMaxPlainLength();
        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (start < bodyLength) {
            int remain = Math.min(maxPlainLength, bytes.length - start);
            byte[] seg = new byte[remain];
            System.arraycopy(bytes, start, seg, 0, remain);
            sb.append(Base64.getEncoder().encodeToString(rsaEncryptor.encrypt(seg)))
                    .append(DELIMITER);
            start += maxPlainLength;
        }

        return StringUtils.strip(sb.toString(), DELIMITER);
    }

    /**
     * 解密数据
     *
     * @param body
     * @return
     */
    public String decryptBody(String body) {
        String[] segArr = body.split(DELIMITER);
        ByteBuffer byteBuffer = ByteBuffer.allocate(rsaEncryptor.getMaxPlainLength() * segArr.length);
        for (String seg : segArr) {
            byteBuffer.put(rsaEncryptor.decrypt(Base64.getDecoder().decode(seg.getBytes(StandardCharsets.UTF_8))));
        }

        return new String(byteBuffer.array(), StandardCharsets.UTF_8);
    }
}
