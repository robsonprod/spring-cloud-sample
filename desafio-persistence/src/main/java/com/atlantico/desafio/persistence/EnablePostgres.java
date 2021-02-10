package com.atlantico.desafio.persistence;

import com.atlantico.desafio.persistence.config.PostgresConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(PostgresConfig.class)
public @interface EnablePostgres {
}

