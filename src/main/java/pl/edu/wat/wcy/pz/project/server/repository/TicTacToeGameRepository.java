package pl.edu.wat.wcy.pz.project.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.entity.game.enumeration.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.enumeration.GameType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicTacToeGameRepository extends JpaRepository<TicTacToeGame, Long> {
    List<TicTacToeGame> findAllByGameTypeAndFirstPlayerNot(GameType gameType, User firstPlayer);

    List<TicTacToeGame> findAllBySecondPlayer_UsernameAndGameStatus(String secondPlayer_username, GameStatus gameStatus);

    List<TicTacToeGame> findAllByFirstPlayer_UsernameAndGameStatusIn(String firstPlayer_username, Collection<GameStatus> gameStatus);

    Optional<TicTacToeGame> findFirstByFirstPlayer_UsernameAndGameStatus(String firstPlayer_username, GameStatus gameStatus);

    List<TicTacToeGame> findAllByFirstPlayer_UsernameOrSecondPlayer_Username(String firstPlayer_username, String secondPlayer_username);

    boolean existsByFirstPlayer_UsernameAndGameStatusIn(String firstPlayer_username, Collection<GameStatus> gameStatus);
}
