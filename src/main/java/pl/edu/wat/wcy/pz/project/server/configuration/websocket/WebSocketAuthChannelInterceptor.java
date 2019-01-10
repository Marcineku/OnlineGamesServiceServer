package pl.edu.wat.wcy.pz.project.server.configuration.websocket;

import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private WebSocketAuthenticationService webSocketAuthenticationService;
    private static final String TOKEN_HEADER = "Authorization";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        //todo
        if (accessor == null) {
            System.out.println("NullPointer");
        }
        //todo
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            final String headerValue = accessor.getFirstNativeHeader(TOKEN_HEADER);

            UsernamePasswordAuthenticationToken authenticationToken = webSocketAuthenticationService.authenticateToken(headerValue);
            //todo
            // authenticationToken.setDetails(???);
            //todo
            accessor.setUser(authenticationToken);
        }
        return message;
    }
}
