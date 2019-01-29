package pl.edu.wat.wcy.pz.project.server.configuration.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final String TOKEN_HEADER = "Authorization";
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketAuthChannelInterceptor.class);

    private WebSocketAuthenticationService webSocketAuthenticationService;
    private SimpUserRegistry userRegistry;

    @Autowired
    public WebSocketAuthChannelInterceptor(WebSocketAuthenticationService webSocketAuthenticationService) {
        this.webSocketAuthenticationService = webSocketAuthenticationService;
    }

    @Autowired
    public void setUserRegistry(@Lazy SimpUserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            LOGGER.error("Null pointer exception in StompHeaderAccessor");
            throw new RuntimeException("Error in websocket authorization");
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            final String headerValue = accessor.getFirstNativeHeader(TOKEN_HEADER);

            UsernamePasswordAuthenticationToken authenticationToken = webSocketAuthenticationService.authenticateToken(headerValue);
            accessor.setUser(authenticationToken);

            if (userRegistry.getUser(authenticationToken.getName()) != null) {
                LOGGER.warn("Logged user " + authenticationToken.getName() + " was trying to log in second time");
                throw new RuntimeException("User is already logged!");
            }
        }
        LOGGER.info("Logged users: " + userRegistry.getUsers());
        return message;
    }
}
