package pl.edu.wat.wcy.pz.project.server.service.logic;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeMoveRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class TicTacToeLogic {

    private TicTacToeMoveRepository moveRepository;
    private TicTacToeGameRepository gameRepository;

    private Map<Integer,TicTacToeGameStateDTO> gameStateDTOMap = new HashMap<>();

    public TicTacToeGameStateDTO updateGame(Integer gameId, String username, Integer fieldNumber) {

        if(!gameStateDTOMap.containsKey(gameId)) {
            //TicTacToeGameStateDTO gameState = new TicTacToeGameStateDTO()
        }


        return null;
    }
}
