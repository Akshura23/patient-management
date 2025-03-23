//package com.patient.management.util;
//
//import com.patient.management.configuration.CustomJasyptEncryptorConfig;
//import org.jasypt.encryption.StringEncryptor;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//public class EncryptionUtil {
//
//    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(CustomJasyptEncryptorConfig.class);
//        StringEncryptor encryptor = context.getBean("jasyptStringEncryptor", StringEncryptor.class);
//
//        String username = "root";
//        String password = "admin";
//
//        String encryptedUsername = encryptor.encrypt(username);
//        String encryptedPassword = encryptor.encrypt(password);
//
//        System.out.println("Encrypted Username: " + encryptedUsername);
//        System.out.println("Encrypted Password: " + encryptedPassword);
//    }
//}
