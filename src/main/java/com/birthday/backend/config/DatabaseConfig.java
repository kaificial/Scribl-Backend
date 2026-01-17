package com.birthday.backend.config;

import org.springframework.context.annotation.Configuration;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    // we use a static block to set properties before spring starts the datasource
    static {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && (databaseUrl.startsWith("postgres") || databaseUrl.startsWith("jdbc:postgresql"))) {
            try {
                // if it's already jdbc format, we just use it
                if (databaseUrl.startsWith("jdbc:postgresql")) {
                    System.setProperty("JDBC_DATABASE_URL", databaseUrl);
                    System.out.println("database config: using provided jdbc database url");
                } else {
                    // convert render's postgres:// format to jdbc:postgresql://
                    URI uri = new URI(databaseUrl);
                    String userInfo = uri.getUserInfo();

                    if (userInfo != null && userInfo.contains(":")) {
                        String username = userInfo.split(":")[0];
                        String password = userInfo.split(":")[1];
                        String host = uri.getHost();
                        int port = uri.getPort();
                        String path = uri.getPath();

                        // build the jdbc string
                        String dbUrl = "jdbc:postgresql://" + host + ":" + (port == -1 ? "5432" : port) + path;

                        System.setProperty("JDBC_DATABASE_URL", dbUrl);
                        System.setProperty("DB_USER", username);
                        System.setProperty("DB_PASSWORD", password);

                        System.out.println("database config: converted render url to jdbc format");
                    }
                }
            } catch (URISyntaxException | ArrayIndexOutOfBoundsException e) {
                System.err.println("database config error: failed to parse DATABASE_URL");
            }
        } else {
            System.out.println("database config: no database_url found, defaulting to h2");
        }
    }
}
