package pl.edu.wat.wcy.pz.project.server.form;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;

@Getter
@Setter
@AllArgsConstructor
public class TicTacToeGameStateDTO {
    private Integer gameId;
    private GameStatus gameStatus;
    private Integer[] gameFields;
    private String userTurn;

    public TicTacToeGameStateDTO(Integer gameId, String userTurn) {
        this.gameId = gameId;
        this.userTurn = userTurn;
        this.gameFields = new Integer[9];
        this.gameStatus = GameStatus.IN_PROGERSS;
    }
}
