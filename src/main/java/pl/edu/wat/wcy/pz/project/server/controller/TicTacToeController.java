package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeMoveDTO;
import pl.edu.wat.wcy.pz.project.server.service.TicTacToeService;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/games")
public class TicTacToeController {

    private TicTacToeService ticTacToeService;

    private SimpMessagingTemplate template;

    @PostMapping("/tictactoe")
    public ResponseEntity<?> createGame(@RequestBody TicTacToeDTO ticTacToeDTO) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        TicTacToeGameDTO createdGameDto;
        try {
            createdGameDto = ticTacToeService.createGame(ticTacToeDTO, username);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        template.convertAndSend("/tictactoe/add", createdGameDto);
        return new ResponseEntity<>(createdGameDto, HttpStatus.OK);
    }

    @GetMapping("/tictactoe/{gameId}")
    public TicTacToeGameDTO getGame(@PathVariable Long gameId) {
        return ticTacToeService.getGame(gameId);
    }

    @GetMapping("/tictactoe/games/active")
    public List<TicTacToeGame> getActiveGame() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return ticTacToeService.getActiveGames(username);
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

        template.convertAndSend("/tictactoe/update", gameDto);
        return new ResponseEntity<>(gameDto, HttpStatus.OK);
    }

    @GetMapping("/tictactoe/start/{gameId}")
    public ResponseEntity<?> startGame(@PathVariable Long gameId) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        TicTacToeGameDTO gameDTO = ticTacToeService.startGame(gameId, username);

        template.convertAndSend("/tictactoe/update", gameDTO);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @GetMapping("/tictactoe/state/{gameId}")
    public TicTacToeGameStateDTO getGameState(@PathVariable Long gameId) {
        return ticTacToeService.getGameState(gameId);
    }

    @DeleteMapping("/tictactoe/abandon")
    public ResponseEntity<?> abandonGame() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        Long gameId = ticTacToeService.abandonGame(username);

        template.convertAndSend("/tictactoe/delete", gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //todo
    @DeleteMapping("/tictactoe/kick/{gameId}")
    public ResponseEntity<?> kickPlayer(@PathVariable Long gameId, @RequestBody String playerName) {
        return null;
    }

    /**
     * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     *
     * @return
     */

    @GetMapping("/tictactoe/history")
    public List<TicTacToeGameDTO> getGamesHistory() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ticTacToeService.getUserGamesHistory(principal.getUsername());
    }

    @GetMapping("/tictactoe/history/moves/{gameId}")
    public List<TicTacToeMoveDTO> getGameMoves(@PathVariable Long gameId) {
        return ticTacToeService.getGameMoves(gameId);
    }
}
