package pl.edu.wat.wcy.pz.project.server.entity.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.wat.wcy.pz.project.server.entity.User;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TIC_TAC_TOE_MOVE")
public class TicTacToeMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVE_ID")
    private Long moveId;

    @Column(name = "MOVE_NO")
    private Long moveNo;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "FIELD")
    private int field;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = false)
    private TicTacToeGame game;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
