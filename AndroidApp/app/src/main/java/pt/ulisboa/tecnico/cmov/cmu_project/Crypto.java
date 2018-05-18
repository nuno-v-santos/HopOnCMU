package pt.ulisboa.tecnico.cmov.cmu_project;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Nuno Santos on 18/05/2018.
 */

public class Crypto {

    public byte[] encrypt(String message, String sessionID) throws Exception {
        byte[] message_bytes = message.getBytes();
        Key key = getKeyFromSessionID(sessionID);
        Cipher cipher;

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message_bytes);
    }

    public byte[] decrypt(String message, String sessionID) throws Exception {
        byte[] message_bytes = message.getBytes();
        Key key = getKeyFromSessionID(sessionID);
        Cipher cipher;

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(message_bytes);
    }

    private Key getKeyFromSessionID(String sessionID){
        byte[] keyStart = sessionID.getBytes();
        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            kgen.init(128, sr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return kgen.generateKey();
    }

    public String calculateHMAC(String data, String key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
