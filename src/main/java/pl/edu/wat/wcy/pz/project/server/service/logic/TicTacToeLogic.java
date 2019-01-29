package pl.edu.wat.wcy.pz.project.server.service.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.dto.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeMove;
import pl.edu.wat.wcy.pz.project.server.entity.game.enumeration.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.enumeration.GameType;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeMoveRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.beans.ConstructorProperties;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicTacToeLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeLogic.class);

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

        LOGGER.info("Starting new game. Id: " + game.getGameId() + ". Type:" + game.getGameType());

        TicTacToeGameStateDTO newGameState = new TicTacToeGameStateDTO(game.getGameId(), game.getFirstPlayer().getUsername(), game.getSecondPlayer() == null ? null : game.getSecondPlayer().getUsername(), game.getGameType());
        gameStateDTOMap.put(game.getGameId(), newGameState);
        return true;
    }

    public TicTacToeGameStateDTO updateGame(Long gameId, String username, Integer fieldNumber) {
        if (!gameStateDTOMap.containsKey(gameId)) {
            LOGGER.warn("Game not exist: " + gameId + ". Cannot move!");
            throw new RuntimeException("Game not exist");
        }
        TicTacToeGameStateDTO ticTacToeGameStateDTO = gameStateDTOMap.get(gameId);
        int playerNumber;
        if (ticTacToeGameStateDTO.getGameType() == GameType.MULTIPLAYER) {
            if (!username.equals(ticTacToeGameStateDTO.getUserTurn())) {
                LOGGER.info("Wrong user was trying to make move. User: " + username + ". Turn: " + ticTacToeGameStateDTO.getUserTurn());
                throw new RuntimeException("Wrong user");
            }
            playerNumber = ticTacToeGameStateDTO.getUserTurn().equals(ticTacToeGameStateDTO.getFirstUser()) ? 1 : 2;
        } else {
            if (username == null) {
                if (ticTacToeGameStateDTO.getUserTurn() != null) {
                    LOGGER.info("Wrong user was trying to make move. User: " + "AI" + ". Turn: " + ticTacToeGameStateDTO.getUserTurn());
                    throw new RuntimeException("Wrong user");
                }
                playerNumber = 2;
            } else {
                if (!username.equals(ticTacToeGameStateDTO.getUserTurn())) {
                    LOGGER.info("Wrong user was trying to make move. User: " + username + ". Turn: " + ticTacToeGameStateDTO.getUserTurn());
                    throw new RuntimeException("Wrong user");
                }
                playerNumber = 1;
            }
        }
        Integer[] gameFields = ticTacToeGameStateDTO.getGameFields();
        if (gameFields[fieldNumber] != 0) {
            LOGGER.info("Field already taken. Game: " + gameId + ". Field: " + fieldNumber);
            throw new RuntimeException("This field is already taken");
        }

        gameFields[fieldNumber] = playerNumber;

        Move move = new Move(ticTacToeGameStateDTO.getGameId(), 9 - ticTacToeGameStateDTO.howManyEmptyFields(), Calendar.getInstance().getTime(), fieldNumber, ticTacToeGameStateDTO.getUserTurn());
        moves.add(move);
        LOGGER.trace("Created move: " + fieldNumber + ". Game: " + gameId);

        boolean statusChanged = updateGameStatus(ticTacToeGameStateDTO, playerNumber);

        if (statusChanged) {
            gameRepository.getOne(ticTacToeGameStateDTO.getGameId());
            Optional<TicTacToeGame> gameOptional = gameRepository.findById(ticTacToeGameStateDTO.getGameId());
            TicTacToeGame game = gameOptional.get();
            game.setGameStatus(ticTacToeGameStateDTO.getGameStatus());
            gameRepository.save(game);
            List<Move> movesToAdd = moves.stream().filter(m -> m.getGameId().equals(ticTacToeGameStateDTO.getGameId())).collect(Collectors.toList());
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
            gameStateDTOMap.remove(ticTacToeGameStateDTO.getGameId());
            template.convertAndSend("/tictactoe/delete", ticTacToeGameStateDTO.getGameId());
        } else {
            ticTacToeGameStateDTO.setUserTurn(playerNumber == 1 ? ticTacToeGameStateDTO.getSecondUser() : ticTacToeGameStateDTO.getFirstUser());
        }
        return ticTacToeGameStateDTO;
    }

    private boolean updateGameStatus(TicTacToeGameStateDTO ticTacToeGameStateDTO, int playerNumber) {
        Integer[] gameFields = ticTacToeGameStateDTO.getGameFields();
        boolean endGame = checkIfPlayerWon(gameFields, playerNumber);
        if (endGame) {
            LOGGER.info("Changed game state. Id: " + ticTacToeGameStateDTO.getGameId() + ". Winner: " + (playerNumber == 1 ? ticTacToeGameStateDTO.getFirstUser() : ticTacToeGameStateDTO.getSecondUser()));
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

    public Optional<TicTacToeGameStateDTO> getGameState(Long gameId) {
        return Optional.ofNullable(gameStateDTOMap.get(gameId));
    }

    public int getNextAIMove(Integer[] gameFields) {
        List<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < gameFields.length; i++) {
            if (gameFields[i] == 0)
                possibleMoves.add(i);
        }
        Random random = new Random();
        int i = random.nextInt(possibleMoves.size());
        return possibleMoves.get(i);
    }

    public TicTacToeGameStateDTO createAIMove(TicTacToeGameStateDTO gameState) throws InterruptedException {
        Thread.sleep(2000);
        int aiMove = getNextAIMove(gameState.getGameFields());
        LOGGER.trace("Next AI move: " + aiMove + ". Game: " + gameState.getGameId());
        return updateGame(gameState.getGameId(), null, aiMove);
    }
}
