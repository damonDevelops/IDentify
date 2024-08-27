package com.team.identify.IdentifyAPI.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class JwtBeans {
    private static final Logger logger = LoggerFactory.getLogger(JwtBeans.class);
    @Autowired
    ResourceLoader resourceLoader;

    @Bean
    public KeyPair jwtKeyPair() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Resource publicKeyResource = resourceLoader.getResource("file:/identify/keys/jwtkey_pub");
        Resource privateKeyResource = resourceLoader.getResource("file:/identify/keys/jwtkey");
        try {
            if (1 < publicKeyResource.contentLength() && 1 < privateKeyResource.contentLength()) {
                byte[] publicKeyBytes = publicKeyResource.getContentAsByteArray();
                byte[] privateKeyBytes = privateKeyResource.getContentAsByteArray();

                KeyFactory kf = KeyFactory.getInstance("RSA");

                //test private key and generate new if invalid
                PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
                PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

                return new KeyPair(publicKey, privateKey);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return generateAndSaveKeyPair();
    }

    private KeyPair generateAndSaveKeyPair() {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        logger.info("Generating new jwt keypair!");
        try {
            File pubKeyFile = resourceLoader.getResource("file:/identify/keys/jwtkey_pub").getFile();
            File privKeyFile = resourceLoader.getResource("file:/identify/keys/jwtkey").getFile();
            Files.write(pubKeyFile.toPath(), publicKey);
            Files.write(privKeyFile.toPath(), privateKey);
        } catch (IOException ex) {
            logger.error("IOException when writing key files! {}", ex.getMessage());
        }
        return keyPair;
    }
}
