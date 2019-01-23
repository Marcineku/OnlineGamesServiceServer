package pl.edu.wat.wcy.pz.project.server.service.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Move {
    private Long gameId;
    private int moveNo;
    private Date created;
    private int field;
    private String username;

}
