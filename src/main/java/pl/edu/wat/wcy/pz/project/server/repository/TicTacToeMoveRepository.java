package pl.edu.wat.wcy.pz.project.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeMove;

import java.util.List;

@Repository
public interface TicTacToeMoveRepository extends JpaRepository<TicTacToeMove, Long> {
    List<TicTacToeMove> findAllByGame_GameId(Long game_gameId);
}
