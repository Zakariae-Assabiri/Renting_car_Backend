package Car.project.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.security.SecureRandom;

@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding"; // Mode GCM pour chiffrement et authentification
    private static final int GCM_TAG_LENGTH = 128; // Taille du tag d'authentification en bits
    private static final int GCM_IV_LENGTH = 12; // Taille de l'IV en octets (recommandé pour GCM)

    private static String secretKey;

    @Value("${encryption.secret.key}")
    private String injectedKey;

    @PostConstruct
    public void init() {
        if (injectedKey == null || injectedKey.isEmpty()) {
            throw new IllegalArgumentException("La clé de chiffrement ne peut pas être vide.");
        }
        secretKey = injectedKey.trim();
    }

    private static void checkKey() throws Exception {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("La clé de chiffrement n'a pas été définie.");
        }
    }

    // Génère un IV aléatoire
    private static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    // Chiffrement avec IV
    public static String encrypt(String data) throws Exception {
        checkKey();
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");

        // Génère un IV aléatoire
        byte[] iv = generateIV();
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        // Initialise le chiffrement
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        // Chiffre les données
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        // Concatène l'IV et les données chiffrées
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

        // Encode en Base64 pour stockage ou transmission
        return Base64.getEncoder().encodeToString(combined);
    }

    // Déchiffrement avec IV
    public static String decrypt(String encryptedData) throws Exception {
        checkKey();
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");

        // Décode les données chiffrées
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // Extrait l'IV (les premiers 12 octets)
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, iv.length);

        // Extrait les données chiffrées (le reste)
        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        // Initialise le déchiffrement
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // Déchiffre les données
        byte[] decryptedData = cipher.doFinal(encryptedBytes);
        return new String(decryptedData);
    }
}