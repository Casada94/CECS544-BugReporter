package com.cecs544.BugReporter.config;

import com.cecs544.BugReporter.crypto.CryptoUtil;
import com.cecs544.BugReporter.exceptions.CryptoException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class MySqlConfig {
    @Autowired
    private CryptoUtil cryptoUtil;

    @Value("${spring.mySqlDatasource.url}")
    private String url;

    @Value("${spring.mySqlDatasource.username}")
    private String username;
    @Value("${spring.mySqlDatasource.password}")
    private String password;
    @Value("${spring.mySqlDatasource.driver}")
    private String driver;

    @Bean(name = "mysqlDatasource")
    public DataSource mysqlDatasource() throws CryptoException {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setUrl(url);
        poolProperties.setUsername(username);
        poolProperties.setPassword(cryptoUtil.decrypt(password));
        poolProperties.setDriverClassName(driver);
        poolProperties.setMaxIdle(2);
        poolProperties.setMaxActive(4);
        return new DataSource(poolProperties);
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate mysqlTemplate(@Qualifier("mysqlDatasource")DataSource mysqlDatasource){
        return new JdbcTemplate(mysqlDatasource);
    }

    @Bean(name = "namedParamJdbcTemplate")
    public NamedParameterJdbcTemplate mysqlNamedParamTemplate(@Qualifier("mysqlDatasource")DataSource mysqlDatasource){
        return new NamedParameterJdbcTemplate(mysqlDatasource);
    }
}
