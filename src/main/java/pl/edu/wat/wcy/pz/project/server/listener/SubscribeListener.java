package pl.edu.wat.wcy.pz.project.server.listener;

import lombok.AllArgsConstructor;
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

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

        if (userRegistry == null) System.out.println("NUll");


        String user = event.getUser().getName();
        SimpUser user1 = userRegistry.getUser(user);

        System.out.println("SimpUser: " + user1);

        Set<SimpSession> sessions = userRegistry.getUser(user).getSessions();
        Set<SimpSubscription> subscriptions = new HashSet<>();
        sessions.forEach(simpSession -> subscriptions.addAll(simpSession.getSubscriptions()));

        System.out.println("Subscribe:");
        subscriptions.forEach(simpSubscription -> {
            System.out.println("id " + simpSubscription.getId());
            System.out.println("destination " + simpSubscription.getDestination());
        });

        subscribers.addSubscriptions(user, subscriptions.stream().map(SimpSubscription::getDestination).collect(Collectors.toSet()));
    }
}
