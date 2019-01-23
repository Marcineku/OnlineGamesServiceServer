package pl.edu.wat.wcy.pz.project.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;

import java.util.Collection;
import java.util.List;

@Repository
public interface TicTacToeGameRepository extends JpaRepository<TicTacToeGame, Long> {
    List<TicTacToeGame> findAllByGameTypeAndFirstPlayerNot(GameType gameType, User firstPlayer);
    List<TicTacToeGame> findAllByFirstPlayer_Username(String firstPlayer_username);
    List<TicTacToeGame> findAllByFirstPlayer_UsernameAndGameTypeAndGameStatusIn(String firstPlayer_username, GameType gameType, Collection<GameStatus> gameStatus);
    List<TicTacToeGame> findAllBySecondPlayer_Username(String secondPlayer_username);

    List<TicTacToeGame> findAllBySecondPlayer_UsernameAndGameStatus(String secondPlayer_username, GameStatus gameStatus);

    List<TicTacToeGame> findAllByFirstPlayer_UsernameAndGameStatusIn(String firstPlayer_username, Collection<GameStatus> gameStatus);

    boolean existsByFirstPlayer_UsernameAndGameStatusIn(String firstPlayer_username, Collection<GameStatus> gameStatus);
}
