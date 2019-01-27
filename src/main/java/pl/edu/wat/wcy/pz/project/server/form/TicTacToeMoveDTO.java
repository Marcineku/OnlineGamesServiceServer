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
public class TicTacToeMoveDTO {
    private Long moveNo;
    private Date created;
    private int field;
    private Long gameId;
    private String username;
}
