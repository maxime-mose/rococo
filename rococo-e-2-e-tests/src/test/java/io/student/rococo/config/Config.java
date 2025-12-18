package io.student.rococo.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String authUrl();

  String frontUrl();

  String jdbcUrl();

  String dbUsername();

  String dbPassword();
}
