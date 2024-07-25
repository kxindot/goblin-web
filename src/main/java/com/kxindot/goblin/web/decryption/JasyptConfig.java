package com.kxindot.goblin.web.decryption;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 2024-07-15
 */
@Configuration
public class JasyptConfig {

    private static final Logger log = LoggerFactory.getLogger(JasyptConfig.class);

    @Bean("jasyptStringEncryptor")
    @Primary
    public StringEncryptor stringEncryptor() {
        log.info("lcap-config-decryptor is auto configured, init jasyptStringEncryptor");

        String visiblePassword = System.getProperty("JASYPT_PASSWORD");
        if (visiblePassword == null || visiblePassword.isEmpty()) {
            visiblePassword = System.getenv("JASYPT_PASSWORD");
        }
        if (visiblePassword == null || visiblePassword.isEmpty()) {
            log.warn("Cannot obtain visiblePassword from systemEnv nor systemProperty");
            visiblePassword = "";
        }

        String actualPassword = new CaesarCipher().encrypt(visiblePassword, 1);

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(actualPassword);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGenerator(new RandomSaltGenerator());
        config.setIvGenerator(new RandomIvGenerator());
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

}
