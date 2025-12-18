package io.student.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("password")
    String password

    // TODO Раскомментировать, когда понадобятся все поля из wiremock/rest/mappings/get_user.json
    //  @JsonProperty("firstname")
    //  String firstName,
    //  @JsonProperty("lastname")
    //  String lastName,
    //  @JsonProperty("avatar")
    //  String avatar,
) {
}
