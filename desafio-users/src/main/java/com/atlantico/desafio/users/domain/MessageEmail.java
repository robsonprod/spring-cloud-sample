package com.atlantico.desafio.users.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email
    @NotNull
    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("message")
    private String message;
}
