package pl.edu.wat.wcy.pz.project.server.form.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class JwtResponse {

    private String jwtToken;
    private String tokenType;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String jwtToken, String username, Collection<? extends GrantedAuthority> authorities) {
        this.jwtToken = jwtToken;
        this.tokenType = "Bearer";
        this.username = username;
        this.authorities = authorities;
    }
}
