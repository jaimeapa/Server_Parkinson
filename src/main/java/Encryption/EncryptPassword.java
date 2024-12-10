package Encryption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The `EncryptPassword` class provides utility methods for encrypting passwords
 * using the MD5 hashing algorithm.
 */
public class EncryptPassword {

    /**
     * Encrypts a given password using the MD5 hashing algorithm.
     *
     * <p>This method converts the input password into a byte array, updates the
     * message digest with the password's bytes, and then returns the computed
     * hash as a byte array. It uses the `MessageDigest` class to perform
     * the hashing operation.</p>
     *
     * @param password the plain text password to encrypt.
     * @return a byte array representing the MD5 hash of the password.
     * @throws NoSuchAlgorithmException if the MD5 algorithm is not available
     *                                  in the current environment.
     */
    public static byte[] encryptPassword(String password) throws NoSuchAlgorithmException {
        // Get the MD5 message digest instance
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Convert the password into bytes
        byte[] passwordBytes = password.getBytes();

        // Update the digest with the bytes of the password
        md.update(passwordBytes);

        // Return the hash as a byte array
        return md.digest();
    }
}

