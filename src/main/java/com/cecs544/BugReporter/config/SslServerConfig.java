package com.cecs544.BugReporter.config;

import com.cecs544.BugReporter.crypto.CryptoUtil;
import com.cecs544.BugReporter.exceptions.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SslServerConfig {
    @Autowired
    private CryptoUtil cryptoUtil;
    @Value("${spring.server.ssl.key-store}")
    private String keyStore;
    @Value("${spring.server.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${spring.server.ssl.key-store-type}")
    private String keyStoreType;

    @Bean
    public ServletWebServerFactory servletContainer() throws CryptoException {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        Ssl ssl = new Ssl();
        ssl.setKeyStore(keyStore);
        ssl.setKeyStorePassword(cryptoUtil.decrypt(keyStorePassword));
        ssl.setKeyStoreType(keyStoreType);
        ssl.setKeyAlias("cecs544");
        tomcat.setSsl(ssl);

        tomcat.setPort(443);

        return tomcat;
    }
}
