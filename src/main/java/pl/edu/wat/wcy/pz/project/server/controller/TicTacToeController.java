package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameType;
import pl.edu.wat.wcy.pz.project.server.entity.game.PieceCode;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeDTO;
import pl.edu.wat.wcy.pz.project.server.form.response.Message;
import pl.edu.wat.wcy.pz.project.server.service.TicTacToeService;

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
        TicTacToeGame createdGame = ticTacToeService.createGame(ticTacToeDTO, username);
        //return createdGame;
        //todo return game instead of String
        return ResponseEntity.ok(new Message("Game created"));
    }

    @GetMapping("/tictactoe")
    public TicTacToeDTO get() {
        TicTacToeDTO dto = new TicTacToeDTO();
        dto.setGameType(GameType.SINGLEPLAYER);
        dto.setPieceCode(PieceCode.X);
        return dto;
    }
}
