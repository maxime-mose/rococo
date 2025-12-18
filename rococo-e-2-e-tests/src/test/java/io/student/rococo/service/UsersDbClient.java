package io.student.rococo.service;

import io.student.rococo.config.Config;
import io.student.rococo.model.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

  private final static String INSERT_USER_QUERY = """
      INSERT INTO user (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
      VALUES (UUID_TO_BIN(?, TRUE), ?, ?, TRUE, TRUE, TRUE, TRUE);
      """;
  private final static String INSERT_AUTHORITY_QUERY =
      "INSERT INTO authority (id, user_id, authority) VALUES (UUID_TO_BIN(?, TRUE), UUID_TO_BIN(?, TRUE), ?);";

  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public UserJson createUser(String username, String password) {
    UUID userId = UUID.randomUUID();
    JdbcTemplate jdbcTemplate = getJdbcTemplate();

    jdbcTemplate.update(c -> {
          PreparedStatement ps = c.prepareStatement(INSERT_USER_QUERY);
          ps.setString(1, userId.toString());
          ps.setString(2, username);
          ps.setString(3, passwordEncoder.encode(password));
          return ps;
        }
    );

    Arrays.stream(Authority.values()).forEach(authority ->
        jdbcTemplate.update(c -> {
              PreparedStatement ps = c.prepareStatement(INSERT_AUTHORITY_QUERY);
              ps.setString(1, UUID.randomUUID().toString());
              ps.setString(2, userId.toString());
              ps.setString(3, authority.name().toLowerCase());
              return ps;
            }
        )
    );

    return new UserJson(userId, username, password);
  }

  private JdbcTemplate getJdbcTemplate() {
    Connection connection;
    try {
      connection = DriverManager.getConnection(
          Config.getInstance().jdbcUrl(),
          Config.getInstance().dbUsername(),
          Config.getInstance().dbPassword()
      );
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
  }

  private enum Authority {READ, WRITE}
}
