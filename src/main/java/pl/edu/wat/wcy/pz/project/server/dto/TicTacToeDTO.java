package pl.edu.wat.wcy.pz.project.server.dto;

import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.pz.project.server.entity.game.enumeration.PieceCode;

@Getter
@Setter
public class TicTacToeDTO {
    private String gameType;
    private PieceCode pieceCode;
}
