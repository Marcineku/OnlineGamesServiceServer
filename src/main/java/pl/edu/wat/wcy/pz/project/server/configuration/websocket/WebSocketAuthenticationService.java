package pl.edu.wat.wcy.pz.project.server.configuration.websocket;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.pz.project.server.configuration.security.jwt.JwtProvider;
import pl.edu.wat.wcy.pz.project.server.configuration.security.service.UserDetailsServiceImpl;

@AllArgsConstructor
@Component
public class WebSocketAuthenticationService {

    private JwtProvider jwtProvider;
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketAuthenticationService.class);

    public UsernamePasswordAuthenticationToken authenticateToken(String headerValue) {
        String token = getJwtFromHeaderValue(headerValue);

        if (token == null) {
            LOGGER.info("No token in websocket connection");
            throw new AuthenticationCredentialsNotFoundException("No token");
        }
        if (!jwtProvider.validateJwtToken(token)) {
            LOGGER.info("Invalid token");
            throw new BadCredentialsException("Invalid token");
        }
        String username = jwtProvider.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String getJwtFromHeaderValue(String headerValue) {
        if (headerValue != null && headerValue.startsWith("Bearer "))
            return headerValue.replace("Bearer ", "");
        return null;
    }
}
