package Car.project.Entities;

import Car.project.util.EncryptionUtil;

public class test {{
        try {
            String data = "HelloWorld";  // Données à tester
            String encryptedData = EncryptionUtil.encrypt(data);
            System.out.println("Données chiffrées : " + encryptedData);

            String decryptedData = EncryptionUtil.decrypt(encryptedData);
            System.out.println("Données déchiffrées : " + decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
}}
