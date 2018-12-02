package pl.edu.wat.wcy.pz.project.server;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.wat.wcy.pz.project.server.entity.Role;
import pl.edu.wat.wcy.pz.project.server.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User byUserName) {
        this.username = byUserName.getUserName();
        this.password = byUserName.getPassword();

        List<GrantedAuthority> auths = new ArrayList<>();
        byUserName.getRoles().stream().map(Role::getRoleName).map(String::toUpperCase).map(SimpleGrantedAuthority::new).forEach(auths::add);
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
