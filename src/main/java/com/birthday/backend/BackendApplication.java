package com.birthday.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		// we parse the render database url before anything else starts
		String databaseUrl = System.getenv("DATABASE_URL");

		if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("postgres")) {
			try {
				// java needs the jdbc prefix and a slightly different format
				URI uri = new URI(databaseUrl);
				String userInfo = uri.getUserInfo();

				if (userInfo != null && userInfo.contains(":")) {
					String username = userInfo.split(":")[0];
					String password = userInfo.split(":")[1];
					String host = uri.getHost();
					int port = uri.getPort();
					String path = uri.getPath();

					// build the jdbc string that spring boot expects
					String jdbcUrl = "jdbc:postgresql://" + host + ":" + (port == -1 ? "5432" : port) + path;

					System.setProperty("JDBC_DATABASE_URL", jdbcUrl);
					System.setProperty("DB_USER", username);
					System.setProperty("DB_PASSWORD", password);

					System.out.println("backend: converted render url to jdbc format successfully");
				}
			} catch (URISyntaxException | ArrayIndexOutOfBoundsException e) {
				System.err.println("backend error: failed to parse the database url");
			}
		} else {
			System.out.println("backend: no remote database url found, falling back to local settings");
		}

		SpringApplication.run(BackendApplication.class, args);
	}
}
