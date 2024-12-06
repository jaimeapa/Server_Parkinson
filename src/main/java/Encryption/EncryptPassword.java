package Encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptPassword {
    public static byte[] encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        // Convertir la contraseña a bytes
        byte[] contrasenaBytes = password.getBytes();
        // Actualizar el digest con los bytes de la contraseña
        md.update(contrasenaBytes);
        // Devolver el hash como un arreglo de bytes
        return md.digest();
    }


}
