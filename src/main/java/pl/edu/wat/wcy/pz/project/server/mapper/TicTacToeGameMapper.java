package pl.edu.wat.wcy.pz.project.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.edu.wat.wcy.pz.project.server.dto.TicTacToeGameDTO;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;

@Mapper(componentModel = "spring")
public interface TicTacToeGameMapper {

    @Mappings({
            @Mapping(target = "firstPlayer", source = "firstPlayer.username"),
            @Mapping(target = "secondPlayer", source = "secondPlayer.username")
    })
    TicTacToeGameDTO toDto(TicTacToeGame ticTacToeGame);
}
