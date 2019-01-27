package pl.edu.wat.wcy.pz.project.server.form;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;

@Getter
@Setter
@AllArgsConstructor
public class TicTacToeGameStateDTO {
    private Long gameId;
    private GameStatus gameStatus;
    private Integer[] gameFields;
    private String userTurn;
    private String firstUser;
    private String secondUser;
    @JsonIgnore
    private GameType gameType;

    public TicTacToeGameStateDTO(Long gameId, String firstUser, String secondUser, GameType gameType) {
        this.gameId = gameId;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.userTurn = firstUser;
        this.gameType = gameType;
        this.gameFields = new Integer[9];
        this.gameStatus = GameStatus.IN_PROGRESS;

        for (int i = 0; i < gameFields.length; i++) {
            gameFields[i] = 0;
        }
    }

    public Integer getField(Integer fieldNumber) {
        return gameFields[fieldNumber];
    }

    public void setField(Integer fieldNumber, Integer value) {
        gameFields[fieldNumber] = value;
    }

    public int howManyEmptyFields() {
        int howMany = 0;
        for (int i = 0; i < gameFields.length; i++) {
            if (gameFields[i] == 0)
                howMany++;
        }
        return howMany;
    }
}
