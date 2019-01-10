package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeMove;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeDTO;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeMoveRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TicTacToeService {

    private TicTacToeGameRepository ticTacToeGameRepository;
    private TicTacToeMoveRepository ticTacToeMoveRepository;

    private UserRepository userRepository;

    public TicTacToeGame createGame(TicTacToeDTO ticTacToeDTO, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        TicTacToeGame newGame = TicTacToeGame.builder()
                .firstPlayer(user)
                .firstPlayerPieceCode(ticTacToeDTO.getPieceCode())
                .gameType(ticTacToeDTO.getGameType())
                .gameStatus(ticTacToeDTO.getGameType() == GameType.SINGLEPLAYER ? GameStatus.IN_PROGERSS : GameStatus.WATINIG_FOR_PLAYER)
                .build();

        ticTacToeGameRepository.save(newGame);
        return newGame;
    }

    public List<TicTacToeGame> getAvailableGames(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ticTacToeGameRepository.findAllByGameTypeAndGameStatusAndFirstPlayerNot(GameType.MULTIPLAYER, GameStatus.WATINIG_FOR_PLAYER, user);
    }

    public List<TicTacToeGame> getUserGames(String username) {
        return ticTacToeGameRepository.findAllByFirstPlayer_Username(username);
    }

    public List<TicTacToeGame> getUserGamesHistory(String username, GameType gameType) {
        if (gameType.equals(GameType.SINGLEPLAYER))
            return ticTacToeGameRepository.findAllByFirstPlayer_UsernameAndGameTypeAndGameStatusIn(username, GameType.SINGLEPLAYER, Arrays.asList(GameStatus.FIRST_PLAYER_WON, GameStatus.SECOND_PLAYER_WON, GameStatus.DRAW));
        else {
            List<TicTacToeGame> games = ticTacToeGameRepository.findAllByFirstPlayer_Username(username);
            games.addAll(ticTacToeGameRepository.findAllBySecondPlayer_Username(username));

            games = games.stream().filter(ticTacToeGame -> ticTacToeGame.getGameType().equals(GameType.MULTIPLAYER))
                    .filter(ticTacToeGame ->
                            ticTacToeGame.getGameStatus().equals(GameStatus.FIRST_PLAYER_WON) ||
                                    ticTacToeGame.getGameStatus().equals(GameStatus.SECOND_PLAYER_WON) ||
                                    ticTacToeGame.getGameStatus().equals(GameStatus.DRAW)
                    ).collect(Collectors.toList());
            return games;
        }
    }

    public List<TicTacToeMove> getGameMoves(Long gameId) {
        return ticTacToeMoveRepository.findAllByGame_GameId(gameId);
    }
}
