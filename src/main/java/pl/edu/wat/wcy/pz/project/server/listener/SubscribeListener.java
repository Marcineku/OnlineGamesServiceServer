package pl.edu.wat.wcy.pz.project.server.listener;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class SubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    private final SimpMessagingTemplate messagingTemplate;

    private SimpUserRegistry userRegistry;

    private Subscribers subscribers;

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeListener.class);

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

        if (event.getUser() == null) {
            LOGGER.warn("Unauthenticated user - subscribe event.");
            return;
        }

        String username = event.getUser().getName();

        LOGGER.info("Subscription event: " + username);

        SimpUser user = userRegistry.getUser(username);
        if (user == null) {
            LOGGER.warn("User with this name not exist in userRegistry.");
            return;
        }
        Set<SimpSession> sessions = user.getSessions();
        Set<SimpSubscription> subscriptions = new HashSet<>();
        sessions.forEach(simpSession -> subscriptions.addAll(simpSession.getSubscriptions()));

        LOGGER.info("Subscriptions: ");
        subscriptions.forEach(simpSubscription -> {
            LOGGER.info("Id: " + simpSubscription.getId());
            LOGGER.info("Destination: " + simpSubscription.getDestination());
        });

        subscribers.addSubscriptions(username, subscriptions.stream().map(SimpSubscription::getDestination).collect(Collectors.toSet()));
    }
}
