package pl.edu.wat.wcy.pz.project.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.edu.wat.wcy.pz.project.server.dto.TicTacToeMoveDTO;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeMove;

@Mapper(componentModel = "spring")
public interface TicTacToeMoveMapper {

    @Mappings({
            @Mapping(target = "gameId", source = "game.gameId"),
            @Mapping(target = "username", source = "user.username")
    })
    TicTacToeMoveDTO toDto(TicTacToeMove ticTacToeMove);
}
