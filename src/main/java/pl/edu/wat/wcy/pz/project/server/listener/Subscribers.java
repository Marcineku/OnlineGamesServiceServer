package pl.edu.wat.wcy.pz.project.server.listener;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class Subscribers {

    private Map<String, Set<String>> subscriptionMap = new HashMap<>();

    public void addSubscriptions(String user, Set<String> subscriptions) {
        subscriptions.forEach(s -> addSubscription(user, s));
    }

    public void addSubscription(String user, String subscription) {
        if (subscriptionMap.containsKey(user)) {
            subscriptionMap.get(user).add(subscription);
        } else {
            Set<String> setToAdd = new HashSet<>();
            setToAdd.add(subscription);
            subscriptionMap.put(user, setToAdd);
        }
    }

    public String updateAndReturnDifference(String user, Set<String> subscriptions) {
        String unsubscribed = null;
        if (!subscriptionMap.containsKey(user)) {
            addSubscriptions(user, subscriptions);
            return null;
        }
        Set<String> oldSubs = subscriptionMap.get(user);
        oldSubs.removeAll(subscriptions);
        if (oldSubs.size() == 1) {
            System.out.println("Test");
            unsubscribed = oldSubs.iterator().next();
        }
        oldSubs.clear();
        addSubscriptions(user, subscriptions);
        return unsubscribed;
    }


}
