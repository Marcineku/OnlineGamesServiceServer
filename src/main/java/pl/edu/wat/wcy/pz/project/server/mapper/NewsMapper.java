package pl.edu.wat.wcy.pz.project.server.mapper;

import org.mapstruct.Mapper;
import pl.edu.wat.wcy.pz.project.server.dto.NewsDTO;
import pl.edu.wat.wcy.pz.project.server.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsDTO toDto(News news);

    News toEntity(NewsDTO newsDTO);
}
