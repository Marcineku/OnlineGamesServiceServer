package pl.edu.wat.wcy.pz.project.server.service.logic;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameDTO;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeMoveRepository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TicTacToeLogic {

    private TicTacToeMoveRepository moveRepository;
    private TicTacToeGameRepository gameRepository;

    private List<TicTacToeGameDTO> gameDTOList = new ArrayList<>();

    public TicTacToeGameDTO updateGame(Integer gameId, String username, Integer fieldNumber) {


        return null;
    }
}
