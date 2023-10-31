package server;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CryptoHasher {
    private static final int ITERATIONS = 1024;
    private static final int KEY_LENGTH = 512;

    private static final int SALT_LENGTH = 64;

    public static void main(String[] args) {
        assert(hashPassword("test".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8))
                == hashPassword("test".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8)));
        System.out.println("Test passed");
    }

    public static byte[] hashPassword(final char[] password, final byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
