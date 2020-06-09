package user.minicrm.server.util;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

public class PasswordManager {
	
	private  final String UNICODE_FORMAT = "UTF8";
    public  final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private  KeySpec ks;
    private  SecretKeyFactory skf;
    private  Cipher cipher;
    private  byte[] arrayBytes;
    private  String myEncryptionKey;
    private  String myEncryptionScheme;
    private  SecretKey key;
    
	private PasswordManager() {
		try {
        myEncryptionKey = "1e5r8tgjkl32adklfdf";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
		}
		catch (Exception e) {
		}
    }


    public  String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
        	if(cipher!=null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        	}
        	} catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }


    public  String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}
