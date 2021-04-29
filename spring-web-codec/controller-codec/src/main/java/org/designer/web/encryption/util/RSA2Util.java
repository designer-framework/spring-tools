package org.designer.web.encryption.util;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 加解密工具类
 *
 * @author Designer
 * @version 1.0
 * @date 2019年3月24日 下午9:27:40
 */
public final class RSA2Util {
    public static final String CHARSET = "UTF-8";
    // 密钥算法
    public static final String ALGORITHM_RSA = "RSA";
    // RSA 签名算法
    public static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
    public static final int ALGORITHM_RSA_PRIVATE_KEY_LENGTH = 2048;

    /**
     * 初始化RSA算法密钥对
     *
     * @param keysize RSA1024已经不安全了,建议2048
     * @return 经过Base64编码后的公私钥Map, 键名分别为publicKey和privateKey
     */
    public static Map<String, String> initRSAKey(int keysize) {
        if (keysize != ALGORITHM_RSA_PRIVATE_KEY_LENGTH) {
            throw new IllegalArgumentException("RSA1024已经不安全了,请使用" + ALGORITHM_RSA_PRIVATE_KEY_LENGTH + "初始化RSA密钥对");
        }
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + ALGORITHM_RSA + "]");
        }
        // 初始化KeyPairGenerator对象,不要被initialize()源码表面上欺骗,其实这里声明的size是生效的
        kpg.initialize(ALGORITHM_RSA_PRIVATE_KEY_LENGTH);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * RSA算法公钥加密数据
     *
     * @param data 待加密的明文字符串
     * @param key  RSA公钥字符串
     * @return RSA公钥加密后的经过Base64编码的密文字符串
     */
    public static String buildRSAEncryptByPublicKey(String data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        // 通过X509编码的Key指令获得公钥对象
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder()
                .encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
    }

    /**
     * RSA算法公钥解密数据
     *
     * @param data 待解密的经过Base64编码的密文字符串
     * @param key  RSA公钥字符串
     * @return RSA公钥解密后的明文字符串
     */
    public static String buildRSADecryptByPublicKey(String data, String key) {
        try {
            // 通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data)), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * RSA算法私钥加密数据
     *
     * @param data 待加密的明文字符串
     * @param key  RSA私钥字符串
     * @return RSA私钥加密后的经过Base64编码的密文字符串
     */
    public static String buildRSAEncryptByPrivateKey(String data, String key) {
        try {
            // 通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.getEncoder()
                    .encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * RSA算法私钥解密数据
     *
     * @param data 待解密的经过Base64编码的密文字符串
     * @param key  RSA私钥字符串
     * @return RSA私钥解密后的明文字符串
     */
    public static String buildRSADecryptByPrivateKey(String data, String key) {
        try {
            // 通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data)), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * RSA算法使用私钥对数据生成数字签名
     *
     * @param data 待签名的明文字符串
     * @param key  RSA私钥字符串
     * @return RSA私钥签名后的经过Base64编码的字符串
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static String buildRSASignByPrivateKey(String data, String key) throws RuntimeException {
        try {
            // 通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
            signature.initSign(privateKey);
            signature.update(data.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("签名字符串[" + data + "]时遇到异常", e);
        }

    }

    /**
     * RSA算法使用公钥校验数字签名
     *
     * @param data 参与签名的明文字符串
     * @param key  RSA公钥字符串
     * @param sign RSA签名得到的经过Base64编码的字符串
     * @return true--验签通过,false--验签未通过
     */
    public static boolean buildRSAVerifyByPublicKey(String data, String key, String sign) {
        try {
            // 通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
            Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(CHARSET));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * RSA算法分段加解密数据
     *
     * @param cipher 初始化了加解密工作模式后的javax.crypto.Cipher对象
     * @param opmode 加解密模式,值为javax.crypto.Cipher.ENCRYPT_MODE/DECRYPT_MODE
     * @return 加密或解密后得到的数据的字节数组
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8;
        } else {
            maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8 - 11;
        }
        ;
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SignatureException, NoSuchPaddingException {
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCDW/CV9cc43bjC9txOWPLptVEdwXDyWIPaV347tT8852Nso0eTLA16+xdGTgssvE7+gU6II8DteZsPmnHSD1XSCRbQGHk39HMf6+nBop3CM9ouDjMBwNrfsXqCoZobDrI3ZR9vGiNCTYGJVuoPdC5FTzCsJq4VY/I54TsElwkW+WURo0OD/FouBcFmuzgjqM6t/Crz2nJB79PxLazwS7Vd1lA4XQ5UQmEiclfABq861UH2mna7bF4aLwmj3pCT83zK/1g2CDpt0FB6zCq+mn5cXQN5xiwQVd+dTHY/GEa4vaZz0fXy4X6tSGkVwHCE2pqjw7bA8L/AbBfcVqhcpI8rAgMBAAECggEAVuonduHHrs2t6XpzbHeYSqDJCjorJH8Cxx5rDNCxBfhIJeKngi58wOmQjdkntbLnRaiDMLsPdVGoEpvrQcUZ0L5o1MnMswRQhEeRRKj3Xz711tFYWImliKsi13pbwBlqPBLxyI5v0/f6hMf4WL9TulJabx2sw8R8ymVHqpLQ1dy9k9uA9S5qce9pZ+R+0BQIIKWQCV2S+LJsow/SrU6PdmGCm1/utGrkJMYUKsDVzzPhkt2OEKu/QTUF5S7HbNNVP5G7InKwOG/KEJAkNrEniEls6ybRgIAVrUvG5k3BAWdizFH5ZbQbZU0HpAbv3Fr826r3SXTTUGxvPIfxr+Qo0QKBgQC7VL52MG5C0uROTFtb8t69V6ZJZWqTgICb0dPFi5vfLl9lZXfK/K/2wqupvpt/xKLMMsFhG7eCaSOq5MzbOMQG7v3vfO/TqBludqhsnEhSSgiKGAbK6T6drIdzL6n4CfF7rMpmD5iv6PIpqUgAmVgznVS6APXrgxVURHeHau9deQKBgQCzgsKRYG2/UZRTpYYqLKbbbVB2oUEU64Tsy3+5Za7kuHV6E5ovlJsz5zLYxETHYU/TeyfT6LNIRxcHejX+nGwSnp6VbilMtn6ly/6ju+JzPM0UH/n/t4s3Tkfdfq/uNddac4AIm4pO364bMVMDvV+NS2y1cXFgLyXlR5k9b6g8wwKBgG+9mO5+7QZHLY/W1BgAwwRGKBZXvD7Hw+8uNhf7srpputWMtOwL1hpm/GaAH9kSvdj33YnDMq9Db9ccK2YIckWAgVA9QgEKoEDX3pcKkckkB/v3YPXgM01yAEmhAM3HgJe49fHzE6y6Utiu9RgGYCpJiqR4tjgJNMvTG0lXa/hhAoGBAKMG6ytaVvF85md9GTauLK91PLEwS8nzB1qCijHW7hoch9xhdxMlNDrUCvKgdRJaB0Q6QfaLDF9k6L5KuETOzLuwQLfXPzzDNIvgkxciaEtd+aYAUGxzxcRpnIzFJIQDc9xarvmiQN7W9+kMR6dK0j+hM04pg9zllNYAy2PnRvQ/AoGBAKd2Cm8kwPf9kx6caVIYGe8ExZj5QJmnWNh3K1LcsBKvaihRLDysWtKGP4Xyx2f9gkhoEKpN9ge7pcxAZ8CeTC59bOR/r3XiSGtSZzkWomnfDgx5ACGY65kzEWKj1sn6gplvCgFHDEZKmxasYjibhz1f4a2v3T1p8SDnnFuf/BFF";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg1vwlfXHON24wvbcTljy6bVRHcFw8liD2ld+O7U/POdjbKNHkywNevsXRk4LLLxO/oFOiCPA7XmbD5px0g9V0gkW0Bh5N/RzH+vpwaKdwjPaLg4zAcDa37F6gqGaGw6yN2UfbxojQk2BiVbqD3QuRU8wrCauFWPyOeE7BJcJFvllEaNDg/xaLgXBZrs4I6jOrfwq89pyQe/T8S2s8Eu1XdZQOF0OVEJhInJXwAavOtVB9pp2u2xeGi8Jo96Qk/N8yv9YNgg6bdBQeswqvpp+XF0DecYsEFXfnUx2PxhGuL2mc9H18uF+rUhpFcBwhNqao8O2wPC/wGwX3FaoXKSPKwIDAQAB";
        //公钥加密
        String s1 = buildRSAEncryptByPublicKey("a=2&b=1&data={\"a\":2, \"b\":1}", publicKey);
        //私钥解密
        String s3 = buildRSADecryptByPrivateKey(s1, privateKey);
        System.out.println(s1 + "\n" + s3);
        //私钥生成签名
        String s = buildRSASignByPrivateKey("a=2&b=1&data={\"c\":3, \"d\":4}", privateKey);
        //公钥校验
        boolean b = buildRSAVerifyByPublicKey("a=2&b=1&data={\"a\":2, \"b\":1}", publicKey, s);
        System.out.println(b);
        System.out.println(s + "\n" + b);
    }

}