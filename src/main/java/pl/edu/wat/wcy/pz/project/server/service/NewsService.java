package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.dto.NewsDTO;
import pl.edu.wat.wcy.pz.project.server.entity.News;
import pl.edu.wat.wcy.pz.project.server.mapper.NewsMapper;
import pl.edu.wat.wcy.pz.project.server.repository.NewsRepository;

import java.util.Calendar;
import java.util.List;

@AllArgsConstructor
@Service
public class NewsService {

    private NewsMapper newsMapper;

    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> getAllNewsByLanguage(String language) {
        return newsRepository.findByLanguage(language.toUpperCase());
    }

    public News createNews(NewsDTO newsDTO) {
        News news = newsMapper.toEntity(newsDTO);
        news.setPublicationDate(Calendar.getInstance().getTime());
        return newsRepository.save(news);
    }
}
