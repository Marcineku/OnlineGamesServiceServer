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
public class TicTacToeGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private PieceCode firstPlayerPieceCode;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
}
