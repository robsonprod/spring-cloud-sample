package com.atlantico.desafio.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @SequenceGenerator(name = "seq_users", sequenceName = "seq_users", allocationSize = 1)
    @GeneratedValue(generator = "seq_users", strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @Column(unique = true)
    private String login;

    @Email
    @Column(unique = true)
    private String email;

    @Column(length = 200)
    @JsonIgnore
    private String password;

    @Column(name = "is_admin", columnDefinition = "boolean default false")
    private boolean admin;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @CreatedBy
    @Column(name = "created_by")
    protected String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected String lastModifiedBy;

    public User(Long id, String name, String login, String email, String password, boolean admin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.admin = admin;
    }

    @JsonIgnore
    public Role getRole() {
        return admin ? Role.ADMIN : Role.USER;
    }

    public enum Role {
        USER,
        ADMIN,
    }
}
