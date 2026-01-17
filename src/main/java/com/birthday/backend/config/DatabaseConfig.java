package com.birthday.backend.config;

import org.springframework.context.annotation.Configuration;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    // this runs before the datasource is initialized
    static {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && databaseUrl.startsWith("postgres")) {
            try {
                // render gives us postgres://user:pass@host:port/db
                // java needs jdbc:postgresql://host:port/db
                URI uri = new URI(databaseUrl);
                String username = uri.getUserInfo().split(":")[0];
                String password = uri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();

                // we set these so spring boot can find them in application.properties
                System.setProperty("JDBC_DATABASE_URL", dbUrl);
                System.setProperty("DB_USER", username);
                System.setProperty("DB_PASSWORD", password);
                System.setProperty("DB_DRIVER", "org.postgresql.Driver");
                System.setProperty("DB_DIALECT", "org.hibernate.dialect.PostgreSQLDialect");

                // log a little something to help debugging
                System.out.println("database config: found postgres url, switching to prod mode");
            } catch (URISyntaxException | ArrayIndexOutOfBoundsException e) {
                System.err.println("database config error: couldn't parse DATABASE_URL");
            }
        } else {
            System.out.println("database config: no remote url found, staying in local h2 mode");
        }
    }
}
