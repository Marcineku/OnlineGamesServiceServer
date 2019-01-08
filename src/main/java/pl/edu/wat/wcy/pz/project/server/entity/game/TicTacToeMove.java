package pl.edu.wat.wcy.pz.project.server.entity.game;

import lombok.*;
import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.pz.project.server.entity.User;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TicTacToeMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVE_ID")
    private Long moveId;
    @Column(name = "MOVE_NO")
    private Long moveNo;
    @Column(name = "CREATED")
    private Date created;
    @Column(name = "ROW")
    private int row;
    @Column(name = "COLUMN")
    private int column;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = false)
    private TicTacToeGame game;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = true)
    private User user;
}
