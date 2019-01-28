package pl.edu.wat.wcy.pz.project.server.entity.game;

import lombok.*;
import pl.edu.wat.wcy.pz.project.server.entity.User;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TIC_TAC_TOE_GAME")
public class TicTacToeGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_ID")
    private Long gameId;

    @Column(name = "CREATED")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "FIRST_PLAYER_ID", nullable = false)
    private User firstPlayer;

    @ManyToOne
    @JoinColumn(name = "SECOND_PLAYER_ID")
    private User secondPlayer;

    @Enumerated(EnumType.STRING)
    @Column(name = "FIRST_PLAYER_PIECE_CODE")
    private PieceCode firstPlayerPieceCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "GAME_TYPE")
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "GAME_STATUS")
    private GameStatus gameStatus;
}
