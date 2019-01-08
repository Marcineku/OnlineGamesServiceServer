package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeDTO;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

@AllArgsConstructor
@Service
public class TicTacToeService {

    private TicTacToeGameRepository ticTacToeGameRepository;

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
}
