package br.com.coletaverde.domain.user.entities;

import br.com.coletaverde.domain.user.enums.Role;
import br.com.coletaverde.domain.user.enums.UserStatus;
import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity()
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends PersistenceEntity implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "Email is mandatory.")
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email should be valid.")
    @Size(max = 150, message = "Email must be at most 150 characters.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * @return 
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * @return 
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * @return 
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * @return 
     */
    @Override
    public boolean isEnabled() {
        return this.status.equals(UserStatus.ACTIVE);
    }
}
