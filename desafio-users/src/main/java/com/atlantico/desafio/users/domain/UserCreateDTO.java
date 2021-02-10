package com.atlantico.desafio.users.domain;

import com.atlantico.desafio.persistence.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreateDTO implements Serializable {
    private final static long serialVersionUID = 1L;

    @NotNull
    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotNull
    @NotBlank
    @JsonProperty("login")
    private String login;

    @NotNull
    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;

    @NotNull
    @NotBlank
    @JsonProperty("password")
    private String password;

    public UserCreateDTO(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
    }

    public User toUser() {
        return new User(null, this.name, this.login, this.email, this.password, false);
    }
}
