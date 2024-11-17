package Car.project.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class EncryptionUtil {
    
    private static String secretKey;
    private static final String ALGORITHM = "AES";

    @Value("${encryption.secret.key}")
    private String injectedKey;

    // Méthode d'initialisation pour définir la clé de chiffrement statique
    @PostConstruct
    public void init() {
        if (injectedKey == null || injectedKey.isEmpty()) {
            throw new IllegalArgumentException("La clé de chiffrement ne peut pas être vide.");
        }
        String encodedKey = injectedKey.trim();  
        secretKey = encodedKey;  // Affectation de la clé statique après injection
        System.out.println("Longueur de la clé : " + secretKey.length());
        System.out.println("Clé de chiffrement : " + secretKey);  // Affiche la clé pour vérifier
    }
   
    // Vérification que la clé est bien définie
    private static void checkKey() throws Exception {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("La clé de chiffrement n'a pas été définie.");
        }
    }

    // Méthode de chiffrement
    public static String encrypt(String data) throws Exception {
        checkKey();  // Vérifie que la clé a été définie
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);  // Décodage de la clé base64
        SecretKeySpec key = new SecretKeySpec(decodedKey, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // Méthode de déchiffrement
    public static String decrypt(String encryptedData) throws Exception {
        checkKey();  // Vérifie que la clé a été définie
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);  // Décodage de la clé base64
        SecretKeySpec key = new SecretKeySpec(decodedKey, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decodedData));
    }
}
