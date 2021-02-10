package com.atlantico.desafio.persistence.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource({"classpath:persistence.properties"})
@ConfigurationProperties("db")
public class PostgresProperty {

    private Hibernate hibernate;
    private String database;
    private String hostname;
    private String username;
    private String password;
    private String driver;
    private Integer port;
    private String url;

    @Getter
    @Setter
    public static class Hibernate {
        private Ejb ejb;
        private Envers envers;
        private String dialect;
        private Boolean showSql;
        private Boolean formatSql;
        private String ddlAuto;
        private Boolean nonContextualCreation;
        private Boolean useJdbcMetadataDefaults;

        @Getter
        @Setter
        public static class Ejb {
            private String namingStrategy;
        }

        @Getter
        @Setter
        public static class Envers {
            private String revisionTypeFieldName;
            private Boolean integrationEnabled;
            private String revisionFieldName;
            private String auditTableSuffix;
            private Boolean autoRegister;
            private String defaultSchema;
            private String auditStrategy;
        }
    }
}
