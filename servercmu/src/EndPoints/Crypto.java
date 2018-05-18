package EndPoints;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
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

    public static String encrypt(String message, String sessionID) throws Exception {
        byte[] message_bytes = message.getBytes();
        Key key = getKeyFromSessionID(sessionID);
        Cipher cipher;

        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return toHexString(cipher.doFinal(message_bytes));
    }

    public static String decrypt(byte[] message, String sessionID) throws Exception {
        Key key = getKeyFromSessionID(sessionID);
        Cipher cipher;

        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(message));
    }

    private static Key getKeyFromSessionID(String sessionID) {
        Key secret = new SecretKeySpec(sessionID.getBytes(), "AES");
        return secret;
    }

    public static String calculateHMAC(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data.getBytes());
        return toHexString(messageDigest.digest());
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
