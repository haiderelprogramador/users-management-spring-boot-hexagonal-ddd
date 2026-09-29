package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DatabaseConfig")
class DatabaseConfigTest {

  private static final String HOST = "db.local";
  private static final String DB_NAME = "crud_usuarios";
  private static final String USER = "user";
  private static final String PASS = "pass";

  @Test
  @DisplayName("buildJdbcUrl() builds a MySQL URL when engine is MYSQL")
  void shouldBuildMySqlUrl() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig(DatabaseEngine.MYSQL, HOST, 3306, DB_NAME, USER, PASS, "disable");

    // Act
    final String url = config.buildJdbcUrl();

    // Assert
    assertEquals(
        "jdbc:mysql://db.local:3306/crud_usuarios?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        url);
  }

  @Test
  @DisplayName("buildJdbcUrl() builds a PostgreSQL URL with sslmode when engine is POSTGRESQL")
  void shouldBuildPostgreSqlUrl() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig(DatabaseEngine.POSTGRESQL, HOST, 5432, DB_NAME, USER, PASS, "require");

    // Act
    final String url = config.buildJdbcUrl();

    // Assert
    assertEquals("jdbc:postgresql://db.local:5432/crud_usuarios?sslmode=require", url);
  }

  @Test
  @DisplayName("DatabaseEngine.fromString() resolves values ignoring case and spaces")
  void shouldResolveEngineFromString() {
    // Act & Assert
    assertAll(
        () -> assertEquals(DatabaseEngine.MYSQL, DatabaseEngine.fromString(" MySQL ")),
        () -> assertEquals(DatabaseEngine.POSTGRESQL, DatabaseEngine.fromString("postgresql")));
  }

  @Test
  @DisplayName("DatabaseEngine.fromString() rejects unsupported engines")
  void shouldRejectUnsupportedEngine() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> DatabaseEngine.fromString("oracle"));
  }
}
