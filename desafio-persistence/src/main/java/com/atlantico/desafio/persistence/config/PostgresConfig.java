package com.atlantico.desafio.persistence.config;


import com.atlantico.desafio.persistence.config.properties.PostgresProperty;
import com.atlantico.desafio.persistence.domain.AuditorAwareImpl;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.util.Properties;

@ComponentScan(
        basePackages = {
                "com.atlantico.desafio.persistence.model",
                "com.atlantico.desafio.persistence.repository",
                "com.atlantico.desafio.persistence.service",
        },
        basePackageClasses = {
                PostgresProperty.class
        }
)
@EnableJpaRepositories("com.atlantico.desafio.persistence.repository")
@EnableTransactionManagement
@EnableJpaAuditing
@Configuration
public class PostgresConfig {

    @Autowired
    private PostgresProperty postgresProperty;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(postgresProperty.getDriver());
        driver.setUrl(postgresProperty.getUrl());
        driver.setUsername(postgresProperty.getUsername());
        driver.setPassword(postgresProperty.getPassword());
        return driver;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        val adapter = new HibernateJpaVendorAdapter();

        adapter.setDatabasePlatform("PostgreSQLDialect");
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setGenerateDdl(true);

        return adapter;
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactoryBean(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        Properties jpaProperties = new Properties();

        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("com.atlantico.desafio.persistence.model");
        factory.setValidationMode(ValidationMode.NONE);

        jpaProperties.put("hibernate.dialect", postgresProperty.getHibernate().getDialect());
        jpaProperties.put("hibernate.hbm2ddl.auto", postgresProperty.getHibernate().getDdlAuto());
        jpaProperties.put("hibernate.show_sql", postgresProperty.getHibernate().getShowSql());
        jpaProperties.put("hibernate.format_sql", postgresProperty.getHibernate().getFormatSql());

        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }


}
