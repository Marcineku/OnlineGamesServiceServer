package pl.edu.wat.wcy.pz.project.server.form;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameStateDto {
    private GameStatus gameStatus;
    private Map<Integer, String> fields;

}
