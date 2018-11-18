package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.News;
import pl.edu.wat.wcy.pz.project.server.repository.NewsRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class NewsService {

    NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> getAllNewsByLanguage(String language) {
        return newsRepository.findByLanguage(language.toUpperCase());
    }

    public News createNews(News news) {
        return newsRepository.save(news);
    }
}
