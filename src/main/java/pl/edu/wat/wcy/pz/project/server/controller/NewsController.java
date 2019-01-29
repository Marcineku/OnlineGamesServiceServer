package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.wat.wcy.pz.project.server.dto.NewsDTO;
import pl.edu.wat.wcy.pz.project.server.entity.News;
import pl.edu.wat.wcy.pz.project.server.service.NewsService;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    private NewsService newsService;

    @GetMapping("/news")
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/news/{language}")
    public List<News> getAllNewsByLanguage(@PathVariable("language") String language) {
        return newsService.getAllNewsByLanguage(language);
    }

    @PostMapping("/news")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createNews(@RequestBody NewsDTO newsDTO) {
        News createdNews = newsService.createNews(newsDTO);

        LOGGER.info("News added. Id: " + createdNews.getNewsId());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdNews.getNewsId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
