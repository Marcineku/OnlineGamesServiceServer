package pl.edu.wat.wcy.pz.project.server.listener;

import jdk.nashorn.internal.parser.JSONParser;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameDTO;
import pl.edu.wat.wcy.pz.project.server.mapper.TicTacToeGameMapper;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Component
public class UnsubscribeListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private TicTacToeGameRepository ticTacToeGameRepository;

    private SimpUserRegistry userRegistry;

    private SimpMessagingTemplate template;

    private TicTacToeGameMapper ticTacToeGameMapper;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {

        //will fix it later
//        System.out.println(event.toString() + "<--");
//        SimpUser user = userRegistry.getUser(event.getUser().getName());
//        Set<SimpSession> sessions = user.getSessions();
//        System.out.println(sessions.size() + "fds");
//        sessions.forEach(simpSession -> System.out.println("xD" + simpSession));
//
//        String id = event.getMessage().getHeaders().get("nativeHeaders").toString();
//        Pattern regex = Pattern.compile("(?<=([=][\\[]))[!-~]*(?=(]}))");
//        Matcher matcher = regex.matcher(id);
//        if (matcher.find())
//            id = matcher.group(0);
//        System.out.println("xD" + id);
//        Set<SimpSubscription> subscriptions = userRegistry.findSubscriptions(new SimpSubscriptionMatcher() {
//            @Override
//            public boolean match(SimpSubscription subscription) {
//                return true;
//            }
//        });
//        System.out.println("fsda" + subscriptions.size());
//        subscriptions.forEach(System.out::println);
        String username = event.getUser().getName();
        List<TicTacToeGame> games = ticTacToeGameRepository.findAllBySecondPlayer_UsernameAndGameStatus(username, GameStatus.WAITING_FOR_PLAYER);
        games.forEach(ticTacToeGame -> ticTacToeGame.setSecondPlayer(null));
        ticTacToeGameRepository.saveAll(games);

        games.forEach(game -> {
                TicTacToeGameDTO dto = ticTacToeGameMapper.toDto(game);
                template.convertAndSend("/tictactoe/update", dto);
        });
    }
}
