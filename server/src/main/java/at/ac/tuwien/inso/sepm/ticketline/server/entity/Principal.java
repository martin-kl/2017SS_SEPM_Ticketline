package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthorityService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"principalNews"})
@Entity
public class Principal extends Audited implements UserDetails {

    public enum Role {
        ADMIN,
        SELLER
    }

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false, length = 100, unique = true)
    @NotNull
    @Length(min = 3)
    private String username;

    @Column
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 10_000, unique = true)
    @Email
    @Length(min = 3)
    private String email;

    @Column
    private int failedLoginCount = 0;

    //this column is taken to do the unlock/locked user differentiation
    @Column
    @NotNull
    private boolean enabled = true;

    @Transient
    private boolean locked = false;

    @Transient
    private boolean expired = false;

    @Transient
    private boolean credentialsExpired = false;

    @Column
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.SELLER;

    // Relations

    @Getter
    @OneToMany(mappedBy = "principal", cascade = CascadeType.REMOVE)
    private Set<PrincipalNews> principalNews;

    // User Details implementations

    @JsonIgnore
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityService.getAuthoritiesForRole(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
