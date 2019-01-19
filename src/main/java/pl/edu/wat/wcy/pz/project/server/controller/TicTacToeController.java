package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeMove;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameDTO;
import pl.edu.wat.wcy.pz.project.server.service.TicTacToeService;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/games")
public class TicTacToeController {

    private TicTacToeService ticTacToeService;

    @PostMapping("/tictactoe")
    public ResponseEntity<?> createGame(@RequestBody TicTacToeDTO ticTacToeDTO) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        TicTacToeGameDTO createdGameDto = ticTacToeService.createGame(ticTacToeDTO, username);

        return new ResponseEntity<>(createdGameDto, HttpStatus.OK);
    }

    @GetMapping("/tictactoe/list")
    public List<TicTacToeGameDTO> getAvailableGames() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ticTacToeService.getAvailableGames(principal.getUsername());
    }

    @GetMapping("/tictactoe/join/{gameId}")
    public ResponseEntity<?> joinToGame(@PathVariable Long gameId) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        TicTacToeGameDTO gameDto = ticTacToeService.addSecondPlayerToGame(gameId, username);
        return new ResponseEntity<>(gameDto, HttpStatus.OK);
    }

    //@GetMapping("/tictactoe/start/{gameId}")
    //public ResponseEntity<?> startGame()

    /**
     * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     *
     * @return
     */
    @GetMapping("/tictactoe/myGames")
    public List<TicTacToeGame> getUserGames() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ticTacToeService.getUserGames(principal.getUsername());
    }

    @GetMapping("/tictactoe/gamesHistory/{gameType}")
    public List<TicTacToeGame> getGamesHistory(@PathVariable GameType gameType) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ticTacToeService.getUserGamesHistory(principal.getUsername(), gameType);
    }

    @GetMapping("/moves/{gameId}")
    public List<TicTacToeMove> getGameMoves(@PathVariable Long gameId) {
        return ticTacToeService.getGameMoves(gameId);
    }
}
