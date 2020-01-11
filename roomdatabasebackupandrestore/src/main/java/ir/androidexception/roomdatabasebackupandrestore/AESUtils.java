package ir.androidexception.roomdatabasebackupandrestore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    public static String encrypt(String cleartext, String key) throws Exception {
        byte[] keyValue = normalizeKey(key);
        byte[] rawKey = getRawKey(keyValue);
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String encrypted, String key) throws Exception {
        byte[] keyValue = normalizeKey(key);
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(enc,keyValue);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] keyValue) throws Exception {
        SecretKey key = new SecretKeySpec(keyValue, "AES");
        byte[] raw = key.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] encrypted, byte[] keyValue) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(keyValue, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private static byte[] normalizeKey(String key){
        int length = key.length();
        if(length < 16){
            String tmp = "";
            for(int i=0;i<16-length;i++) tmp = tmp.concat("l");
            return key.concat(tmp).getBytes();
        }
        else if(length == 16){
            return key.getBytes();
        }
        else if(length < 24){
            String tmp = "";
            for(int i=0;i<24-length;i++) tmp = tmp.concat("l");
            return key.concat(tmp).getBytes();
        }
        else if(length == 24){
            return key.getBytes();
        }
        else if(length < 32){
            String tmp = "";
            for(int i=0;i<32-length;i++) tmp = tmp.concat("l");
            return key.concat(tmp).getBytes();
        }
        else if(length == 32){
            return key.getBytes();
        }
        else{
            return key.substring(0,32).getBytes();
        }
    }
}