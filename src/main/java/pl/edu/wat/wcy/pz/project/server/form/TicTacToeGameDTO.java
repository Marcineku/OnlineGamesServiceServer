package pl.edu.wat.wcy.pz.project.server.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicTacToeGameDTO {
    private Long gameId;
    private Date created;
    private String firstPlayer;
    private String secondPlayer;
    private String firstPlayerPieceCode;
    private String gameType;
    private String gameStatus;
}
