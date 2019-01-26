package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.exception.GameNotFoundException;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeMoveDto;
import pl.edu.wat.wcy.pz.project.server.mapper.TicTacToeGameMapper;
import pl.edu.wat.wcy.pz.project.server.mapper.TicTacToeMoveMapper;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeMoveRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;
import pl.edu.wat.wcy.pz.project.server.service.logic.TicTacToeLogic;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TicTacToeService {

    private TicTacToeGameMapper ticTacToeGameMapper;
    private TicTacToeMoveMapper ticTacToeMoveMapper;

    private TicTacToeLogic ticTacToeLogic;

    private TicTacToeGameRepository ticTacToeGameRepository;
    private TicTacToeMoveRepository ticTacToeMoveRepository;
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeService.class);

    public TicTacToeGameDTO createGame(TicTacToeDTO ticTacToeDTO, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            LOGGER.error("User not found in repository: " + username);
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        if (ticTacToeGameRepository.existsByFirstPlayer_UsernameAndGameStatusIn(username, Arrays.asList(GameStatus.IN_PROGRESS, GameStatus.WAITING_FOR_PLAYER))) {
            LOGGER.error("User " + username + " is trying to create second game");
            throw new RuntimeException("This player has already created a game.");
        }
        TicTacToeGame newGame = TicTacToeGame.builder()
                .firstPlayer(user)
                .created(Calendar.getInstance().getTime())
                .firstPlayerPieceCode(ticTacToeDTO.getPieceCode())
                .gameType("singleplayer".equalsIgnoreCase(ticTacToeDTO.getGameType()) ? GameType.SINGLEPLAYER : GameType.MULTIPLAYER)
                .gameStatus(GameStatus.WAITING_FOR_PLAYER)
                .build();
        ticTacToeGameRepository.save(newGame);
        return ticTacToeGameMapper.toDto(newGame);
    }

    public TicTacToeGameDTO addSecondPlayerToGame(Long gameId, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            LOGGER.error("User not found in repository: " + username);
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        Optional<TicTacToeGame> gameOptional = ticTacToeGameRepository.findById(gameId);
        if (!gameOptional.isPresent()) {
            LOGGER.error("Game not found in repository: " + username);
            throw new RuntimeException("Game not found");
        }
        TicTacToeGame game = gameOptional.get();

        if (game.getGameStatus() != GameStatus.WAITING_FOR_PLAYER && game.getGameStatus() != GameStatus.IN_PROGRESS) {
            LOGGER.error("Bad game status: " + username);
            throw new RuntimeException("Bad game status");
        }

        if (game.getSecondPlayer() != null) {
            LOGGER.error("User: " + username + "was trying to join full game.");
            throw new RuntimeException("Somebody has already joined this game.");
        }
        if (game.getFirstPlayer().getUsername().equals(user.getUsername())) {
            LOGGER.error("User: " + username + "is a first player and is trying to join as second player.");
            throw new RuntimeException("This user is a first player");
        }
        game.setSecondPlayer(user);
        TicTacToeGame savedGame = ticTacToeGameRepository.save(game);
        return ticTacToeGameMapper.toDto(savedGame);
    }

    public List<TicTacToeGameDTO> getAvailableGames(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            LOGGER.error("User not found in repository: " + username);
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        List<TicTacToeGame> games = ticTacToeGameRepository.findAllByGameTypeAndFirstPlayerNot(GameType.MULTIPLAYER, user);
        List<GameStatus> statusList = new ArrayList<>();

        statusList.add(GameStatus.WAITING_FOR_PLAYER);
        statusList.add(GameStatus.IN_PROGRESS);

        games = games.stream().filter(ticTacToeGame -> statusList.contains(ticTacToeGame.getGameStatus())).collect(Collectors.toList());
        return games.stream().map(ticTacToeGameMapper::toDto).collect(Collectors.toList());
    }

    public List<TicTacToeGameDTO> getUserGamesHistory(String username) {
        List<TicTacToeGame> userGamesList = ticTacToeGameRepository.findAllByFirstPlayer_UsernameOrSecondPlayer_Username(username, username);
        userGamesList.removeIf(ticTacToeGame -> ticTacToeGame.getGameStatus() == GameStatus.WAITING_FOR_PLAYER || ticTacToeGame.getGameStatus() == GameStatus.IN_PROGRESS);
        return userGamesList.stream().map(ticTacToeGame -> ticTacToeGameMapper.toDto(ticTacToeGame)).collect(Collectors.toList());
    }

    public List<TicTacToeMoveDto> getGameMoves(Long gameId) {
        return ticTacToeMoveRepository.findAllByGame_GameId(gameId).stream().map(ticTacToeMove -> ticTacToeMoveMapper.toDto(ticTacToeMove)).collect(Collectors.toList());
    }

    public List<TicTacToeGame> getActiveGames(String username) {
        return ticTacToeGameRepository.findAllByFirstPlayer_UsernameAndGameStatusIn(username, Arrays.asList(GameStatus.WAITING_FOR_PLAYER, GameStatus.IN_PROGRESS));
    }

    public TicTacToeGameDTO startGame(Long gameId, String username) {
        Optional<TicTacToeGame> gameOptional = ticTacToeGameRepository.findById(gameId);
        if (!gameOptional.isPresent()) {
            LOGGER.error("Game not found. Id: " + gameId);
            throw new RuntimeException("Game with id " + gameId + "not exist");
        }
        TicTacToeGame game = gameOptional.get();
        if (game.getSecondPlayer() == null) {
            LOGGER.error("Second player is null. Cannot start game: " + gameId);
            throw new RuntimeException("Second player is null");
        }
        if (!username.equals(game.getFirstPlayer().getUsername())) {
            LOGGER.error("User: " + username + "was trying to start game without permission.");
            throw new RuntimeException("Only first player can start a game");
        }
        if (game.getGameStatus() != GameStatus.WAITING_FOR_PLAYER) {
            LOGGER.error("Invalid game status: " + game.getGameStatus() + ". Cannot start.");
            throw new RuntimeException("Invalid game status");
        }

        game.setGameStatus(GameStatus.IN_PROGRESS);
        ticTacToeGameRepository.save(game);
        ticTacToeLogic.startNewGame(game);
        return ticTacToeGameMapper.toDto(game);
    }

    public TicTacToeGameDTO getGame(Long gameId) {
        Optional<TicTacToeGame> gameById = ticTacToeGameRepository.findById(gameId);
        if (!gameById.isPresent())
            throw new GameNotFoundException("Game not found");
        //throw new RuntimeException("Game not found");
        return ticTacToeGameMapper.toDto(gameById.get());
    }

    public TicTacToeGameStateDTO getGameState(Long gameId) {
        Optional<TicTacToeGameStateDTO> gameState = ticTacToeLogic.getGameState(gameId);
        if (!gameState.isPresent())
            throw new GameNotFoundException("Game not found");
        return gameState.get();
    }

    public Long abandonGame(String username) {
        Optional<TicTacToeGame> userGame = ticTacToeGameRepository.findFirstByFirstPlayer_UsernameAndGameStatus(username, GameStatus.WAITING_FOR_PLAYER);
        if (!userGame.isPresent()) {
            LOGGER.info("User " + username + " has no games to abandon.");
            throw new RuntimeException("User has no games to abandon.");
        }
        TicTacToeGame game = userGame.get();
        Long gameId = game.getGameId();
        ticTacToeGameRepository.delete(game);
        return gameId;
    }
}
