package com.kxindot.goblin.web.decryption;

import javax.annotation.PostConstruct;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ulisesbocchio.jasyptspringboot.detector.DefaultPropertyDetector;

/**
 * @author 2024-07-16
 */
@Component
public class ManualDecryptor {

    @Value("${jasypt.encryptor.property.prefix:ENC(}")
    private String prefix;

    @Value("${jasypt.encryptor.property.suffix:)}")
    private String suffix;

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    private DefaultPropertyDetector defaultPropertyDetector;

    @PostConstruct
    public void init() {
        defaultPropertyDetector = new DefaultPropertyDetector(prefix, suffix);
    }

    /**
     * Decrypt an encrypted message
     *
     * @param encryptedMessage the encrypted message to be decrypted
     * @return the result of decryption
     */
    public String decrypt(String encryptedMessage) {
        if (!defaultPropertyDetector.isEncrypted(encryptedMessage)) {
            return encryptedMessage;
        }
        return stringEncryptor.decrypt(defaultPropertyDetector.unwrapEncryptedValue(encryptedMessage));
    }

}
