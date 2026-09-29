package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/** Motores de base de datos soportados por los adaptadores de persistencia. */
public enum DatabaseEngine {
  MYSQL("mysql"),
  POSTGRESQL("postgresql");

  private static final String ERROR_UNSUPPORTED_ENGINE =
      "Motor de base de datos no soportado: '%s'. Valores permitidos: mysql, postgresql";

  private final String propertyValue;

  DatabaseEngine(final String propertyValue) {
    this.propertyValue = propertyValue;
  }

  public String propertyValue() {
    return propertyValue;
  }

  public static DatabaseEngine fromString(final String value) {
    final String normalized =
        Objects.requireNonNull(value, "Database engine cannot be null").trim().toLowerCase(Locale.ROOT);
    return Arrays.stream(values())
        .filter(engine -> engine.propertyValue.equals(normalized))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(ERROR_UNSUPPORTED_ENGINE, value)));
  }
}
