package io.student.rococo.service;

import io.student.rococo.model.UserJson;

public interface UsersClient {

  UserJson createUser(String username, String password);
}
