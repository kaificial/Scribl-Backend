package com.birthday.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		String databaseUrl = System.getenv("DATABASE_URL");
		String jdbcUrlEnv = System.getenv("JDBC_DATABASE_URL");
		String dbHost = System.getenv("DB_HOST");
		String dbUser = System.getenv("DB_USER");
		String dbPass = System.getenv("DB_PASSWORD");

		String finalUrl = "jdbc:h2:mem:birthday_cards;DB_CLOSE_DELAY=-1";
		String finalUser = "sa";
		String finalPass = "";
		boolean isPostgres = false;

		// 1. check for combined jdbc url first
		if (jdbcUrlEnv != null && !jdbcUrlEnv.isEmpty()) {
			finalUrl = jdbcUrlEnv;
			isPostgres = finalUrl.contains("postgresql");
			if (dbUser != null)
				finalUser = dbUser;
			if (dbPass != null)
				finalPass = dbPass;
			System.out.println("backend: using provided jdbc_database_url");
		}
		// 2. check for render's native postgres url
		else if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("postgres")) {
			try {
				URI uri = new URI(databaseUrl);
				String userInfo = uri.getUserInfo();
				if (userInfo != null && userInfo.contains(":")) {
					finalUser = userInfo.split(":")[0];
					finalPass = userInfo.split(":")[1];
					String host = uri.getHost();
					int port = uri.getPort();
					String path = uri.getPath();
					String query = uri.getQuery();

					finalUrl = "jdbc:postgresql://" + host + ":" + (port == -1 ? "5432" : port) + path;
					if (query != null)
						finalUrl += "?" + query;
					isPostgres = true;
					System.out.println("backend: converted database_url to jdbc format");
				}
			} catch (URISyntaxException | ArrayIndexOutOfBoundsException e) {
				System.err.println("backend error: failed to parse database_url");
			}
		}
		// 3. check for individual variables
		else if (dbHost != null && !dbHost.isEmpty()) {
			String dbName = System.getenv("DB_NAME");
			finalUrl = "jdbc:postgresql://" + dbHost + ":5432/" + (dbName != null ? dbName : "neondb");
			if (dbUser != null)
				finalUser = dbUser;
			if (dbPass != null)
				finalPass = dbPass;
			isPostgres = true;
			System.out.println("backend: built jdbc url from individual variables");
		}

		// 4. force spring properties directly
		System.setProperty("spring.datasource.url", finalUrl);
		System.setProperty("spring.datasource.username", finalUser);
		System.setProperty("spring.datasource.password", finalPass);

		if (isPostgres) {
			System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
			System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
		} else {
			System.out.println("backend: using default (local) h2 database");
			System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.H2Dialect");
			System.setProperty("spring.datasource.driver-class-name", "org.h2.Driver");
		}

		SpringApplication.run(BackendApplication.class, args);
	}
}
