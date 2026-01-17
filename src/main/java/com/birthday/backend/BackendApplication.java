package com.birthday.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		// prioritizing combined urls but cleaning them for the fussy postgres driver
		String urlToParse = (jdbcUrlEnv != null && !jdbcUrlEnv.isEmpty()) ? jdbcUrlEnv : databaseUrl;

		if (urlToParse != null && !urlToParse.isEmpty()
				&& (urlToParse.startsWith("postgres") || urlToParse.contains("postgresql"))) {
			isPostgres = true;

			// we use a regex to handle the common "user:pass@host:port/db" format
			// this handles both postgres:// and jdbc:postgresql:// prefixes
			Pattern pattern = Pattern.compile(
					"(?:jdbc:postgresql://|postgres://)([^:]+):([^@]+)@([^:/]+)(?::(\\d+))?(/[^?]+)?(?:\\?(.+))?");
			Matcher matcher = pattern.matcher(urlToParse);

			if (matcher.find()) {
				finalUser = matcher.group(1);
				finalPass = matcher.group(2);
				String host = matcher.group(3);
				String port = matcher.group(4) != null ? matcher.group(4) : "5432";
				String path = matcher.group(5) != null ? matcher.group(5) : "/neondb";
				String query = matcher.group(6);

				finalUrl = "jdbc:postgresql://" + host + ":" + port + path;
				if (query != null)
					finalUrl += "?" + query;

				System.out.println("backend: detected and cleaned uri-style connection string");
			} else {
				// if no @ symbol, we assume it's already a valid jdbc url or parts are provided
				// separately
				finalUrl = urlToParse.startsWith("postgres") ? urlToParse.replace("postgres://", "jdbc:postgresql://")
						: urlToParse;
				if (dbUser != null)
					finalUser = dbUser;
				if (dbPass != null)
					finalPass = dbPass;
				System.out.println("backend: using connection string as-is (no credentials in url)");
			}
		} else if (dbHost != null && !dbHost.isEmpty()) {
			String dbName = System.getenv("DB_NAME");
			finalUrl = "jdbc:postgresql://" + dbHost + ":5432/" + (dbName != null ? dbName : "neondb");
			if (dbUser != null)
				finalUser = dbUser;
			if (dbPass != null)
				finalPass = dbPass;
			isPostgres = true;
			System.out.println("backend: built url from individual parts");
		}

		// force everything into system properties so spring doesn't have to guess
		System.setProperty("spring.datasource.url", finalUrl);
		System.setProperty("spring.datasource.username", finalUser);
		System.setProperty("spring.datasource.password", finalPass);

		if (isPostgres) {
			System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
			System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
		} else {
			System.out.println("backend: falling back to local h2 database");
			System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.H2Dialect");
			System.setProperty("spring.datasource.driver-class-name", "org.h2.Driver");
		}

		SpringApplication.run(BackendApplication.class, args);
	}
}
