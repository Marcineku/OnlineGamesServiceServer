package pl.edu.wat.wcy.pz.project.server.service.logic;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeMove;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeMoveRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.beans.ConstructorProperties;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicTacToeLogic {

    private TicTacToeMoveRepository moveRepository;
    private TicTacToeGameRepository gameRepository;
    private UserRepository userRepository;

    private final SimpMessagingTemplate template;

    private Map<Long, TicTacToeGameStateDTO> gameStateDTOMap;
    private List<Move> moves;

    @ConstructorProperties({"moveRepository", "gameRepository", "userRepository", "template"})
    public TicTacToeLogic(TicTacToeMoveRepository moveRepository, TicTacToeGameRepository gameRepository, UserRepository userRepository, SimpMessagingTemplate template) {
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.template = template;
        this.gameStateDTOMap = new HashMap<>();
        this.moves = new ArrayList<>();
    }


    public boolean startNewGame(TicTacToeGame game) {

        if (gameStateDTOMap.containsKey(game.getGameId()))
            return false;

        gameStateDTOMap.put(game.getGameId(), new TicTacToeGameStateDTO(game.getGameId(), game.getFirstPlayer().getUsername(), game.getFirstPlayer().getUsername(), game.getSecondPlayer().getUsername()));
        return true;
    }

    public TicTacToeGameStateDTO updateGame(Long gameId, String username, Integer fieldNumber) {

        if (!gameStateDTOMap.containsKey(gameId)) {
            throw new RuntimeException("Game not exist");
        }
        TicTacToeGameStateDTO ticTacToeGameStateDTO = gameStateDTOMap.get(gameId);
        if (!username.equals(ticTacToeGameStateDTO.getUserTurn())) {
            throw new RuntimeException("Wrong user");
        }
        Integer[] gameFields = ticTacToeGameStateDTO.getGameFields();
        if (gameFields[fieldNumber] != 0) {
            throw new RuntimeException("This field is already taken");
        }

        //make move
        //check if somebody won
        //update gameStatus or update user
        int playerNumber = ticTacToeGameStateDTO.getUserTurn().equals(ticTacToeGameStateDTO.getFirstUser()) ? 1 : 2;

        gameFields[fieldNumber] = playerNumber;

        Move move = new Move(gameId, 9 - ticTacToeGameStateDTO.howManyEmptyFields(), Calendar.getInstance().getTime(), fieldNumber, ticTacToeGameStateDTO.getUserTurn());
        moves.add(move);

        boolean statusChanged = updateGameStatus(ticTacToeGameStateDTO, playerNumber);

        if (statusChanged) {
            Optional<TicTacToeGame> gameOptional = gameRepository.findById(gameId);
            TicTacToeGame game = gameOptional.get();
            game.setGameStatus(ticTacToeGameStateDTO.getGameStatus());
            gameRepository.save(game);
            List<Move> movesToAdd = moves.stream().filter(m -> m.getGameId().equals(gameId)).collect(Collectors.toList());
            moves.removeAll(movesToAdd);
            for (Move m : movesToAdd) {
                TicTacToeMove ticTacToeMove = new TicTacToeMove();
                ticTacToeMove.setGame(game);
                ticTacToeMove.setCreated(m.getCreated());
                ticTacToeMove.setField(m.getField());
                ticTacToeMove.setMoveNo((long) m.getMoveNo());
                ticTacToeMove.setUser(userRepository.findByUsername(m.getUsername()).orElse(null));
                moveRepository.save(ticTacToeMove);
            }
            gameStateDTOMap.remove(gameId);

            template.convertAndSend("/tictactoe/delete", gameId);
        }

        return ticTacToeGameStateDTO;
    }

    private boolean updateGameStatus(TicTacToeGameStateDTO ticTacToeGameStateDTO, int playerNumber) {
        Integer[] gameFields = ticTacToeGameStateDTO.getGameFields();
        boolean endGame = checkIfPlayerWon(gameFields, playerNumber);
        if (endGame) {
            ticTacToeGameStateDTO.setGameStatus(playerNumber == 1 ? GameStatus.FIRST_PLAYER_WON : GameStatus.SECOND_PLAYER_WON);
            return true;
        }
        if (ticTacToeGameStateDTO.howManyEmptyFields() == 0) {
            ticTacToeGameStateDTO.setGameStatus(GameStatus.DRAW);
            return true;
        }
        return false;
    }

    private boolean checkIfPlayerWon(Integer[] gameFields, int playerNumber) {
        if (gameFields[0] == playerNumber && gameFields[1] == playerNumber && gameFields[2] == playerNumber)
            return true;
        if (gameFields[3] == playerNumber && gameFields[4] == playerNumber && gameFields[5] == playerNumber)
            return true;
        if (gameFields[6] == playerNumber && gameFields[7] == playerNumber && gameFields[8] == playerNumber)
            return true;
        if (gameFields[0] == playerNumber && gameFields[3] == playerNumber && gameFields[6] == playerNumber)
            return true;
        if (gameFields[1] == playerNumber && gameFields[4] == playerNumber && gameFields[7] == playerNumber)
            return true;
        if (gameFields[2] == playerNumber && gameFields[5] == playerNumber && gameFields[8] == playerNumber)
            return true;
        if (gameFields[0] == playerNumber && gameFields[4] == playerNumber && gameFields[8] == playerNumber)
            return true;
        if (gameFields[2] == playerNumber && gameFields[4] == playerNumber && gameFields[6] == playerNumber)
            return true;
        return false;
    }
}
