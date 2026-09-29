package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

public record DatabaseConfig(
    DatabaseEngine engine,
    String host,
    int port,
    String databaseName,
    String username,
    String password,
    String sslMode) {

  private static final String MYSQL_URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

  // sslMode: disable (local / Docker) | require (Supabase, Render y otros PG en la nube)
  private static final String POSTGRESQL_URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s?sslmode=%s";

  public String buildJdbcUrl() {
    return switch (engine) {
      case MYSQL -> String.format(MYSQL_URL_TEMPLATE, host, port, databaseName);
      case POSTGRESQL -> String.format(POSTGRESQL_URL_TEMPLATE, host, port, databaseName, sslMode);
    };
  }
}
